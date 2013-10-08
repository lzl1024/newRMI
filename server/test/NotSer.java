package test;
import java.io.File;
public class NotSer {
	File myFile;
	/**
	 * This is a class that is not serializable or remote (for testing usage).
	 */
	public NotSer(){
		this.myFile = new File("");
	}
	public void printMe() {
		System.out.println("I am not a Ser Object!");
	}
}
