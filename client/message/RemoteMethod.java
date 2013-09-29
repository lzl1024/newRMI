package message;

import java.io.Serializable;

import registry.RemoteObjectRef;

public class RemoteMethod implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    //fields
    private String name;
    private Object[] args;
    private RemoteObjectRef ror;
    private RemoteException exception;
    private Object returnValue;
    
    public RemoteMethod(){}
    
    public RemoteMethod(String name, Object[] args, RemoteObjectRef ror) {
        this.name = name;
        this.args = args;
        this.ror = ror;
    }

    public RemoteException getException() {
        return exception;
    }

    public void setException(RemoteException e) {
        this.exception = e;
    }

    public Object getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String getName() {
        return this.name;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public RemoteObjectRef getROR() {
        return this.ror;
    }

}
