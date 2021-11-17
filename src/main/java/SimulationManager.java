import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationManager implements Runnable {
    Thread t;
    int N; // number of clients
    int Q; // number of queues
    int maxSimulationTime;
    int minArrivalTime;
    int maxArrivalTime;
    int minServiceTime;
    int maxServiceTime;
    int currentSimulationTime;
    int peekHour;
    float averageWaitingTime = 0;
    float averageServiceTime = 0;
    List<Server> servers = new ArrayList<>();
    List<Task> waitingTasks = new ArrayList<>();



    public SimulationManager(int N, int Q, int maxSimulationTime, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        t = new Thread(this);
        this.N = N;
        this.Q = Q;
        this.maxSimulationTime = maxSimulationTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;

    }

    public void generateServers() {
        for (int index = 0; index < Q; index++) {
            BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
            Server server = new Server(tasks);
            server.setProcessingTime();
            servers.add(index, server);
            servers.get(index).setCurrentSimulationTime(0);
            servers.get(index).t.start();
        }
    }

    public void generateClients() {
        Random random = new Random();
        int arrivalTime;
        int serviceTime;
        for (int index = 0; index < N; index++) {
            do {
                arrivalTime = random.nextInt(maxArrivalTime);
            } while (arrivalTime < minArrivalTime);
            do {
                serviceTime = random.nextInt(maxServiceTime);
            } while (serviceTime < minServiceTime);
            Task task = new Task(index, arrivalTime, serviceTime);
            waitingTasks.add(task);
        }
    }

    public int getQueueWithMinimumTasks() {
        int minimum = maxServiceTime + 1;
        int queue = 0;
        for (Server server : servers) {
            if (server.getProcessingTime() < minimum) {
                minimum = server.getProcessingTime();
                queue = servers.indexOf(server);
            }
        }
        return queue;
    }

    public boolean addTasksToQueue() {
        boolean emptyWaitingList = true;
        for (Task task : waitingTasks) {
            if (task.getServiceTime() != 0) {
                emptyWaitingList = false;
            }
            if (currentSimulationTime >= task.getArrivalTime() && taskNotInQueueYet(task) == null && task.getServiceTime() != 0) {
                int goToQueue = getQueueWithMinimumTasks();
                servers.get(goToQueue).getTasks().add(task);
                servers.get(goToQueue).setProcessingTime(task.getServiceTime());
                averageWaitingTime += servers.get(goToQueue).getProcessingTime();
                averageServiceTime += task.getServiceTime();
            }
        }
        return emptyWaitingList;
    }

    public Server taskNotInQueueYet(Task task) {
        for (Server server : servers) {
            if (server.getTasks().contains(task)) {
                return server;
            }
        }
        return null;
    }

    public int getNumberOfTasksInServers() {
        int maximum = 0;
        for (Server server : servers) {
            maximum += server.getTasks().size();
        }
        return maximum;
    }

    public String displayQueues() throws IOException {
        String content = "";
        for (Server server : servers) {
            boolean first = true;

            content = content.concat("Queue ");
            content = content.concat(String.valueOf(servers.indexOf(server)) + ": ");

            if (server.getTasks().isEmpty()) {
                content = content.concat("closed");
            } else {
                for (Task task : server.getTasks()) {
                    if (first) {
                        content = content.concat("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
                        first = false;
                    } else {
                        content = content.concat("; (" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
                    }
                }
            }
            content = content.concat("\n");
            System.out.println();
        }
        return content;
    }

    public boolean areServersEmpty() {
        for (Server server : servers) {
            if (!server.getTasks().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void doStuff() throws IOException {

        int maximum = 0;
        FileWriter fileWriter = new FileWriter("queue.txt");
        while (currentSimulationTime <= maxSimulationTime) {
            fileWriter.append("Time " + currentSimulationTime);
            fileWriter.append("\n");
            if (addTasksToQueue()) {
                if (areServersEmpty()) {
                    currentSimulationTime = maxSimulationTime;
                }
            }
            if (getNumberOfTasksInServers() > maximum) {
                maximum = getNumberOfTasksInServers();
                peekHour = currentSimulationTime;
            }
            String content = displayQueues();
            fileWriter.append(content);
            currentSimulationTime++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        averageWaitingTime = averageWaitingTime / N;
        averageServiceTime = averageServiceTime / N;
        fileWriter.append("\nPeek hour " + peekHour);
        fileWriter.append("\nAverage waiting time " + averageWaitingTime);
        fileWriter.append("\nAverage service time " + averageServiceTime);
        fileWriter.close();
    }

    @Override
    public void run() {
        generateServers();
        generateClients();
        currentSimulationTime = 0;
        try {
            doStuff();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
