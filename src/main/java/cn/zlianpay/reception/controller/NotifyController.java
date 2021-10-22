package cn.zlianpay.reception.controller;

import cn.hutool.crypto.SecureUtil;
import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.pays.payjs.SignUtil;
import cn.zlianpay.common.core.pays.paypal.PaypalSend;
import cn.zlianpay.common.core.pays.zlianpay.ZlianPay;
import cn.zlianpay.common.core.utils.FormCheckUtil;
import cn.zlianpay.common.core.web.JsonResult;
import cn.zlianpay.reception.dto.NotifyDTO;
import cn.zlianpay.settings.entity.ShopSettings;
import cn.zlianpay.settings.service.ShopSettingsService;
import cn.zlianpay.website.entity.Website;
import cn.zlianpay.website.service.WebsiteService;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.zlianpay.common.core.pays.mqpay.mqPay;
import cn.zlianpay.common.core.utils.RequestParamsUtil;
import cn.zlianpay.common.core.utils.StringUtil;
import cn.zlianpay.common.system.service.EmailService;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ProductsService;
import cn.zlianpay.settings.entity.Pays;
import cn.zlianpay.settings.service.PaysService;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Transactional
public class NotifyController {

    @Autowired
    private PaysService paysService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private OrderCardService orderCardService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    /**
     * 返回成功xml
     */
    private String WxpayresXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    private String WxpayH5resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 返回失败xml
     */
    private String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    @RequestMapping("/mqpay/notifyUrl")
    @ResponseBody
    public String notifyUrl(HttpServletRequest request) {
        /**
         *验证通知 处理自己的业务
         */
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String param = params.get("param");
        String price = params.get("price");
        String money = params.get("reallyPrice");
        String sign = params.get("sign");
        String payId = params.get("payId");
        String type = params.get("type");

        String key = null;

        if (Integer.parseInt(type) == 1) { // wxpay
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "mqpay_wxpay"));
            Map mapTypes = JSON.parseObject(wxPays.getConfig());
            key = mapTypes.get("key").toString();
        } else if (Integer.parseInt(type) == 2) { // alipay
            Pays aliPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "mqpay_alipay"));
            Map mapTypes = JSON.parseObject(aliPays.getConfig());
            key = mapTypes.get("key").toString();
        }

        String mysign = mqPay.md5(payId + param + type + price + money + key);

        if (mysign.equals(sign)) {
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String seconds = new SimpleDateFormat("HHmmss").format(new Date());
            String number = StringUtil.getRandomNumber(6);
            String payNo = date + seconds + number;
            String big = returnBig(money, price, payId, payNo, param, "success", "fiald");
            return big; // 通知成功
        } else {
            return "fiald";
        }
    }

    @RequestMapping("/mqpay/returnUrl")
    public void returnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /**
         *验证通知 处理自己的业务
         */
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String param = params.get("param");
        String price = params.get("price");
        String reallyPrice = params.get("reallyPrice");
        String sign = params.get("sign");
        String payId = params.get("payId");
        String type = params.get("type");

        String key = null;
        if (Integer.parseInt(type) == 1) { // wxpay
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "mqpay_wxpay"));
            Map mapTypes = JSON.parseObject(wxPays.getConfig());
            key = mapTypes.get("key").toString();
        } else if (Integer.parseInt(type) == 2) { // alipay
            Pays aliPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "mqpay_alipay"));
            Map mapTypes = JSON.parseObject(aliPays.getConfig());
            key = mapTypes.get("key").toString();
        }
        String mysign = mqPay.md5(payId + param + type + price + reallyPrice + key);
        if (mysign.equals(sign)) {
            String url = "/pay/state/" + payId;
            response.sendRedirect(url);
        }
    }

    @RequestMapping("/zlianpay/notifyUrl")
    @ResponseBody
    public String zlianpNotify(HttpServletRequest request) {
        Map<String, String> parameterMap = RequestParamsUtil.getParameterMap(request);

        String pid = parameterMap.get("pid");
        String type = parameterMap.get("type");

        String driver = "";
        if (type.equals("wxpay")) {
            driver = "zlianpay_wxpay";
        } else if (type.equals("alipay")) {
            driver = "zlianpay_alipay";
        }

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver));
        Map mapTypes = JSON.parseObject(pays.getConfig());

        // 你的key 在后台获取
        String secret_key = mapTypes.get("key").toString();
        String trade_no = parameterMap.get("trade_no");
        String out_trade_no = parameterMap.get("out_trade_no");
        String name = parameterMap.get("name");
        String money = parameterMap.get("money");
        String trade_status = parameterMap.get("trade_status");
        String return_url = parameterMap.get("return_url");
        String notify_url = parameterMap.get("notify_url");
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("return_url", return_url);
        params.put("notify_url", notify_url);
        params.put("trade_status", trade_status);

        String sign1 = ZlianPay.createSign(params, secret_key);

        if (sign1.equals(sign)) {
            String big = returnBig(money, money, out_trade_no, trade_no, name, "success", "final");
            return big;
        } else {
            return "签名错误！！";
        }
    }

    @RequestMapping("/zlianpay/returnUrl")
    @ResponseBody
    public void zlianpReturnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /**
         *验证通知 处理自己的业务
         */
        Map<String, String> parameterMap = RequestParamsUtil.getParameterMap(request);

        String pid = parameterMap.get("pid");
        String type = parameterMap.get("type");

        String driver = "";
        if (type.equals("wxpay")) {
            driver = "zlianpay_wxpay";
        } else if (type.equals("alipay")) {
            driver = "zlianpay_alipay";
        }

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver));
        Map mapTypes = JSON.parseObject(pays.getConfig());

        // 你的key 在后台获取
        String secret_key = mapTypes.get("key").toString();
        String trade_no = parameterMap.get("trade_no");
        String out_trade_no = parameterMap.get("out_trade_no");
        String name = parameterMap.get("name");
        String money = parameterMap.get("money");
        String trade_status = parameterMap.get("trade_status");
        String return_url = parameterMap.get("return_url");
        String notify_url = parameterMap.get("notify_url");
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("return_url", return_url);
        params.put("notify_url", notify_url);
        params.put("trade_status", trade_status);

        String sign1 = ZlianPay.createSign(params, secret_key);

        if (sign1.equals(sign)) {
            String url = "/pay/state/" + out_trade_no;
            response.sendRedirect(url);
        }
    }

    /**
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/yungouos/notify")
    public String notify(HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String payNo = params.get("payNo");
        String code = params.get("code");
        String mchId = params.get("mchId");
        String orderNo = params.get("orderNo");
        String money = params.get("money");
        String openId = params.get("openId");
        String outTradeNo = params.get("outTradeNo");
        String sign = params.get("sign");
        String payChannel = params.get("payChannel");
        String attach = params.get("attach");
        String time = params.get("time");

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("orderNo", orderNo);
        map.put("outTradeNo", outTradeNo);
        map.put("payNo", payNo);
        map.put("money", money);
        map.put("mchId", mchId);

        String key = null;

        switch (payChannel) {
            //此处因为没启用独立密钥 支付密钥支付宝与微信支付是一样的 （密钥获取：登录 yungouos.com-》我的账户-》商户管理-》商户密钥）
            case "wxpay":
                Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "yungouos_wxpay"));
                Map wxMap = JSON.parseObject(wxPays.getConfig());
                key = wxMap.get("key").toString();
                break;
            case "alipay":
                Pays alipays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "yungouos_alipay"));
                Map aliMap = JSON.parseObject(alipays.getConfig());
                key = aliMap.get("key").toString();
                break;
            default:
                break;
        }

        String mySign = createSign(map, key);
        if (mySign.equals(sign) && Integer.parseInt(code) == 1) {
            // 处理通知成功后的业务逻辑
            String big = returnBig(money, money, outTradeNo, payNo, attach, "SUCCESS", "FIALD");
            return big;
        } else {
            //签名错误
            return "FIALD";
        }
    }

    /**
     * 虎皮椒支付通知
     *
     * @param request
     * @return
     */
    @RequestMapping("/xunhupay/notifyUrl")
    @ResponseBody
    public String xunhuNotifyUrl(HttpServletRequest request) {
        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        if ("OD".equals(params.get("status"))) {
            String returnBig = returnBig(params.get("total_fee"), params.get("total_fee"), params.get("trade_order_id"), params.get("transaction_id"), params.get("plugins"), "success", "fiald");
            return returnBig;
        } else {
            return "fiald";
        }
    }

    /**
     * 虎皮椒支付通知
     *
     * @param request
     * @return
     */
    @RequestMapping("/xunhupay/returnUrl")
    @ResponseBody
    public void xunhuReturnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String url = "/pay/state/" + params.get("trade_order_id");
        response.sendRedirect(url);
    }

    private String order_id;

    /**
     * 捷支付通知
     *
     * @param request
     * @return
     */
    @RequestMapping("/jiepay/notifyUrl")
    @ResponseBody
    public String jiepayNotifyUrl(HttpServletRequest request) {
        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String code = params.get("code");
        String order_id = params.get("order_id");
        String order_rmb = params.get("order_rmb");
        String diy = params.get("diy");
        String sign = params.get("sign");

        String appid = "";
        String apptoken = "";

        if (code.equals("1")) {
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "jiepay_alipay"));
            Map wxMap = JSON.parseObject(wxPays.getConfig());
            appid = wxMap.get("appid").toString();
            apptoken = wxMap.get("apptoken").toString();
        } else if (code.equals("2")) {
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "jiepay_wxpay"));
            Map wxMap = JSON.parseObject(wxPays.getConfig());
            appid = wxMap.get("appid").toString();
            apptoken = wxMap.get("apptoken").toString();
        }

        String newSign = SecureUtil.md5(appid + apptoken + code + order_id + order_rmb + diy);
        if (sign.equals(newSign)) {
            this.order_id = order_id;
            String returnBig = returnBig(order_rmb, order_rmb, order_id, System.currentTimeMillis() + "", diy, "success", "fiald");
            return returnBig;
        } else {
            return "error";
        }
    }

    /**
     * 捷支付通知
     *
     * @param request
     * @return
     */
    @RequestMapping("/jiepay/returnUrl")
    @ResponseBody
    public void jiepayReturnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        while (true) {
            if (!StringUtils.isEmpty(this.order_id)) {
                break;
            }
        }
        String url = "/pay/state/" + this.order_id;
        response.sendRedirect(url);
    }

    /**
     * 异步通知
     * @param notifyDTO
     * @return
     */
    @RequestMapping("/payjs/notify")
    @ResponseBody
    public Object payjsNotify(NotifyDTO notifyDTO){
        Map<String, Object> notifyData = new HashMap<>();
        notifyData.put("return_code", notifyDTO.getReturn_code());
        notifyData.put("total_fee", notifyDTO.getTotal_fee());
        notifyData.put("out_trade_no", notifyDTO.getOut_trade_no());
        notifyData.put("payjs_order_id", notifyDTO.getPayjs_order_id());
        notifyData.put("transaction_id", notifyDTO.getTransaction_id());
        notifyData.put("time_end", notifyDTO.getTime_end());
        notifyData.put("openid", notifyDTO.getOpenid());
        notifyData.put("mchid", notifyDTO.getMchid());

        // options
        if (notifyDTO.getAttach() != null) {
            notifyData.put("attach", notifyDTO.getAttach());
        }
        if (notifyDTO.getType() != null) {
            notifyData.put("type", notifyDTO.getType());
        }

        String key = null;
        if (notifyDTO.getType() != null) { // 支付宝
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "payjs_alipay"));
            Map wxMap = JSON.parseObject(wxPays.getConfig());
            key = wxMap.get("key").toString();
        } else { // 微信
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "payjs_wxpay"));
            Map wxMap = JSON.parseObject(wxPays.getConfig());
            key = wxMap.get("key").toString();
        }

        String sign = SignUtil.sign(notifyData, key);
        if(sign.equals(notifyDTO.getSign())){
            // 验签通过，这里修改订单状态
            String returnBig = returnBig(notifyDTO.getTotal_fee(), notifyDTO.getTotal_fee(), notifyDTO.getOut_trade_no(), notifyDTO.getTransaction_id(), notifyDTO.getAttach(), "success", "failure");
            return returnBig;
        } else {
            return "failure";
        }
    }

    @RequestMapping("/yunfu/notify")
    @ResponseBody
    public String yunfuNotify(HttpServletRequest request) {
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);

        String bank = params.get("bank");
        String out_trade_no = params.get("out_trade_no");
        String appid = params.get("appid");
        String sign = params.get("sign");
        String trade_no = params.get("trade_no");

        Map<String, Object> payData = new HashMap<>();
        payData.put("bank", bank);
        payData.put("out_trade_no", out_trade_no);
        payData.put("appid", appid);
        payData.put("trade_no", trade_no);

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "yunfu_wxpay"));
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String key = mapTypes.get("key").toString(); // 密钥
        String newSign = cn.zlianpay.common.core.pays.yunfupay.SignUtil.createSign(payData, key);

        if (sign.equals(newSign)) {
            Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));
            Products products = productsService.getById(member.getProductId());

            String returnBig = returnBig(member.getMoney().toString(), member.getMoney().toString(), out_trade_no, trade_no, products.getId().toString(), "SUCCESS", "fiald");
            return returnBig;
        } else {
            return "fiald";
        }
    }

    /**
     * 微信官方异步通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/wxpay/notify")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response){
        String resXml = "";
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            System.out.println("wxnotify:微信支付----start----");
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outSteam.toByteArray(), "utf-8");
            System.out.println("wxnotify:微信支付----result----=" + result);

            // 关闭流
            outSteam.close();
            inStream.close();

            // xml转换为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            boolean isSuccess = false;
            String result_code = resultMap.get("result_code");
            if ("SUCCESS".equals(result_code)) {
                Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "wxpay"));
                Map mapTypes = JSON.parseObject(pays.getConfig());
                String key = mapTypes.get("key").toString(); // 密钥

                /**
                 * 签名成功
                 */
                if (WXPayUtil.isSignatureValid(resultMap, key)) {
                    String total_fee = resultMap.get("total_fee");// 订单总金额，单位为分
                    String out_trade_no = resultMap.get("out_trade_no");// 商户系统内部订单号
                    String transaction_id = resultMap.get("transaction_id");// 微信支付订单号
                    String attach = resultMap.get("attach");// 商家数据包，原样返回
                    String appid = resultMap.get("appid");// 微信分配的小程序ID

                    BigDecimal bigDecimal = new BigDecimal(total_fee);
                    BigDecimal multiply = bigDecimal.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    String money = new DecimalFormat("0.##").format(multiply);
                    Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));
                    if (member.getPayType().equals("wxpay")) {
                        String returnBig = returnBig(money, money, out_trade_no, transaction_id, attach, WxpayresXml, resFailXml);
                        resXml = returnBig;
                    } else {
                        String returnBig = returnBig(money, money, out_trade_no, transaction_id, attach, WxpayH5resXml, resFailXml);
                        resXml = returnBig;
                    }
                } else {
                    System.out.println("签名判断错误！！");
                }
            }
        } catch (Exception e) {
            System.out.println("wxnotify:支付回调发布异常：" +  e);
        } finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                System.out.println("wxnotify:支付回调发布异常:out：" + e);
            }
        }
        return resXml;
    }

    /**
     * 支付宝当面付 异步通知
     * @param request 接收
     * @return 返回
     */
    @RequestMapping("/alipay/notify")
    @ResponseBody
    public String alipayNotifyUrl(HttpServletRequest request) {

        String success = "success";
        String failure = "failure";

        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        /**
         * 非常重要,签名不带，避免签名的sign 不匹配。
         * 验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
         */
        params.remove("sign_type");

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "alipay"));
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String alipay_public_key = mapTypes.get("alipay_public_key").toString(); // 密钥

        String returnBig = null;
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, alipay_public_key, "utf-8", "RSA2");
            if (alipayRSACheckedV2) {
                String out_trade_no = params.get("out_trade_no");// 商户订单号
                String total_amount = params.get("total_amount");// 付款金额
                String trade_no = params.get("trade_no");// 流水
                String receipt_amount = params.get("receipt_amount");// 实际支付金额
                String body = params.get("body");// 状态
                returnBig = returnBig(receipt_amount, total_amount, out_trade_no, trade_no, body, success, failure);
            } else {
                System.out.println("签名错误！！");
                return failure;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failure;
        }
        return returnBig;
    }

    /**
     * 取消订单
     * @return
     */
    @GetMapping("/paypal/cancel")
    @ResponseBody
    public String cancelPay() {
        return "cancel";
    }

    /**
     * 完成支付
     * @param paymentId
     * @param payerId
     * @param response
     * @return
     */
    @GetMapping("/paypal/success")
    @ResponseBody
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) {
        try {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "paypal"));
            Map mapTypes = JSON.parseObject(pays.getConfig());
            String clientId = mapTypes.get("clientId").toString();
            String clientSecret = mapTypes.get("clientSecret").toString();
            Payment payment = PaypalSend.executePayment(clientId, clientSecret, paymentId, payerId);
            if (payment.getState().equals("approved")) {
                String member = null; // 订单号
                String total = null;  // 金额
                String pay_no = payment.getId();
                List<Transaction> transactions = payment.getTransactions();
                for (Transaction transaction : transactions) {
                    member = transaction.getDescription();
                    total = transaction.getAmount().getTotal(); // 实际付款金额
                }
                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", member));
                String returnBig = returnBig(total, orders.getPrice().toString(), member, pay_no, orders.getProductId().toString(), "success", "failure");
                if (returnBig.equals("success")) {
                    response.sendRedirect("/search/order/" + member);
                } else {
                    response.sendRedirect("/search/order/" + member);
                }
            }
        } catch (PayPalRESTException | IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    /**
     * 业务处理
     * @param money 实收款金额
     * @param price 订单金额
     * @param payId 订单号
     * @param pay_no 流水号
     * @param param 自定义内容
     * @param success 返回成功
     * @param fiald  返回失败
     * @return this
     */
    private String returnBig(String money, String price, String payId, String pay_no, String param, String success, String fiald) {

        /**
         * 通过订单号查询
         */
        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", payId));
        if (member == null) return fiald; // 本地没有这个订单

        int count = orderCardService.count(new QueryWrapper<OrderCard>().eq("order_id", member.getId()));
        if (count >= 1)  return success;

        Products products = productsService.getById(param);
        if (products == null) return fiald; // 商品没了

        Website website = websiteService.getById(1);
        ShopSettings shopSettings = shopSettingsService.getById(1);

        if (products.getShipType() == 0) { // 自动发货的商品
            List<Cards> card = cardsService.getCard(0, products.getId(), member.getNumber());
            if (card == null) return fiald;

            List<OrderCard> cardList = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (Cards cards : card) {
                OrderCard orderCard = new OrderCard();
                orderCard.setCardId(cards.getId());
                orderCard.setOrderId(member.getId());
                orderCard.setCreatedAt(new Date());
                cardList.add(orderCard);

                Cards cards1 = new Cards();
                cards1.setId(cards.getId());
                cards1.setStatus(1);
                cards1.setUpdatedAt(new Date());

                if (cards.getCardInfo().contains(" ")) {
                    String[] split = cards.getCardInfo().split(" ");
                    stringBuilder.append("卡号：").append(split[0]).append(" ").append("卡密：").append(split[1]).append("\n");
                } else {
                    stringBuilder.append("卡密：").append(cards.getCardInfo()).append("\n");
                }
                // 设置售出的卡密
                cardsService.updateById(cards1);
            }

            if (shopSettings.getIsWxpusher() == 1) {
                Message message = new Message();
                message.setContent(website.getWebsiteName() + "新订单提醒<br>订单号：<span style='color:red;'>" + member.getMember() + "</span><br>商品名称：<span>" + products.getName() + "</span><br>购买数量：<span>" + member.getNumber() + "</span><br>订单金额：<span>"+ member.getMoney() +"</span><br>支付状态：<span style='color:green;'>成功</span><br>");
                message.setContentType(Message.CONTENT_TYPE_HTML);
                message.setUid(shopSettings.getWxpushUid());
                message.setAppToken(shopSettings.getAppToken());
                WxPusher.send(message);
            }

            if (shopSettings.getIsEmail() == 1) {
                if (!StringUtils.isEmpty(member.getEmail())) {
                    if (FormCheckUtil.isEmail(member.getEmail())) {
                        Map<String, Object> map = new HashMap<>();  // 页面的动态数据
                        map.put("title", website.getWebsiteName());
                        map.put("member", member.getMember());
                        map.put("date", new Date());
                        map.put("info", stringBuilder.toString());
                        try {
                            emailService.sendHtmlEmail(website.getWebsiteName() + "发货提醒", "email/sendShip.html", map, new String[]{member.getEmail()});
                            // emailService.sendTextEmail("卡密购买成功", "您的订单号为：" + member.getMember() + "  您的卡密：" + cards.getCardInfo(), new String[]{member.getEmail()});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            /**
             * 关联卡密
             */
            orderCardService.saveBatch(cardList);

        } else { // 手动发货商品
            Products products1 = new Products();
            products1.setId(products.getId());
            products1.setInventory(products.getInventory() - member.getNumber());
            products1.setSales(products.getSales() + member.getNumber());

            if (shopSettings.getIsWxpusher() == 1) {
                Message message = new Message();
                message.setContent(website.getWebsiteName() + "新订单提醒<br>订单号：<span style='color:red;'>" + member.getMember() + "</span><br>商品名称：<span>" + products.getName() + "</span><br>购买数量：<span>" + member.getNumber() + "</span><br>订单金额：<span>"+ member.getMoney() +"</span><br>支付状态：<span style='color:green;'>成功</span><br>");
                message.setContentType(Message.CONTENT_TYPE_HTML);
                message.setUid(shopSettings.getWxpushUid());
                message.setAppToken(shopSettings.getAppToken());
                WxPusher.send(message);
            }

            if (shopSettings.getIsEmail() == 1) {
                if (FormCheckUtil.isEmail(member.getEmail())) {
                    emailService.sendTextEmail(website.getWebsiteName() + " 订单提醒", "您的订单号为：" + member.getMember() + "  本商品为手动发货，请耐心等待！", new String[]{member.getEmail()});
                }
            }
            productsService.updateById(products1);
        }

        /**
         * 更新订单
         */
        Orders orders = new Orders();
        orders.setId(member.getId());

        if (products.getShipType() == 0) {
            orders.setStatus(1); // 设置已售出
        } else {
            orders.setStatus(2); // 手动发货模式 为待处理
        }

        orders.setPayTime(new Date());
        orders.setPayNo(pay_no);
        orders.setPrice(new BigDecimal(price));
        orders.setMoney(new BigDecimal(money));
        boolean b = ordersService.updateById(orders);// 更新售出
        return success;
    }

    @GetMapping("/order/state/{orderid}")
    @ResponseBody
    public JsonResult state(@PathVariable("orderid") String orderid){
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("id", orderid));
        if (!StringUtils.isEmpty(orders.getPayNo())) {
            return JsonResult.ok().setCode(200).setData(1);
        } else {
            return JsonResult.ok().setData(0);
        }
    }


    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try {
                    value = urlEncode(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
            sb.append(value);
        }
        return sb.toString();
    }

    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }

    public static String createSign(Map<String, String> params, String partnerKey) throws NoSuchAlgorithmException {
        // 生成签名前先去除sign
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + partnerKey;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((stringSignTemp).getBytes());
        String mySign = new BigInteger(1, md.digest()).toString(16).toUpperCase();
        if (mySign.length() != 32) {
            mySign = "0" + mySign;
        }
        return mySign;
    }

}
