package com.github.gabrielerastelli.contractnet.be.contractnet;

import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.contractnet.server.ContractNetServerImpl;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContractNetSimulationTests {

    @Test
    public void oneTaskAndOneServer() throws Exception {
        List<IServer> servers = List.of(new ContractNetServerImpl("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000));

        new ContractNetSimulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void twoTasksAndOneServer() throws Exception {
        List<IServer> servers = List.of(new ContractNetServerImpl("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new ContractNetSimulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void oneTaskAndTwoServer() throws Exception {
        List<IServer> servers = List.of(new ContractNetServerImpl("0.0.0.0", 1, new ArrayList<>()),
                new ContractNetServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000));

        new ContractNetSimulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void twoTasksAndTwoServer() throws Exception {
        List<IServer> servers = List.of(new ContractNetServerImpl("0.0.0.0", 1, new ArrayList<>()),
                new ContractNetServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new ContractNetSimulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void sixTasksAndTwoServer() throws Exception {
        List<IServer> servers = List.of(new ContractNetServerImpl("0.0.0.0", 1, new ArrayList<>()),
                new ContractNetServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new ContractNetSimulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }
}
