package com.jsz.partner_backend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsz.partner_backend.common.ErrorCode;
import com.jsz.partner_backend.exception.BusinessException;
import com.jsz.partner_backend.model.domain.User;
import com.jsz.partner_backend.service.UserService;
import com.jsz.partner_backend.mapper.UserMapper;
import com.jsz.partner_backend.utils.AlgorithmUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.jsz.partner_backend.contant.UserConstant.USER_LOGIN_STATE;


/**
* @author ex_shengzhou.jin
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-14 16:23:07
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{



    private static final String SALT = "jsz";

    @Resource
    private UserMapper userMapper;

    @Override
    public long usrRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验


        //内容为空
        if(StrUtil.hasBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //用户名长度
//        if(StrUtil.length(userAccount) < 4){
//            return -1;
//        }

        //密码长度
        if(StrUtil.length(userPassword) > 15 || StrUtil.length(userPassword) < 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码错误");
        }


        //账户名字不能包含特殊字符,6-12位数
        if(!Validator.isGeneral(userAccount,6,12)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户错误");
        }

        //密码2次输入
        if (!StrUtil.equals(userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码输入不一致");
        }


        //账户不能重复,对数据库的调用放校验最后
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);

        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        //密码加密
        byte[] bytes = DigestUtil.md5(SALT + userPassword);
        String encryptPassWord = DigestUtil.md5Hex(bytes);
        //插入
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassWord);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户添加失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验


        //内容为空
        if(StrUtil.isAllBlank(userAccount, userPassword)){
            return null;
        }

        //密码长度
        if(StrUtil.length(userPassword) > 15 || StrUtil.length(userPassword) < 6){
            return null;
        }


        //账户名字不能包含特殊字符,6-12位数
        if(!Validator.isGeneral(userAccount,6,12)){
            return null;
        }



        //密码加密
        byte[] bytes = DigestUtil.md5(SALT + userPassword);
        String encryptPassWord = DigestUtil.md5Hex(bytes);


        //查询用户存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassWord);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码错误");
        }

        //用户脱敏
        User safetyUser = getSafeUser(user);

        //用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);



        return safetyUser;
    }


    @Override
    public User getSafeUser(User user){
        if (user == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAvatar(user.getUserAvatar());
        safetyUser.setUserGender(user.getUserGender());
        safetyUser.setUserProfile(user.getUserProfile());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUpdateTime(user.getUpdateTime());
        safetyUser.setUserGender(user.getUserGender());
        safetyUser.setTags(user.getTags());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return  1;
    }


    /**
     * 标签查询用户 by 内存
     *
     * @param tagList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagList){
        if (CollectionUtil.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //内存查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询所有用户
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        //内存中判断
        return userList.stream().filter(user -> {
            String tags = user.getTags();
//            if (StrUtil.isBlank(tags)) {
//                return false;
//            }
            Set<String> tagNameSet = gson.fromJson(tags, new TypeToken<Set<String>>(){}.getType());
            // java 8  Optional 判断
            tagNameSet = Optional.ofNullable(tagNameSet).orElse(new HashSet<>());
            for (String tagName : tagList) {
                if(!tagNameSet.contains(tagName)){
                    return false;
                }
            }
            return true;
        }).map(this::getSafeUser).collect(Collectors.toList());
    }

    @Override
    public List<User> searchUsersByTagsBySQL(List<String> tagList) {

        if (CollectionUtil.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //拼接查询 like ''
        for (String tag : tagList) {
            queryWrapper = queryWrapper.like("tags",tag);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(this::getSafeUser).collect(Collectors.toList());
    }

    @Override
    public Integer updateUser(User user,User loginUser) {
        long userId = user.getId();
        if(userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //管理员可以修改任意用户信息
        //用户修改本人信息
        if(!isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User olduser = userMapper.selectById(userId);
        if(olduser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        return (User) userObj;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;

        return user != null && user.getUserRole().equals("admin");
    }

    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole().equals("admin");
    }

    /**
     * 推荐匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    @Override
    public List<User> matchUsers(long num, User loginUser) {
//        这里我因为电脑内存问题，没有办法像鱼皮电脑那样可以存放100万数据，可以直接运行。所以我选择了运行5万条数据。
//        不然的话会报 OOM（内存）的问题
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.last("limit 50000");
//        List<User> userList = this.list(queryWrapper);
//         或者用page分页查询，自己输入或默认数值，但这样匹配就有限制了
//        List<User> userList = this.page(new Page<>(pageNum,pageSize),queryWrapper);
//		这里查了所有用户，近100万条
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("tags");
        queryWrapper.select("id", "tags");
        List<User> userList = this.list(queryWrapper);

        String tags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下表 => 相似度'
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算当前用户和所有用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            //无标签的 或当前用户为自己
            if (StrUtil.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            //计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        //按编辑距离有小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        //有顺序的userID列表
        List<Long> userListVo = topUserPairList.stream().map(pari -> pari.getKey().getId()).collect(Collectors.toList());

        //根据id查询user完整信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userListVo);
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper).stream()
                .map(this::getSafeUser)
                .collect(Collectors.groupingBy(User::getId));

        // 因为上面查询打乱了顺序，这里根据上面有序的userID列表赋值
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userListVo) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;

    }


}





