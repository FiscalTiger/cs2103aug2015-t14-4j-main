package easycheck.logicController;
import java.util.ArrayList;

import easycheck.commandParser.*;
import easycheck.storage.*;

public class LogicController {
	private UserInterface userInterface;
	private CommandParser commandParser;
	private StorageManager storageManager;
	// to pass on to Storage
	private static File easyCheckFile;
	
	/**
	 * This operation is used to instantiate dependencies
	 * 
	 * @return returns a boolean if the command was executed successfully
	 */	
	public static boolean initialise(){
		UserInterface userInterface = new UserInterface("JIM");
		CommandParser commandParser = new CommandParser();
		StorageManager storageManager = new StorageManager(easyCheckFile);
		return true;
	}
	
	/**
	 * This operation is called by UserInterface to execute a command
	 * 
	 * @param userInput
	 * 		is the line of user input
	 * @return returns a boolean if the command was executed successfully
	 */	
	public static boolean executeCommand(String userInput){
		Command command = commandParser.parseCommand(userInput);
		ArrayList<EventInterface> execute(Command cmd);
		return true;
	}
	
	
}
