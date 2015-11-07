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
	private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private String taskName; 
	private DateTime start;
	private DateTime end;
	private boolean isRepeating;
	private String frequency;
    private DateTime stopDate = null;
    
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

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
	}
    
    public boolean hasStopDate() {
    	return stopDate != null;
    }

	public DateTime getStopDate() {
		return stopDate;
	}

	public void setStopDate(DateTime stopDate) {
		this.stopDate = stopDate;
	}
	
	public static boolean isValidFrequency(String frequency) {
		if(frequency.equals(REPEATING_DAILY) || frequency.equals(REPEATING_WEEKLY) ||
				frequency.equals(REPEATING_BIWEEKLY) || frequency.equals(REPEATING_MONTHLY) ||
				frequency.equals(REPEATING_YEARLY)) {
			return true;
		} else {
			return false;
		}
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	// @@author A0145668R
}
