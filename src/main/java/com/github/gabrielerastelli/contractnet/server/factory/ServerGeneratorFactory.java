package com.github.gabrielerastelli.contractnet.server.factory;

import com.github.gabrielerastelli.contractnet.server.RandomServerGenerator;
import com.github.gabrielerastelli.contractnet.server.ServerGenerator;

public class ServerGeneratorFactory {

    public ServerGenerator createServerGenerator() {
        return new RandomServerGenerator();
    }
}
