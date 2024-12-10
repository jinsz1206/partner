package com.jsz.partner_backend.service;
import com.jsz.partner_backend.mapper.UserMapper;
import com.jsz.partner_backend.model.domain.User;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    /**
     * 循环插入用户
     *  10000  条单线程11782
     */
    @Test
    public  void doInsertUserByFor() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 10000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUserAccount("fake");
            user.setUserPassword("fake");
            user.setUserName("fake");
            user.setUserAvatar("fake");
            user.setUserGender("fake");
            user.setUserProfile("fake");
            user.setUserRole("user");
            user.setTags("[]");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println( stopWatch.getLastTaskTimeMillis());

    }

    /**
     * 循环插入用户
     *  100000 条   10000 batchSize 13862
     *  100000 条   50000 batchSize 14023
     */
    @Test
    public  void doInsertUserByService() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUserAccount("fake");
            user.setUserPassword("fake");
            user.setUserName("fake");
            user.setUserAvatar("fake");
            user.setUserGender("fake");
            user.setUserProfile("fake");
            user.setUserRole("user");
            user.setTags("[]");
            userList.add(user);

        }
        userService.saveBatch(userList,10000);
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

    }



    /**
     * cpu 4内核  8逻辑  最高调用 8-1 个
     * 并发批量插入用户   100000  耗时： 5539   线程7个
     * 并发批量插入用户   100000  耗时： 5013   线程5个
     */
    @Test
    public void doConcurrencyInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        // 分十组
        int j = 0;
        //批量插入数据的大小
        int batchSize = 20000;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM/batchSize; i++) {
            List<User> userList = new ArrayList<>();
            while (true){
                j++;
                User user = new User();
                user.setUserAccount("fake");
                user.setUserPassword("fake");
                user.setUserName("fake");
                user.setUserAvatar("fake");
                user.setUserGender("fake");
                user.setUserProfile("fake");
                user.setUserRole("user");
                user.setTags("[]");
                userList.add(user);
                if (j % batchSize == 0 ){
                    break;
                }
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                System.out.println("ThreadName：" + Thread.currentThread().getName());
                userService.saveBatch(userList,batchSize);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println( stopWatch.getLastTaskTimeMillis());

    }

}
