package com.codvision.base.services;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/7-下午8:27
 */
public class TaskCenter {
    private static volatile ArrayList<Task> tasks = new ArrayList<>();

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void setTasks(ArrayList<Task> tasks) {
        TaskCenter.tasks = tasks;
    }

    public static void push(Task task) {
        tasks.add(task);
    }

    public static void pop(Task task) {
        tasks.remove(task);
    }

    public static void executeTasks(ThreadPoolExecutor executor) {
        LogTool.d("执行任务队列。。。。");
        for (Task task : tasks) {
            executor.execute(task.getRunnable());
        }
    }
}
