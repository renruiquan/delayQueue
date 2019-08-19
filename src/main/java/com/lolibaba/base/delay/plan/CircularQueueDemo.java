package com.lolibaba.base.delay.plan;

import com.lolibaba.base.delay.utils.CalendarUtils;

import java.util.*;

/**
 * 环形队列实现取消订单
 */
public class CircularQueueDemo {
    /**
     * 循环队列的槽数（定义360个槽，每秒读一个槽中的数据，十分钟一轮）
     */
    public static final int SLOT_NUM = 60;

    private static List<LinkedList<Task>> queueList = new ArrayList<>(SLOT_NUM);

    private static int eachNum = 1;
    private static int currentIndex = 0;

    public static void main(String[] args) {
        for (int i = 0; i < SLOT_NUM; i++) {
            queueList.add(i, new LinkedList<>());
        }

        new Thread(new ProducerThread()).start();
        new Thread(new ConsumerThread()).start();
    }

    static class ProducerThread implements Runnable {
        @Override
        public void run() {
            //订单前缀
            String prefix = "1010101";

            //生成10个订单
            for (int i = 0; i < 10; i++) {
                //订单超时时间
                int expire = i * 2 + 5;

                //根据订单超时时间+当前轮询到第几个槽，计算任务应该 落在哪个槽里
                int expireIndex = (currentIndex + expire) % SLOT_NUM;
                //根据订单超时时间+当前第几次轮询，计算任务应该是第几次轮询时触发
                int expireEachNum = (currentIndex + expire) / SLOT_NUM + eachNum;

                //创建订单
                 String orderId = prefix + String.format("%1$02d", i);
                Task task = new Task(expireEachNum, orderId);
                //将创建的任务对象放到循环列表的指定槽中
                CircularQueueDemo.queueList.get(expireIndex).addFirst(task);
                System.out.println("生成了一个" + CalendarUtils.getCurrentTimeInMillis(expire) + "执行的任务【orderId:" + orderId + "】==>expire:" + expire);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ConsumerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (CircularQueueDemo.queueList.size() > 0) {
                    LinkedList<Task> list = CircularQueueDemo.queueList.get(currentIndex);
                    for (int i = 0; i < list.size(); i++) {
                        Task task = list.get(i);

                        if (task.getNum() < CircularQueueDemo.eachNum) {
                            list.remove(i);
                            continue;
                        }
                        if (task.getNum() == CircularQueueDemo.eachNum) {
                            System.out.println(CalendarUtils.getCurrentTimeInMillis(0) + "消费者处理了一个任务：orderid:" + task.getOrderId() + "轮询的下标：" + currentIndex + "第" + eachNum + "轮循环");
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (CircularQueueDemo.currentIndex == CircularQueueDemo.SLOT_NUM - 1) {
                    CircularQueueDemo.currentIndex = 0;
                    CircularQueueDemo.eachNum++;
                } else {
                    CircularQueueDemo.currentIndex++;
                }
            }
        }
    }

    static class Task {
        private int num;
        private String orderId;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        Task(int num, String orderId) {
            this.num = num;
            this.orderId = orderId;
        }
    }
}
