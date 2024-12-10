package com.jsz.partner_backend.config;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {
    private String host;

    private Integer port;

    @Bean
    public RedissonClient redissonClient() {
        //配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        //用单个redis
        config.useSingleServer().setAddress(redisAddress).setDatabase(3);
        //创建实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;


    }


}
