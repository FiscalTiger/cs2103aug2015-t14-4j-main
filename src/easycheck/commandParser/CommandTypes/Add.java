package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

/**
 * ADD Command Type represents a parsed command for Easy Check application.
 * To be called:
 * getTaskName()
 * getStart()
 * getEnd() 
 * 
 * @author A0126989H
 */

public class Add extends Command {
	private String taskName; 
	private String start;
	private String end;

	public Add(String command, String[] arguments) {
		super(command, arguments);
		taskName = arguments[0];
		if (arguments.length == 1) {
			start = "";
			end = "";
		}
		if (arguments.length==2){
			start = arguments[1];
			end = "";
		}
		if (arguments.length==3){
			start = arguments[1];
			end = arguments[2];
		}
	}
	public String getTaskName(){
		return taskName;
	}
	public String getStart(){
		return start;
	}
	public String getEnd(){
		return end;
	}
}
