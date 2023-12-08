package com.github.gabrielerastelli.contractnet.be;

import com.github.gabrielerastelli.contractnet.be.server.IServer;
import com.github.gabrielerastelli.contractnet.be.task.Task;
import com.github.gabrielerastelli.contractnet.interfaces.TaskPublisher;

import java.util.List;

public abstract class Simulation implements TaskPublisher {

    public abstract void startSimulation(SimulationType simulationType, List<Task> tasks, List<IServer> servers) throws Exception;

}
