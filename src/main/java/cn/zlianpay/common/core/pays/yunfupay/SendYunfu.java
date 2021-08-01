package cn.zlianpay.common.core.pays.yunfupay;

import cn.hutool.http.HttpUtil;
import cn.zlianpay.settings.entity.Pays;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SendYunfu {

    public static String pay(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription, String ip) throws IOException {

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String appid = mapTypes.get("appid").toString();  // id
        String key = mapTypes.get("key").toString(); // 密钥
        String notifyUrl = mapTypes.get("notify_url").toString() + "/yunfu/notify";   // 异步跳转地址

        Map<String, Object> payData = new HashMap<>();
        payData.put("appid", appid); // 应用id

        /**
         * 支付宝：alipay
         * 微信公众号：wxpay
         */
        if (pays.getDriver().equals("yunfu_alipay")) {
            payData.put("type", "alipay"); // 交易通道
        } else if (pays.getDriver().equals("yunfu_wxpay")) {
            payData.put("type", "wxpay");
        }

        payData.put("price", price); // 交易金额（单位为元）
        payData.put("name", goodsName); // 商品名称
        payData.put("body", goodsDescription); // 商品描述
        payData.put("notify_url", notifyUrl); // 异步跳转
        payData.put("out_trade_no", ordersMember); // 商户订单号
        payData.put("ip", ip); // 用户的ip地址 (不能为)
        payData.put("server", mapTypes.get("notify_url").toString()); // 商家地址
        payData.put("sign", SignUtil.createSign(payData, key));

        String result = HttpUtil.post("https://yunfu.hmy3.com/api/gateway/request.html", JSON.toJSONString(payData));
        JSONObject jsonObject = JSON.parseObject(result);
        String state = jsonObject.get("state").toString();
        if (state.equals("succeeded")) {

            String return_data = jsonObject.get("return_data").toString();
            JSONObject returnData = JSON.parseObject(return_data);
            String url = returnData.get("url").toString();
            Object h5 = returnData.get("h5");
            return url;
        } else {

            return null;
        }
    }

}
