package easycheck.userInterface;
import java.util.*;
/*
* User Interface to take in User input and pass to Logic Controller.
*/

public class UserInterface {}
	public userName;
	
	private Scanner sc = new Scanner(System.in);

	public UserInterface(String name){
		userName = name;
	}
	public getUserName(){
		return userName;
	}
	public getInput(){
		String line = sc.nextLine();
		LogicController.executeCommand(line);
	}
	public display()
		StorageManager.Display();
	}
}
