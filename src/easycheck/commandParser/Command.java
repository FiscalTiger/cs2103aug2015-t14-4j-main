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
 * @author thiennguyen
 */
public class Command {
	private static final String MESSAGE_COMMAND_NULL = "Error in reading command";
	private static final String MESSAGE_UNRECOGNIZE = "Unrecognized command input!";
	
	static ArrayList<String> cachedFile = new ArrayList<String>();

	public Command(String commandType, String[] arguments) throws IOException {
		System.out.println(executeCommand(commandType,arguments));
	}

	enum COMMAND_TYPE {
		ADD, ADD_EVENT,DISPLAY,UPDATE, DELETE, UNDO, SEARCH, REVIEW, SAVE_AT, INVALID
	};
	
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
		} else if (commandType.equalsIgnoreCase("edit")) {
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
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	public static String executeCommand(String commandType,String[] arguments) throws IOException {
		COMMAND_TYPE command = determineCommandType(commandType);
		switch (command) {
		case ADD:
			return add(arguments);
		case ADD_EVENT:
			return addEvent(arguments);
		case DISPLAY:
			return display(arguments);
		case UPDATE:
			return update(arguments);
		case DELETE:
			return delete(arguments);
		case UNDO:
			return undo(arguments);
		case SEARCH:
			return search(arguments);
		case REVIEW:
			return review(arguments);
		case SAVE_AT:
			return saveAt(arguments);
		default:
			throw new Error(MESSAGE_UNRECOGNIZE);
		}
}
	
	/* Add requires arguments to be of Events + Deadline
	 * if deadline is empty string, means its is a floating task
	 * 
	 */
	public static String add(String[] arguments) {
		String line = "";
		if (arguments.length==1){
			line = arguments[0];
		} else {
			line = arguments[0] + " " + arguments[1];
		}
			cachedFile.add(line);
			return line;
	}

	public static String addEvent(String[] arguments) {
		return "";
	}
	/* DISPLAY requires arguments to be of ""
	 * 
	 */
	public static String display(String[] arguments){
		for (int i = 0; i<cachedFile.size(); i++){
			System.out.println(cachedFile.get(i));
		}
		return "Display";
	}
	/* UPDATE requires arguments to be of "Event name" + "Updated Event" 
	 * 
	 */
	public static String update(String[] arguments) {
		for (int i = 0; i<cachedFile.size(); i++){
			if (cachedFile.get(i).contains(arguments[0])){
				cachedFile.remove(i);
				cachedFile.add(i,arguments[1]);
			}
		}
		return "Updated Successfully";
	}
	/* DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	public static String delete(String[] arguments) {
		for (int i = 0; i<cachedFile.size(); i++){
			if (cachedFile.get(i).contains(arguments[0])){
				cachedFile.remove(i);
			}
		}
		return "Delete Successfully";
	}

	public static String undo(String[] arguments) {
		return "";
	}

	public static String search(String[] arguments) {
		return "";
	}

	public static String review(String[] arguments) {
		return "";
	}

	public static String saveAt(String[] arguments) {
		return "Successfully Saved";
	}
	public static void checkCached(){
		System.out.println(cachedFile);
	}
	public static ArrayList<String> getCachedFile(){
		return cachedFile;
	}
}