package com.github.gabrielerastelli.contractnet.be.randomassignment;

import com.github.gabrielerastelli.contractnet.be.Simulation;
import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.contractnet.model.TaskCompletion;
import com.github.gabrielerastelli.contractnet.be.randomassignment.model.TaskAssignation;
import com.github.gabrielerastelli.contractnet.be.randomassignment.remote.RandomAssignmentRemoteClient;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.remote.RemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.TaskUpdateListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RandomAssignmentSimulation extends Simulation {

    List<TaskUpdateListener> listeners;

    @Override
    public void startSimulation(SimulationType simulationType, List<Task> tasks, List<IServer> servers) throws Exception {
        log.info("Random assignment simulation");

        IRemoteTupleSpace space = RemoteTupleSpace.startTupleSpace();

        ExecutorService executor = Executors.newFixedThreadPool(servers.size(), r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        for(IServer server : servers) {
            executor.submit(server);
        }

        RandomAssignmentRemoteClient remoteClient = new RandomAssignmentRemoteClient(space);

        /* populate tuple space with tasks assignations */
        Collections.shuffle(tasks);
        for(Task task : tasks) {
            Collections.shuffle(servers);
            IServer randomServer = servers.get(0);
            log.info("Assigning task: {} to server: {}", task.getId(), randomServer.getIp());
            TaskAssignation ta = new TaskAssignation(task.getId(), task.getExecutionTime(), randomServer.getIp());
            remoteClient.outTuple(ta);
            notifyUpdate(task.getId(), "assigned");
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
