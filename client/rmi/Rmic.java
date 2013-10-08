package rmi;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import message.RMIMessage;
import message.RMIMessage.MSG_TYPE;
import message.RemoteMethod;
import registry.RemoteObjectRef;
import util.Constants;

public class Rmic {
	/**
     * create a stub 
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Object localise(RemoteObjectRef ror) 
    		throws IOException, ClassNotFoundException {

    	String remoteInterfaceName = ror.getInterfacename();
    	//create a stub to pass-by-value    	
    	if(remoteInterfaceName.startsWith("fake:")) {
    		String className = remoteInterfaceName.substring(
    				remoteInterfaceName.indexOf(":")+1);
    		System.out.println(className);
    		//see whether we have .class file
            try{
            	Class.forName(className);
            } catch (ClassNotFoundException e) {
    			//download the .class file if class not found
            	
    			String interfaceName = className.substring(
    					className.indexOf(".")+1);
    			String url = Constants.S3_URL + interfaceName + ".class";
    			String filename = Constants.CLASS_REALVALUE_PREFIX + interfaceName + ".class";
    			httpDownload(url, filename);	
    		}

    		//build method
    		RemoteMethod rMethod = new RemoteMethod(null, null, ror);
    		//build RMIMessage
    		RMIMessage request = new RMIMessage(MSG_TYPE.GET_REALVALUE, rMethod);
    		//send request
    		Socket sock = request.send(null,  ror.getIpaddr(), ror.getPort());
    		//get response
            RMIMessage response = RMIMessage.receive(sock, ror.getIpaddr(), ror.getPort());
            if(!sock.isClosed())
            	sock.close();
            
            //Real object are stored in the content
            return response.getContent();

    	}//create a stub to pass-by-reference
    	else {
    		RMIProxy handler = new RMIProxy(ror.getIpaddr(), ror.getPort(), ror);
    		Class<?> remoteIfClass = null;
    		try{
    			remoteIfClass = Class.forName(remoteInterfaceName);
    		} catch (ClassNotFoundException e) {
    			//download the .class file if class not found
    			
    			String interfaceName = remoteInterfaceName.substring(
    					remoteInterfaceName.indexOf(".")+1);
    			String url = Constants.S3_URL + interfaceName + ".class";
    			String filename = Constants.CLASS_PREFIX + interfaceName + ".class";
    			httpDownload(url, filename);
    			remoteIfClass = Class.forName(remoteInterfaceName);
    		}
    		Object stub = Proxy.newProxyInstance(remoteIfClass.getClassLoader(), 
    		        new Class[]{remoteIfClass}, handler);
    		return stub;
    	}
    	
    }

    /**
     * Download interface class file from Amazon S3
     * @param urlAddr
     * @param filename
     */
    public static void httpDownload(String urlAddr,String filename){

        try {
        	System.out.printf("Download %s to %s\n", urlAddr, filename);
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