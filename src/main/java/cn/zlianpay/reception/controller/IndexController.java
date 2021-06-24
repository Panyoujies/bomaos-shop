package cn.zlianpay.reception.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.web.JsonResult;
import cn.zlianpay.settings.entity.Coupon;
import cn.zlianpay.settings.service.CouponService;
import cn.zlianpay.settings.vo.PaysVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.orders.vo.OrdersVo;
import cn.zlianpay.products.entity.Classifys;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ClassifysService;
import cn.zlianpay.products.service.ProductsService;
import cn.zlianpay.products.vo.ClassifysVo;
import cn.zlianpay.products.vo.ProductsVo;
import cn.zlianpay.products.vo.ProductsVos;
import cn.zlianpay.settings.entity.Pays;
import cn.zlianpay.settings.service.PaysService;
import cn.zlianpay.website.entity.Website;
import cn.zlianpay.website.service.WebsiteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private PaysService paysService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderCardService orderCardService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private CouponService couponService;

    @RequestMapping({"/","/index"})
    public String IndexView(Model model) {
        List<Classifys> classifysList = classifysService.list(new QueryWrapper<Classifys>().eq("status", 1));

        AtomicInteger index = new AtomicInteger(0);
        List<ClassifysVo> classifysVoList = classifysList.stream().map((classifys) -> {
            ClassifysVo classifysVo = new ClassifysVo();
            BeanUtils.copyProperties(classifys, classifysVo);
            int count = productsService.count(new QueryWrapper<Products>().eq("classify_id", classifys.getId()).eq("status", 1));
            classifysVo.setProductsMember(count);
            int andIncrement = index.getAndIncrement();
            classifysVo.setAndIncrement(andIncrement); // 索引
            return classifysVo;
        }).collect(Collectors.toList());
        model.addAttribute("classifysListJson", JSON.toJSONString(classifysVoList));

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        return "index.html";
    }

    @ResponseBody
    @RequestMapping("/getProductList")
    public JsonResult getProductList(Integer classifyId){
        List<Products> productsList = productsService.list(new QueryWrapper<Products>().eq("classify_id", classifyId).eq("status", 1));
        List<ProductsVo> productsVoList = productsList.stream().map((products) -> {
            ProductsVo productsVo = new ProductsVo();
            BeanUtils.copyProperties(products, productsVo);
            int count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0));
            productsVo.setCardMember(count);
            productsVo.setPrice(products.getPrice().toString());

            int count1 = couponService.count(new QueryWrapper<Coupon>().eq("product_id", products.getId()));
            productsVo.setIsCoupon(count1);

            return productsVo;
        }).collect(Collectors.toList());
        return JsonResult.ok("ok").setData(productsVoList);
    }

    /**
     * 商品购买页面
     * @param model
     * @param link
     * @return
     */
    @RequestMapping("/product/{link}")
    public String product(Model model, @PathVariable("link") String link) {
        // 查出商品
        Products products = productsService.getOne(new QueryWrapper<Products>().eq("link", link));
        // 查出分类
        Classifys classifys = classifysService.getById(products.getClassifyId());

        model.addAttribute("products", products);
        model.addAttribute("classifyName", classifys.getName());

        AtomicInteger index = new AtomicInteger(0);
        /**
         * 查出启用的支付驱动列表
         */
        List<Pays> paysList = paysService.list(new QueryWrapper<Pays>().eq("enabled", 1));
        List<PaysVo> paysVoList = paysList.stream().map((pays) -> {
            PaysVo paysVo = new PaysVo();
            BeanUtils.copyProperties(pays, paysVo);
            int andIncrement = index.getAndIncrement();
            paysVo.setAndIncrement(andIncrement); // 索引
            return paysVo;
        }).collect(Collectors.toList());

        model.addAttribute("paysList", paysVoList);

        if (products.getIsWholesale() == 1) {
            String wholesale = products.getWholesale();
            String[] wholesales = wholesale.split("\\n");

            List<Map<String, String>> list = new ArrayList<>();
            for (String s : wholesales) {
                String[] split = s.split("=");
                Map<String, String> map = new HashMap<>();
                map.put("number", split[0]);
                map.put("money", split[1]);
                list.add(map);
            }
            model.addAttribute("wholesaleList", list);
        }

        int isCoupon = couponService.count(new QueryWrapper<Coupon>().eq("product_id", products.getId()));
        model.addAttribute("isCoupon", isCoupon);

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        return "product.html";
    }

    @ResponseBody
    @RequestMapping("/getProductById")
    public JsonResult getProductById(Integer id) {
        Products products = productsService.getById(id);

        ProductsVos productsVos = new ProductsVos();
        BeanUtils.copyProperties(products, productsVos);
        productsVos.setId(products.getId());
        productsVos.setPrice(products.getPrice().toString());
        Integer count = getCardListCount(cardsService, products); // 计算卡密使用情况
        productsVos.setCardsCount(count.toString());

        return JsonResult.ok().setData(productsVos);
    }

    /**
     * 计算卡密使用情况
     * @param cardsService
     * @param products
     * @return
     */
    public static Integer getCardListCount(CardsService cardsService, Products products) {
        List<Cards> cardsList = cardsService.list(new QueryWrapper<Cards>().eq("product_id", products.getId()));
        Integer count = 0;
        for (Cards cards : cardsList) {
            if (cards.getStatus() == 0) {
                count++;
            }
        }
        return count;
    }

    @RequestMapping("/search")
    public String search(Model model) {
        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        return "search.html";
    }

    @RequestMapping("/search/order/{order}")
    public String searchOrder(Model model, @PathVariable("order") String order) {

        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", order));

        Products products = productsService.getById(member.getProductId());
        Classifys classifys = classifysService.getById(products.getClassifyId());

        List<OrderCard> ordersList = orderCardService.list(new QueryWrapper<OrderCard>().eq("order_id", member.getId()));

        List<Cards> cardsList = new ArrayList<>();
        for (OrderCard orderCard : ordersList) {
            Cards cards = cardsService.getById(orderCard.getCardId());
            cardsList.add(cards);
        }

        OrdersVo ordersVo = new OrdersVo();
        BeanUtils.copyProperties(member,ordersVo);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");//设置日期格式
        if (member.getPayTime() != null) {
            String date = df.format(member.getPayTime());// new Date()为获取当前系统时间，也可使用当前时间戳
            ordersVo.setPayTime(date);
        } else {
            ordersVo.setPayType(null);
        }

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        model.addAttribute("cardsList", cardsList); // 订单
        model.addAttribute("orders", ordersVo); // 订单
        model.addAttribute("goods", products);  // 商品
        model.addAttribute("classify", classifys);  // 分类
        return "order.html";
    }

    /**
     * 支付状态
     * @param model
     * @return
     */
    @RequestMapping("/pay/state/{payId}")
    public String payState(Model model, @PathVariable("payId") String payId) {
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member",payId));
        model.addAttribute("orderId",orders.getId());
        model.addAttribute("ordersMember",orders.getMember());

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        return "payState.html";
    }

    @ResponseBody
    @GetMapping("/getProductSearchList")
    public JsonResult getProductSearchList(Integer classifyId, String content) {
        List<Products> productsList = productsService.list(new QueryWrapper<Products>().eq("classify_id", classifyId).eq("status", 1).like("name", content));
        List<ProductsVo> productsVoList = productsList.stream().map((products) -> {
            ProductsVo productsVo = new ProductsVo();
            BeanUtils.copyProperties(products, productsVo);
            int count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0));
            productsVo.setCardMember(count);
            productsVo.setPrice(products.getPrice().toString());
            return productsVo;
        }).collect(Collectors.toList());
        return JsonResult.ok("查询成功！").setData(productsVoList);
    }
}
