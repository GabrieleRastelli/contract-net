package com.github.gabrielerastelli.contractnet.be.server.factory;

import com.github.gabrielerastelli.contractnet.be.server.ServerGenerator;
import com.github.gabrielerastelli.contractnet.be.server.ServerGeneratorImpl;

public class ServerGeneratorFactory {

    public ServerGenerator createServerGenerator() {
        return new ServerGeneratorImpl();
    }
}
