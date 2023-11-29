package com.github.gabrielerastelli.contractnet;

import com.github.gabrielerastelli.contractnet.remote.IRemoteTupleSpace;
import com.github.gabrielerastelli.contractnet.remote.RemoteClient;
import com.github.gabrielerastelli.contractnet.remote.RemoteTupleSpace;
import lights.interfaces.ITuple;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        String name;

        if (args.length == 1) {
            name = args[0];
        } else {
            name = "TupleSpace";
        }

        IRemoteTupleSpace space = RemoteTupleSpace.startTupleSpace(name);

        RemoteClient remoteClient = new RemoteClient();

        List<Object> fields = List.of("Davide", 10);
        remoteClient.addTupleToTupleSpace(space, fields);
        log.info("Added tuple with fields: {}", fields);

        ITuple tupleRead = remoteClient.readTupleFromTupleSpace(space);
        log.info("Read tuple: {}", tupleRead);
    }
}