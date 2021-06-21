package cn.zlianpay.common.core.pays.mqpay;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.zlianpay.settings.entity.Pays;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class mqPay {

    public static Map sendCreateMqPay(Pays pays, String price, String payId, String cloudPayid, String param) {

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String key = mapTypes.get("key").toString();

        String create_url = mapTypes.get("create_url").toString();
        String notify_url = mapTypes.get("notify_url").toString();

        String notifyUrl = notify_url + "/mqpay/notifyUrl";
        String returnUrl = notify_url + "/mqpay/returnUrl";

        Integer type = 2; // 默认支付宝
        if (pays.getDriver().equals("mqpay_alipay")) {
            type = 2;
        } else if (pays.getDriver().equals("mqpay_wxpay")){
            type = 1;
        }

        Integer isHtml = 0;
        String jsSign =  md5(payId + param + type + price + key);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("payId", payId);
        paramMap.put("type", type.toString());
        paramMap.put("price", price);
        paramMap.put("notifyUrl", notifyUrl);
        paramMap.put("returnUrl", returnUrl);
        paramMap.put("sign", jsSign);
        paramMap.put("param", param);
        paramMap.put("isHtml", isHtml.toString());

        Map<String, Object> getOrder = new HashMap<>();
        getOrder.put("orderId", cloudPayid);
        String getOrder1 = HttpUtil.post(create_url + "/getOrder", getOrder);
        JSONObject jsonObject1 = JSONObject.parseObject(getOrder1);
        String httppostjson = "";
        if (jsonObject1.get("code").toString().equals("-1")) { // 云端没有就创建
            httppostjson = HttpUtil.post(create_url + "/createOrder", paramMap);
        } else if (jsonObject1.get("code").toString().equals("1")) { // 有的话直接查询
            httppostjson = getOrder1;
        }

        JSONObject jsonObject = JSONObject.parseObject(httppostjson);
        String code = jsonObject.get("code").toString();
        String msg = jsonObject.get("msg").toString();

        Map<String,String> map = new HashMap<>();
        if (code.equals("-1") && msg.equals("监控端状态异常，请检查")) {
            map.put("code", "0"); // 0 为监控错误
        } else {
            JSONObject object = JSONObject.parseObject(jsonObject.get("data").toString());
            System.out.println(object.toJSONString());
            String payUrl = object.get("payUrl").toString();
            String orderId = object.get("orderId").toString();
            map.put("payUrl", payUrl);
            map.put("orderId", orderId);
            map.put("date", object.get("date").toString());
            map.put("timeOut", object.get("timeOut").toString());
            map.put("state", object.get("state").toString());
            map.put("code", "1"); // 1 为正常
            map.put("reallyPrice", object.get("reallyPrice").toString());
        }
        return map;
    }

    public static String md5(String text) {
        //加密后的字符串
        String encodeStr = DigestUtils.md5DigestAsHex(text.getBytes());
        return encodeStr;
    }

}
