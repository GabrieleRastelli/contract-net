package com.github.gabrielerastelli.contractnet.be.server;

import com.github.gabrielerastelli.contractnet.be.model.*;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.remote.RemoteClient;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.ServerPublisher;
import com.github.gabrielerastelli.contractnet.interfaces.ServerUpdateListener;
import lights.interfaces.TupleSpaceException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Getter
@Setter
public class Server implements Runnable, ServerPublisher {

    String ip;

    int numberOfThreads;

    List<ServerUpdateListener> listeners;

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

        List<Future<String>> currentWorkload = new ArrayList<>();

        notifyUpdate(this, 0);

        RemoteClient remoteClient = new RemoteClient(space);
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
                        remoteClient.outTuple(new TaskCompletion(taskId, ip));
                        notifyUpdate(this, currentWorkload.size());
                    }
                } catch (RemoteException | TupleSpaceException | InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                CallForProposal cfp = remoteClient.readCallForProposal();
                if(cfp != null) {
                    if (currentWorkload.size() < numberOfThreads) {
                        log.info("[{}] Making a proposal for task: {}", ip, cfp.getTaskId());
                        remoteClient.outTuple(new Proposal(Decision.ACCEPT, cfp.getTaskId(), ip));
                        ProposalOutcome proposalOutcome = remoteClient.readProposalOutcome(ip);
                        log.info("[{}] Got an outcome for my proposal for task: {}, outcome: {}", ip,
                            cfp.getTaskId(), proposalOutcome.getDecision().name());
                        if(Decision.ACCEPT.equals(proposalOutcome.getDecision())) {
                            Callable<String> task = Task.getCallableTask(cfp.getTaskId(), cfp.getExecutionTime());
                            currentWorkload.add(executor.submit(task));
                            notifyUpdate(this, currentWorkload.size());
                        }
                    } else {
                        //log.info("[{}] Rejecting call for proposal for task: {}", ip, cfp.getTaskId());
                        // WARNING: actually not used by master
                        space.out(new Proposal(Decision.REJECT, cfp.getTaskId(), ip));
                    }
                }
            } catch (TupleSpaceException | RemoteException e) { // TODO handle
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addUpdateListener(ServerUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyUpdate(Server server, int currentWorkload) {
        for(ServerUpdateListener listener : listeners) {
            listener.onUpdate(server, currentWorkload);
        }
    }
}
