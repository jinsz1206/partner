package com.jsz.partner_backend.service;

import com.jsz.partner_backend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsz.partner_backend.model.domain.User;
import com.jsz.partner_backend.model.dto.TeamQuery;
import com.jsz.partner_backend.model.request.TeamJoinRequest;
import com.jsz.partner_backend.model.request.TeamUpdateRequest;
import com.jsz.partner_backend.model.vo.TeamUserVo;

import java.util.List;

/**
* @author 12099
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-12-09 19:51:26
*/
public interface TeamService extends IService<Team> {


    /**
     * 创建队伍
     * @param team
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);


    /**
     * 删除（解散）队伍
     *
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);




}
