package cn.zlianpay.theme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import cn.zlianpay.theme.entity.Theme;

import java.util.List;
import java.util.Map;

/**
 * 主题配置服务类
 * Created by Panyoujie on 2021-06-28 00:36:29
 */
public interface ThemeService extends IService<Theme> {

    /**
     * 分页查询
     */
    PageResult<Theme> listPage(PageParam<Theme> page);

    /**
     * 查询所有
     */
    List<Theme> listAll(Map<String, Object> page);

}
