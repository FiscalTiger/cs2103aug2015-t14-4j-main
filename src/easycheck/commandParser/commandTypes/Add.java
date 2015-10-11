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
	private String TaskName; 
	private String Start;
	private String End;

	public Add(String command, String[] arguments) {
		super(command, arguments);
		TaskName = arguments[0];
		if (arguments.length==2){
			Start = arguments[1];
			End = "";
		}
		if (arguments.length==3){
			Start = arguments[1];
			End = arguments[2];
		}
	}
	public String getTaskName(){
		return TaskName;
	}
	public String getStart(){
		return Start;
	}
	public String getEnd(){
		return End;
	}
}
