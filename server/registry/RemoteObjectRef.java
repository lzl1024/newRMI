package registry;

import java.io.Serializable;


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
