package com.wyp.quartzdemo.service.impl;

import com.wyp.quartzdemo.contants.JobStatusEnum;
import com.wyp.quartzdemo.dao.TaskDao;
import com.wyp.quartzdemo.entity.Task;
import com.wyp.quartzdemo.service.TaskService;
import com.wyp.quartzdemo.utils.QuartzManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Task)表服务实现类
 *
 * @author makejava
 * @since 2020-11-18 11:29:18
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Resource
    private TaskDao taskDao;
    @Resource
    private QuartzManager quartzManager;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Task queryById(Long id) {
        return this.taskDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Task> queryAllByLimit(int offset, int limit) {
        return this.taskDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    @Override
    public Task insert(Task task) {
        this.taskDao.insert(task);
        return task;
    }

    /**
     * 修改数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    @Override
    public Task update(Task task) {
        this.taskDao.update(task);
        return this.queryById(task.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.taskDao.deleteById(id) > 0;
    }

    @Override
    public void initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<Task> jobList = taskDao.queryAll(null);
        for (Task task : jobList) {
            if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus())) {
                quartzManager.addJob(task);
            }
        }
    }

    @Override
    public Task resumeTask(Long taskId) {
        Task task = taskDao.queryById(taskId);
        task.setJobStatus("1");
        taskDao.update(task);
        try {
            quartzManager.resumeJob(task);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Task stopTask(Long taskId) {
        Task task = taskDao.queryById(taskId);
        task.setJobStatus("0");
        taskDao.update(task);
        try {
            quartzManager.pauseJob(task);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        task = this.update(task);
        try {
            quartzManager.updateJobCron(task);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return task;
    }
}