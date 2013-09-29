package rmi;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import message.RMIMessage;
import message.RMIMessage.MSG_TYPE;
import message.RemoteEntry;
import message.RemoteException;
import message.RemoteMethod;
import registry.Registry;
import registry.Registry.RegistryObj;

/**
 * 
 * This class is responsible for marshalling
 *
 */
public class ProxyDispatcher implements Runnable{

    Registry registryModule;
    int serverPort;

    //fields
    public ProxyDispatcher(int serverPort, Registry registryModule) {
        this.serverPort = serverPort;
        this.registryModule = registryModule;
    }

    @Override
    @SuppressWarnings("resource")
    public void run() {
        ServerSocket serverSock = null;
        try {
            serverSock = new ServerSocket(serverPort);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        }
            
        while (true) {
            Socket sock = null;
            RMIMessage msgout = null;
            try {
                sock = serverSock.accept();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(0);
            }

            //get the message from socket
            try{
               
                RMIMessage msgIn = RMIMessage.receive(sock, null, -1);

                if (msgIn != null) {
                    MSG_TYPE type = msgIn.getType();

                    switch(type) {
                    case METHOD_INVOCATION:
                        msgout = unmarshall(msgIn, sock);
                        break;
                    case REMOTE_EXCEPTION:
                        msgout = replyRemoteException(sock, ((RemoteMethod) msgIn.getContent()).getException());
                        break;
                    case LOOK_UP:
                        msgout = replyLookup(msgIn);
                        break;
                    case GET_REALVALUE:
                        msgout = replyRevalue(msgIn, sock);
                        break;
                    default:
                        System.out.println("Mssage type error");
                        continue;
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
                continue;
            }
            
            //write back
            msgout.send(sock, null, -1);
            if(!sock.isClosed())
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        
    }

    private RMIMessage replyRevalue(RMIMessage msgIn, Socket sock) throws IOException {
        RemoteMethod remoteMethod = (RemoteMethod) msgIn.getContent();
        String url = remoteMethod.getROR().getObjectKey();
        Serializable reference = (Serializable) this.registryModule.findRef(url).getObj();
   
        return new RMIMessage(MSG_TYPE.GET_REALVALUE, reference);
    }

    private RMIMessage replyLookup(RMIMessage msgIn) {
        RemoteEntry lookupPair = (RemoteEntry) msgIn.getContent();
        RegistryObj ref;
        for(String e : this.registryModule.getMap().keySet())
        	System.out.println(e);
        System.out.println(lookupPair.getUrl());
        if ((ref = registryModule.findRef(lookupPair.getUrl())) != null ) {
        	System.out.println("find the ref");
        	lookupPair.setRef(ref.getRef());
        } else {
        	System.out.println("cannot find ref!");
            msgIn = new RMIMessage(MSG_TYPE.REMOTE_EXCEPTION, null);
        } 

        return msgIn;
    }

    /**
     * yields a local object reference, invoke a method and parameters
     * @param msgIn
     * @param sock
     * @return
     */
    private RMIMessage unmarshall(RMIMessage msgIn, Socket sock) {
        RemoteMethod remoteMethod = (RemoteMethod) msgIn.getContent();
        
        String url = remoteMethod.getROR().getObjectKey();
        Object reference = this.registryModule.findRef(url).getObj();
         
        
        Object[] args = remoteMethod.getArgs();

        try {
        	System.out.println("Debug:" + reference.getClass().getName() + " " + remoteMethod.getName());
            Method method = reference.getClass().getMethod(remoteMethod.getName(), 
            		getParamTypes(args));          
            //invoke method
            System.out.println("Method is " + method.getName());
            remoteMethod.setReturnValue(method.invoke(reference, args));

        } catch (Exception e) {
        	System.out.println("Get remote Exception!");
            remoteMethod.setException(new RemoteException(e));
        }
            
        return msgIn;
        
    }


    /**
     * replay a remote Exception
     * @param sock
     * @param remoteException 
     * @return 
     */
    private RMIMessage replyRemoteException(Socket sock, RemoteException remoteException) {
        //set up error message
        RemoteMethod errorMethod = new RemoteMethod();
        errorMethod.setException(remoteException);
        RMIMessage err_msg = new RMIMessage(MSG_TYPE.REMOTE_EXCEPTION, errorMethod);

        return err_msg;
    }


    /**
     * get the types of method args
     * @param args
     * @return
     */
    public static Class<?>[] getParamTypes(Object[] args) {
    	//no args
    	if (args == null) {
    		return new Class<?>[0];
    	}

    	Class<?>[] types = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}
