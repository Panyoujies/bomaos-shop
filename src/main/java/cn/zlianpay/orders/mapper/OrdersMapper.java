package cn.zlianpay.orders.mapper;

import cn.zlianpay.common.core.web.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zlianpay.orders.entity.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单表Mapper接口
 * Created by Panyoujie on 2021-03-29 16:24:28
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 分页查询
     */
    List<Orders> listPage(@Param("page") PageParam<Orders> page);

    /**
     * 查询全部
     */
    List<Orders> listAll(@Param("page") Map<String, Object> page);

}
