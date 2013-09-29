package test;

import interfaces.PrintMsg;

public class PrintMsgImpl implements PrintMsg {

	public PrintMsgImpl(){}
	
	public String PrintGreeting(String str) {
		return str;
	}
}
