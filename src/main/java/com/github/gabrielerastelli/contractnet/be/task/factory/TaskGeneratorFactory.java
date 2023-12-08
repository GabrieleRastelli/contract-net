package com.github.gabrielerastelli.contractnet.be.task.factory;

import com.github.gabrielerastelli.contractnet.be.task.TaskGenerator;
import com.github.gabrielerastelli.contractnet.be.task.TaskGeneratorImpl;

public class TaskGeneratorFactory {
    public TaskGenerator createTaskGenerator() {
        return new TaskGeneratorImpl();
    }
}
