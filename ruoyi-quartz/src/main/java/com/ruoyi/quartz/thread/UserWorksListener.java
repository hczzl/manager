package com.ruoyi.quartz.thread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zzl on 2019/12/16
 * 线程启动的方式是：Listener，另外一种启动方式是：使用Servlet时,在项目启动的时候启动它
 */
public class UserWorksListener implements ServletContextListener {

    private UserWorksThread userWorksThread;

    @Override
    public void contextDestroyed(ServletContextEvent e) {

        if (userWorksThread != null && userWorksThread.isInterrupted()) {

            userWorksThread.interrupt();

        }

    }

    @Override
    public void contextInitialized(ServletContextEvent e) {

        String str = null;

        if (str == null && userWorksThread == null) {

            userWorksThread = new UserWorksThread();

            // servlet 上下文初始化时启动 socket
            userWorksThread.start();

        }

    }
}
