package registry;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.io.FileOutputStream;
import java.io.InputStream;

import rmi.RMIProxy;

import message.RMIMessage;
import message.RMIMessage.MSG_TYPE;
import message.RemoteMethod;
import util.Constants;

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
    	if(this.remoteInterfaceName.startsWith("fake:")) {
    		String className = remoteInterfaceName.substring(
    				remoteInterfaceName.indexOf(":"));
    		//see whether we have .class file
            //String className = response.getValueClassName();
            try{
            	Class.forName(className);
            } catch (ClassNotFoundException e) {
    			String interfaceName = className.substring(
    					className.indexOf(".")+1);
    			String url = Constants.S3_URL + interfaceName + ".class";
    			String filename = Constants.CLASS_PREFIX + interfaceName + ".class";
    			httpDownload(url, filename);
    		}
    		
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
    		Class<?> remoteIfClass = null;
    		try{
    			remoteIfClass = Class.forName(this.remoteInterfaceName);
    		} catch (ClassNotFoundException e) {
    			String interfaceName = this.remoteInterfaceName.substring(
    					this.remoteInterfaceName.indexOf(".")+1);
    			String url = Constants.S3_URL + interfaceName + ".class";
    			String filename = Constants.CLASS_PREFIX + interfaceName + ".class";
    			httpDownload(url, filename);
    			remoteIfClass = Class.forName(this.remoteInterfaceName);
    		}
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

    /**
     * Download interface class file from Amazon S3
     * @param urlAddr
     * @param filename
     */
    public void httpDownload(String urlAddr,String filename){

        try {
            URLConnection conn = new URL(urlAddr).openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(filename);
            int byteNum = 0;
            
            byte[] buffer = new byte[1204];
            while ((byteNum = in.read(buffer)) != -1) {
                fs.write(buffer, 0, byteNum);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Failed to download class file from Amazon S3");
        }
    }
}
