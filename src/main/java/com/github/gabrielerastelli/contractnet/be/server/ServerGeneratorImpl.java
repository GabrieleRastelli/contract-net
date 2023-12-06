package com.github.gabrielerastelli.contractnet.be.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerGeneratorImpl implements ServerGenerator {
    @Override
    public List<Server> createServers(int numberOfServers, int numberOfThreads) {
        Random random = new Random();
        if(numberOfServers == 0) {
            numberOfServers = random.nextInt(10 - 1) + 1;
        }

        List<Server> servers = new ArrayList<>();
        for(int i = 0; i < numberOfServers; ++i) {
            String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            if(numberOfThreads == 0) {
                servers.add(new Server(ip, random.nextInt(4 - 1) + 1, new ArrayList<>()));
            } else {
                servers.add(new Server(ip, numberOfThreads, new ArrayList<>()));
            }
        }

        return servers;
    }
}
