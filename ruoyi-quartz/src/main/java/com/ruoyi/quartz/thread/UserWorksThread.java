package com.ruoyi.quartz.thread;

/**
 * Created by zzl on 2019/12/16
 */
public class UserWorksThread extends Thread {
    @Override
    public void run() {
        // 线程未中断执行循环
        while (!this.isInterrupted()) {
            try {
                // 每隔2000ms执行一次
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//           ------------------ 在这里执行自己的代码  ---------------------------
        }

    }


}
