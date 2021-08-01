package cn.zlianpay.common.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.common.system.mapper.RoleMapper;
import cn.zlianpay.common.system.entity.Role;
import cn.zlianpay.common.system.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现类
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
