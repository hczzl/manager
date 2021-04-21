package com.ruoyi.web.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author Suqz
 * @version 1.0
 * @date 2019/12/31 15:34
 */
public class TaskEchar implements Serializable {
    private static final long serialVersionUID = 33677569706821819L;
    /**
     * 任务总数
     */
    private Integer taskTotal;
    /**
     * 任务状态
     */
    private List<String> status;
    /**
     * 各任务状态的数量
     */
    private List<Integer> taskStatusCount;
    /**
     * 各任务状态所占比例
     */
    private List<Double> taskStatusRate;

    public TaskEchar() {
    }

    public TaskEchar(List<String> status, List<Integer> taskStatusCount, List<Double> taskStatusRate) {
        this.status = status;
        this.taskStatusCount = taskStatusCount;
        this.taskStatusRate = taskStatusRate;
    }

    @Override
    public String toString() {
        return "TaskEchar{" +
                "taskTotal=" + taskTotal +
                ", status=" + status +
                ", taskStatusCount=" + taskStatusCount +
                ", taskStatusRate=" + taskStatusRate +
                '}';
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Integer getTaskTotal() {
        return taskTotal;
    }

    public void setTaskTotal(Integer taskTotal) {
        this.taskTotal = taskTotal;
    }

    public List<Integer> getTaskStatusCount() {
        return taskStatusCount;
    }

    public void setTaskStatusCount(List<Integer> taskStatusCount) {
        this.taskStatusCount = taskStatusCount;
    }

    public List<Double> getTaskStatusRate() {
        return taskStatusRate;
    }

    public void setTaskStatusRate(List<Double> taskStatusRate) {
        this.taskStatusRate = taskStatusRate;
    }
}
