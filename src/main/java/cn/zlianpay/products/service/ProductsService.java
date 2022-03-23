package cn.zlianpay.products.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.products.entity.Products;

import java.util.List;
import java.util.Map;

/**
 * 商品服务类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
public interface ProductsService extends IService<Products> {

    /**
     * 分页查询
     */
    PageResult<Products> listPage(PageParam<Products> page);

    /**
     * 查询所有
     */
    List<Products> listAll(Map<String, Object> page);


    List<Products> getRandomProductList(int limit);
}
