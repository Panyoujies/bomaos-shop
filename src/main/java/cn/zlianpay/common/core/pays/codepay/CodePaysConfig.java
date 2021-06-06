package cn.zlianpay.common.core.pays.codepay;

import com.alibaba.fastjson.JSON;
import cn.zlianpay.settings.entity.Pays;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CodePaysConfig {

    public static String CodePayConfigInfo(Pays pays, String price, String pay_id, String param) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String codepay_key = mapTypes.get("key").toString(); //记得更改 http://codepay.fateqq.com 后台可设置
        String codepay_id = mapTypes.get("id").toString();  //记得更改 http://codepay.fateqq.com 后台可获得
        String notify = mapTypes.get("notify_url").toString();   //  外网通知地址 部署到服务器后必须填写 本域名

        String notify_url = notify + "/codepay/notify"; //通知地址 且外网可访问，要不然无法通知到
        String return_url = notify +  "/codepay/return";    //支付后同步跳转地址

        if (price == null) {
            price = "1";
        }

        // 使用map封装 需要提交的字段 必须包括（ id  type  price  pay_id  sign ）
        Map<String, Object> params = new HashMap<>();
        params.put("id", codepay_id);
        if (pays.getDriver().equals("codepay_alipay")) {
            params.put("type", 1);
        } else if (pays.getDriver().equals("codepay_wxpay")) {
            params.put("type", 3);
        }
        params.put("price", price);
        params.put("pay_id", pay_id);
        params.put("param", param);
        params.put("notify_url", notify_url);
        params.put("return_url", return_url);

        // 根据key 首字母排序
        Map<String, Object> resultMap = sortMapByKey(params);

        String sign = "";
        String urls = "";
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            if (entry.getValue() == "" || entry.getValue() == null || entry.getKey() == "sign") {
                continue;
            }
            if (sign != "") { //后面追加&拼接URL
                sign += "&";
                urls += "&";
            }
            sign += entry.getKey() + "=" + entry.getValue(); //拼接为url参数形式
            urls += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"); //拼接为url参数形式并URL编码参数值
        }

        // MD5加密
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((sign + codepay_key).getBytes());
        String  mySign = new BigInteger(1, md.digest()).toString(16).toLowerCase();
        if(mySign.length() != 32) {
            mySign = "0" + mySign;
        }

        //参数有中文则需要URL编码
        String url = "https://api.xiuxiu888.com/creat_order?" + urls + "&sign=" + mySign;
        return url;
    }

    /**
     * 排序算法
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

}
