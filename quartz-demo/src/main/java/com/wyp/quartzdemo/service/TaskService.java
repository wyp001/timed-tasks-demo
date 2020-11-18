package com.wyp.quartzdemo.service;

import com.wyp.quartzdemo.entity.Task;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * (Task)表服务接口
 *
 * @author makejava
 * @since 2020-11-18 11:29:17
 */
public interface TaskService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Task queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Task> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    Task insert(Task task);

    /**
     * 修改数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    Task update(Task task);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    void initSchedule() throws SchedulerException;

    Task resumeTask(Long taskId);

    Task stopTask(Long taskId);

}