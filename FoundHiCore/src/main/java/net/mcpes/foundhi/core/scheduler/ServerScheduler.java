package net.mcpes.foundhi.core.scheduler;


import net.mcpes.foundhi.core.app.APIFunction;

import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nukkit Project team
 */
public class ServerScheduler {

    public static int WORKERS = 4;

    private final AsyncPool asyncPool;

    private final Queue<TaskHandler> pending;
    private final Queue<TaskHandler> queue;
    private final Map<Integer, TaskHandler> taskMap;
    private final AtomicInteger currentTaskId;

    private volatile int currentTick;

    public ServerScheduler() {
        this.pending = new ConcurrentLinkedQueue<>();
        this.currentTaskId = new AtomicInteger();
        this.queue = new PriorityQueue<>(11, (left, right) -> {
            int i = left.getNextRunTick() - right.getNextRunTick();
            if (i == 0) {
                return left.getTaskId() - right.getTaskId();
            }
            return i;
        });
        this.taskMap = new ConcurrentHashMap<>();
        this.asyncPool = new AsyncPool(APIFunction.getInstance(), WORKERS);
    }

    public TaskHandler scheduleTask(Task task) {
        return addTask(task, 0, 0, false);
    }

    public TaskHandler scheduleTask(Runnable task) {
        return addTask(task, 0, 0, false);
    }


    public TaskHandler scheduleTask(Runnable task, boolean asynchronous) {
        return addTask(task, 0, 0, asynchronous);
    }

    public TaskHandler scheduleAsyncTask(AsyncTask task) {
        return addTask(task, 0, 0, true);
    }

    @Deprecated
    public void scheduleAsyncTaskToWorker(AsyncTask task, int worker) {
        scheduleAsyncTask(task);
    }

    public int getAsyncTaskPoolSize() {
        return asyncPool.getCorePoolSize();
    }

    public void increaseAsyncTaskPoolSize(int newSize) {
        throw new UnsupportedOperationException("Cannot increase a working pool size."); //wtf?
    }

    public TaskHandler scheduleDelayedTask(Task task, int delay) {
        return this.addTask(task, delay, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Task task, int delay, boolean asynchronous) {
        return this.addTask(task, delay, 0, asynchronous);
    }

    public TaskHandler scheduleDelayedTask(Runnable task, int delay) {
        return addTask(task, delay, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Runnable task, int delay, boolean asynchronous) {
        return addTask(task, delay, 0, asynchronous);
    }

    public TaskHandler scheduleRepeatingTask(Runnable task, int period) {
        return addTask(task, 0, period, false);
    }

    public TaskHandler scheduleRepeatingTask(Runnable task, int period, boolean asynchronous) {
        return addTask(task, 0, period, asynchronous);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int period) {
        return addTask(task, 0, period, false);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int period, boolean asynchronous) {
        return addTask(task, 0, period, asynchronous);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int delay, int period) {
        return addTask(task, delay, period, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int delay, int period, boolean asynchronous) {
        return addTask(task, delay, period, asynchronous);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable task, int delay, int period) {
        return addTask(task, delay, period, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable task, int delay, int period, boolean asynchronous) {
        return addTask(task, delay, period, asynchronous);
    }


    public void cancelTask(int taskId) {
        if (taskMap.containsKey(taskId)) {
            try {
                taskMap.remove(taskId).cancel();
            } catch (RuntimeException ex) {
                APIFunction.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
            }
        }
    }

    public void cancelAllTasks() {
        for (Map.Entry<Integer, TaskHandler> entry : this.taskMap.entrySet()) {
            try {
                entry.getValue().cancel();
            } catch (RuntimeException ex) {
                APIFunction.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
            }
        }
        this.taskMap.clear();
        this.queue.clear();
        this.currentTaskId.set(0);
    }

    public boolean isQueued(int taskId) {
        return this.taskMap.containsKey(taskId);
    }

    private TaskHandler addTask(Runnable task, int delay, int period, boolean asynchronous) {
        if (delay < 0 || period < 0) {
            throw new RuntimeException("Attempted to register a task with negative delay or period.");
        }
        TaskHandler taskHandler = new TaskHandler(task, nextTaskId(), asynchronous);
        taskHandler.setDelay(delay);
        taskHandler.setPeriod(period);
        taskHandler.setNextRunTick(taskHandler.isDelayed() ? currentTick + taskHandler.getDelay() : currentTick);
        if (task instanceof Task) {
            ((Task) task).setHandler(taskHandler);
        }
        pending.offer(taskHandler);
        taskMap.put(taskHandler.getTaskId(), taskHandler);
        return taskHandler;
    }

    public void mainThreadHeartbeat(int currentTick) {
        this.currentTick = currentTick;
        // Accepts pending.
        while (!pending.isEmpty()) {
            queue.offer(pending.poll());
        }
        // Main heart beat.
        while (isReady(currentTick)) {
            TaskHandler taskHandler = queue.poll();
            if (taskHandler.isCancelled()) {
                taskMap.remove(taskHandler.getTaskId());
                continue;
            } else if (taskHandler.isAsynchronous()) {
                asyncPool.execute(taskHandler.getTask());
            } else {
                try {
                    taskHandler.run(currentTick);
                } catch (Throwable e) {
                    APIFunction.getInstance().getLogger().critical("Could not execute taskHandler " + taskHandler.getTaskId() + ": " + e.getMessage());
                    APIFunction.getInstance().getLogger().logException(e instanceof Exception ? (Exception) e : new RuntimeException(e));
                }
            }
            if (taskHandler.isRepeating()) {
                taskHandler.setNextRunTick(currentTick + taskHandler.getPeriod());
                pending.offer(taskHandler);
            } else {
                try {
                    Optional.ofNullable(taskMap.remove(taskHandler.getTaskId())).ifPresent(TaskHandler::cancel);
                } catch (RuntimeException ex) {
                    APIFunction.getInstance().getLogger().critical("Exception while invoking onCancel", ex);
                }
            }
        }
        AsyncTask.collectTask();
    }

    public int getQueueSize() {
        return queue.size() + pending.size();
    }

    private boolean isReady(int currentTick) {
        return this.queue.peek() != null && this.queue.peek().getNextRunTick() <= currentTick;
    }

    private int nextTaskId() {
        return currentTaskId.incrementAndGet();
    }
}
