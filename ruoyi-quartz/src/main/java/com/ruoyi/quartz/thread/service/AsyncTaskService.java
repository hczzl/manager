package com.ruoyi.quartz.thread.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * 线程执行任务类
 */
@Service
public class AsyncTaskService {
    private Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

    /**
     * 表明是异步方法
     * 无返回值
     *
     * @param msg
     */
    @Async
    public void executeAsyncTask(String msg) {
        System.out.println(Thread.currentThread().getName() + "开启新线程执行" + msg);
    }

    @Async
    public Future<String> doReturn(int i) {
        // logger.info(">>>>>>>>>>>>>>线程名>>>>>>>>>>>>>>" + Thread.currentThread().getName());
        try {
            // 这个方法需要调用500毫秒
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 消息汇总
        return new AsyncResult<>(String.format("这个是第{%s}个异步调用的证书", i));
    }


    /**
     * 异常调用返回Future
     *
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<Long> asyncInvokeReturnFuture() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        // Future接收返回值，这里是String类型，可以指明其他类型
        Future<Long> future = new AsyncResult<Long>((System.currentTimeMillis() - start));
        return future;
    }


}
