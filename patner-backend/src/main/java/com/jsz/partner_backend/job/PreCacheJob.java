package com.jsz.partner_backend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsz.partner_backend.exception.BusinessException;
import com.jsz.partner_backend.model.domain.User;
import com.jsz.partner_backend.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    // 重点用户
    private  List<Long> mainUserList = Arrays.asList(4L);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 32 16 * * * ")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("jsz:partner:doCatch:lock");
        try {
            if (lock.tryLock(0,30000,TimeUnit.MILLISECONDS)){
                System.out.println("getLock " + Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    //查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
                    String redisKey = String.format("partner:user:recommend:%s",userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //写缓存,30s过期
                    try {
                        valueOperations.set(redisKey,userPage,100, TimeUnit.SECONDS);
                    } catch (Exception e){
                        log.error("redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("redis lock error",e);
        }finally {
            //只释放自己的🔒
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }


    }

}