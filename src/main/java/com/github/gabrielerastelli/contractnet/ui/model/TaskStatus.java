package com.github.gabrielerastelli.contractnet.ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TaskStatus {
    private String taskId;

    private String status;
}
