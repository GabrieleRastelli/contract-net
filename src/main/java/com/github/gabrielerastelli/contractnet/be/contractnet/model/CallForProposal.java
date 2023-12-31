package com.github.gabrielerastelli.contractnet.be.contractnet.model;

import lights.Field;
import lights.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallForProposal extends Tuple {

    private static final String TUPLE_ID = "cfp";

    String taskId;

    int executionTime;

    public CallForProposal(String taskId, int executionTime) {
        this.taskId = taskId;
        this.executionTime = executionTime;
        add(new Field().setValue(TUPLE_ID)).add(new Field().setValue(taskId)).add(new Field().setValue(executionTime));
    }
}
