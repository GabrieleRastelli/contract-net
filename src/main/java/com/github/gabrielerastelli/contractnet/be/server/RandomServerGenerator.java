package com.github.gabrielerastelli.contractnet.be.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomServerGenerator implements ServerGenerator {
    @Override
    public List<Server> createServers() {
        Random random = new Random();
        int numberOfServers = random.nextInt(10 - 1) + 1;

        List<Server> servers = new ArrayList<>();
        for(int i = 0; i < numberOfServers; ++i) {
            String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            int numberOfThreads = random.nextInt(4 - 1) + 1;
            servers.add(new Server(ip, numberOfThreads, new ArrayList<>()));
        }

        return servers;
    }
}
