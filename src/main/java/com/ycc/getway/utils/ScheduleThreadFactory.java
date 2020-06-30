package com.ycc.getway.utils;

import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MysticalYcc
 * @date 2020/6/1
 */
public class ScheduleThreadFactory implements ThreadFactory {
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public ScheduleThreadFactory(String namePre) {
        namePrefix = namePre +
                "-thread";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t;
        if(StringUtils.isEmpty(namePrefix)){
             t = new Thread(r, "process" + threadNumber.getAndIncrement());
        }else {
             t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        }

        if (t.isDaemon()) {
            t.setDaemon(true);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        return t;
    }
}