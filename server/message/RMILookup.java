package message;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import registry.RemoteObjectRef;
import message.RMIMessage.MSG_TYPE;

public class RMILookup {
	/**
	 * look up ror from the registry
	 * @param remoteIp
	 * @param remotePort
	 * @param url
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static RemoteObjectRef lookup(String remoteIp, int remotePort, String url) 
			throws UnknownHostException, IOException {
		//make up message
		RemoteEntry remoteNaming = new RemoteEntry(url,null);
		RMIMessage rmiMsg = new RMIMessage(MSG_TYPE.LOOK_UP, remoteNaming);
		
		Socket sock = new Socket(remoteIp, remotePort);
		//send and receive message
		rmiMsg.send(sock, remoteIp, remotePort);
		RMIMessage response = RMIMessage.receive(sock, remoteIp, remotePort);

        if(!sock.isClosed())
        	sock.close();
        
        //exception handler
        if(response.getType() != MSG_TYPE.LOOK_UP)
        	System.out.println("response is null" + response.getType());
        
        if(response == null || response.getType() != MSG_TYPE.LOOK_UP)
        	return null;
        RemoteEntry remoteName = (RemoteEntry)response.getContent();
        //return the ror
        return remoteName.getRef();
	}

}
