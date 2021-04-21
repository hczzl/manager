package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ParticipantsTable;
import com.ruoyi.web.mapper.item.ParticipantsMapper;
import com.ruoyi.web.service.item.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhongzhilong
 * @date 2019/12/4
 */
@Service
public class ParticipantsServiceImpl implements ParticipantsService {

    @Autowired
    private ParticipantsMapper participantsMapper;

    @Override
    public int insertparticipants(ParticipantsTable participantsTable) {

        return participantsMapper.insertparticipants(participantsTable);
    }

    @Override
    public List<ParticipantsTable> selectParticients(ParticipantsTable participantsTable) {
        return participantsMapper.selectParticients(participantsTable);
    }

    @Override
    public List<ParticipantsTable> selectUserJoin(ParticipantsTable participantsTable) {
        return participantsMapper.selectUserJoin(participantsTable);
    }

    @Override
    public int getAlltimes(int id) {
        return participantsMapper.getAlltimes(id);
    }

    @Override
    public List<ParticipantsTable> selectAllUserId(ParticipantsTable participantsTable) {
        return participantsMapper.selectAllUserId(participantsTable);
    }

    @Override
    public List<ParticipantsTable> getProjectByProjectId(ParticipantsTable participantsTable) {
        return participantsMapper.getProjectByProjectId(participantsTable);
    }


    @Override
    public List<ParticipantsTable> getUserIdByPid(int projectId) {
        return participantsMapper.getUserIdByPid(projectId);
    }

    @Override
    public List<ParticipantsTable> selectInfo(ParticipantsTable participantsTable) {
        return participantsMapper.selectInfo(participantsTable);
    }

    @Override
    public int deletePeople(ParticipantsTable participantsTable) {
        return participantsMapper.deletePeople(participantsTable);
    }

    @Override
    public List<ParticipantsTable> selectInfo1(ParticipantsTable p) {
        return participantsMapper.selectInfo1(p);
    }
}
