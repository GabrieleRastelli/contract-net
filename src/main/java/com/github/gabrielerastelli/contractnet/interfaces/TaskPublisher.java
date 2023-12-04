package com.github.gabrielerastelli.contractnet.interfaces;

public interface TaskPublisher {
    void addUpdateListener(TaskUpdateListener listener);

    void notifyUpdate(String taskId, String status);
}
