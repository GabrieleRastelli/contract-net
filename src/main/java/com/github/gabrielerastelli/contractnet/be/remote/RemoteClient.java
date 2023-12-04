package com.github.gabrielerastelli.contractnet.be.remote;

import com.github.gabrielerastelli.contractnet.be.model.*;
import lights.Field;
import lights.Tuple;
import lights.interfaces.ITuple;
import lights.interfaces.TupleSpaceException;
import lombok.AllArgsConstructor;

import java.rmi.RemoteException;

@AllArgsConstructor
public class RemoteClient {

    IRemoteTupleSpace space;

    public void outTuple(ITuple tuple) throws TupleSpaceException, RemoteException {
        space.out(tuple);
    }

    public CallForProposal readCallForProposal() throws TupleSpaceException, RemoteException {
        ITuple template = new Tuple().add(new Field().setValue("cfp"))
                .add(new Field().setType(String.class)).add(new Field().setType(Integer.class));
        ITuple retrieved = space.rdp(template);
        return retrieved == null ? null : new CallForProposal(retrieved.get(1).toString(), Integer.parseInt(retrieved.get(2).toString()));
    }

    public CallForProposal removeCallForProposal(String taskId) throws TupleSpaceException, RemoteException {
        ITuple template = new Tuple().add(new Field().setValue("cfp"))
                .add(new Field().setValue(taskId)).add(new Field().setType(Integer.class));
        ITuple retrieved = space.in(template);
        return new CallForProposal(retrieved.get(1).toString(), Integer.parseInt(retrieved.get(2).toString()));
    }

    public ProposalOutcome readProposalOutcome(String ipAddress) throws TupleSpaceException, RemoteException {
        ITuple template = new Tuple().add(new Field().setValue("po")).add(new Field().setType(String.class))
                .add(new Field().setType(String.class)).add(new Field().setValue(ipAddress));
        ITuple retrieved = space.in(template);
        return new ProposalOutcome(Decision.valueOf(retrieved.get(1).toString()),
                retrieved.get(2).toString(), retrieved.get(3).toString());
    }

    public  Proposal[] readProposals() throws TupleSpaceException, RemoteException {
        ITuple template = new Tuple().add(new Field().setValue("p")).add(new Field().setType(String.class))
                .add(new Field().setType(String.class)).add(new Field().setType(String.class));
        ITuple[] retrieved = space.ing(template);
        if(retrieved == null || retrieved.length == 0) {
            return null;
        }
        Proposal[] proposals = new Proposal[retrieved.length];
        for(int i = 0; i < retrieved.length; ++i) {
            proposals[i] = new Proposal(Decision.valueOf(retrieved[i].get(1).toString()),
                    retrieved[i].get(2).toString(), retrieved[i].get(3).toString());
        }
        return proposals;
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
