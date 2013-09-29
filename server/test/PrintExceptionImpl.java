package test;

import interfaces.PrintFields;

public class PrintExceptionImpl implements PrintFields{

    String[] outmsg;

    public PrintExceptionImpl(String[] msg){
        outmsg = msg;
    }

    @Override
    //intentionally throw an exception
    public String PrintGreeting() {
        throw new IllegalArgumentException();
    }

}
