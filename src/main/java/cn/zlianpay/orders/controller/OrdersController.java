package cn.zlianpay.orders.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.utils.FormCheckUtil;
import cn.zlianpay.common.core.web.*;
import cn.zlianpay.orders.vo.OrderVos;
import cn.zlianpay.reception.controller.NotifyController;
import cn.zlianpay.reception.dto.SearchDTO;
import cn.zlianpay.settings.entity.ShopSettings;
import cn.zlianpay.settings.service.ShopSettingsService;
import cn.zlianpay.website.entity.Website;
import cn.zlianpay.website.service.WebsiteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.zlianpay.common.core.utils.RequestParamsUtil;
import cn.zlianpay.common.core.web.*;
import cn.zlianpay.common.core.annotation.OperLog;
import cn.zlianpay.common.system.service.EmailService;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.orders.vo.OrdersVo;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ProductsService;
import cn.zlianpay.settings.service.PaysService;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 订单表管理
 * Created by Panyoujie on 2021-03-29 16:24:28
 */
@Controller
@RequestMapping("/orders/orders")
public class OrdersController extends BaseController {

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

    @RequiresPermissions("orders:orders:view")
    @RequestMapping()
    public String view() {
        return "orders/orders.html";
    }

    /**
     * 分页查询订单表
     */
    @RequiresPermissions("orders:orders:list")
    @OperLog(value = "订单表管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult page(HttpServletRequest request) {
        PageParam<Orders> pageParam = new PageParam<>(request);
        pageParam.setDefaultOrder(null, new String[]{"create_time"});
        PageResult<Orders> ordersPageResult = ordersService.listPage(pageParam);
        List<OrdersVo> ordersVoList = ordersPageResult.getData().stream().map((orders) -> {
            OrdersVo ordersVo = new OrdersVo();
            BeanUtils.copyProperties(orders, ordersVo);

            Products products = productsService.getById(orders.getProductId());
            ordersVo.setProductName(products.getName());

            List<OrderCard> orderCardList = orderCardService.list(new QueryWrapper<OrderCard>().eq("order_id", orders.getId()));
            List<Cards> list = new ArrayList<>();
            for (OrderCard orderCard : orderCardList) {
                Cards cards = cardsService.getById(orderCard.getCardId());
                list.add(cards);
            }
            ordersVo.setCardInfo(list);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");//设置日期格式

            if (orders.getPayTime() != null) {
                String date = df.format(orders.getPayTime());// new Date()为获取当前系统时间，也可使用当前时间戳
                ordersVo.setPayTime(date);
            } else {
                ordersVo.setPayTime(null);
            }

            ordersVo.setMoney(orders.getMoney().toString());

            // 发货模式
            ordersVo.setShipType(products.getShipType());

            return ordersVo;
        }).collect(Collectors.toList());

        BigDecimal totalAmount = new BigDecimal(0.00);
        for (OrdersVo ordersVo : ordersVoList) {
            if (ordersVo.getStatus() >= 1) {
                totalAmount = totalAmount.add(new BigDecimal(ordersVo.getMoney())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            }
        }
        Map<String, String> totalRow = new HashMap<>();
        totalRow.put("money", totalAmount.toString());

        return JsonResult.ok("查询成功！").put("totalRow", totalRow).setData(ordersVoList);
    }

    /**
     * 分页查询
     */
    @OperLog(value = "管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/pageAll")
    public JsonResult pageall(HttpServletRequest request) {
        PageParam<Orders> pageParam = new PageParam<>(request);

        Map parameterMap = RequestParamsUtil.getParameterMap(request);
        String contact = (String) parameterMap.get("contact");
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("contact", contact)
                .or().eq("email", contact)
                .or().eq("member", contact)
                .or().eq("pay_no", contact)
                .orderByDesc("create_time");

        List<Orders> ordersList = ordersService.page(pageParam, wrapper).getRecords();

        AtomicInteger index = new AtomicInteger(0);
        List<SearchDTO> orderVosList = ordersList.stream().map((orders) -> {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");//设置日期格式
            String date = df.format(orders.getCreateTime());// new Date()为获取当前系统时间，也可使用当前时间戳

            SearchDTO searchDTO = new SearchDTO();
            searchDTO.setId(orders.getId().toString());
            Integer andIncrement = (Integer) index.getAndIncrement();
            searchDTO.setAndIncrement(andIncrement.toString());
            searchDTO.setCreateTime(date);
            searchDTO.setMoney(orders.getMoney().toString());
            if (orders.getPayType().equals("mqpay_alipay")
                    || orders.getPayType().equals("zlianpay_alipay")
                    || orders.getPayType().equals("yungouos_alipay")
                    || orders.getPayType().equals("xunhupay_alipay")
                    || orders.getPayType().equals("jiepay_alipay")
                    || orders.getPayType().equals("payjs_alipay")
                    || orders.getPayType().equals("yunfu_alipay")
                    || orders.getPayType().equals("alipay")) {
                searchDTO.setPayType("支付宝");
            } else if (orders.getPayType().equals("mqpay_wxpay")
                    || orders.getPayType().equals("zlianpay_wxpay")
                    || orders.getPayType().equals("yungouos_wxpay")
                    || orders.getPayType().equals("xunhupay_wxpay")
                    || orders.getPayType().equals("jiepay_wxpay")
                    || orders.getPayType().equals("payjs_wxpay")
                    || orders.getPayType().equals("yunfu_wxpay")
                    || orders.getPayType().equals("wxpay")
                    || orders.getPayType().equals("wxpay_h5")) {
                searchDTO.setPayType("微信");
            } else if (orders.getPayType().equals("paypal")) {
                searchDTO.setPayType("Paypal");
            }
            if (orders.getStatus() == 1) {
                searchDTO.setStatus("付款成功");
            } else if (orders.getStatus() == 2) {
                searchDTO.setStatus("待发货");
            } else if (orders.getStatus() == 3) {
                searchDTO.setStatus("已发货");
            } else {
                searchDTO.setStatus("未付款");
            }

            searchDTO.setMember(orders.getMember());

            return searchDTO;
        }).collect(Collectors.toList());

        return JsonResult.ok("查询成功！").setData(orderVosList);
    }

    /**
     * 查询全部订单表
     */
    @RequiresPermissions("orders:orders:list")
    @OperLog(value = "订单表管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Orders> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(ordersService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询订单表
     */
    @RequiresPermissions("orders:orders:list")
    @OperLog(value = "订单表管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(ordersService.getById(id));
    }

    /**
     * 添加订单表
     */
    @RequiresPermissions("orders:orders:save")
    @OperLog(value = "订单表管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Orders orders) {
        if (ordersService.save(orders)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改订单表
     */
    @RequiresPermissions("orders:orders:update")
    @OperLog(value = "订单表管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Orders orders) {
        if (ordersService.updateById(orders)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除订单表
     */
    @RequiresPermissions("orders:orders:remove")
    @OperLog(value = "订单表管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (ordersService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加订单表
     */
    @RequiresPermissions("orders:orders:save")
    @OperLog(value = "订单表管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Orders> list) {
        if (ordersService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改订单表
     */
    @RequiresPermissions("orders:orders:update")
    @OperLog(value = "订单表管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Orders> batchParam) {
        if (batchParam.update(ordersService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除订单表
     */
    @RequiresPermissions("orders:orders:remove")
    @OperLog(value = "订单表管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (ordersService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量删除订单表
     */
    @RequiresPermissions("orders:orders:remove")
    @OperLog(value = "订单表管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/clearRemove")
    public JsonResult clearRemove() {
        if (ordersService.clearRemove()) {
            return JsonResult.ok("清理未支付的订单成功！");
        }
        return JsonResult.error("没有可以清理的订单！");
    }

    /**
     * 批量删除订单表
     */
    @RequiresPermissions("orders:orders:remove")
    @OperLog(value = "订单表管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/clearAllRemove")
    public JsonResult clearAllRemove() {
        if (ordersService.clearAllRemove()) {
            return JsonResult.ok("清理的订单成功！");
        }
        return JsonResult.error("没有可以清理的订单！");
    }

    /**
     * 删除订单表
     */
    @RequiresPermissions("orders:orders:remove")
    @OperLog(value = "订单表管理", desc = "订单删除", result = true)
    @ResponseBody
    @RequestMapping("/deleteById")
    public JsonResult deleteById(Integer id) {
        if (ordersService.deleteById(id)) {
            return JsonResult.ok("删除订单成功！");
        }
        return JsonResult.error("没有可以删除的订单！");
    }

    /**
     * @param id       商品id
     * @param shipInfo 需要发货的内容
     * @return
     */
    @OperLog(value = "商品列表管理", desc = "手动发货", result = true)
    @RequiresPermissions("orders:orders:update")
    @ResponseBody
    @RequestMapping("/sendShip")
    public JsonResult sendShip(Integer id, String email, String shipInfo) throws MessagingException, IOException {

        /**
         * 查出订单
         */
        Orders orders = ordersService.getById(id);
        Products products = productsService.getById(orders.getProductId()); // 查出对应的商品

        Cards cards = new Cards();
        cards.setCardInfo(shipInfo);
        cards.setCreatedAt(new Date());
        cards.setProductId(products.getId());
        cards.setStatus(1); // 默认已使用
        cards.setUpdatedAt(new Date());

        if (cardsService.save(cards)) {
            OrderCard orderCard = new OrderCard();
            orderCard.setCardId(cards.getId());
            orderCard.setOrderId(orders.getId());
            orderCard.setCreatedAt(new Date());
            orderCardService.save(orderCard); // 关联卡密
        }

        Orders orders1 = new Orders();
        orders1.setId(orders.getId());
        orders1.setStatus(3);

        if (ordersService.updateById(orders1)) { // 成功发送邮件
            if (FormCheckUtil.isEmail(email)) {
                Website website = websiteService.getById(1);
                Map<String, Object> map = new HashMap<>();  // 页面的动态数据
                map.put("title", website.getWebsiteName());
                map.put("member", orders.getMember());
                map.put("date", new Date());
                map.put("info", shipInfo);
                try {
                    ShopSettings shopSettings = shopSettingsService.getById(1);
                    if (shopSettings.getIsEmail() == 1) {
                        emailService.sendHtmlEmail(website.getWebsiteName() + "发货提醒", "email/sendShip.html", map, new String[]{email});
                        return JsonResult.ok("发货成功、并邮件通知成功。");
                    }
                    return JsonResult.ok("发货成功！");
                } catch (AuthenticationFailedException e) {
                    return JsonResult.ok("发货成功、邮件提醒失败！");
                }
            }
            return JsonResult.ok("发货成功！邮件提醒失败！");
        }
        return JsonResult.error("发货失败！");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "商品列表管理", desc = "修改状态", result = true)
    @RequiresPermissions("orders:orders:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, String payNo, Integer productId) {

        Orders member = ordersService.getById(id);
        if (member == null) return JsonResult.error("没有找到相关订单"); // 本地没有这个订单

        int count = orderCardService.count(new QueryWrapper<OrderCard>().eq("order_id", member.getId()));
        if (count >= 1) return JsonResult.ok("已经支付成功！自动发卡成功，补单失败");

        Products products = productsService.getById(productId);
        if (products == null) return JsonResult.error("该订单的商品找不到！"); // 商品没了

        Website website = websiteService.getById(1);
        ShopSettings shopSettings = shopSettingsService.getById(1);

        if (products.getShipType() == 0) { // 自动发货的商品
            List<Cards> card = cardsService.getCard(0, products.getId(), member.getNumber());
            if (card == null) return JsonResult.error("卡密为空！请补充后再试。");

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
                            return JsonResult.error("补单失败、邮箱系统配置错误！！");
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
            products1.setInventory(products.getInventory() - 1);
            products1.setSales(products.getSales() + 1);

            if (shopSettings.getIsWxpusher() == 1) {
                Message message = new Message();
                message.setContent(website.getWebsiteName() + "新订单提醒<br>订单号：<span style='color:red;'>" + member.getMember() + "</span><br>商品名称：<span>" + products.getName() + "</span><br>订单金额：<span>" + member.getMoney() + "</span><br>支付状态：<span style='color:green;'>成功</span><br>");
                message.setContentType(Message.CONTENT_TYPE_HTML);
                message.setUid(shopSettings.getWxpushUid());
                message.setAppToken(shopSettings.getAppToken());
                WxPusher.send(message);
            }
            if (shopSettings.getIsEmail() == 1) {
                if (FormCheckUtil.isEmail(member.getEmail())) {
                    try {
                        emailService.sendTextEmail("订单提醒", "您的订单号为：" + member.getMember() + " 本商品为手动发货，请耐心等待！", new String[]{member.getEmail()});
                    } catch (Exception e) {
                        return JsonResult.error("补单失败、邮箱系统配置错误！！");
                    }
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
        orders.setPayNo(payNo);
        orders.setPrice(member.getPrice());
        orders.setMoney(member.getMoney());

        boolean b = ordersService.updateById(orders);// 更新售出

        return JsonResult.ok("补单成功！！");
    }

}
