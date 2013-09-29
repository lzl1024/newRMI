package registry;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.Socket;

import rmi.RMIProxy;

import message.RMIMessage;
import message.RMIMessage.MSG_TYPE;
import message.RemoteMethod;

/**
 * 
 * @author zhuolinl, dil1
 * 
 * The remote object reference class
 *
 */
public class RemoteObjectRef implements Serializable {
    //fields
	private static final long serialVersionUID = 1L;
    private String IPaddr;
    private int port;
    private String  objectKey;
    private String remoteInterfaceName;

    public RemoteObjectRef(String ip, int port, String obj_key, String riname) {
        this.IPaddr = ip;
        this.port = port;
        this.objectKey = obj_key;
        this.remoteInterfaceName = riname;
    }

    /**
     * create a stub 
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public Object localise(Object[] args) throws IOException, ClassNotFoundException {
    	if(this.remoteInterfaceName == null) {
    		//build method
    		RemoteMethod rMethod = new RemoteMethod(null, args, this);
    		//build RMIMessage
    		RMIMessage request = new RMIMessage(MSG_TYPE.GET_REALVALUE, rMethod);
    		//send request
    		Socket sock = request.send(null, this.IPaddr, this.port);
    		//get response
            RMIMessage response = RMIMessage.receive(sock, this.IPaddr, port);
            if(!sock.isClosed())
            	sock.close();
 
            //Real object are stored in the content
            return response.getContent();

    	}
    	else {
    		RMIProxy handler = new RMIProxy(this.IPaddr, this.port, this);
    		Class<?> remoteIfClass = Class.forName(this.remoteInterfaceName);
    		Object stub = Proxy.newProxyInstance(remoteIfClass.getClassLoader(), 
    		        new Class[]{remoteIfClass}, handler);
    		return stub;
    	}
    	
    }
    
    public String getIpaddr() {
    	return this.IPaddr;
    }
    
    public int getPort() {
    	return this.port;
    }
    public String getObjectKey() {
    	return this.objectKey;
    }
    
    public String getInterfacename() {
    	return this.remoteInterfaceName;
    }
}
