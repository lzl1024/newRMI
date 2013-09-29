package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;


public class RMIMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum MSG_TYPE {
        METHOD_INVOCATION, REMOTE_EXCEPTION, GET_REALVALUE, LOOK_UP
    }
    
    //fields
    private MSG_TYPE type;
    Serializable content;

    public RMIMessage (MSG_TYPE type, Serializable content) {
        this.type = type;
        this.content = content;
    }

    public MSG_TYPE getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }


    /**
     * send itself to remote place 
     * @param reusedSocket
     * @param Ipaddr
     * @param port
     * @return
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public Socket send(Socket reusedSocket, String Ipaddr, int port) {
        Socket sock = reusedSocket;
        try {
            if (sock == null) {
                sock = new Socket(Ipaddr, port);
            }
            System.out.println("before write object");
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            System.out.println("just before");
            out.writeObject(this);
            System.out.println("After write Object");
            out.flush();

        } catch(Exception e) {
        	e.printStackTrace();
        	if(this.type == MSG_TYPE.LOOK_UP) {
        		RemoteEntry rEntry = (RemoteEntry)this.content;
        		rEntry.setException(new RemoteException (e));
        	}
        	else {
        		RemoteMethod rMethod = (RemoteMethod)this.content;
        		rMethod.setException(new RemoteException(e));
        	}
        }

        return sock;
    }

    /**
     * receive message from remote 
     * @param reusedSocket
     * @param Ipaddr
     * @param port
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("resource")
	public static RMIMessage receive(Socket reusedSocket, String Ipaddr, int port) {
        RMIMessage msg = null;
        Socket sock = reusedSocket;
        try{
            if (sock == null) {
                sock = new Socket(Ipaddr, port);
            }
    
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            msg = (RMIMessage) in.readObject();
            
        } catch (Exception e) {
            if (msg == null) {
                msg = new RMIMessage(MSG_TYPE.REMOTE_EXCEPTION, new RemoteMethod());
            }
            RemoteMethod rMethod = (RemoteMethod)msg.content;
            rMethod.setException(new RemoteException(e));
        }
        
        return msg;
    }
}
