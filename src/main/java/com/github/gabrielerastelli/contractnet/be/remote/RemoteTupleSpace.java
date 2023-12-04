package com.github.gabrielerastelli.contractnet.be.remote;

import lights.TupleSpace;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.interfaces.TupleSpaceException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoteTupleSpace extends UnicastRemoteObject implements IRemoteTupleSpace {

    static long serialVersionUID = 1L;

    ITupleSpace space;

    protected RemoteTupleSpace(String name) throws RemoteException {
        super();

        this.space = new TupleSpace(name);
    }


    @Override
    public String getName() throws RemoteException {
        // TODO Auto-generated method stub
        return this.space.getName();
    }


    @Override
    public void out(ITuple tuple) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        this.space.out(tuple);
    }


    @Override
    public void outg(ITuple[] tuples) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        this.space.outg(tuples);
    }


    @Override
    public ITuple in(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.in(template);
    }


    @Override
    public ITuple inp(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.inp(template);
    }


    @Override
    public ITuple[] ing(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.ing(template);
    }


    @Override
    public ITuple rd(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.rd(template);
    }


    @Override
    public ITuple rdp(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.rdp(template);
    }


    @Override
    public ITuple[] rdg(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.rdg(template);
    }


    @Override
    public int count(ITuple template) throws RemoteException, TupleSpaceException {
        // TODO Auto-generated method stub
        return this.space.count(template);
    }


    public static IRemoteTupleSpace startTupleSpace() throws Exception {
        IRemoteTupleSpace remoteTupleSpace = new RemoteTupleSpace("TupleSpace");

        try {
            LocateRegistry.createRegistry(1099);
            log.info("Java RMI registry created.");
        } catch (RemoteException e) {
            log.warn("Java RMI registry already exists.");
        }

        Naming.rebind("//localhost/TupleSpace", remoteTupleSpace);
        log.info("Remote TupleSpace registered on //localhost:1099/TupleSpace.");

        return remoteTupleSpace;
    }


}
