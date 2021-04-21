package com.ruoyi.web.service;

import com.ruoyi.web.domain.SysFileInfo;

import java.util.List;

/**
 * 文件上传Service接口
 *
 * @author ruoyi
 * @date 2019-08-26
 */
public interface SysFileInfoService {
    /**
     * 查询文件上传
     *
     * @param fileId 文件上传ID
     * @return 文件上传
     */
    SysFileInfo selectFileInfoById(Long fileId);

    /**
     * 查询文件上传列表
     *
     * @param sysFileInfo 文件上传
     * @return 文件上传集合
     */
    List<SysFileInfo> selectFileInfoList(SysFileInfo sysFileInfo);

    /**
     * 新增文件上传
     *
     * @param sysFileInfo 文件上传
     * @return 结果
     */
    int insertFileInfo(SysFileInfo sysFileInfo);

    /**
     * 修改文件上传
     *
     * @param sysFileInfo 文件上传
     * @return 结果
     */
    int updateFileInfo(SysFileInfo sysFileInfo);

    /**
     * 批量删除文件上传
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteFileInfoByIds(String ids);

    /**
     * 删除文件上传信息
     *
     * @param fileId 文件上传ID
     * @return 结果
     */
    int deleteFileInfoById(Long fileId);

    int updateFileInfoByUserId(Long[] userIds);

    /**
     * 查询所有文件信息
     *
     * @return
     */
    List<SysFileInfo> selectAllFileInfo();

    /**
     * 查询我的所有文件信息
     *
     * @return
     */
    List<SysFileInfo> selectMyAllFileInfoByUserId(Long userId);

    /**
     * 查询我的所有文件信息
     *
     * @return
     */
    List<SysFileInfo> selectAllShareFileInfo();

    /**
     * 查询所有文件信息的数量
     *
     * @return
     */
    Integer selectAllFileInfoCount();

    /**
     * 查询我的所有文件信息的数量
     *
     * @return
     */
    Integer selectMyAllFileInfoByUserIdCount(Long userId);


    /**
     * 根据文件名查询文件信息
     *
     * @param fileName
     * @return
     */
    List<SysFileInfo> selectFileInfoListByName(String fileName);

    /**
     * 根据文件名查询文件信息的数量
     *
     * @param fileName
     * @return
     */
    Integer selectFileInfoListByNameCount(String fileName);

    /**
     * 根据所属任务id查询所有文件信息
     */
    List<SysFileInfo> selectFileInfoByTaskId(Integer tid);

    /**
     * 根据所属项目Id查询所有文件信息
     */
    List<SysFileInfo> selectFileInfoByProjectId(Integer pid);
}
