package cn.zlianpay.products.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.products.entity.Classifys;

import java.util.List;
import java.util.Map;

/**
 * 分类服务类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
public interface ClassifysService extends IService<Classifys> {

    /**
     * 分页查询
     */
    PageResult<Classifys> listPage(PageParam<Classifys> page);

    /**
     * 查询所有
     */
    List<Classifys> listAll(Map<String, Object> page);

}
