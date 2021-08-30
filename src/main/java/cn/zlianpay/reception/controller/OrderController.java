package cn.zlianpay.reception.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.pays.alipay.SendAlipay;
import cn.zlianpay.common.core.pays.jiepay.JiepaySend;
import cn.zlianpay.common.core.pays.payjs.sendPayjs;
import cn.zlianpay.common.core.pays.paypal.PaypalSend;
import cn.zlianpay.common.core.pays.paypal.config.PaypalPaymentIntent;
import cn.zlianpay.common.core.pays.paypal.config.PaypalPaymentMethod;
import cn.zlianpay.common.core.pays.wxpay.SendWxPay;
import cn.zlianpay.common.core.pays.xunhupay.PayUtils;
import cn.zlianpay.common.core.pays.yunfupay.SendYunfu;
import cn.zlianpay.common.core.pays.zlianpay.ZlianPay;
import cn.zlianpay.common.core.utils.FormCheckUtil;
import cn.zlianpay.common.core.utils.UserAgentGetter;
import cn.zlianpay.common.core.web.BaseController;
import cn.zlianpay.common.core.web.JsonResult;
import cn.zlianpay.common.system.service.EmailService;
import cn.zlianpay.settings.entity.Coupon;
import cn.zlianpay.settings.entity.ShopSettings;
import cn.zlianpay.settings.service.CouponService;
import cn.zlianpay.settings.service.ShopSettingsService;
import cn.zlianpay.theme.entity.Theme;
import cn.zlianpay.theme.service.ThemeService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.common.core.annotation.OperLog;
import cn.zlianpay.common.core.pays.mqpay.mqPay;
import cn.zlianpay.common.core.pays.yungouos.YunGouosConfig;
import cn.zlianpay.common.core.utils.StringUtil;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ProductsService;
import cn.zlianpay.settings.entity.Pays;
import cn.zlianpay.settings.service.PaysService;
import cn.zlianpay.website.entity.Website;
import cn.zlianpay.website.service.WebsiteService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@Transactional
public class OrderController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private PaysService paysService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderCardService orderCardService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    /**
     * 添加
     */
    @ResponseBody
    @RequestMapping("/buy")
    public JsonResult save(Integer goodsId, Integer number, String contact, String coupon, String payType, String password, HttpServletResponse response, HttpServletRequest request) {

        if (StringUtils.isEmpty(goodsId)) {
            return JsonResult.error("商品不能为空");
        } else if (StringUtils.isEmpty(contact)) {
            return JsonResult.error("联系方式不能为空！");
        } else if (StringUtils.isEmpty(number)) {
            return JsonResult.error("商品数量不能小于或等于0");
        } else if (StringUtils.isEmpty(payType)) {
            return JsonResult.error("请选择付款方式！");
        }

        Products products = productsService.getById(goodsId);
        if (products.getRestricts() >= 1) { // 判断是不是限购
            if (number > products.getRestricts()) { // 判断限购
                return JsonResult.error("本商品最多只能购买" + products.getRestricts() + "个、请核对后再试！");
            }
        }

        if (!StringUtils.isEmpty(products.getIsPassword())) {
            if (products.getIsPassword() == 1) {
                if (StringUtils.isEmpty(password)) {
                    return JsonResult.error("商品查询密码不能为空！！");
                }
            }
        }

        if (products.getShipType() == 0) { // 自动发货模式
            int count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", goodsId).eq("status", 0));
            if (count == 0) {
                return JsonResult.error("本商品已售空，请联系店长补货！");
            } else if (number > count) {
                return JsonResult.error("商品购买数量不能大于商品剩余数量！");
            }
        } else { // 手动发货模式
            if (products.getInventory() == 0) {
                return JsonResult.error("本商品已售空，请联系店长补货！");
            } else if (number > products.getInventory()) {
                return JsonResult.error("商品购买数量不能大于商品剩余数量！");
            }
        }

        try {
            Integer couponId = null;
            if (!StringUtils.isEmpty(coupon)) {
                QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("product_id", goodsId) // 商品id
                        .eq("coupon", coupon) // 优惠券代码
                        .eq("status", 0); // 没有使用的

                Coupon coupon1 = couponService.getOne(queryWrapper);
                if (!ObjectUtils.isEmpty(coupon1)) { // 判断 coupon1 是否不为空
                    /**
                     * 拿到优惠券 entity
                     */
                    couponId = coupon1.getId();
                } else {
                    /**
                     * 给前端返回优惠券失效或者为空的信息
                     */
                    return JsonResult.error("该优惠券代码已被使用过，或不能使用在本商品，请核对后再试！");
                }
            }
            Map<String, String> buy = ordersService.buy(goodsId, number, contact, couponId, payType, password, request);
            Cookie[] cookies = request.getCookies();
            if (ObjectUtils.isEmpty(cookies)) {
                /**
                 * 创建 cookie
                 * 将订单信息保存到浏览器
                 */
                Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", buy.get("member"));
                cookie1.setMaxAge(7 * 24 * 60 * 60); // 7天过期
                // 将cookie对象加入response响应
                response.addCookie(cookie1);
            } else {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if ("BROWSER_ORDERS_CACHE".equals(cookieName)) {
                        String cookieValue = cookie.getValue();
                        /**
                         * 创建 cookie
                         * 将订单信息保存到浏览器
                         */
                        Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", cookieValue + "=" + buy.get("member"));
                        cookie1.setMaxAge(7 * 24 * 60 * 60); // 7天过期
                        // 将cookie对象加入response响应
                        response.addCookie(cookie1);
                        break;
                    } else {
                        /**
                         * 创建 cookie
                         * 将订单信息保存到浏览器
                         */
                        Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", buy.get("member"));
                        cookie1.setMaxAge(7 * 24 * 60 * 60); // 7天过期
                        // 将cookie对象加入response响应
                        response.addCookie(cookie1);
                    }
                }
            }

            return JsonResult.ok("订单创建成功！").setCode(200).setData(buy);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("订单创建失败");
        }
    }

    @OperLog(value = "支付", desc = "提交支付")
    @RequestMapping(value = "/pay/{member}", produces = "text/html")
    public String pay(Model model, @PathVariable("member") String member, HttpServletResponse response, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        Orders orders = ordersService.selectByMember(member);
        Products products = productsService.getById(orders.getProductId());

        String productDescription = products.getId().toString(); // 订单备注
        String ordersMember = orders.getMember(); // 订单号
        String goodsName = products.getName(); // 订单主题
        String cloudPayid = orders.getCloudPayid();
        String price = orders.getMoney().toString();
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", orders.getPayType()).eq("enabled", 1));

        if (price.equals("0.00")) { // 0元商品 直接完成支付
            long time = new Date().getTime();
            String toString = Long.toString(time);
            boolean big = returnBig(price, price, orders.getMember(), toString, productDescription);
            if (big) {
                response.sendRedirect("/search/order/" + orders.getMember());
                return null;
            }
        }

        if (orders.getPayType().equals("mqpay_alipay")) {
            String createMqPay = mqPay.sendCreateMqPay(pays, price, ordersMember, cloudPayid, productDescription);
            response.sendRedirect(createMqPay);
            return null;
        } else if (orders.getPayType().equals("mqpay_wxpay")) {
            String createMqPay = mqPay.sendCreateMqPay(pays, price, ordersMember, cloudPayid, productDescription);
            response.sendRedirect(createMqPay);
            return null;
        } else if (orders.getPayType().equals("zlianpay_wxpay") || orders.getPayType().equals("zlianpay_alipay")) {
            String url = ZlianPay.zlianSendPay(pays, price, ordersMember, productDescription);
            response.sendRedirect(url);
            return null;
        } else if (orders.getPayType().equals("yungouos_wxpay") || orders.getPayType().equals("yungouos_alipay")) {
            String gouos = "";
            if (orders.getPayType().equals("yungouos_wxpay")) {
                model.addAttribute("type", 1);
                gouos = YunGouosConfig.yunGouosWxPay(pays, price, ordersMember, goodsName, productDescription);
            } else if (orders.getPayType().equals("yungouos_alipay")) {
                model.addAttribute("type", 2);
                gouos = YunGouosConfig.yunGouosAliPay(pays, price, ordersMember, goodsName, productDescription);
            }

            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", JSON.toJSONString(gouos));
            model.addAttribute("orderId", orders.getId());

            Website website = websiteService.getById(1);
            model.addAttribute("website", website);

            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());

            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/yunpay.html";
        } else if (orders.getPayType().equals("xunhupay_wxpay") || orders.getPayType().equals("xunhupay_alipay")) {
            if (orders.getPayType().equals("xunhupay_wxpay")) {
                model.addAttribute("type", 1);
            } else if (orders.getPayType().equals("xunhupay_alipay")) {
                model.addAttribute("type", 2);
            }

            Map pay = PayUtils.pay(getWebName(), pays, goodsName, price, ordersMember, productDescription);
            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", pay.get("url_qrcode"));
            model.addAttribute("wap", pay.get("url1"));
            model.addAttribute("orderId", orders.getId());

            Website website = websiteService.getById(1);
            model.addAttribute("website", website);

            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());

            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/xunhupay.html";
        } else if (orders.getPayType().equals("jiepay_wxpay") || orders.getPayType().equals("jiepay_alipay")) {
            String payUtils = JiepaySend.jiePayUtils(pays, price, ordersMember, productDescription);
            response.sendRedirect(payUtils);
            return null;
        } else if (orders.getPayType().equals("payjs_wxpay") || orders.getPayType().equals("payjs_alipay")) {
            String payjs = "";
            if (orders.getPayType().equals("payjs_wxpay")) {
                model.addAttribute("type", 1);
                payjs = sendPayjs.pay(pays, price, ordersMember, goodsName, productDescription);
            } else if (orders.getPayType().equals("payjs_alipay")) {
                model.addAttribute("type", 2);
                payjs = sendPayjs.pay(pays, price, ordersMember, goodsName, productDescription);
            }

            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", JSON.toJSONString(payjs));
            model.addAttribute("orderId", orders.getId());

            Website website = websiteService.getById(1);
            model.addAttribute("website", website);

            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());

            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/yunpay.html";
        } else if (orders.getPayType().equals("yunfu_wxpay") || orders.getPayType().equals("yunfu_alipay")) {
            String yunfu = "";

            if (orders.getPayType().equals("yunfu_wxpay")) {
                model.addAttribute("type", 1);
                yunfu = SendYunfu.pay(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
            } else if (orders.getPayType().equals("yunfu_alipay")) {
                model.addAttribute("type", 2);
                yunfu = SendYunfu.pay(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
            }

            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", JSON.toJSONString(yunfu));
            model.addAttribute("orderId", orders.getId());

            Website website = websiteService.getById(1);
            model.addAttribute("website", website);

            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());

            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/yunpay.html";
        } else if (pays.getDriver().equals("wxpay")) { // 微信扫码支付
            String pay = SendWxPay.payNattve(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
            model.addAttribute("type", 1); // 微信支付
            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", JSON.toJSONString(pay));
            model.addAttribute("orderId", orders.getId());
            Website website = websiteService.getById(1);
            model.addAttribute("website", website);
            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());
            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/yunpay.html";
        } else if (pays.getDriver().equals("wxpay_h5")) {
            String pay = SendWxPay.payMweb(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
            response.sendRedirect(pay);
            return null;
        } else if (pays.getDriver().equals("alipay")) {
            String pay = SendAlipay.pay(pays, price, ordersMember, goodsName, productDescription, request);
            model.addAttribute("type", 2); // 支付宝当面付
            model.addAttribute("goodsName", goodsName);
            model.addAttribute("price", price);
            model.addAttribute("ordersMember", ordersMember);
            model.addAttribute("result", JSON.toJSONString(pay));
            model.addAttribute("orderId", orders.getId());

            Website website = websiteService.getById(1);
            model.addAttribute("website", website);

            ShopSettings shopSettings = shopSettingsService.getById(1);
            model.addAttribute("isBackground", shopSettings.getIsBackground());

            Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
            return "theme/" + theme.getDriver() + "/yunpay.html";
        } else if (pays.getDriver().equals("paypal")) {
            try {
                Payment payment = PaypalSend.createPayment(pays, price, "USD", PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, ordersMember);
                for (Links links : payment.getLinks()) {
                    System.out.println(links.toString());
                    if (links.getRel().equals("approval_url")) {
                        System.out.println(links.getHref());
                        return "redirect:" + links.getHref();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    /**
     * 业务处理
     * @param money 实收款金额
     * @param price 订单金额
     * @param payId 订单号
     * @param pay_no 流水号
     * @param param 自定义内容
     * @return this
     */
    private boolean returnBig(String money, String price, String payId, String pay_no, String param) {

        /**
         * 通过订单号查询
         */
        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", payId));
        if (member == null) return false; // 本地没有这个订单

        int count = orderCardService.count(new QueryWrapper<OrderCard>().eq("order_id", member.getId()));
        if (count >= 1)  return true;

        Products products = productsService.getById(param);
        if (products == null) return false; // 商品没了

        Website website = websiteService.getById(1);
        ShopSettings shopSettings = shopSettingsService.getById(1);

        if (products.getShipType() == 0) { // 自动发货的商品
            List<Cards> card = cardsService.getCard(0, products.getId(), member.getNumber());
            if (card == null) return false;

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
        return b;
    }

}
