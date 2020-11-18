package com.wyp.quartzdemo.tasks;

import com.wyp.quartzdemo.utils.DateUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

@DisallowConcurrentExecution //作业不并发
@Component
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        System.out.println("正在执行helloworld job 定时任务!"+ DateUtils.fullTime(new Date()));
        
    }

}