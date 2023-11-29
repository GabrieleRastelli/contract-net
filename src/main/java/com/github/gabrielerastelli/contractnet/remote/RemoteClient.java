package com.github.gabrielerastelli.contractnet.remote;

import lights.Field;
import lights.Tuple;
import lights.interfaces.ITuple;
import lights.interfaces.TupleSpaceException;

import java.rmi.RemoteException;
import java.util.List;


public class RemoteClient {
	
	public void addTupleToTupleSpace(IRemoteTupleSpace space, List<Object> values) throws TupleSpaceException, RemoteException {
        ITuple tuple = new Tuple();

        for(Object obj : values) {
            tuple.add(new Field().setValue(obj));
        }

        space.out(tuple);
	}

    public ITuple readTupleFromTupleSpace(IRemoteTupleSpace space) throws TupleSpaceException, RemoteException {
        ITuple MyTemplate = new Tuple().add(new Field().setType(String.class)).add(new Field().setType(Integer.class));
        return space.rd(MyTemplate);
    }
}
