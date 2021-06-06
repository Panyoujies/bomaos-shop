package cn.zlianpay.orders.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.web.*;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private PaysService paysService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private OrderCardService orderCardService;

    @Autowired
    private EmailService emailService;

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
    public PageResult<OrdersVo> page(HttpServletRequest request) {
        PageParam<Orders> pageParam = new PageParam<>(request);
        pageParam.setDefaultOrder(null, new String[]{"create_time"});
        List<Orders> records = ordersService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<OrdersVo> ordersVoList = records.stream().map((orders) -> {
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

            return ordersVo;
        }).collect(Collectors.toList());

        return new PageResult<>(ordersVoList, pageParam.getTotal());
    }

    /**
     * 分页查询
     */
    @OperLog(value = "管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/pageAll")
    public PageResult<Orders> pageall(HttpServletRequest request) {
        Map parameterMap = RequestParamsUtil.getParameterMap(request);
        String contact = (String) parameterMap.get("contact");
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("contact", contact)
                .or().eq("member", contact)
                .or().eq("pay_no", contact)
                .orderByDesc("create_time");

        List<Orders> ordersList = ordersService.list(wrapper);
        return new PageResult<>(ordersList, ordersList.size() + 1);
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
     * 修改商品状态
     */
    @OperLog(value = "商品列表管理", desc = "修改状态", result = true)
    @RequiresPermissions("orders:orders:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, String payNo, Integer productId) {

        /**
         * 通过订单号查询
         */
        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("id", id));
        if (member == null) {
            return JsonResult.error("没有这个订单"); // 本地没有这个订单
        }

        Products products = productsService.getById(productId);
        if (products == null) {
            return JsonResult.error("没有找到这个商品"); // 商品没了
        }

        List<Cards> card = cardsService.getCard(0, products.getId(), member.getNumber());

        if (card == null) {
            return JsonResult.error("卡密已售空！");
        }

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
        orders.setPayNo(payNo);
        orders.setPrice(member.getPrice());
        orders.setMoney(member.getPrice().multiply(new BigDecimal(member.getNumber())));

        if (ordersService.updateById(orders)) {
            return JsonResult.ok("补单成功！");
        }

        return JsonResult.error("补单失败");

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

}
