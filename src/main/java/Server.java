import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable { // queue class / thread

    private BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
    private int processingTime;
    public int currentSimulationTime = 0;
    Thread t;


    public void setCurrentSimulationTime(int currentSimulationTime) {
        this.currentSimulationTime = currentSimulationTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = getProcessingTime() + processingTime;
    }

    public void setProcessingTime() {
        this.processingTime = 0;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public Server(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
        t = new Thread(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (tasks.size() > 0) { // if there are tasks in the server
                    for (Task task : tasks) { // go through the tasks
                        if (task.getServiceTime() != 0) { // if task is in server and still has service time
                            task.setServiceTime();
                            setProcessingTime(-1);
                            if (task.getServiceTime() == 0) {
                                tasks.remove(task);
                            }
                            Thread.sleep(100);
                        }
                    }
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
