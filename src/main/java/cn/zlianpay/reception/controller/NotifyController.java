package cn.zlianpay.reception.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.pays.zlianpay.ZlianPay;
import cn.zlianpay.common.core.web.JsonResult;
import com.alibaba.fastjson.JSON;
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
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
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

    @RequestMapping("/mqpay/notifyUrl")
    @ResponseBody
    public String notifyUrl(HttpServletRequest request){
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
            System.out.println(big);
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

    @RequestMapping("/codepay/notify")
    @ResponseBody
    public String codePayNotifyUrl(HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);

        String type = "";
        if (params.get("type").equals("1")) { // 支付宝
            type = "codepay_alipay";
        } else if (params.get("type").equals("3")) { // 微信
            type = "codepay_wxpay";
        } else if (params.get("type").equals("2")) { // qq钱包
            type = "codepay_qqpay";
        }

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", type).eq("enabled", 1));
        Map mapTypes = JSON.parseObject(pays.getConfig());

        /**
         * 验证通知 处理自己的业务
         */
        String codepay_key = mapTypes.get("key").toString(); //记得更改 http://codepay.fateqq.com 后台可设置

        List<String> keys = new ArrayList<>(params.keySet()); //转为数组
        Collections.sort(keys); //重新排序
        String prestr = "";
        String sign = params.get("sign"); //获取接收到的sign 参数

        String param = params.get("param");

        for (int i = 0; i < keys.size(); i++) { //遍历拼接url 拼接成a=1&b=2 进行MD5签名
            String key_name = keys.get(i);
            String value = params.get(key_name);
            if (value == null || value.equals("") || key_name.equals("sign")) { //跳过这些 不签名
                continue;
            }
            if (prestr.equals("")) {
                prestr = key_name + "=" + value;
            } else {
                prestr = prestr + "&" + key_name + "=" + value;
            }
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((prestr + codepay_key).getBytes());
        String mySign = new BigInteger(1, md.digest()).toString(16).toLowerCase();
        if (mySign.length() != 32) {
            mySign = "0" + mySign;
        }
        if (mySign.equals(sign)) {
            //编码要匹配 编码不一致中文会导致加密结果不一致
            //参数合法处理业务
            String pay_no = request.getParameter("pay_no");//流水号
            String member = request.getParameter("pay_id");//用户唯一标识
            String money = request.getParameter("money");//付款金额
            String price = request.getParameter("price");//提交的金额

            // 处理业务逻辑
            String big = returnBig(money, price, member, pay_no, param, "ok", "fail");

            return big;
        } else {
            //参数不合法
            return "fail";
        }
    }

    /**
     * 吗支付返回接口
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/codepay/return")
    @ResponseBody
    public void codePayReturnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         *验证通知 处理自己的业务
         */
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);

        String pay_no = params.get("pay_no");
        String pay_id = params.get("pay_id");
        if (pay_no != null || pay_no != "") {
            String url = "/pay/state/" + pay_id;
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
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("trade_status", trade_status);

        String sign1 = ZlianPay.createSign(params, secret_key);
        if (sign1.equals(sign)) {
            String big = returnBig(money, money, out_trade_no, trade_no, name, "success", "final");

            System.out.println(big);

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
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
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

        System.out.println("1111111111111111");
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

        List<Cards> card = cardsService.getCard(0, products.getId(), member.getNumber());
        if (card == null) return fiald;

        List<OrderCard> cardList = new ArrayList<>();
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
            // 设置售出的卡密
            cardsService.updateById(cards1);

            if (!StringUtils.isEmpty(member.getEmail())) {
                if (isEmail(member.getEmail())) {
                    emailService.sendTextEmail("卡密购买成功", "您的卡密：" + cards.getCardInfo(), new String[]{member.getEmail()});
                }
            }
        }

        /**
         * 关联卡密
         */
        orderCardService.saveBatch(cardList);

        /**
         * 更新订单
         */
        Orders orders = new Orders();
        orders.setId(member.getId());
        orders.setStatus(1); // 设置已售出
        orders.setPayTime(new Date());
        orders.setPayNo(pay_no);
        orders.setPrice(new BigDecimal(price));
        orders.setMoney(new BigDecimal(money));

        ordersService.updateById(orders); // 更新售出

        return success;
    }

    /**
     * 判断是否为邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null == email || "".equals(email)){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
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

        System.out.println(mySign);

        return mySign;
    }

}
