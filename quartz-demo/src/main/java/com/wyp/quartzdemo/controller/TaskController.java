package com.wyp.quartzdemo.controller;

import com.wyp.quartzdemo.entity.Task;
import com.wyp.quartzdemo.service.TaskService;
import javafx.geometry.Pos;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;

/**
 * (Task)表控制层
 *
 * @author makejava
 * @since 2020-11-18 11:29:18
 */
@RestController
@RequestMapping("task")
public class TaskController {
    /**
     * 服务对象
     */
    @Resource
    private TaskService taskService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Task selectOne(Long id) {
        return this.taskService.queryById(id);
    }

    @PostMapping("addTask")
    public Task addTask(Task task){
        return this.taskService.insert(task);
    }

    @GetMapping("stopTask")
    public Task stopTask(Long taskId){
        Task task = this.taskService.stopTask(taskId);
        return task;
    }

    @GetMapping("resumeTask")
    public Task startTask(Long taskId){
        Task task = taskService.resumeTask(taskId);
        return task;
    }

    @PutMapping("updateTask")
    public Task updateTask(Task task){
        return taskService.updateTask(task);
    }



}