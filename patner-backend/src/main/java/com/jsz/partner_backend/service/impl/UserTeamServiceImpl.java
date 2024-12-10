package com.jsz.partner_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsz.partner_backend.model.domain.UserTeam;
import com.jsz.partner_backend.mapper.UserTeamMapper;
import com.jsz.partner_backend.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author 12099
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-12-09 19:54:06
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




