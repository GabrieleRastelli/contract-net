package com.github.gabrielerastelli.contractnet.task.factory;

import com.github.gabrielerastelli.contractnet.task.RandomTaskGenerator;
import com.github.gabrielerastelli.contractnet.task.TaskGenerator;

public class TaskGeneratorFactory {
    public TaskGenerator createTaskGenerator() {
        return new RandomTaskGenerator();
    }
}
