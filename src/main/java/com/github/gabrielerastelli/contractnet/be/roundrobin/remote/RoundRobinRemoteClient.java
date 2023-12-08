package com.github.gabrielerastelli.contractnet.be.roundrobin.remote;

import com.github.gabrielerastelli.contractnet.be.roundrobin.model.TaskCompletion;
import com.github.gabrielerastelli.contractnet.be.roundrobin.model.TaskAssignation;
import com.github.gabrielerastelli.contractnet.be.remote.IRemoteTupleSpace;
import lights.Field;
import lights.Tuple;
import lights.interfaces.ITuple;
import lights.interfaces.TupleSpaceException;
import lombok.AllArgsConstructor;

import java.rmi.RemoteException;

@AllArgsConstructor
public class RoundRobinRemoteClient {

    IRemoteTupleSpace space;

    public void outTuple(ITuple tuple) throws TupleSpaceException, RemoteException {
        space.out(tuple);
    }

    public TaskAssignation readTaskAssignation(String serverIp) throws TupleSpaceException, RemoteException {
        ITuple template = new Tuple().add(new Field().setValue("ta")).add(new Field().setType(String.class))
                .add(new Field().setType(Integer.class)).add(new Field().setValue(serverIp));
        ITuple retrieved = space.inp(template);
        return retrieved == null ? null : new TaskAssignation(retrieved.get(1).toString(),
                Integer.parseInt(retrieved.get(2).toString()), retrieved.get(3).toString());
    }

    public  TaskCompletion[] readCompletedTasks() throws TupleSpaceException, RemoteException {
        ITuple proposal = new Tuple().add(new Field().setValue("tc")).add(new Field().setType(String.class)).add(new Field().setType(String.class));
        ITuple[] retrieved = space.ing(proposal);
        if(retrieved == null || retrieved.length == 0) {
            return null;
        }
        TaskCompletion[] completedTasks = new TaskCompletion[retrieved.length];
        for(int i = 0; i < retrieved.length; ++i) {
            completedTasks[i] = new TaskCompletion(retrieved[i].get(1).toString(), retrieved[i].get(2).toString());
        }
        return completedTasks;
    }

}
