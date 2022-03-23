package cn.zlianpay.products.service.impl;

import cn.zlianpay.carmi.entity.Cards;
import cn.zlianpay.carmi.mapper.CardsMapper;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.products.mapper.ProductsMapper;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品服务实现类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Service
@Transactional
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {

    @Autowired
    private CardsMapper cardsMapper;

    @Override
    public PageResult<Products> listPage(PageParam<Products> page) {
        List<Products> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Products> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public List<Products> getRandomProductList(int limit) {
        return baseMapper.getRandomProductList(limit);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        for (Serializable serializable : idList) {
            Integer count = cardsMapper.selectCount(new QueryWrapper<Cards>().eq("product_id", serializable));
            if (count > 0) {
                return false;
            } else {
                baseMapper.deleteById(serializable);
            }
        }
        return true;
    }
}
