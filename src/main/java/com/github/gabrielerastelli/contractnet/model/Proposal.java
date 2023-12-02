package com.github.gabrielerastelli.contractnet.model;

import lights.Field;
import lights.Tuple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Proposal extends Tuple {

    private static final String TUPLE_ID = "p";

    Decision decision;

    String taskId;

    String serverIp;

    public Proposal(Decision decision, String taskId, String serverIp) {
        this.decision = decision;
        this.taskId = taskId;
        this.serverIp = serverIp;
        add(new Field().setValue(TUPLE_ID)).add(new Field().setValue(decision.name())).add(new Field().setValue(taskId)).add(new Field().setValue(serverIp));
    }
}