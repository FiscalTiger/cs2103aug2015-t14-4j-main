package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;

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
	private String taskName; 

	private DateTime start;
	private DateTime end;
	//end @author A0145668R
	
	public Add(String taskName) {
		this.setTaskName(taskName);
	};
	
	public Add(String taskName, DateTime deadline) {
		this.setTaskName(taskName);
		this.setEnd(deadline);
	}

	public Add(String taskName, DateTime start, DateTime end) {
		this.setTaskName(taskName);
		this.setStart(start);
		this.setEnd(end);
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	
	public void setStart(DateTime start) {
		this.start = start;
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
	
	public void setEnd(DateTime end) {
		this.end = end;
	}
}
