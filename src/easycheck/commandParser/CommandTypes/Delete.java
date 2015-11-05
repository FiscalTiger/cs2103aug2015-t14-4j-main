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
	private static final int SUBSTRING_COMMAND_START = 4;
	private static final int ALL_COMMAND_END = 3;
	private static final int ZERO_CONSTANT = 0;
	private static final String MESSAGE_DELETE_CMD_SPECIALCOMMAND = "all";
	private static final String MESSAGE_DELETE_CMD_DONECOMMAND = "done";
	private String task;
	
	public Delete(String[] arguments) {
	    if (arguments != null) {
            task = arguments[0];
        } else {
            task = null;
        }
    }
    public String getTaskName(){
    	if (task == null){
    		return task;
    	} else {
    		return task.toLowerCase();
    	}
	}
    public String getTaskNameAll(){
    	if (task.length() >= SUBSTRING_COMMAND_START)
    		return task.substring(SUBSTRING_COMMAND_START );
    	else
    		return "";
    }
    public boolean isDeleteAll(){
    	return task.length() >= ALL_COMMAND_END
		&& task.substring(ZERO_CONSTANT, ALL_COMMAND_END).equals(MESSAGE_DELETE_CMD_SPECIALCOMMAND);
    }
    public boolean isDeleteDone(){
    	return task.length() >= SUBSTRING_COMMAND_START && task.equals(MESSAGE_DELETE_CMD_DONECOMMAND);
    }
}
