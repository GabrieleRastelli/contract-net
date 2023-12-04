package com.github.gabrielerastelli.contractnet.be.remote;

import lights.interfaces.ITuple;
import lights.interfaces.TupleSpaceException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteTupleSpace extends Remote{
	
	String getName() throws RemoteException;

	void out(ITuple tuple) throws RemoteException, TupleSpaceException;

	void outg(ITuple[] tuples) throws RemoteException, TupleSpaceException;

	ITuple in(ITuple template) throws RemoteException, TupleSpaceException;

	ITuple inp(ITuple template) throws RemoteException, TupleSpaceException;

	ITuple[] ing(ITuple template) throws RemoteException, TupleSpaceException;

	ITuple rd(ITuple template) throws RemoteException, TupleSpaceException;

	ITuple rdp(ITuple template) throws RemoteException, TupleSpaceException;

	ITuple[] rdg(ITuple template) throws RemoteException, TupleSpaceException;

	int count(ITuple template) throws RemoteException, TupleSpaceException;
	
}
