package com.github.gabrielerastelli.contractnet.be.randomassignment.model;

import lights.Field;
import lights.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAssignation extends Tuple {

    private static final String TUPLE_ID = "ta";

    String taskId;

    int executionTime;

    String serverIp;

    public TaskAssignation(String taskId, int executionTime, String serverIp) {
        this.taskId = taskId;
        this.executionTime = executionTime;
        this.serverIp = serverIp;
        add(new Field().setValue(TUPLE_ID)).add(new Field().setValue(taskId))
                .add(new Field().setValue(executionTime)).add(new Field().setValue(serverIp));
    }
}
