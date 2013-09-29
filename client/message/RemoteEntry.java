package message;

import java.io.Serializable;

import registry.RemoteObjectRef;

public class RemoteEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private RemoteObjectRef ref;
	private RemoteException exception;
	
	public RemoteEntry(String url, RemoteObjectRef ref) {
		this.url = url;
		this.ref = ref;
	}
	
	public String getUrl() {
		return this.url;
	}
	public RemoteObjectRef getRef() {
		return this.ref;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setRef(RemoteObjectRef ref) {
		this.ref = ref;
	}
	
	public void setException(RemoteException e) {
        this.exception = e;
    }
	public RemoteException getException() {
        return exception;
    }
}
