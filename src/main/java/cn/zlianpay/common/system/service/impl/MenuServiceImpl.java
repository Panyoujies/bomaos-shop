package cn.zlianpay.common.system.service.impl;

import cn.zlianpay.common.core.web.PageParam;
import cn.zlianpay.common.core.web.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zlianpay.common.system.mapper.MenuMapper;
import cn.zlianpay.common.system.entity.Menu;
import cn.zlianpay.common.system.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 菜单服务实现类
 * Created by wangfan on 2018-12-24 16:10
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public PageResult<Menu> listPage(PageParam<Menu> pageParam) {
        return new PageResult<>(baseMapper.listPage(pageParam), pageParam.getTotal());
    }

    @Override
    public List<Menu> getUserMenu(Integer userId, Integer menuType) {
        return baseMapper.listByUserId(userId, menuType);
    }

    @Override
    public List<Menu> toMenuTree(List<Menu> menus, Integer parentId) {
        List<Menu> list = new ArrayList<>();
        for (Menu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(toMenuTree(menus, menu.getMenuId()));
                list.add(menu);
            }
        }
        return list;
    }

}
