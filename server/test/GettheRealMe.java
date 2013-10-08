package test;

import java.io.Serializable;

public class GettheRealMe implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GettheRealMe(){}

	public void proveMe(){
		System.out.println("To prove me a real object, I " +
				"can use stdout in client side!");
	}
}
