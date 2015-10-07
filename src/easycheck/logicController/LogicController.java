package easycheck.logicController;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandParser;
import easycheck.storage.StorageManager;


public class LogicController {
    private CommandParser commandParser;
    private CommandExecutor commandExecutor;
	private StorageManager storageManager;
	
	/**
	 * This operation is used to instantiate dependencies
	 * 
	 * @return returns a boolean if the command was executed successfully
	 */	
	public LogicController(String easyCheckFile){
		commandParser = new CommandParser();
		storageManager = new StorageManager(easyCheckFile);
		commandExecutor = new CommandExecutor(storageManager.readDataFromEasyCheckFile());
	}
	
	
	/**
	 * This operation is called by UserInterface to execute a command
	 * 
	 * @param userInput
	 * 		is the line of user input
	 * @return returns a boolean if the command was executed successfully
	 */	
	public String executeCommand(String userInput){
		Command command = commandParser.parseCommand(userInput);
		return commandExecutor.executeCommand(command);
	}
	
	
}
