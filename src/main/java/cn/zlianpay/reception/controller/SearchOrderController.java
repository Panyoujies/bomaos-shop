package cn.zlianpay.reception.controller;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.web.BaseApiController;
import cn.zlianpay.common.system.entity.User;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.orders.vo.OrdersVo;
import cn.zlianpay.products.entity.Classifys;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ClassifysService;
import cn.zlianpay.products.service.ProductsService;
import cn.zlianpay.settings.entity.ShopSettings;
import cn.zlianpay.settings.service.ShopSettingsService;
import cn.zlianpay.theme.entity.Theme;
import cn.zlianpay.theme.service.ThemeService;
import cn.zlianpay.website.entity.Website;
import cn.zlianpay.website.service.WebsiteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class SearchOrderController extends BaseApiController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private OrderCardService orderCardService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private WebsiteService websiteService;

    @RequestMapping("/order")
    public String Chat(Model model, HttpServletRequest request) {
        Long orderId = getLoginUserId(request);

        Orders member = ordersService.getById(orderId);

        /**
         * 商品没有找到
         */
        Theme theme = themeService.getOne(new QueryWrapper<Theme>().eq("enable", 1));
        if (ObjectUtils.isEmpty(member)) return "theme/" + theme.getDriver() + "/error.html";

        Products products = productsService.getById(member.getProductId());
        Classifys classifys = classifysService.getById(products.getClassifyId());

        List<OrderCard> ordersList = orderCardService.list(new QueryWrapper<OrderCard>().eq("order_id", member.getId()));
        List<String> cardsList = new ArrayList<>();
        for (OrderCard orderCard : ordersList) {
            Cards cards = cardsService.getById(orderCard.getCardId());

            String cardInfo = cards.getCardInfo();
            StringBuilder builder = new StringBuilder();

            if (products.getShipType() == 0) {
                if (cardInfo.contains(" ")) {
                    String[] split = cardInfo.split(" ");
                    builder.append("卡号：").append(split[0]).append(" ").append("卡密：").append(split[1]).append("\n");
                    cardsList.add(builder.toString());
                } else {
                    builder.append("卡密：").append(cardInfo).append("\n");
                    cardsList.add(builder.toString());
                }
            } else {
                builder.append(cardInfo);
                cardsList.add(builder.toString());
            }
        }

        OrdersVo ordersVo = new OrdersVo();
        BeanUtils.copyProperties(member,ordersVo);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");//设置日期格式
        if (member.getPayTime() != null) {
            String date = df.format(member.getPayTime());// new Date()为获取当前系统时间，也可使用当前时间戳
            ordersVo.setPayTime(date);
        } else {
            ordersVo.setPayTime(null);
        }

        ordersVo.setMoney(member.getMoney().toString());

        /**
         * 发货模式
         */
        ordersVo.setShipType(products.getShipType());

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        model.addAttribute("cardsList", cardsList); // 订单
        model.addAttribute("orders", ordersVo); // 订单
        model.addAttribute("goods", products);  // 商品
        model.addAttribute("classify", classifys);  // 分类

        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());

        return "theme/" + theme.getDriver() + "/order.html";
    }

}
