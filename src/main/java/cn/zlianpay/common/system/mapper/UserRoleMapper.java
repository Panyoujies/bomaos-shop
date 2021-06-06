package cn.zlianpay.common.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zlianpay.common.system.entity.Role;
import cn.zlianpay.common.system.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Mapper接口
 * Created by wangfan on 2018-12-24 16:10
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 批量添加用户角色
     */
    int insertBatch(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

    /**
     * 批量查询用户角色
     */
    List<Role> listByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 查询某个用户的角色
     */
    List<Role> listByUserId(@Param("userId") Integer userId);

}
