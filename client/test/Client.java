package test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.lang.reflect.Method;
import java.lang.Integer;

import message.RMILookup;
import registry.RemoteObjectRef;
import util.Constants;

public class Client {
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException {
		try{
			int port = Integer.parseInt(args[0]);
			
			if (port <=0 || port > Constants.MAX_PORT_NUM) {
				throw new Exception();
			}

			System.out.println("Start testing");
			//test1 basic type
			System.out.println("test1: basic test");
			invokeMethod("localhost " + port +" PrintMsg1 PrintGreeting", "Hello world!");
			/*interfaces.PrintMsg printExample = (interfaces.PrintMsg)ror.localise(null);
			String result = printExample.PrintGreeting("Hello world!");
			System.out.println(result);
			
			//test2 two instances with same type
			System.out.println("test2: same instances test");
			ror = RMILookup.lookup("localhost", port, "PrintFields1");
			interfaces.PrintFields printfields = (interfaces.PrintFields)ror.localise(null);
			System.out.println(printfields.PrintGreeting());

			ror = RMILookup.lookup("localhost", port, "PrintFields2");
			printfields = (interfaces.PrintFields)ror.localise(null);
			System.out.println(printfields.PrintGreeting());

			//test3 no remote interface, real object
			System.out.println("test3: Real object test");
			ror = RMILookup.lookup("localhost", port, "GettheRealMe1");
			interfaces.GettheRealMe realMe = (interfaces.GettheRealMe) ror.localise(null);
			realMe.proveMe();

			//test4 Exception test
	        System.out.println("test4: Exception test");
	        invokeMethod("localhost " + "port PrintException1")
	        ror = RMILookup.lookup("localhost", port, "PrintException1");
	        invokeMethod(ror, )
	        printfields = (interfaces.PrintFields) ror.localise(null);
	        System.out.println(printfields.PrintGreeting());*/
	        
	        //TODO: 1. connection caching
	        //TODO: 2. create stub class and download stub.class using HTTP
			
		} catch (Exception e){
			System.out.println("Usage: <serverPort>");
		}
	}


	private static void invokeMethod(String cmdInput, Object... args) throws Exception{
		String[] classResource = cmdInput.split("\\s+");
		RemoteObjectRef ror = RMILookup.lookup(classResource[0], 
		Integer.parseInt(classResource[1]), classResource[2]);
		
		if (ror == null) {
			System.out.println("Sorry, the resource has not been registered.");
			return;
		}
		
		Object object = ror.localise(null);
	    try {
			Method method = object.getClass().getMethod(classResource[3], 
            		getParamTypes(args));          
            //invoke method
            System.out.println(method.invoke(object, args));
        } catch(Exception e) {
            System.out.println("Invalid Input!");
        }
        
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
