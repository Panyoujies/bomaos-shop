package cn.zlianpay.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import cn.zlianpay.content.mapper.CarouselMapper;
import cn.zlianpay.content.entity.Carousel;
import cn.zlianpay.content.service.CarouselService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务实现类
 * Created by Panyoujie on 2021-11-10 02:54:31
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {

    @Override
    public PageResult<Carousel> listPage(PageParam<Carousel> page) {
        List<Carousel> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Carousel> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
