package com.jsz.partner_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AllowedConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //允许跨域的路径
        registry.addMapping("/**")

                //允许跨域请求的域名
                //前端域名
                .allowedOrigins("http://localhost:4396/","http://192.168.101.3:4396/")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);





    }


}
