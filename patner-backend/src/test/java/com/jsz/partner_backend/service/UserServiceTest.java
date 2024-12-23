package com.jsz.partner_backend.service;


import com.jsz.partner_backend.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: UserServiceTest
 * @Author jsz
 * @Package com.jsz.usercenter.service
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserAccount("1209990432");
        user.setUserPassword("jsz20031216");

        user.setUserGender("男");
        user.setUserName("jsz");
        user.setUserProfile("神人");
        user.setUserRole("admin");
        boolean reasult = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(reasult);

    }

    @Test
    public void userRegiister() {

        //check 不一样
        String account = "qwe123345";
        String password = "jsz20031216";
        String check = "213213213";
        long result = userService.usrRegister(account, password, check);
        Assertions.assertEquals(-1, result);

        //特殊字符
        account = "12@dw321eq ";
        check = "jsz20031216";
        result  = userService.usrRegister(account, password, check);
        Assertions.assertEquals(-1, result);

        //账号位数
        account = "1234";
        result  = userService.usrRegister(account, password, check);
        Assertions.assertEquals(-1, result);

        //密码位数
        account = "12345678ab";
        password = "1234578901231231231231";
        check = "1234578901231231231231";
        result  = userService.usrRegister(account, password, check);
        Assertions.assertEquals(-1, result);

        //重复插入
        result  = userService.usrRegister(account, password, check);
        Assertions.assertEquals(-1, result);


    }

    @Test
    public void userInsert() {
        String account = "12345678";
        String password = "jsz123456";
        String check = "jsz123456";
        Long result  = userService.usrRegister(account, password, check);
        Assertions.assertTrue(result>1);
    }

    @Test
    public void SearchUsersByTags() {
        List<String> tags = Arrays.asList("java");
        List<User> users = userService.searchUsersByTags(tags);

        Assert.assertNotNull(users);
    }


    @Test
    public void testAddUser123() {
        User user = new User();
        user.setUserAccount("1209990432");
        user.setUserPassword("jsz20031216");

        user.setUserGender("男");
        user.setUserName("呃呃呃");
        user.setUserProfile("神人");
        user.setUserRole("user");
        boolean reasult = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(reasult);

    }









}