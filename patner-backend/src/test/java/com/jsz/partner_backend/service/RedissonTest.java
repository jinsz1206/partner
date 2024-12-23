package com.jsz.partner_backend.service;


import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void Test(){
        //list
       List<String> list = new ArrayList<>();
       list.add("jsz");
       System.out.println(list.get(0));
       list.remove(0);

        RList<String> rlist = redissonClient.getList("rlist");
//        rlist.add("jsz");
        System.out.println(rlist.get(0));
        rlist.remove(0);



    }



    @Test
    void testWatchDog() {
        RLock lock = redissonClient.getLock("jsz:partner:doCache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                Thread.sleep(300000);//todo 实际要执行的代码
                System.out.println("getLock: " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }


}
