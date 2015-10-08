package easycheck.commandParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

/**
 * Command Type represents a parsed command for Easy Check application.
 * To be called: Command.excecuteCommand(commandType, arguments) which return string
 * In order to pass data into Storage, please call method, getCachedFile() which 
 * returns the ArrayList<string> cachedFile. 
 * 
 * @author A0126989H
 */
public class Command {
	private static final String MESSAGE_COMMAND_NULL = "Error in reading command";
	private static final String MESSAGE_UNRECOGNIZE = "Unrecognized command input!";
	
	static ArrayList<String> cachedFile = new ArrayList<String>();
    private COMMAND_TYPE commandType;
    private String[] arguments;
    
    public Command(String commandType, String[] arguments){
        this.commandType = determineCommandType(commandType);
        this.arguments = arguments;
    }
    
    public COMMAND_TYPE getCommandType() {
    	return commandType;
    }
    
    public String[] getCommandArguments() {
    	return arguments;
    }

	public enum COMMAND_TYPE {
		ADD, ADD_EVENT,DISPLAY,UPDATE, DELETE, UNDO, SEARCH, REVIEW, SAVE_AT, INVALID, EXIT
	};
	
	//TODO magic strings??
	private static COMMAND_TYPE determineCommandType(String commandType) {
		if (commandType == null) {
			throw new Error(MESSAGE_COMMAND_NULL);
		}
		if (commandType.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandType.equalsIgnoreCase("add_event")){
			return COMMAND_TYPE.ADD_EVENT;
		} else if (commandType.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandType.equalsIgnoreCase("update")) {
			return COMMAND_TYPE.UPDATE;
		} else if (commandType.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandType.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandType.equalsIgnoreCase("search")) {
			return COMMAND_TYPE.SEARCH;
		} else if (commandType.equalsIgnoreCase("review")) {
			return COMMAND_TYPE.REVIEW;
		} else if (commandType.equalsIgnoreCase("save_at")){
			return COMMAND_TYPE.SAVE_AT; 
		} else if (commandType.equalsIgnoreCase("exit")){
			return COMMAND_TYPE.EXIT;
		} else {
		
			return COMMAND_TYPE.INVALID;
		}
	}
}