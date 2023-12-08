package com.github.gabrielerastelli.contractnet.be.server;

import com.github.gabrielerastelli.contractnet.be.SimulationType;

import java.util.List;

public interface ServerGenerator {
    List<IServer> createServers(SimulationType simulationType, int numberOfServers, int numberOfThreads);
}
