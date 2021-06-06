package cn.zlianpay.products.service.impl;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.products.entity.Products;
import cn.zlianpay.products.mapper.ClassifysMapper;
import cn.zlianpay.products.entity.Classifys;
import cn.zlianpay.products.mapper.ProductsMapper;
import cn.zlianpay.products.service.ClassifysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分类服务实现类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Service
@Transactional
public class ClassifysServiceImpl extends ServiceImpl<ClassifysMapper, Classifys> implements ClassifysService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public PageResult<Classifys> listPage(PageParam<Classifys> page) {
        List<Classifys> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Classifys> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        for (Serializable serializable : idList) {
            Integer count = productsMapper.selectCount(new QueryWrapper<Products>().eq("classify_id", serializable));
            if (count > 0) {
                return false;
            } else {
                baseMapper.deleteById(serializable);
            }
        }
        return true;
    }
}
