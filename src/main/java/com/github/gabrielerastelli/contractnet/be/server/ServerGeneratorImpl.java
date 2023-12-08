package com.github.gabrielerastelli.contractnet.be.server;

import com.github.gabrielerastelli.contractnet.be.SimulationType;
import com.github.gabrielerastelli.contractnet.be.contractnet.server.ContractNetServerImpl;
import com.github.gabrielerastelli.contractnet.be.randomassignment.server.RandomAssignementServerImpl;
import com.github.gabrielerastelli.contractnet.be.roundrobin.server.RoundRobinServerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerGeneratorImpl implements ServerGenerator {
    @Override
    public List<IServer> createServers(SimulationType simulationType, int numberOfServers, int numberOfThreads) {
        Random random = new Random();
        if(numberOfServers == 0) {
            numberOfServers = random.nextInt(10 - 1) + 1;
        }

        List<IServer> servers = new ArrayList<>();
        for(int i = 0; i < numberOfServers; ++i) {
            String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            boolean contractNet = SimulationType.CONTRACT_NET_BALANCED.equals(simulationType) || SimulationType.CONTRACT_NET_ACCEPT_FIRST.equals(simulationType);
            if(numberOfThreads == 0) {
                if(contractNet) {
                    servers.add(new ContractNetServerImpl(ip, random.nextInt(4 - 1) + 1, new ArrayList<>()));
                } else if (SimulationType.RANDOM_A_PRIORI.equals(simulationType)) {
                    servers.add(new RandomAssignementServerImpl(ip, random.nextInt(4 - 1) + 1, new ArrayList<>()));
                } else if (SimulationType.ROUND_ROBIN.equals(simulationType)) {
                    servers.add(new RoundRobinServerImpl(ip, random.nextInt(4 - 1) + 1, new ArrayList<>()));
                }
            } else {
                if(contractNet) {
                    servers.add(new ContractNetServerImpl(ip, numberOfThreads, new ArrayList<>()));
                } else if (SimulationType.RANDOM_A_PRIORI.equals(simulationType)) {
                    servers.add(new RandomAssignementServerImpl(ip, numberOfThreads, new ArrayList<>()));
                } else if (SimulationType.ROUND_ROBIN.equals(simulationType)) {
                    servers.add(new RoundRobinServerImpl(ip, numberOfThreads, new ArrayList<>()));
                }
                servers.add(new ContractNetServerImpl(ip, numberOfThreads, new ArrayList<>()));
            }
        }

        return servers;
    }
}
