package message;

import java.io.Serializable;

public class RemoteException implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Exception e;
    
    public RemoteException(Exception e) {
        this.e = e;
    }

    public void exceptionMsg() {
        System.out.println(e.getClass().getSimpleName() + " caught");
    }

}
