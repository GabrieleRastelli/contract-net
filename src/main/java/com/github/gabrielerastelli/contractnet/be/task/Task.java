package com.github.gabrielerastelli.contractnet.be.task;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class Task {

    String id;

    int executionTime;

    public static Callable<String> getCallableTask(String id, int executionTime) {
        return () -> {
            log.info("Task: {}, starting...", id);
            TimeUnit.MILLISECONDS.sleep(executionTime);
            log.info("Task: {}, finished...", id);
            return id;
        };
    }

}
