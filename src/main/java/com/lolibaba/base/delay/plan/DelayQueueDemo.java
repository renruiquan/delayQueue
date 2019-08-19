package com.lolibaba.base.delay.plan;

import com.lolibaba.base.delay.utils.MyDelayed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;


/**
 * 延时队列取消订单
 */
public class DelayQueueDemo {

    public static void main(String[] args) {
        DelayQueue<MyDelayed> delayQueue = new DelayQueue<>();

        new Thread(new ProducerDelay(delayQueue, 10)).start();
        new Thread(new ConsumerDelay(delayQueue)).start();
    }

    public static class ProducerDelay implements Runnable {

        DelayQueue<MyDelayed> delayQueue;
        /**
         * 延迟时间(单位秒）
         */
        int delaySecond;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ProducerDelay(DelayQueue<MyDelayed> delayQueue, int delaySecond) {
            this.delayQueue = delayQueue;
            this.delaySecond = delaySecond;
        }

        @Override
        public void run() {
            String orderId = "1010101";
            for (int i = 0; i < 10; i++) {
                MyDelayed delayed = new MyDelayed(this.delaySecond, orderId + i);
                delayQueue.add(delayed);
                System.out.println(sdf.format(new Date()) + " - Thread - " + Thread.currentThread() + "产生了一个delay,orderId:" + delayed.getOrderId());

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ConsumerDelay implements Runnable {

        DelayQueue<MyDelayed> delayQueue;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ConsumerDelay(DelayQueue<MyDelayed> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            while (true) {
                MyDelayed delayed = null;
                try {
                    /*delayed = delayQueue.poll();*/
                    delayed = delayQueue.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (delayed != null) {
                    System.out.println(sdf.format(new Date()) + " Thread " + Thread.currentThread() + "消费了一个delay. orderId:" + delayed.getOrderId());
                } else {
                    try {
                        System.out.println("任务扫描中……");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
