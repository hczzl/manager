package com.ruoyi.web.service.imp;

import java.util.Date;
import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.mapper.SysFileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.domain.SysFileInfo;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 文件上传Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-26
 */
@Service
public class SysSysFileInfoServiceImpl implements SysFileInfoService {
    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 查询文件上传
     *
     * @param fileId 文件上传ID
     * @return 文件上传
     */
    @Override
    public SysFileInfo selectFileInfoById(Long fileId) {
        return sysFileInfoMapper.selectFileInfoById(fileId);
    }

    /**
     * 查询文件上传列表
     *
     * @param sysFileInfo 文件上传
     * @return 文件上传
     */
    @Override
    public List<SysFileInfo> selectFileInfoList(SysFileInfo sysFileInfo) {
        return sysFileInfoMapper.selectFileInfoList(sysFileInfo);
    }

    /**
     * 新增文件上传
     *
     * @param sysFileInfo 文件上传
     * @return 结果
     */
    @Override
    public int insertFileInfo(SysFileInfo sysFileInfo) {
        //文件上传时间
        sysFileInfo.setFileTime(new Date());
        //上传用户的id
        sysFileInfo.setUserId(ShiroUtils.getUserId());
        return sysFileInfoMapper.insertFileInfo(sysFileInfo);
    }

    /**
     * 修改文件上传
     *
     * @param sysFileInfo 文件上传
     * @return 结果
     */
    @Override
    public int updateFileInfo(SysFileInfo sysFileInfo) {
        return sysFileInfoMapper.updateFileInfo(sysFileInfo);
    }

    /**
     * 删除文件上传对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFileInfoByIds(String ids) {
        return sysFileInfoMapper.deleteFileInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文件上传信息
     *
     * @param fileId 文件上传ID
     * @return 结果
     */
    @Override
    public int deleteFileInfoById(Long fileId) {
        return sysFileInfoMapper.deleteFileInfoById(fileId);
    }

    @Override
    public int updateFileInfoByUserId(Long[] userIds) {
        return sysFileInfoMapper.updateFileInfoByUserId(userIds);
    }

    @Override
    public List<SysFileInfo> selectAllFileInfo() {
        return sysFileInfoMapper.selectAllFileInfo();
    }

    @Override
    public List<SysFileInfo> selectMyAllFileInfoByUserId(Long userId) {
        return sysFileInfoMapper.selectMyAllFileInfoByUserId(userId);
    }

    @Override
    public List<SysFileInfo> selectAllShareFileInfo() {
        return sysFileInfoMapper.selectAllShareFileInfo();
    }

    @Override
    public Integer selectAllFileInfoCount() {
        return sysFileInfoMapper.selectAllFileInfoCount();
    }

    @Override
    public Integer selectMyAllFileInfoByUserIdCount(Long userId) {
        return sysFileInfoMapper.selectMyAllFileInfoByUserIdCount(userId);
    }

    @Override
    public List<SysFileInfo> selectFileInfoListByName(String fileName) {
        return sysFileInfoMapper.selectFileInfoListByName(fileName);
    }

    @Override
    public Integer selectFileInfoListByNameCount(String fileName) {
        return sysFileInfoMapper.selectFileInfoListByNameCount(fileName);
    }

    @Override
    public List<SysFileInfo> selectFileInfoByTaskId(Integer tid) {
        return sysFileInfoMapper.selectFileInfoByTaskId(tid);
    }

    @Override
    public List<SysFileInfo> selectFileInfoByProjectId(Integer pid) {
        return sysFileInfoMapper.selectFileInfoByProjectId(pid);
    }
}
