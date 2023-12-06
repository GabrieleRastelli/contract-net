package com.github.gabrielerastelli.contractnet.be.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TaskGeneratorImpl implements TaskGenerator {
    @Override
    public List<Task> createTasks(int numberOfTasks, int executionTime) {
        Random random = new Random();
        if(numberOfTasks == 0) {
            numberOfTasks = random.nextInt(100 - 1) + 1;
        }

        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i < numberOfTasks; ++i) {
            if(executionTime == 0) {
                executionTime = random.nextInt(30 - 1) + 1;
            }
            tasks.add(new Task(UUID.randomUUID().toString(), executionTime));
        }

        return tasks;
    }
}
