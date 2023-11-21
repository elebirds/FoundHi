package net.mcpes.foundhi.core.scheduler;

import net.mcpes.foundhi.core.app.APIFunction;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Nukkit Project team
 */
public class AsyncPool extends ThreadPoolExecutor {
    private final APIFunction server;

    public AsyncPool(APIFunction server, int size) {
        super(size, Integer.MAX_VALUE, 60, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        this.setThreadFactory(runnable -> new Thread(runnable) {{
            setDaemon(true);
            setName(String.format("FoundHi Asynchronous Task Handler #%s", getPoolSize()));
        }});
        this.server = server;
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        if (throwable != null) {
            server.getLogger().critical("Exception in asynchronous task", throwable);
        }
    }

    public APIFunction getAPIFunction() {
        return server;
    }
}
