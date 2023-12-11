package com.github.gabrielerastelli.contractnet.be.decentralized;

import com.github.gabrielerastelli.contractnet.be.Simulation;
import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.decentralized.model.TaskTuple;
import com.github.gabrielerastelli.contractnet.be.decentralized.remote.DecentralizedRemoteClient;
import com.github.gabrielerastelli.contractnet.be.decentralized.model.TaskCompletion;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.remote.RemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.TaskUpdateListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DecentralizedSimulation extends Simulation {

    List<TaskUpdateListener> listeners;

    @Override
    public void startSimulation(SimulationType simulationType, List<Task> tasks, List<IServer> servers) throws Exception {
        log.info("Decentralized simulation");

        IRemoteTupleSpace space = RemoteTupleSpace.startTupleSpace();

        ExecutorService executor = Executors.newFixedThreadPool(servers.size(), r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        for(IServer server : servers) {
            executor.submit(server);
        }

        DecentralizedRemoteClient remoteClient = new DecentralizedRemoteClient(space);

        /* populate tuple space with tasks */
        for(Task task : tasks) {
            TaskTuple ta = new TaskTuple(task.getId(), task.getExecutionTime());
            remoteClient.outTuple(ta);
            notifyUpdate(task.getId(), "new");
        }

        log.info("Waiting for tasks completion");
        /* check task completion */
        int tasksCompleted = 0;
        while(tasksCompleted < tasks.size()) {
            TaskCompletion[] t = remoteClient.readCompletedTasks();

            if(t == null || t.length == 0) {
                continue;
            }

            for(TaskCompletion t1 : t) {
                log.info("Task: {} was completed by server: {}", t1.getTaskId(), t1.getServerIp());
                ++tasksCompleted;
                notifyUpdate(t1.getTaskId(), "completed");
            }
        }

        log.info("All tasks have been completed.");
    }

    @Override
    public void addUpdateListener(TaskUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyUpdate(String taskId, String status) {
        log.debug("Notifying listeners about task: {}", taskId);

        for(TaskUpdateListener listener : listeners) {
            listener.onUpdate(taskId, status);
        }
    }
}
