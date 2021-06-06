package cn.zlianpay.carmi.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.vo.CardsDts;

import java.util.List;
import java.util.Map;

/**
 * 卡密服务类
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
public interface CardsService extends IService<Cards> {

    /**
     * 分页查询
     */
    PageResult<Cards> listPage(PageParam<Cards> page);

    /**
     * 查询所有
     */
    List<Cards> listAll(Map<String, Object> page);

    boolean addCards(CardsDts cardsDts);

    List<Cards> getCard(Integer status, Integer productId, Integer number);

}
