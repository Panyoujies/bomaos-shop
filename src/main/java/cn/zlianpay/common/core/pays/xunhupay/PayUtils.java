package cn.zlianpay.common.core.pays.xunhupay;

import cn.hutool.crypto.SecureUtil;
import cn.zlianpay.common.core.web.BaseController;
import cn.zlianpay.settings.entity.Pays;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PayUtils extends BaseController {

	/**
	 * 虎皮椒支付
	 *
	 * @param product_type 商品名称
	 * @param price        金额
	 * @param orderMember  订单id
	 * @return
	 */
	public static Map pay(String webName, Pays pays, String product_type, String price, String orderMember, String plugins) {

		String config = pays.getConfig();
		JSONObject configs = JSONObject.parseObject(config);

		/**
		 * @param appid 商品名称
		 * @param appsecret 密钥
		 * @param url  订单id
		 */
		String appid = (String) configs.get("appid");
		String appsecret = (String) configs.get("appsecret");
		String notify_url = (String) configs.get("notify_url");
		String url = "https://api.xunhupay.com/payment/do.html";

		Map<String, Object> sortParams = new HashMap<>();
		sortParams.put("version", "1.1");
		sortParams.put("appid", appid);
		sortParams.put("trade_order_id", orderMember);
		if (StringUtils.equals(pays.getDriver(),"xunhupay_wxpay")) {
			sortParams.put("payment", "wechat"); // 微信
			sortParams.put("type", "WAP");
			sortParams.put("wap_url", notify_url);
			sortParams.put("wap_name", webName);
		} else {
			sortParams.put("payment", "alipay"); // 支付宝
		}
		sortParams.put("total_fee", price);
		sortParams.put("title", product_type);
		sortParams.put("time", getSecondTimestamp(new Date()));
		sortParams.put("notify_url", notify_url + "/xunhupay/notifyUrl");

		sortParams.put("return_url", notify_url + "/xunhupay/returnUrl?trade_order_id=" + orderMember);
        sortParams.put("callback_url", "http://2h8446682m.51vip.biz");
		sortParams.put("plugins", plugins);
		sortParams.put("nonce_str", getRandomNumber(9));
		sortParams.put("hash", createSign(sortParams,appsecret));

		String response = HttpUtils.httppostjson(url, JSON.toJSONString(sortParams));
        JSONObject jsonObject = JSONObject.parseObject(response);

		System.out.println(jsonObject.toJSONString());
        Map map1 = null;
		try {
            Integer errcode = (Integer) jsonObject.get("errcode");
            String errmsg = (String) jsonObject.get("errmsg");
            if (errcode == 0 || StringUtils.equals(errmsg, "success!")) {
                String url_qrcode = (String) jsonObject.get("url_qrcode");
                String url1 = (String) jsonObject.get("url");
				HashMap<String, String> map = new HashMap<>();
				map.put("url_qrcode", url_qrcode);
				map.put("url1", url1);
				map1 = map;
            }
        } catch (Exception e) {
			e.printStackTrace();
		}

        return map1;
	}

	/**
	 * 生成密钥
	 * @param params
	 * @param privateKey
	 * @return
	 */
	public static String createSign(Map<String, Object> params, String privateKey) {

		// 使用HashMap，并使用Arrays.sort排序
		String[] sortedKeys = params.keySet().toArray(new String[]{});
		Arrays.sort(sortedKeys);// 排序请求参数
		StringBuilder builder = new StringBuilder();
		for (String key : sortedKeys) {
			builder.append(key).append("=").append(params.get(key)).append("&");
		}
		String result = builder.deleteCharAt(builder.length() - 1).toString();

		/**
		 * 拼接上appsecret
		 */
		String stringSignTemp = result + privateKey;
		String signValue = SecureUtil.md5(stringSignTemp);

		return signValue;
	}


	/**
	 * 获取精确到秒的时间戳   原理 获取毫秒时间戳，因为 1秒 = 100毫秒 去除后三位 就是秒的时间戳
	 * @return
	 */
	public static int getSecondTimestamp(Date date){
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Integer.valueOf(timestamp.substring(0,length-3));
		} else {
			return 0;
		}
	}

	/**
	 * 生成一个随机数字
	 * @param length 长度自定义
	 * @return
	 */
	public static String getRandomNumber(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < length; ++i) {
			int number = random.nextInt(str.length());
			sb.append(str.charAt(number));
		}

		return sb.toString();
	}
}
