package com.ruoyi.web.mapper.item;


import com.ruoyi.web.domain.ParticipantsTable;

import java.util.List;
import java.util.Map;

/**
 * mapper接口
 *
 * @author zhongzhilong
 * @date 2019/8/10
 */
public interface ParticipantsMapper {
    /**
     * @param participantsTable
     * @return
     */
    int insertparticipants(ParticipantsTable participantsTable);

    /**
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectParticients(ParticipantsTable participantsTable);

    /**
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectUserJoin(ParticipantsTable participantsTable);

    /**
     * @param id
     * @return
     */
    int getAlltimes(int id);

    /**
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectAllUserId(ParticipantsTable participantsTable);

    /**
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> getProjectByProjectId(ParticipantsTable participantsTable);

    /**
     * @param projectId
     * @return
     */
    List<ParticipantsTable> getUserIdByPid(int projectId);

    /**
     * @param participantsTable
     * @return
     */
    List<ParticipantsTable> selectInfo(ParticipantsTable participantsTable);

    /**
     * @param participantsTable
     * @return
     */
    int deletePeople(ParticipantsTable participantsTable);

    /**
     * @param p
     * @return
     */
    List<ParticipantsTable> selectInfo1(ParticipantsTable p);

    /**
     * @param userId
     * @return
     */
    List<Integer> querypIdByUserId(Integer userId);

    /**
     * @param pId
     * @return
     */
    List<Map<String, Object>> selectUserName(Integer pId);
}
