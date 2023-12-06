package com.github.gabrielerastelli.contractnet.be.server.factory;

import com.github.gabrielerastelli.contractnet.be.server.ServerGeneratorImpl;
import com.github.gabrielerastelli.contractnet.be.server.ServerGenerator;

public class ServerGeneratorFactory {

    public ServerGenerator createServerGenerator() {
        return new ServerGeneratorImpl();
    }
}
