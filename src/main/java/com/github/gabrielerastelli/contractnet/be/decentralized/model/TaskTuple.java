package com.github.gabrielerastelli.contractnet.be.decentralized.model;

import lights.Field;
import lights.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskTuple extends Tuple {

    private static final String TUPLE_ID = "t";

    String taskId;

    int executionTime;

    public TaskTuple(String taskId, int executionTime) {
        this.taskId = taskId;
        this.executionTime = executionTime;
        add(new Field().setValue(TUPLE_ID)).add(new Field().setValue(taskId)).add(new Field().setValue(executionTime));
    }
}

