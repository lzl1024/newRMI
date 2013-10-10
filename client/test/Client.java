package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.lang.reflect.Method;
import java.lang.Integer;

import message.RMILookup;
import rmi.Rmic;
import registry.RemoteObjectRef;
import util.Constants;
/**
 * Main function in client side. Add five test cases to proceed testing
 * Display command line to accept coming RMI request.
 *
 */
public class Client {
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException {
		try{
			int port = Integer.parseInt(args[0]);
			Constants.Download_PORT = Integer.parseInt(args[2]);
			
			if (port <=0 || port > Constants.MAX_PORT_NUM ||
					Constants.Download_PORT <=0 || Constants.Download_PORT > Constants.MAX_PORT_NUM	) {
				System.out.println("Port number error");
				throw new Exception();
			}

			System.out.println("Start testing");
			//test1 basic type
			System.out.println("test1: Basic test");
			System.out.println(invokeMethod(args[1] + " " + port +" PrintMsg1 " +
					"PrintGreeting", "Hello world!"));
			
			//test2 two instances with same type
			System.out.println("\ntest2: Two instances with same type");
			System.out.println(invokeMethod(args[1] + " " + port +" PrintFields1 " +
					"PrintGreeting"));

			System.out.println(invokeMethod(args[1] + " " + port +" PrintFields2 " +
					"PrintGreeting"));


			//test3 no remote interface, real object
			System.out.println("\ntest3: pass-by-value-test");
			invokeMethod(args[1] + " " + port +" GettheRealMe1 " + "proveMe");

			//test4 Exception test
			System.out.println("\ntest4: Exception caught test");
	        invokeMethod(args[1] + " " + port +" PrintException1 " + "PrintGreeting");
	        System.out.println();
	        
	        System.out.println("\ntest5: Look up fail test");
	        invokeMethod(args[1] + " " + port +" PrintException3 " + "PrintGreeting");
	        
	        printUseage();
	        String cmdInput = "";
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        while (!cmdInput.equals("quit")) {
	        	System.out.print("cmd% ");
	        	try {
	                cmdInput = in.readLine();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        	
	        	try {
	        		if (!cmdInput.equals("quit")) {
	        			if (cmdInput.split("\\s+").length == 4) {
	        				System.out.println("Please input args, split by space:");
	        				String str = in.readLine();
	        				if(str.length() != 0) {	
	        					String[] arg = str.split("\\s+");
	        					System.out.println(invokeMethod(cmdInput, (Object[])arg));
	        				} else {
	        					System.out.println(invokeMethod(cmdInput));
	        				}
	        			} else {
	        				System.out.println("Input Invalid!");
	        			}
	        		}
	        	} catch (Exception e) {
	        		System.out.println("Input Invalid!");
	        	}
	        }

	        //TODO: 1. connection caching
			
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Usage: <serverPort>");
		}
	}

    /**
     * the interface for handling any client request
     * @param cmdInput, args
     * @return the return value
     */
	private static Object invokeMethod(String cmdInput, Object... args) throws Exception{
		String[] classResource = cmdInput.split("\\s+");
		RemoteObjectRef ror = RMILookup.lookup(classResource[0], 
		Integer.parseInt(classResource[1]), classResource[2]);
		if (ror == null) {
			System.out.println("Sorry, the resource has not been registered.");
			return null;
		}

		Object object = Rmic.localise(ror);
	    try {
			Method method = object.getClass().getMethod(classResource[3], 
            		getParamTypes(args));          
            //invoke method
            return method.invoke(object, args);
        } catch(Exception e) {
            System.out.println("Invalid Input!");
        }
        return null;
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

    private static void printUseage() {
        System.out.println("\nYou can add your own test now...");
        System.out.println("Useage:");
        System.out.println("quit : client quit");
        System.out.println("<ServerIp> <ServerPort> <url> <MethodName> : remote call");
    }
}
