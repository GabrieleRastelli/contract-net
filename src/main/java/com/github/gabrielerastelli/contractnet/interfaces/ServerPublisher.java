package com.github.gabrielerastelli.contractnet.interfaces;

import com.github.gabrielerastelli.contractnet.be.server.IServer;

public interface ServerPublisher {
    void addUpdateListener(ServerUpdateListener listener);

    void notifyUpdate(IServer server, int currentWorkload);
}
