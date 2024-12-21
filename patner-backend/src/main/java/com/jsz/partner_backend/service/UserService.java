package com.jsz.partner_backend.service;

import com.jsz.partner_backend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jsz.partner_backend.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author ex_shengzhou.jin
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-14 16:23:07
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 确认
     * @return 新用户ID
     */
    long usrRegister(String userAccount, String userPassword,String checkPassword);


    /**
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @return User对象
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     *  用户脱敏
     * @param user
     * @return
     */
    User getSafeUser(User user);


    /**
     * 用户登出
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 标签查询用户
     * @param tagList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagList);
    List<User> searchUsersByTagsBySQL(List<String> tagList);


    /**
     *
     * @param user
     * @param loginUser
     * @return
     */
    Integer updateUser(User user,User loginUser);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     *  是否管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);


    /**
     *  是否管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
