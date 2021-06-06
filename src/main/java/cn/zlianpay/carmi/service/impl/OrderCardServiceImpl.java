package cn.zlianpay.carmi.service.impl;

import cn.zlianpay.carmi.entity.OrderCard;
import cn.zlianpay.carmi.mapper.OrderCardMapper;
import cn.zlianpay.carmi.service.OrderCardService;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 订单关联卡密表服务实现类
 * Created by Panyoujie on 2021-03-29 22:27:41
 */
@Service
@Transactional
public class OrderCardServiceImpl extends ServiceImpl<OrderCardMapper, OrderCard> implements OrderCardService {

    @Override
    public PageResult<OrderCard> listPage(PageParam<OrderCard> page) {
        List<OrderCard> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<OrderCard> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
