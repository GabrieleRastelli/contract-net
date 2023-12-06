package com.github.gabrielerastelli.contractnet.be.server;

import java.util.List;

public interface ServerGenerator {
    List<Server> createServers(int numberOfServers, int numberOfThreads);
}
