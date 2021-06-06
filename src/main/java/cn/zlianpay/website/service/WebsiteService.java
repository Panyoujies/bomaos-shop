package cn.zlianpay.website.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.website.entity.Website;

import java.util.List;
import java.util.Map;

/**
 * 网站设置服务类
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
public interface WebsiteService extends IService<Website> {

    /**
     * 分页查询
     */
    PageResult<Website> listPage(PageParam<Website> page);

    /**
     * 查询所有
     */
    List<Website> listAll(Map<String, Object> page);

}
