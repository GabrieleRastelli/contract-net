package com.github.gabrielerastelli.contractnet.be.roundrobin.server;

import com.github.gabrielerastelli.contractnet.be.roundrobin.model.TaskAssignation;
import com.github.gabrielerastelli.contractnet.be.roundrobin.model.TaskCompletion;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.roundrobin.remote.RoundRobinRemoteClient;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.ServerUpdateListener;
import lights.interfaces.TupleSpaceException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class RoundRobinServerImpl extends IServer {

    List<ServerUpdateListener> listeners;

    public RoundRobinServerImpl(String ip, int numberOfThreads, List<ServerUpdateListener> listeners) {
        super(ip, numberOfThreads);
        this.listeners = listeners;
    }

    @Override
    public void run() {
        IRemoteTupleSpace space;
        try {
            space = (IRemoteTupleSpace) Naming.lookup("//localhost/TupleSpace");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            log.error("[{}] Error initializing tuple space", ip, e);
            throw new RuntimeException("Error initializing tuple space", e);
        }

        log.info("[{}] Initialized remote tuple space", ip);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        log.info("[{}] Instantiated thread pool of {} threads", ip, numberOfThreads);

        log.info("[{}] Waiting for tasks to perform...", ip);

        List<TaskAssignation> queue = new ArrayList<>();
        List<Future<String>> currentWorkload = new ArrayList<>();

        notifyUpdate(this, 0);

        RoundRobinRemoteClient roundRobinRemoteClient = new RoundRobinRemoteClient(space);
        while (true) {
            Iterator<Future<String>> i = currentWorkload.iterator();
            while(i.hasNext()) {
                Future<String> task = i.next();
                try {
                    String taskId;
                    if(task.isDone()) {
                        taskId = task.get();
                        i.remove();
                        log.info("[{}] Completed task: {}, putting confirmation in tuple space", ip, taskId);
                        roundRobinRemoteClient.outTuple(new TaskCompletion(taskId, ip));
                        notifyUpdate(this, currentWorkload.size());
                    }
                } catch (RemoteException | TupleSpaceException | InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            if(currentWorkload.size() < numberOfThreads && !queue.isEmpty()) {
                Iterator<TaskAssignation> taskAssignationIterator = queue.listIterator();
                while(currentWorkload.size() < numberOfThreads && taskAssignationIterator.hasNext()) {
                    TaskAssignation ta = taskAssignationIterator.next();
                    Callable<String> task = Task.getCallableTask(ta.getTaskId(), ta.getExecutionTime());
                    currentWorkload.add(executor.submit(task));
                    notifyUpdate(this, currentWorkload.size());
                    taskAssignationIterator.remove();
                }
            }

            TaskAssignation ta;
            try {
                ta = roundRobinRemoteClient.readTaskAssignation(ip);
            } catch (TupleSpaceException | RemoteException e) { // TODO handle
                throw new RuntimeException(e);
            }

            if(ta != null) {
                if(currentWorkload.size() < numberOfThreads) {
                    Callable<String> task = Task.getCallableTask(ta.getTaskId(), ta.getExecutionTime());
                    currentWorkload.add(executor.submit(task));
                    notifyUpdate(this, currentWorkload.size());
                } else { /* add to queue */
                    queue.add(ta);
                }
            }
        }
    }

    @Override
    public void addUpdateListener(ServerUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyUpdate(IServer server, int currentWorkload) {
        for(ServerUpdateListener listener : listeners) {
            listener.onUpdate(server, currentWorkload);
        }
    }

}
