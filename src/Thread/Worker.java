package Thread;

/**
 * Created by Thibault on 31/05/2016.
 */
public class Worker implements Runnable {

    ThreadPool threadPool;

    public Worker(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!threadPool.shouldStop) {
            Action action = threadPool.consume();
            if (action != null) {
                action.apply();
            }
        }
    }
}
