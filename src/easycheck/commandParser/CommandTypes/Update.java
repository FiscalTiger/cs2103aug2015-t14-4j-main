package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;
/**
 * Edit Command Type represents a parsed command for Easy Check application.
 * To be called:
 * 
 * getTaskName()
 * getNewEvent()
 * 
 * @author A0126989H
 */
public class Update extends Command {
	private String task;
	private String newEvent;
	
	public Update(String commandType, String[] arguments) {
		super(commandType, arguments);
		if (arguments.length==2){
			task = arguments[0];
			newEvent = arguments[1];
		} else if (arguments.length==1){
			task = arguments [0];
			newEvent = arguments[1];
		}
	}
	public String getTaskName(){
		return task;
	}
	public String getNewEvent(){
		return newEvent;
	}
}