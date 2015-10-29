package easycheck.commandParser;
import java.util.ArrayList;

import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.commandParser.CommandTypes.Update;
import easycheck.commandParser.CommandTypes.Exit;
import easycheck.commandParser.CommandTypes.Invalid;
import easycheck.commandParser.CommandTypes.Review;
import easycheck.commandParser.CommandTypes.SaveAt;
import easycheck.commandParser.CommandTypes.Search;
import easycheck.commandParser.CommandTypes.Undo;

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


	
	static ArrayList<String> cachedFile = new ArrayList<String>();
    private COMMAND_TYPE commandType;
    private String[] arguments;
    
    public Command() {};
    
    public Command(String commandType, String[] arguments){
    	this.commandType = determineCommandType(commandType);
    	assert(arguments!=null);
        this.arguments = arguments;
    }
    
    public COMMAND_TYPE getCommandType() {
    	return commandType;
    }
    
    public String[] getCommandArguments() {
    	return arguments;
    }

	public enum COMMAND_TYPE {
		ADD, DISPLAY, UPDATE, DELETE, UNDO, SEARCH, REVIEW, SAVE_AT, INVALID, EXIT
	};
	
	public static Command createObject(String commandType, String[] arguments) {
        switch (determineCommandType(commandType)) {
	        case ADD:
//	            return new Add(commandType,arguments);
	        case DISPLAY:
//	        	return new Display(commandType,arguments);
	        case UPDATE:
//	            return new Update();
	        case DELETE:
//	            return new Delete(arguments);
	        case UNDO:
//	            return new Undo();
	        case SEARCH:
	            return new Search(commandType,arguments);
	        case REVIEW:
	            return new Review(commandType,arguments);
	        case SAVE_AT:
	            return new SaveAt(commandType,arguments);
	        case INVALID: 
	        	return new Invalid(commandType, arguments);
	        default:
	        	return null;
	        }
    }
	
	//TODO magic strings??
	private static COMMAND_TYPE determineCommandType(String commandType) {
		if (commandType == null) {
			throw new Error(MESSAGE_COMMAND_NULL);
		}
		if (commandType.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
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