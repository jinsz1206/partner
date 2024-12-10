package com.jsz.partner_backend.service;


import com.jsz.partner_backend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;



    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("jszString", "fish");
        valueOperations.set("jszInt", 1);
        valueOperations.set("jszDouble", 2.0);
        User user = new User();
        user.setId(145141L);
        user.setUserName("jsz");
        valueOperations.set("jszUser", user);

        // 查
        Object jsz = valueOperations.get("jszString");
        Assertions.assertTrue("fish".equals((String) jsz));
        jsz = valueOperations.get("jszInt");
        Assertions.assertTrue(1 == (Integer) jsz);
        jsz = valueOperations.get("jszDouble");
        Assertions.assertTrue(2.0 == (Double) jsz);
        System.out.println(valueOperations.get("jszUser"));


        //删
//        redisTemplate.delete("shayuString");
    }

}
