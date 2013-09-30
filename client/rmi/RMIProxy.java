package rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

import message.RMIMessage;
import message.RMIMessage.MSG_TYPE;
import message.RemoteMethod;
import registry.RemoteObjectRef;

/**
 * 
 * Proxy object : stub class. Its job its to handle the marshalling of the 
 * method invocation into a message and deliver the message to the 
 * communication module and the reverse of this process.
 *
 */
public class RMIProxy implements InvocationHandler {
    //fields
    private String Ipaddr;
    private int port;
    private RemoteObjectRef ror;
    
    public RMIProxy (String Ipaddr, int port, RemoteObjectRef ror) {
        this.Ipaddr = Ipaddr;
        this.port = port;
        this.ror = ror;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
    	System.out.printf("Called %s\n", method.getName());
        
        //build method
        RemoteMethod rMethod = new RemoteMethod(method.getName(), args, ror);
        //send request
        RMIMessage request = new RMIMessage(MSG_TYPE.METHOD_INVOCATION, rMethod);
        Socket sock = request.send(null, Ipaddr, port);
        //get response
        RMIMessage response = RMIMessage.receive(sock, Ipaddr, port);
        if(!sock.isClosed())
        	sock.close();

        RemoteMethod returnMethod = (RemoteMethod)response.getContent(); 
        if (response.getType() != MSG_TYPE.METHOD_INVOCATION || returnMethod.getException() != null) {
            returnMethod.getException().exceptionMsg();
            return null;
        }
        return returnMethod.getReturnValue();
    }

    public RemoteObjectRef getRef() {
    	return this.ror;
    }
}
