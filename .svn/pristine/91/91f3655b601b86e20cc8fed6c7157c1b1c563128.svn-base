package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ProjectMarketStageTable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * mapper接口
 *
 * @author zhongzhilong
 * @date 2019/10/10
 */
public interface ProjectMarkStageMapper {

    int insertStage(ProjectMarketStageTable projectMarketStageTable);

    List<ProjectMarketStageTable> selectAll(ProjectMarketStageTable projectMarketStageTable);

    int deleteStage(ProjectMarketStageTable projectMarketStageTable);

    boolean batchImport(String fileName, MultipartFile file) throws Exception;

    void updateUserByName(ProjectMarketStageTable projectMarketStageTable);

    void addInfo(ProjectMarketStageTable projectMarketStageTable);

    List<ProjectMarketStageTable> selectById(int p);

    List<ProjectMarketStageTable> selectProjects();

    int openExcel();

    List<ProjectMarketStageTable> selectStage(ProjectMarketStageTable projectMarketStageTable);

    ProjectMarketStageTable selectStageById(ProjectMarketStageTable p);

    int updateStage(ProjectMarketStageTable p);

    List<ProjectMarketStageTable> selectAllStage(ProjectMarketStageTable projectMarketStageTable);

    List<ProjectMarketStageTable> selectStageByProjectId(ProjectMarketStageTable p);

    int updateStageById(ProjectMarketStageTable projectMarketStageTable);

}
