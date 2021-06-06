package cn.zlianpay.common.core.web;

import cn.zlianpay.common.system.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Controller基类
 * Created by wangfan on 2017-06-10 10:10
 */
public class BaseController {

    /**
     * 获取当前登录的user
     */
    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) return null;
        Object object = subject.getPrincipal();
        if (object != null) return (User) object;
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public Integer getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

}
