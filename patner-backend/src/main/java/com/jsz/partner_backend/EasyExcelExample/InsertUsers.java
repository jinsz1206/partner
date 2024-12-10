package com.jsz.partner_backend.EasyExcelExample;
import com.jsz.partner_backend.mapper.UserMapper;
import com.jsz.partner_backend.model.domain.User;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;



@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 循环插入用户
     */
//
//    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE )
    public  void doInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
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

}
