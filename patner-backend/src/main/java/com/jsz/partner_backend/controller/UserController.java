package com.jsz.partner_backend.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsz.partner_backend.common.BaseResponse;
import com.jsz.partner_backend.common.ErrorCode;
import com.jsz.partner_backend.exception.BusinessException;
import com.jsz.partner_backend.model.domain.User;
import com.jsz.partner_backend.model.request.UserLoginRequest;
import com.jsz.partner_backend.model.request.UserRegisterRequest;
import com.jsz.partner_backend.service.UserService;
import com.jsz.partner_backend.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.jsz.partner_backend.contant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.usrRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword,request);
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
           throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //todo 检验合法
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyuser  = userService.getSafeUser(user);
        return ResultUtils.success(safetyuser);
    }


    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout( HttpServletRequest request) {
        if (request == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result =  userService.userLogout(request);
        return ResultUtils.success(result);
    }



    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize,long pageNum,HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        String redisKey = String.format("partner:user:recommend:%s",loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //如果有缓存，直接读取缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null){
            return ResultUtils.success(userPage);
        }

        //如果无缓存，数据库查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), userQueryWrapper);
        //加缓存
        try {
            valueOperations.set(redisKey,userPage,100, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }

        return ResultUtils.success(userPage);
    }


    //管理用户接口

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username,HttpServletRequest request) {
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (username != null){
            userQueryWrapper.like("username", username);
        }
        List<User> userList = userService.list(userQueryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserTags(@RequestParam(required = false)List<String> tagNameList) {
        if (CollectionUtil.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTagsBySQL(tagNameList);
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request) {
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request) {
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user,loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 匹配用户
     * @param num
     * @param request
     * @return
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num,HttpServletRequest request) {
        if(num<= 0 || num > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num,loginUser));
    }




}
