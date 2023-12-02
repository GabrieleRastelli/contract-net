package com.github.gabrielerastelli.contractnet;

import com.github.gabrielerastelli.contractnet.server.factory.ServerGeneratorFactory;
import com.github.gabrielerastelli.contractnet.task.factory.TaskGeneratorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        MainService mainService = new MainService(new ServerGeneratorFactory(), new TaskGeneratorFactory());
        mainService.startSimulation();
    }
}