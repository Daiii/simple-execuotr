import java.util.List;

/**
 * 手写线程池
 */
public class ThreadPoolExecutor {

    // 核心线程数
    private volatile int threadNum;
    // 最大线程数
    // TODO 待实现
    private volatile int maxThreadNum;
    // 线程队列
    private final List<Runnable> queue;
    // 工作线程
    private WorkerThread[] workerThreads;

    public ThreadPoolExecutor(int threadNum, int maxThreadNum, List<Runnable> queue) {
        this.threadNum = threadNum;
        this.maxThreadNum = maxThreadNum;
        this.queue = queue;
        init();
    }

    /**
     * 提交任务
     *
     * @param task 线程
     */
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notifyAll();
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        for (int i = 0; i < threadNum; i++) {
            workerThreads[i].sotp();
            workerThreads[i] = null;
        }
    }

    /**
     * 初始化线程池
     */
    private void init() {
        workerThreads = new WorkerThread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start();
        }
    }

    /**
     * 工作线程
     */
    public class WorkerThread extends Thread {

        // 工作标识
        private volatile boolean isRunning = true;

        @Override
        public void run() {
            Runnable task = null;
            try {
                while (isRunning && !isInterrupted()) {
                    synchronized (queue) {
                        // 等待
                        while (isRunning && !isInterrupted() && queue.isEmpty()) {
                            queue.wait();
                        }

                        // 取第一个
                        if (isRunning && !isInterrupted() && !queue.isEmpty()) {
                            task = queue.remove(0);
                        }

                        if (task != null) {
                            task.run();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task = null;
        }

        /**
         * 取消工作
         */
        public void sotp() {
            isRunning = false;
            interrupt();
        }
    }
}
