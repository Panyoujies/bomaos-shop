package cn.zlianpay.carmi.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.carmi.entity.OrderCard;

import java.util.List;
import java.util.Map;

/**
 * 订单关联卡密表服务类
 * Created by Panyoujie on 2021-03-29 22:27:41
 */
public interface OrderCardService extends IService<OrderCard> {

    /**
     * 分页查询
     */
    PageResult<OrderCard> listPage(PageParam<OrderCard> page);

    /**
     * 查询所有
     */
    List<OrderCard> listAll(Map<String, Object> page);

}
