package com.github.gabrielerastelli.contractnet.be;

import com.github.gabrielerastelli.contractnet.be.server.Server;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimulationTests {

    @Test
    public void oneTaskAndOneServer() throws Exception {
        List<Server> servers = List.of(new Server("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000));

        new Simulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void twoTasksAndOneServer() throws Exception {
        List<Server> servers = List.of(new Server("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new Simulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void oneTaskAndTwoServer() throws Exception {
        List<Server> servers = List.of(new Server("0.0.0.0", 1, new ArrayList<>()),
                new Server("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000));

        new Simulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void twoTasksAndTwoServer() throws Exception {
        List<Server> servers = List.of(new Server("0.0.0.0", 1, new ArrayList<>()),
                new Server("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new Simulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }

    @Test
    public void sixTasksAndTwoServer() throws Exception {
        List<Server> servers = List.of(new Server("0.0.0.0", 1, new ArrayList<>()),
                new Server("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = List.of(new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000),
                new Task(UUID.randomUUID().toString(), 3000));

        new Simulation(new ArrayList<>()).startSimulation(SimulationType.CONTRACT_NET_ACCEPT_FIRST, tasks, servers);
    }
}
