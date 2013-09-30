package rmi;

import interfaces.Remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;


import registry.Registry;
import registry.Registry.RegistryObj;
import registry.RemoteObjectRef;
import interfaces.GettheRealMe;
import test.PrintExceptionImpl;
import test.PrintFieldsImpl;
import test.PrintMsgImpl;
import util.Constants;

public class Server {

    public static void main(String[] args) {
        Registry registryModule = new Registry();
        int port = parse(args);
        //start dispatcher service
        new Thread(new ProxyDispatcher(port, registryModule)).start();
        executing(registryModule, port);
        System.exit(0);
    }

    private static void executing(Registry registryModule, int port) {
        String cmdInput = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        printUseage();
        fillRegistry(registryModule, port);

        while (!cmdInput.equals("quit")) {
            System.out.print("cmd% ");
            try {
                cmdInput = in.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //instantiate a instance
            if (cmdInput.equals("ps")) {
            	for (RegistryObj object : registryModule.getMap().values()) {
            		RemoteObjectRef reference = object.getRef();
            		System.out.printf("url: %s, interfaceName: %s\n",
            				reference.getObjectKey(), reference.getInterfacename());
            	}
            } else if (!cmdInput.equals("quit")) {
                String[] classResource = cmdInput.split("\\s+"); 

                try {
                    Class<?> obj = Class.forName(Constants.CLASS_PREFIX+
                    		classResource[0].toString());
                    String url = classResource[1];
                    Object newInstance;
                    if (classResource.length == 2) {

                    	Constructor<?> objConstructor = obj.getConstructor();
                    	newInstance = objConstructor.newInstance();
                    } else {
                    	Constructor<?> objConstructor = obj.getConstructor(String[].class);
                    	String[] args = new String[classResource.length - 2];
                    
                        System.arraycopy(classResource, 2, args, 0, classResource.length - 2);

                    	newInstance = objConstructor.newInstance(new Object[] {args});
                    }
                    String interfaceName = getInterfaceName(obj.getInterfaces());
                    if (interfaceName == null) {
                    	interfaceName = "fake:"+Constants.CLASS_PREFIX+
                        		classResource[0].toString();
                    }
                    RemoteObjectRef ref = new RemoteObjectRef(null, port, url, 
                    		interfaceName);
                    //add instance reference into registry 
                    RegistryObj newObj = registryModule.new RegistryObj(ref, newInstance);
                    registryModule.addItem(url, newObj);
                } catch (Exception e) {
                    System.out.println("Invalid command!");
                } 
            }
        }
        
    }

    /**
     * fill Registry for test case
     * @param registryModule
     * @param port 
     */
    private static void fillRegistry(Registry registryModule, int port) {
		try {
    	//for test1
    	PrintMsgImpl instance1 = new PrintMsgImpl();
    	RemoteObjectRef ref = new RemoteObjectRef(null, port, "PrintMsg1", 
         		"interfaces.PrintMsg");
    	RegistryObj newObj = registryModule.new RegistryObj(ref, instance1);
    	registryModule.addItem("PrintMsg1", newObj);

    	//for test2
    	PrintFieldsImpl instance2 = new PrintFieldsImpl(new String[]
    			{"I", "Like", "This"});
    	ref = new RemoteObjectRef(null, port, "PrintFields1", 
         		"interfaces.PrintFields");
    	newObj = registryModule.new RegistryObj(ref, instance2);
    	registryModule.addItem("PrintFields1", newObj);

    	PrintFieldsImpl instance3 = new PrintFieldsImpl(new String[]
    			{"I", "Don't", "Like", "This"});
    	ref = new RemoteObjectRef(null, port, "PrintFields2", 
         		"interfaces.PrintFields");
    	newObj = registryModule.new RegistryObj(ref, instance3);
    	registryModule.addItem("PrintFields2", newObj);
 
    	//for test3
    	GettheRealMe realMe = new GettheRealMe();
    	ref = new RemoteObjectRef(null, port, "GettheRealMe1", "fake:interfaces.GettheRealMe");
    	newObj = registryModule.new RegistryObj(ref, realMe);
    	registryModule.addItem("GettheRealMe1", newObj);

    	//for test4
    	PrintExceptionImpl instance4 = new PrintExceptionImpl(new String[0]);
        ref = new RemoteObjectRef(null, port, "PrintException1", 
                "interfaces.PrintFields");
        newObj = registryModule.new RegistryObj(ref, instance4);
        registryModule.addItem("PrintException1", newObj);
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
     * get the remote interface
     * @param interfaces
     * @return
     */
    private static String getInterfaceName(Class<?>[] interfaces) {
        for (Class<?> inter : interfaces) {
            if (Remote.class.isAssignableFrom(inter)) {
                return inter.getName();
            }
        }
        return null;
    }

    private static void printUseage() {
        System.out.println("Useage:");
        System.out.println("quit : server quit");
        System.out.println("ps : show Registry");
        System.out.println("<ClassName> <URL> <arg1> <arg2> ... : add instance");
    }

    /**
     * parse command line
     * @param args
     */
    private static int parse(String[] args) {
        boolean robustFlag = false;
        int serverPort = -1;

        if (args.length == 1) {
            try{
                if (args[0].equals("-h")) {
                    System.out.println("cmdLine Helper, Usage: <serverport>"); 
                    System.exit(0);
                } else {
                    //parse port
                    serverPort = Integer.parseInt(args[0]);
                    if (serverPort <= 0 || serverPort > Constants.MAX_PORT_NUM) {
                        System.out.println("Port number error");
                        throw new Exception();
                    }
                    robustFlag = true;
                }
    
            } catch (Exception e) {}
        }

        if (!robustFlag) {
            System.out.println("Error Arguments, plase use -h to show cmd usage");
            System.exit(0);
        }

        return serverPort;
    }
}
