package cn.zlianpay.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import cn.zlianpay.content.entity.Carousel;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务类
 * Created by Panyoujie on 2021-11-10 02:54:31
 */
public interface CarouselService extends IService<Carousel> {

    /**
     * 分页查询
     */
    PageResult<Carousel> listPage(PageParam<Carousel> page);

    /**
     * 查询所有
     */
    List<Carousel> listAll(Map<String, Object> page);

}
