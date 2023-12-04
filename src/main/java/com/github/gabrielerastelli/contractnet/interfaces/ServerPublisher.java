package com.github.gabrielerastelli.contractnet.interfaces;

import com.github.gabrielerastelli.contractnet.be.server.Server;

public interface ServerPublisher {
    void addUpdateListener(ServerUpdateListener listener);

    void notifyUpdate(Server server, int currentWorkload);
}
