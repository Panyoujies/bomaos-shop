package cn.zlianpay.orders.service.impl;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.common.core.utils.DateUtil;
import cn.zlianpay.common.core.utils.StringUtil;
import cn.zlianpay.common.core.utils.UserAgentGetter;
import cn.zlianpay.orders.mapper.OrdersMapper;
import cn.zlianpay.orders.entity.Orders;
import cn.zlianpay.orders.service.OrdersService;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.mapper.ProductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表服务实现类
 * Created by Panyoujie on 2021-03-29 16:24:28
 */
@Service
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public PageResult<Orders> listPage(PageParam<Orders> page) {
        List<Orders> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Orders> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    /**
     * 创建订单
     *
     * @param productId 商品id
     * @param contact   购买者信息
     * @param number    购买数量
     * @param payType   支付驱动
     * @param request
     * @return
     */
    @Override
    public String buy(Integer productId, String contact, Integer number, String email, String payType, HttpServletRequest request) {

        Products products = productsMapper.selectById(productId);

        Orders orders = new Orders();
        orders.setPrice(products.getPrice());
        orders.setStatus(0); // 1 为支付，0未支付
        orders.setProductId(productId);
        orders.setContact(contact);
        orders.setPayType(payType);
        orders.setNumber(number); // 订单数量
        if (!StringUtils.isEmpty(email)) {
            orders.setEmail(email);
        }
        orders.setMember("TUD" + DateUtil.subData() + StringUtil.getRandomString(6));
        orders.setCreateTime(new Date());

        UserAgentGetter agentGetter = new UserAgentGetter(request);
        orders.setIp(agentGetter.getIp());
        orders.setDevice(agentGetter.getDevice());

        baseMapper.insert(orders);
        return orders.getMember();
    }

    @Override
    public Orders selectByMember(String member) {
        Orders orders = baseMapper.selectOne(new QueryWrapper<Orders>().eq("member", member));
        return orders;
    }

    @Override
    public boolean clearRemove() {
        QueryWrapper<Orders> status = new QueryWrapper<>();
        status.eq("status", 0);
        if (baseMapper.delete(status) >= 1) {
            return true;
        } else {
            return false;
        }
    }
}
