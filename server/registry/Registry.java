package registry;

import java.util.HashMap;
/**
 * This class use a HashMap to keep the mapping of obj_key and RegistryObj
 * @param RORMap
 */
public class Registry {
	/**
	 * This class is defined as combination of both ror and real value
	 * @param objRef
	 * @param realval
	 */
	public class RegistryObj {
		private RemoteObjectRef ref = null;
		private Object realVal = null;
		
		public RegistryObj(RemoteObjectRef objRef, Object realval) {
			this.ref = objRef;
			this.realVal = realval;
		}
		public RemoteObjectRef getRef() {
			return this.ref;
		}
		public Object getObj() {
			return this.realVal;
		}
	}
    private HashMap<String, RegistryObj> RORMap = new HashMap<String, RegistryObj>();
    
    public Registry() {}
    
    public void addItem(String newUrl, RegistryObj ref) {
    	if(!this.RORMap.containsKey(newUrl)) {
    		this.RORMap.put(newUrl, ref);
    	}
    	else {
    		System.out.println("Receive repetitive item in Registry.");
    	}
    }
    
    public HashMap<String, RegistryObj> getMap() {
    	return this.RORMap;
    }
    
    public RegistryObj findRef(String url) {
    	if(this.RORMap.containsKey(url))
    		return this.RORMap.get(url);
    	return null;
    }
}
