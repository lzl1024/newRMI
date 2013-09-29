package test;

import interfaces.PrintFields;

public class PrintFieldsImpl implements PrintFields {

	String[] outmsg;

	public PrintFieldsImpl(String[] msg){
		outmsg = msg;
	}

	public String PrintGreeting(){
		StringBuilder ret = new StringBuilder("");
		for (String element : outmsg) {
			ret.append(element);
			ret.append(" ");
		}
		return ret.toString();
	}
}
