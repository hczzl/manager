package com.ruoyi.quartz.thread.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 支持定时任务类
 *
 * @author zhongzhilong
 * @EnableScheduling
 * @Date 2019/12/16
 */
@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {


    /**
     * ThredPoolTaskExcutor的处理流程
     * 当池子大小小于corePoolSize，就新建线程，并处理请求
     * 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
     * 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
     * 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(50);
        // 最大线程数
        taskExecutor.setMaxPoolSize(50);
        // 等待队列
        taskExecutor.setQueueCapacity(200);
        // taskExecutor.setThreadNamePrefix("zxl_taskExecutor-");//线程名称前缀
        // 初始化线程
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 异步异常处理器
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }


}
