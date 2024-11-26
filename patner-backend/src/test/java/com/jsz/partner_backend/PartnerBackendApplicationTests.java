package com.jsz.partner_backend;

import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PartnerBackendApplicationTests {

    @Test
    void  testMD5(){
        String text = "jsz20031216";
        byte[] bytes = DigestUtil.md5(text);
        String result = DigestUtil.md5Hex(bytes);
        System.out.println(result);

    }



    @Test
    void contextLoads() {}


}
