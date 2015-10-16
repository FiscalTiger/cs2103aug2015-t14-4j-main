package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;
/**
 * DISPLAY Command Type represents a parsed command for Easy Check application.
 * 
 * To be called:
 * 
 * getCommandType()
 * getCommandArguments()
 * 
 * @author A0126989H
 */

public class Display extends Command {
	public Display(String commandType, String[] arguments) {
		super(commandType, arguments);
	}
}
