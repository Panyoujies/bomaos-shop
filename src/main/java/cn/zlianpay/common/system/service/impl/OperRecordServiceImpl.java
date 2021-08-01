package cn.zlianpay.common.system.service.impl;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.common.system.entity.OperRecord;
import cn.zlianpay.common.system.mapper.OperRecordMapper;
import cn.zlianpay.common.system.service.OperRecordService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 操作日志服务实现类
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Service
public class OperRecordServiceImpl extends ServiceImpl<OperRecordMapper, OperRecord> implements OperRecordService {

    @Override
    public PageResult<OperRecord> listPage(PageParam<OperRecord> page) {
        List<OperRecord> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<OperRecord> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Async
    @Override
    public void saveAsync(OperRecord operRecord) {
        baseMapper.insert(operRecord);
    }

}
