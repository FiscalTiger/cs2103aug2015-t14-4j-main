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

public class AddEvent extends Command {

	public AddEvent(String commandType, String[] arguments) {
		super(commandType, arguments);
		// TODO Auto-generated constructor stub
	}

}
