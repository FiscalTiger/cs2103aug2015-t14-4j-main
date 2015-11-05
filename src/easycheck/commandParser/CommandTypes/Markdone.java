package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

/*
 * @author A0126989H
 */

public class Markdone extends Command{
	private static final int SUBSTRING_COMMAND_START = 4;
	private static final int ALL_COMMAND_END = 3;
	private static final int ZERO_CONSTANT = 0;
	private static final String MESSAGE_MARKDONE_CMD_SPECIALCOMMAND = "all";
	private String task;
	
	
	public Markdone(String[] arguments) {
	    if (arguments != null) {
            task = arguments[0];
        } else {
            task = null;
        }
    }
    public String getTaskName(){
		return task;
	}
    public String getTaskNameAll(){
    	if (task.length() >= SUBSTRING_COMMAND_START)
    		return task.substring(SUBSTRING_COMMAND_START );
    	else
    		return "";
    }
    public boolean isDoneAll(){
    	return task.length() >= ALL_COMMAND_END
		&& task.substring(ZERO_CONSTANT, ALL_COMMAND_END).equals(MESSAGE_MARKDONE_CMD_SPECIALCOMMAND);
    }
}
