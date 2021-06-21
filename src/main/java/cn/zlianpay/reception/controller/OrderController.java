package cn.zlianpay.reception.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.common.core.pays.codepay.CodePaysConfig;
import cn.zlianpay.common.core.pays.jiepay.JiepaySend;
import cn.zlianpay.common.core.pays.xunhupay.PayUtils;
import cn.zlianpay.common.core.pays.zlianpay.ZlianPay;
import cn.zlianpay.common.core.web.BaseController;
import cn.zlianpay.common.core.web.JsonResult;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
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

    /**
     * 添加
     */
    @ResponseBody
    @RequestMapping("/buy")
    public JsonResult save(Integer goodsId, String contact, Integer number, String email, String payType, HttpServletRequest request) {

        if (StringUtils.isEmpty(goodsId)) {
            return JsonResult.error("商品不能为空");
        } else if (StringUtils.isEmpty(contact)) {
            return JsonResult.error("购买信息不能为空");
        } else if (contact.length() < 6) {
            return JsonResult.error("输入购买信息不得低于6位数");
        } else if (StringUtils.isEmpty(number)) {
            return JsonResult.error("商品数量不能小于或等于0");
        } else if (StringUtils.isEmpty(payType)) {
            return JsonResult.error("请选择付款方式！");
        }

        int count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", goodsId));
        if (count == 0) {
            return JsonResult.error("本商品已售空，请联系店长补货！");
        } else if (number > count) {
            return JsonResult.error("商品购买数量不能大于商品剩余数量！");
        }

        try {
            String buy = ordersService.buy(goodsId, contact, number, email, payType, request);
            return JsonResult.ok("订单创建成功！").setCode(200).setData(buy);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("订单创建失败");
        }
    }

    @OperLog(value = "支付", desc = "提交支付")
    @RequestMapping(value = "/pay/{member}", produces = "text/html")
    public String pay(Model model, @PathVariable("member") String member, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        Orders orders = ordersService.selectByMember(member);
        Products products = productsService.getById(orders.getProductId());

        String productDescription = products.getId().toString(); // 订单备注
        String ordersMember = orders.getMember(); // 订单号
        String goodsName = products.getName(); // 订单主题
        String cloudPayid = orders.getCloudPayid();

        BigDecimal bigDecimal = new BigDecimal(0.00);
        // 判断是不是批发商品
        if (products.getIsWholesale() == 1) {
            String wholesale = products.getWholesale();
            String[] split = wholesale.split("\\n");
            for (String s : split) {
                String[] split1 = s.split("=");
                if (orders.getNumber() >= Integer.parseInt(split1[0])) {
                    bigDecimal = new BigDecimal(split1[1]).multiply(new BigDecimal(orders.getNumber()));
                }
            }
        } else {
            bigDecimal = orders.getPrice().multiply(new BigDecimal(orders.getNumber()));
        }
        String price = bigDecimal.toString();

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", orders.getPayType()).eq("enabled", 1));

        if (orders.getPayType().equals("mqpay_alipay")) {
            Map createMqPay = mqPay.sendCreateMqPay(pays, price, ordersMember, cloudPayid, productDescription);
            Integer type = 2;

            String code = createMqPay.get("code").toString();
            if (code.equals("1")) {
                /**
                 * 先保存云订单id
                 */
                Orders orders1 = new Orders();
                orders1.setId(orders.getId());
                orders1.setCloudPayid(createMqPay.get("orderId").toString());
                ordersService.updateById(orders1);

                Map mapTypes = JSON.parseObject(pays.getConfig());
                String create_url = mapTypes.get("create_url").toString();

                model.addAttribute("url", create_url);
                model.addAttribute("price", createMqPay.get("reallyPrice"));
                model.addAttribute("result", createMqPay.get("payUrl").toString());
                model.addAttribute("orderId", createMqPay.get("orderId").toString());
                model.addAttribute("getOdId", orders.getId());
                model.addAttribute("type", type);
                model.addAttribute("ordersMember", ordersMember);

                model.addAttribute("date", createMqPay.get("date"));
                model.addAttribute("timeOut", createMqPay.get("timeOut"));
                model.addAttribute("state", createMqPay.get("state"));

                Website website = websiteService.getById(1);
                model.addAttribute("website", website);

                return "pay.html";
            } else if (code.equals("2")) {
                return "error/1001.html";
            } else {
                return "error/1000.html";
            }
        } else if (orders.getPayType().equals("mqpay_wxpay")) {
            Map createMqPay = mqPay.sendCreateMqPay(pays, price, ordersMember, cloudPayid, productDescription);
            Integer type = 1;
            String code = createMqPay.get("code").toString();
            if (code.equals("1")) {
                /**
                 * 先保存云订单id
                 */
                Orders orders1 = new Orders();
                orders1.setId(orders.getId());
                orders1.setCloudPayid(createMqPay.get("orderId").toString());
                ordersService.updateById(orders1);

                Map mapTypes = JSON.parseObject(pays.getConfig());
                model.addAttribute("price", createMqPay.get("reallyPrice"));
                model.addAttribute("result", createMqPay.get("payUrl").toString());
                model.addAttribute("getOdId", orders.getId());
                model.addAttribute("type", type);
                model.addAttribute("ordersMember", ordersMember);

                model.addAttribute("date", createMqPay.get("date"));
                model.addAttribute("timeOut", createMqPay.get("timeOut"));
                model.addAttribute("state", createMqPay.get("state"));

                Website website = websiteService.getById(1);
                model.addAttribute("website", website);

                return "pay.html";
            } else if (code.equals("2")) {
                return "error/1001.html";
            }  else {
                return "error/1000.html";
            }
        } else if (orders.getPayType().equals("codepay_wxpay") || orders.getPayType().equals("codepay_alipay")) {
            String url = CodePaysConfig.CodePayConfigInfo(pays, price, ordersMember, productDescription);
            response.sendRedirect(url);
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

            return "yunpay.html";
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
            return "xunhupay.html";
        } else if (orders.getPayType().equals("jiepay_wxpay") || orders.getPayType().equals("jiepay_alipay")) {
            String payUtils = JiepaySend.jiePayUtils(pays, price, ordersMember, productDescription);
            response.sendRedirect(payUtils);
        }

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        return "pay.html";
    }

    public static Integer DateToTimestamp(Date time) {
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime()) / 1000);
    }
}
