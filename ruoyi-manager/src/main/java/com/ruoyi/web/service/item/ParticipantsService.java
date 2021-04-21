package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ParticipantsTable;


import java.util.List;

/**
 * @author zhongzhilong
 * @date 2019/12/4
 */
public interface ParticipantsService {

    /**
     * 添加内容
     *
     * @param participantsTable
     * @return
     */
    int insertparticipants(ParticipantsTable participantsTable);

    /**
     * 查询
     *
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectParticients(ParticipantsTable participantsTable);

    /**
     * 查询所有的参与人id
     *
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectUserJoin(ParticipantsTable participantsTable);

    /**
     * 根据id来获取该用户参与了多少个项目
     *
     * @param id
     * @return
     */
    int getAlltimes(int id);

    /**
     * 获得所有的用户id
     *
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectAllUserId(ParticipantsTable participantsTable);

    /**
     * 根据项目id获取项目信息
     *
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> getProjectByProjectId(ParticipantsTable participantsTable);

    /**
     * 根据项目id获取用户id
     *
     * @param projectId
     * @return
     */
    List<ParticipantsTable> getUserIdByPid(int projectId);

    /**
     * 根据项目id和用户id信息
     *
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectInfo(ParticipantsTable participantsTable);

    /**
     * 删除项目参与人
     *
     * @param participantsTable
     * @return
     */
    int deletePeople(ParticipantsTable participantsTable);

    /**
     * 获取参与人列表
     *
     * @param p
     * @return
     */
    List<ParticipantsTable> selectInfo1(ParticipantsTable p);
}
