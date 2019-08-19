package com.lolibaba.base.delay.utils;

import java.util.Calendar;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MyDelayed implements Delayed {
    /**
     * 任务超时时间戳
     */
    private long expire = 0;

    /**
     * 订单ID
     */
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MyDelayed(int delaySecond, String orderId) {
        this.expire = CalendarUtils.getCurrentTimeInMillis(delaySecond);
        this.orderId = orderId;
    }


    /**
     * 还有多久延迟
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        Calendar cal = Calendar.getInstance();
        return this.expire - cal.getTimeInMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}
