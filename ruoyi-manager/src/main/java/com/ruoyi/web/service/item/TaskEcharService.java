package com.ruoyi.web.service.item;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.TaskEcharQueryInfo;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/5/5 22:32
 */
public interface TaskEcharService {
    AjaxResult getTaskEchartsData(TaskEcharQueryInfo queryInfo);
}
