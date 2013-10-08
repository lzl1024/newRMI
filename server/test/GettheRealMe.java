package test;

import java.io.Serializable;

public class GettheRealMe implements Serializable{
	/**
	 * The class that has not extend remote (for test usage)
	 */
	private static final long serialVersionUID = 1L;

	public GettheRealMe(){}

	public void proveMe(){
		System.out.println("To prove me a real object, I " +
				"can use stdout in client side!");
	}
}
