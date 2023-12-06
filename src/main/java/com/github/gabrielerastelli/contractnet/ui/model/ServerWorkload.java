package com.github.gabrielerastelli.contractnet.ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServerWorkload {
    private String serverIp;

    private Integer numberOfThreads;

    private Integer currentWorkload;

    private Integer tasksExecuted;
}
