package com.github.gabrielerastelli.contractnet.be;

import com.github.gabrielerastelli.contractnet.be.model.*;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.remote.RemoteClient;
import com.github.gabrielerastelli.contractnet.be.remote.RemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.be.server.Server;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.TaskPublisher;
import com.github.gabrielerastelli.contractnet.interfaces.TaskUpdateListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Simulation implements TaskPublisher {

    List<TaskUpdateListener> listeners;

    public void startSimulation(SimulationType simulationType, List<Task> tasks, List<Server> servers) throws Exception {
        IRemoteTupleSpace space = RemoteTupleSpace.startTupleSpace();

        ExecutorService executor = Executors.newFixedThreadPool(servers.size(), r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        for(Server server : servers) {
            executor.submit(server);
        }

        RemoteClient remoteClient = new RemoteClient(space);

        /* populate tuple space with cfps for tasks */
        Map<String, Boolean> taskAssignedMap = new HashMap<>();
        int completedTasks = 0;
        for(Task t : tasks) {
            log.info("Making a call for proposal for task: {} in tuple space", t.getId());
            CallForProposal cfp = new CallForProposal(t.getId(), t.getExecutionTime());
            remoteClient.outTuple(cfp);
            taskAssignedMap.put(t.getId(), false);
            notifyUpdate(t.getId(), "new");
        }

        Proposal[] proposals;
        while (((proposals = remoteClient.readProposals()) != null && proposals.length != 0) || completedTasks != tasks.size()) {

            if(proposals == null || proposals.length == 0) {
                continue;
            }

            if(SimulationType.CONTRACT_NET_BALANCED.equals(simulationType)) {
                Arrays.sort(proposals, Comparator.comparingInt(Proposal::getCurrentWorkload));
            }

            for (Proposal proposal : proposals) {
                if (Decision.ACCEPT.equals(proposal.getDecision())) {
                    if (taskAssignedMap.get(proposal.getTaskId())) { /* reject others */
                        log.info("Rejecting proposal for task: {} from server: {} as task is already assigned", proposal.getTaskId(), proposal.getServerIp());
                        remoteClient.outTuple(new ProposalOutcome(Decision.REJECT, proposal.getTaskId(), proposal.getServerIp()));
                    } else { /* accept first */
                        taskAssignedMap.put(proposal.getTaskId(), true);
                        ++completedTasks;

                        log.info("Removing call for proposal");
                        remoteClient.removeCallForProposal(proposal.getTaskId());

                        log.info("Accepting proposal for task: {} from server: {}", proposal.getTaskId(), proposal.getServerIp());
                        remoteClient.outTuple(new ProposalOutcome(Decision.ACCEPT, proposal.getTaskId(), proposal.getServerIp()));

                        notifyUpdate(proposal.getTaskId(), "assigned");
                    }
                }
            }

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
