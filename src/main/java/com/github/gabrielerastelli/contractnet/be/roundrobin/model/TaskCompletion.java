package com.github.gabrielerastelli.contractnet.be.roundrobin.model;

import lights.Field;
import lights.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskCompletion extends Tuple {

    private static final String TUPLE_ID = "tc";

    String taskId;

    String serverIp;

    public TaskCompletion(String taskId, String serverIp) {
        this.taskId = taskId;
        this.serverIp = serverIp;
        add(new Field().setValue(TUPLE_ID)).add(new Field().setValue(taskId)).add(new Field().setValue(serverIp));
    }
}
