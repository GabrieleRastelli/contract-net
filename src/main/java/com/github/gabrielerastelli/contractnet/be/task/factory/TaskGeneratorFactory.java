package com.github.gabrielerastelli.contractnet.be.task.factory;

import com.github.gabrielerastelli.contractnet.be.task.RandomTaskGenerator;
import com.github.gabrielerastelli.contractnet.be.task.TaskGenerator;

public class TaskGeneratorFactory {
    public TaskGenerator createTaskGenerator() {
        return new RandomTaskGenerator();
    }
}
