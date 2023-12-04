package com.github.gabrielerastelli.contractnet.interfaces;

import com.github.gabrielerastelli.contractnet.be.server.Server;

public interface ServerUpdateListener {
    void onUpdate(Server server, int currentWorkload);
}
