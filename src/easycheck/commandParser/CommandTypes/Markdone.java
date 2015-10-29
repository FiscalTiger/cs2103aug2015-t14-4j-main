package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

/*
 * @author A0126989H
 */

public class Markdone extends Command{
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
}
