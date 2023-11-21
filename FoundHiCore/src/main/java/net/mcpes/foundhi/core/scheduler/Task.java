package net.mcpes.foundhi.core.scheduler;


import net.mcpes.foundhi.core.app.APIFunction;

/**
 * 表达一个任务的类。<br>A class that describes a task.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
public abstract class Task implements Runnable {
    private TaskHandler taskHandler = null;

    public final TaskHandler getHandler() {
        return this.taskHandler;
    }

    public final void setHandler(TaskHandler taskHandler) {
        if (this.taskHandler == null || taskHandler == null) {
            this.taskHandler = taskHandler;
        }
    }

    public final int getTaskId() {
        return this.taskHandler != null ? this.taskHandler.getTaskId() : -1;
    }

    /**
     * 这个任务被执行时，会调用的过程。<br>
     * What will be called when the task is executed.
     *
     * @param currentTick 服务器从开始运行到现在所经过的tick数，20ticks = 1秒，1tick = 0.05秒。<br>
     *                    The elapsed tick count from the server is started. 20ticks = 1second, 1tick = 0.05second.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    public abstract void onRun(int currentTick);

    @Override
    public final void run() {
        this.onRun(taskHandler.getLastRunTick());
    }

    public void onCancel() {

    }

    public void cancel() {
        try {
            this.getHandler().cancel();
        } catch (RuntimeException ex) {
            APIFunction.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
        }
    }
}
