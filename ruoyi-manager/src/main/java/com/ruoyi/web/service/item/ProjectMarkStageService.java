package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ProjectMarketStageTable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 所需资源信息表
 */
public interface ProjectMarkStageService {
    /**
     * 添加资源信息
     *
     * @param projectMarketStageTable
     * @return
     */
    int insertStage(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 查询
     *
     * @param projectMarketStageTable
     * @return
     */
    List<ProjectMarketStageTable> selectAll(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 根据id来删除相应的数据
     *
     * @return
     */
    int deleteStage(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 更新名字
     *
     * @param projectMarketStageTable
     */
    void updateUserByName(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 添加信息
     *
     * @param projectMarketStageTable
     */
    void addInfo(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 根据id查询信息
     *
     * @param pid
     * @return
     */
    List<ProjectMarketStageTable> selectById(int pid);

    /**
     * 无参查询所有的内容
     *
     * @return
     */
    List<ProjectMarketStageTable> selectProjects();

    /**
     * 请求打开一个excel文件
     *
     * @return
     */
    int openExcel();

    /**
     * 项目立项列表查询对应的阶段，根据项目id
     *
     * @param projectMarketStageTable
     * @return
     */
    List<ProjectMarketStageTable> selectStage(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 根据id查询项目
     *
     * @param p
     * @return
     */
    ProjectMarketStageTable selectStageById(ProjectMarketStageTable p);

    /**
     * 根据id来更新数据
     *
     * @param p
     * @return
     */
    int updateStage(ProjectMarketStageTable p);

    /**
     * 查询所有的阶段
     *
     * @param projectMarketStageTable
     * @return
     */
    List<ProjectMarketStageTable> selectAllStage(ProjectMarketStageTable projectMarketStageTable);

    /**
     * 完成消息表中详情查询
     *
     * @param p
     * @return
     */
    List<ProjectMarketStageTable> selectStageByProjectId(ProjectMarketStageTable p);

    /**
     * 根据id更新信息。
     *
     * @param projectMarketStageTable
     * @return
     */
    int updateStageById(ProjectMarketStageTable projectMarketStageTable);

    /**
     * @param p
     * @return
     */
    int updateStageBranch(@RequestBody ProjectMarketStageTable p);
}
