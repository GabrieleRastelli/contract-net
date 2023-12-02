package com.github.gabrielerastelli.contractnet;

import com.github.gabrielerastelli.contractnet.model.*;
import com.github.gabrielerastelli.contractnet.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.remote.RemoteClient;
import com.github.gabrielerastelli.contractnet.remote.RemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.server.Server;
import com.github.gabrielerastelli.contractnet.server.ServerGenerator;
import com.github.gabrielerastelli.contractnet.server.factory.ServerGeneratorFactory;
import com.github.gabrielerastelli.contractnet.task.Task;
import com.github.gabrielerastelli.contractnet.task.TaskGenerator;
import com.github.gabrielerastelli.contractnet.task.factory.TaskGeneratorFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MainService {

    ServerGeneratorFactory serverGeneratorFactory;

    TaskGeneratorFactory taskGeneratorFactory;

    public void startSimulation() throws Exception {
        IRemoteTupleSpace space = RemoteTupleSpace.startTupleSpace();

        /* create servers */
        ServerGenerator serverGenerator = serverGeneratorFactory.createServerGenerator();
        List<Server> servers = serverGenerator.createServers();

        ExecutorService executor = Executors.newFixedThreadPool(servers.size());
        for(Server server : servers) {
            executor.submit(server);
        }

        /* create tasks */
        TaskGenerator taskGenerator = taskGeneratorFactory.createTaskGenerator();
        List<Task> tasks = taskGenerator.createTasks();

        RemoteClient remoteClient = new RemoteClient(space);

        /* populate tuple space with cfps for tasks */
        Map<String, Boolean> taskAssignedMap = new HashMap<>();
        int completedTasks = 0;
        for(Task t : tasks) {
            log.info("Making a call for proposal for task: {} in tuple space", t.getId());
            CallForProposal cfp = new CallForProposal(t.getId(), t.getExecutionTime());
            remoteClient.outTuple(cfp);
            taskAssignedMap.put(t.getId(), false);
        }

        /* there are still some task to execute */
        while(completedTasks != tasks.size()) {
            Proposal[] proposals = remoteClient.readProposals();

            if (proposals == null || proposals.length == 0) {
                continue;
            }

            for (Proposal proposal : proposals) {
                if (Decision.ACCEPT.equals(proposal.getDecision())) {
                    if (taskAssignedMap.get(proposal.getTaskId())) { /* reject others */
                        log.info("Rejecting proposal for task: {} from server: {} as task is already assigned", proposal.getTaskId(), proposal.getServerIp());
                        remoteClient.outTuple(new ProposalOutcome(Decision.REJECT, proposal.getTaskId(), proposal.getServerIp()));
                    } else { /* accept first */
                        // TODO base master decision also on other aspects rather than simple thread availability
                        taskAssignedMap.put(proposal.getTaskId(), true);
                        ++completedTasks;

                        log.info("Removing call for proposal");
                        remoteClient.removeCallForProposal(proposal.getTaskId());

                        log.info("Accepting proposal for task: {} from server: {}", proposal.getTaskId(), proposal.getServerIp());
                        remoteClient.outTuple(new ProposalOutcome(Decision.ACCEPT, proposal.getTaskId(), proposal.getServerIp()));
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
            }
        }

        log.info("All tasks have been completed.");
    }
}
