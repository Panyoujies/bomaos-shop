package cn.zlianpay.carmi.mapper;

import cn.zlianpay.common.core.web.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zlianpay.carmi.entity.OrderCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单关联卡密表Mapper接口
 * Created by Panyoujie on 2021-03-29 22:27:41
 */
public interface OrderCardMapper extends BaseMapper<OrderCard> {

    /**
     * 分页查询
     */
    List<OrderCard> listPage(@Param("page") PageParam<OrderCard> page);

    /**
     * 查询全部
     */
    List<OrderCard> listAll(@Param("page") Map<String, Object> page);

}
