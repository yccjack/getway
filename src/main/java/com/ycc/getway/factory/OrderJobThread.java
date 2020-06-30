package com.ycc.getway.factory;

import com.ycc.getway.utils.ScheduleThreadFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * @author MysticalYcc
 * @date 10:38
 */
@EnableScheduling
@Component
@Configurable
public class OrderJobThread implements SchedulingConfigurer {

    @Value("${time.cron.start}")
    String startTime;
    @Value("${time.cron.stop}")
    String stopTime;

    int startRange = 0;
    int stopRange = 0;
    boolean canRunning = true;
    boolean canSchedule = true;
    static   volatile boolean running = true;
    Thread mainStartWork;



    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(2,new ScheduleThreadFactory("scheduleManager")));
        validateTime();
        if (!canRunning && !canSchedule) {
            canRunning = true;
        }
        if (canRunning) {
            System.out.println("启动服务时间在区间:[{" + startRange + "}]:[{" + stopRange + "}] 内，启动线程扫描。");
            mainStartWork = new Thread(() -> {running =true;run("main");});
            mainStartWork.setName("Thread-Schedule-redis-init");
            mainStartWork.start();
        } else {
            System.out.println("启动服务时间不在区间:[{" + startRange + "}]:[{" + stopRange + "}] 内，启动线程扫描。");
        }
        if (canSchedule) {
            String cronStart = getCron(startTime);
            scheduledTaskRegistrar.addCronTask(() -> startWork("startSchedule"), cronStart);
            String cronStop = getCron(stopTime);
            scheduledTaskRegistrar.addCronTask(this::stopWork, cronStop);
        }
    }
    private void startWork(String resource){
        if(mainStartWork!=null&&mainStartWork.isAlive()){
            if(running){
                System.out.println("线程正在执行，schedule等待下一次检测");
            }else {
                while (mainStartWork.isAlive()){
                    try {
                        System.out.println("等待线程终止");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("schedule线程启动");
                running = true;
                run(resource);
            }
        }else {
            System.out.println("schedule线程启动");
            running = true;
            run(resource);
        }

    }

    private void run(String resource){
        System.out.println(resource+":启动任务");
        while (running) {
            System.out.println(resource+":处理业务");
            try {
                Thread.sleep(60*1000*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void stopWork(){
        running = false;
        System.out.println("关闭任务");
    }


    private void validateTime() {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(stopTime)) {
            System.err.println("配置启停时间不完整，直接启动扫描线程");
            canSchedule = false;
            return;
        }
        String start = startTime.replaceAll(":", "");
        String stop = stopTime.replaceAll(":", "");
        startRange = Integer.parseInt(start.length() < 3 ? start + "00" : start);
        stopRange = Integer.parseInt(stop.length() < 3 ? stop + "00" : stop);
        if(stopRange==0){
            stopRange = 2400;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String date = sdf.format(new Date());
        int time = Integer.parseInt(date);
        if (startRange < stopRange) {
            if (time > stopRange || time < startRange) {
                canRunning = false;
            }
        }

        if (startRange > stopRange) {
            if(time > stopRange && time < startRange){
                canRunning = false;
            }
        }

        if (startRange == stopRange) {
            canSchedule = false;
        }
    }

    private String getCron(String time) {
        String cron;
        String[] split = time.split(":");
        if (split.length > 1) {
            cron = "0 " + Integer.parseInt(split[1]) + " " + Integer.parseInt(split[0]) + " * * ? ";
        } else {
            cron = "0 0 " + Integer.parseInt(split[0]) + " * * ? ";
        }
        return cron;
    }

    public static void main(String[] args) {
        String stra = "a";

        String strb = new String("a");
        System.out.println(stra == strb);
        System.out.println(Integer.parseInt("07"));
        Integer a = 1;
        Integer b = 2;
        System.out.println("交换前a= " + a + ",b= " + b);
        swap(a, b);
        System.out.println("交换后a= " + a + ",b= " + b);
    }

    private static void swap(Integer a, Integer b) {
        int temp = a;
        a = b;
        b = temp;
        System.out.println("交换时a= " + a + ",b= " + b);
    }

}