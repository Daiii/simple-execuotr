import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 0, new ArrayList<Runnable>());
        AtomicInteger threadName = new AtomicInteger(0);
        while (true) {
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-" + threadName.incrementAndGet());
                    System.out.println("ThreadName : " + Thread.currentThread() + " , Time : " + System.currentTimeMillis() / 1000);
                }
            });
            executor.execute(task);
        }
    }
}
