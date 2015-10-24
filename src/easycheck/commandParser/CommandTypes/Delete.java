package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

/**
 * DELETE Command Type represents a parsed command for Easy Check application.
 * Delete can have 0 arguments (delete first argument) or 1 argument (delete
 * task containing argument) 
 * 
 * To be called: getTaskName()
 * 
 * @author A0126989H
 */

public class Delete extends Command{
	private String task;
	
	public Delete(String commandType, String[] arguments) {
		super(commandType, arguments);
		if (arguments != null) {
		    task = arguments[0];
		}
	}
	public String getTaskName(){
		return task;
	}
}
