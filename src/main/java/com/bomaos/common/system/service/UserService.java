package com.bomaos.common.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务类
 * Created by Panyoujie on 2018-12-24 16:10
 */
public interface UserService extends IService<User> {

    /**
     * 根据账号查询用户
     */
    User getByUsername(String username);

    /**
     * 根据id查询用户(关联查询)
     */
    User getFullById(Integer userId);

    /**
     * 查询用户角色和权限
     */
    User selectRoleAndAuth(User user);

    /**
     * 关联分页查询用户
     */
    PageResult<User> listPage(PageParam<User> page);

    /**
     * 关联查询全部用户
     */
    List<User> listAll(Map<String, Object> page);

    /**
     * 添加用户(包含角色)
     */
    boolean saveUser(User user);

    /**
     * 修改用户(包含角色)
     */
    boolean updateUser(User user);

    /**
     * 比较用户密码
     *
     * @param dbPsw    数据库存储的密码
     * @param inputPsw 用户输入的密码
     * @return boolean
     */
    boolean comparePsw(String dbPsw, String inputPsw);

    /**
     * md5加密用户密码
     */
    String encodePsw(String psw);

}
