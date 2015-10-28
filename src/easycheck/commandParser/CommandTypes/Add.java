package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	//@author A0145668R
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";

	private String taskName; 
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime start;
	private DateTime end;
	//end @author A0145668R
	
	public Add(String command, String[] arguments) {
		super(command, arguments);
		taskName = arguments[0];
		//@author A0145668R
		if (arguments.length == 1) {
			start = null;
			end = null;
			//end @author A0145668R
		} else if (arguments.length==2){
			start = null;
			end = fmt.parseDateTime(arguments[1]);
		} else if (arguments.length==3){
			start = fmt.parseDateTime(arguments[1]);
			end = fmt.parseDateTime(arguments[2]);
		}
	}
	
	public String getTaskName(){
		return taskName;
	}
	
	/**
	 * Returns whether or not the add command contains
	 * a start date
	 * @return true if start exists
	 * @author A0145668R
	 */
	public boolean hasStart() {
		return !(start == null);
	}
	
	public DateTime getStart(){
		return start;
	}
	
	/**
	 * Returns whether or not the add command contains
	 * an end date
	 * @return true if end exists
	 * @author A0145668R
	 */
	public boolean hasEnd() {
		return !(end == null);
	}
	
	public DateTime getEnd(){
		return end;
	}
}
