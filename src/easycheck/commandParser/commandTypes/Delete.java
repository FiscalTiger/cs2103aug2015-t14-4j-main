package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;
/**
 * DELETE Command Type represents a parsed command for Easy Check application.
 * To be called:
 * getTaskName() 
 * 
 * @author A0126989H
 */

public class Delete extends Command{
	private String task;
	
	public Delete(String commandType, String[] arguments) {
		super(commandType, arguments);
		task=arguments[0];
		// TODO Auto-generated constructor stub
	}
	public String getTaskName(){
		return task;
	}
}
