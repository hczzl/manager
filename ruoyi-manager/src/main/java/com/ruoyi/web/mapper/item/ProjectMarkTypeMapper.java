package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ProjectMarkTypeInfoTable;

import java.util.List;
/**
 * mapper接口
 *
 * @author zhongzhilong
 * @date 2019/10/10
 */
public interface ProjectMarkTypeMapper {

    int insertMarkType(ProjectMarkTypeInfoTable projectMarkTypeInfoTable);

    List<ProjectMarkTypeInfoTable> selectAll(ProjectMarkTypeInfoTable projectMarkTypeInfoTable);

    int deleteType(int id);

    int updateMarkInfo(ProjectMarkTypeInfoTable p);

}
