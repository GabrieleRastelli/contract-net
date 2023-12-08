package com.github.gabrielerastelli.contractnet.be.randomassignement;

import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.contractnet.ContractNetSimulation;
import com.github.gabrielerastelli.contractnet.be.contractnet.server.ContractNetServerImpl;
import com.github.gabrielerastelli.contractnet.be.randomassignment.RandomAssignmentSimulation;
import com.github.gabrielerastelli.contractnet.be.randomassignment.server.RandomAssignementServerImpl;
import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RandomAssignementSimulationTests {

    @Test
    public void oneTaskAndOneServer() throws Exception {
        List<IServer> servers = new ArrayList<>();
        servers.add(new RandomAssignementServerImpl("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));

        new RandomAssignmentSimulation(new ArrayList<>()).startSimulation(SimulationType.RANDOM_A_PRIORI, tasks, servers);
    }

    @Test
    public void twoTasksAndOneServer() throws Exception {
        List<IServer> servers = new ArrayList<>();
        servers.add(new RandomAssignementServerImpl("0.0.0.0", 1, new ArrayList<>()));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));

        new RandomAssignmentSimulation(new ArrayList<>()).startSimulation(SimulationType.RANDOM_A_PRIORI, tasks, servers);
    }

    @Test
    public void oneTaskAndTwoServer() throws Exception {
        List<IServer> servers = new ArrayList<>();
        servers.add(new RandomAssignementServerImpl("0.0.0.0", 1, new ArrayList<>()));
        servers.add(new RandomAssignementServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));

        new RandomAssignmentSimulation(new ArrayList<>()).startSimulation(SimulationType.RANDOM_A_PRIORI, tasks, servers);
    }

    @Test
    public void twoTasksAndTwoServer() throws Exception {
        List<IServer> servers = new ArrayList<>();
        servers.add(new RandomAssignementServerImpl("0.0.0.0", 1, new ArrayList<>()));
        servers.add(new RandomAssignementServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));

        new RandomAssignmentSimulation(new ArrayList<>()).startSimulation(SimulationType.RANDOM_A_PRIORI, tasks, servers);
    }

    @Test
    public void sixTasksAndTwoServer() throws Exception {
        List<IServer> servers = new ArrayList<>();
        servers.add(new RandomAssignementServerImpl("0.0.0.0", 1, new ArrayList<>()));
        servers.add(new RandomAssignementServerImpl("1.1.1.1", 1, new ArrayList<>()));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));
        tasks.add(new Task(UUID.randomUUID().toString(), 1000));

        new RandomAssignmentSimulation(new ArrayList<>()).startSimulation(SimulationType.RANDOM_A_PRIORI, tasks, servers);
    }
}
