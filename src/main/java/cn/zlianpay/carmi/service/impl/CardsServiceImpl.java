package cn.zlianpay.carmi.service.impl;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.mapper.CardsMapper;
import cn.zlianpay.carmi.service.CardsService;
import cn.zlianpay.carmi.vo.CardsDts;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 卡密服务实现类
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
@Service
@Transactional
public class CardsServiceImpl extends ServiceImpl<CardsMapper, Cards> implements CardsService {

    @Override
    public PageResult<Cards> listPage(PageParam<Cards> page) {
        List<Cards> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Cards> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public boolean addCards(CardsDts cardsDts) {

        String[] cardsInfo = cardsDts.getCardInfo().split("\\n");

        List<String> newlist = new ArrayList();
        for (int i = 0; i < cardsInfo.length; ++i) {
            if (cardsDts.getRepeat() == 0) {
                newlist.add(cardsInfo[i]);
            } else if (cardsDts.getRepeat() == 1 && !newlist.contains(cardsInfo[i])) {
                newlist.add(cardsInfo[i]);
            }
        }

        List<Cards> cardsArrayList = new ArrayList<>();
        for (String cardInfo : newlist) {
            Cards cards = new Cards();
            cards.setProductId(cardsDts.getProductId());
            cards.setCardInfo(cardInfo);
            cards.setStatus(0); // 设置未出售
            cards.setCreatedAt(new Date());
            cards.setUpdatedAt(new Date());
            cardsArrayList.add(cards);
        }

        boolean batch = this.saveBatch(cardsArrayList);

        return batch;
    }

    @Override
    public List<Cards> getCard(Integer status, Integer productId, Integer number) {
        return baseMapper.getCard(status, productId, number);
    }

}
