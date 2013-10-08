package test;
import java.io.File;
public class NotSer {
	File myFile;
	public NotSer(){
		this.myFile = new File("");
	}
	public void printMe() {
		System.out.println("I am not a Ser Object!");
	}
}
