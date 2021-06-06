package cn.zlianpay.settings.service;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.settings.entity.Pays;

import java.util.List;
import java.util.Map;

/**
 * 支付配置服务类
 * Created by Panyoujie on 2021-03-29 11:06:11
 */
public interface PaysService extends IService<Pays> {

    /**
     * 分页查询
     */
    PageResult<Pays> listPage(PageParam<Pays> page);

    /**
     * 查询所有
     */
    List<Pays> listAll(Map<String, Object> page);

}
