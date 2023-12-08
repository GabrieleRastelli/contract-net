package com.github.gabrielerastelli.contractnet.interfaces;

import com.github.gabrielerastelli.contractnet.be.server.IServer;

public interface ServerUpdateListener {
    void onUpdate(IServer server, int currentWorkload);
}
