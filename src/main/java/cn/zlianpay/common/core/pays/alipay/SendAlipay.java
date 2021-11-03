package cn.zlianpay.common.core.pays.alipay;

import cn.zlianpay.settings.entity.Pays;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendAlipay {

    /**
     * 支付宝 - 当面付
     * @param pays 支付驱动
     * @param price 商品金额
     * @param ordersMember 商品id
     * @param goodsName 商品名称
     * @param goodsDescription 自定义内容
     * @return 支付二维码链接
     */
    public static String payAlipay(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription, HttpServletRequest request){

        Map mapTypes = JSON.parseObject(pays.getConfig());
        String app_id = mapTypes.get("app_id").toString();
        String private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String notifyUrl = mapTypes.get("notify_url").toString() + "/alipay/notify";   // 异步通知地址

        Map<String, String> resultMap = new HashMap<>();
        // 根据userId和orderNo到数据库查询订单是否存在。。。
        resultMap.put("orderNo", ordersMember);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = ordersMember;

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("支付宝扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = price;

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = goodsDescription;

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<>();

        /**
         * 处理金额
         */
        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String money = new DecimalFormat("0.##").format(multiply);

        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance(ordersMember, goodsName, Long.valueOf(money).longValue(), 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder =new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(notifyUrl)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        Configs.setOpenApiDomain("https://openapi.alipay.com/gateway.do"); // 正式环境
        Configs.setMcloudApiDomain("http://mcloudmonitor.com/gateway.do");

        // Configs.setPid("2088621955053967");
        Configs.setAppid(app_id);
        // # RSA私钥、公钥和支付宝公钥
        Configs.setPrivateKey(private_key);
        Configs.setAlipayPublicKey(alipay_public_key); // SHA256withRsa对应支付宝公钥
        Configs.setSignType("RSA2");
        Configs.setQueryDuration(5000);
        Configs.setMaxCancelRetry(3);
        Configs.setCancelDuration(2000);
        Configs.setHeartbeatDelay(5);
        Configs.setHeartbeatDuration(900);
        Configs.description(); // 初始化数据

        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);

        String qr_code = null;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                String results = result.getResponse().getBody();
                JSONObject parseObject = JSON.parseObject(results);
                String alipay_trade_precreate_response = parseObject.get("alipay_trade_precreate_response").toString();
                JSONObject jsonObject = JSON.parseObject(alipay_trade_precreate_response);
                qr_code = jsonObject.get("qr_code").toString();
                break;
            case FAILED:
                System.out.println("支付宝预下单失败!!!");
                break;
            case UNKNOWN:
                System.out.println("系统异常，预下单状态未知!!!");
                break;
            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return qr_code;
    }


    /**
     * alipay pc端支付接口
     * @param pays              支付接口
     * @param price             支付金额
     * @param ordersMember      订单ID
     * @param goodsName         商品名称
     * @param goodsDescription  预留内容
     * @return
     * @throws AlipayApiException
     */
    public static String payAlipayPc(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription) throws AlipayApiException {

        // 签名方式
        String sign_type = "RSA2";

        // 字符编码格式
        String charset = "utf-8";

        // 支付宝沙箱网关； https://openapi.alipaydev.com/gateway.do
        // 支付宝正式网关； https://openapi.alipay.com/gateway.do
        String gatewayUrl = "https://openapi.alipay.com/gateway.do";

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String app_id = mapTypes.get("app_id").toString();
        String merchant_private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String url = mapTypes.get("notify_url").toString();

        String notify_url = url + "/alipay/notify";
        String return_url = url + "/alipay/return_url";

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = ordersMember;
        //付款金额，必填
        String total_amount = price;
        //订单名称，必填
        String subject = goodsName;
        //商品描述，可空
        String body = goodsDescription;

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        // System.out.println("支付宝的响应："+result);
        return result;
    }

}
