package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.TaskPostpone;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 延期Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-27
 */
@Component
public interface TaskPostponeMapper {

    TaskPostpone selectTaskPostponeById(Long id);

    List<TaskPostpone> selectTaskPostponeList(TaskPostpone taskPostpone);

    int insertTaskPostpone(TaskPostpone taskPostpone);

    int updateTaskPostpone(TaskPostpone taskPostpone);

    int deleteTaskPostponeById(Long id);

    int deleteTaskPostponeByIds(String[] ids);

    Integer selectMaxId(TaskPostpone taskPostpone);

    List<TaskPostpone> selectById(TaskPostpone taskPostpone);

    List<TaskPostpone> selectTaskChange(TaskPostpone taskPostpone);

    int insertInitTaskPostpone(TaskPostpone taskPostpone);

    String selectChangeMemo(Long tId);
}
