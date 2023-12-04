package com.github.gabrielerastelli.contractnet.be.server.factory;

import com.github.gabrielerastelli.contractnet.be.server.RandomServerGenerator;
import com.github.gabrielerastelli.contractnet.be.server.ServerGenerator;

public class ServerGeneratorFactory {

    public ServerGenerator createServerGenerator() {
        return new RandomServerGenerator();
    }
}
