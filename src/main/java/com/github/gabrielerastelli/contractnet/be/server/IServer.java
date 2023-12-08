package com.github.gabrielerastelli.contractnet.be.server;

import com.github.gabrielerastelli.contractnet.interfaces.ServerPublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class IServer implements Runnable, ServerPublisher {

    String ip;

    int numberOfThreads;

}
