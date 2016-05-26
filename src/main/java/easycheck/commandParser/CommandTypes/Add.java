package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;

import easycheck.commandParser.Command;

/**
 * ADD Command Type represents a parsed command for Easy Check application.
 */

public class Add extends Command {
	//@@author A0145668R
	private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private String taskName; 
	private DateTime start;
	private DateTime end;
	private boolean isRepeating = false;
	private String frequency = null;
    private DateTime stopDate = null;
    
    // @@author A0145668R
	public Add(String taskName) {
		this.setTaskName(taskName);
	};
	
	// @@author A0145668R
	public Add(String taskName, DateTime deadline) {
		this.setTaskName(taskName);
		this.setEnd(deadline);
	}

	// @@author A0145668R
	public Add(String taskName, DateTime start, DateTime end) {
		this.setTaskName(taskName);
		this.setStart(start);
		this.setEnd(end);
	}
	
	// @@author A0145668R
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	// @@author A0145668R
	public String getTaskName(){
		return taskName;
	}
	
	/**
	 * Returns whether or not the add command contains
	 * a start date
	 * @return true if start exists
	 * @@author A0145668R
	 */
	public boolean hasStart() {
		return !(start == null);
	}
	
	// @@author A0145668R
	public DateTime getStart(){
		return start;
	}
	
	// @@author A0145668R
	public void setStart(DateTime start) {
		this.start = start;
	}
	
	/**
	 * Returns whether or not the add command contains
	 * an end date
	 * @return true if end exists
	 * @@author A0145668R
	 */
	public boolean hasEnd() {
		return !(end == null);
	}
	
	// @@author A0145668R
	public DateTime getEnd(){
		return end;
	}
	
	// @@author A0145668R
	public void setEnd(DateTime end) {
		this.end = end;
	}

	// @@author A0145668R
	public boolean isRepeating() {
		return isRepeating;
	}

	// @@author A0145668R
	public void setRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
	}
	
	// @@author A0145668R
    public boolean hasStopDate() {
    	return stopDate != null;
    }

    // @@author A0145668R
	public DateTime getStopDate() {
		return stopDate;
	}

	// @@author A0145668R
	public void setStopDate(DateTime stopDate) {
		this.stopDate = stopDate;
	}
	
	// @@author A0145668R
	public static boolean isValidFrequency(String frequency) {
		if(frequency.equals(REPEATING_DAILY) || frequency.equals(REPEATING_WEEKLY) ||
				frequency.equals(REPEATING_BIWEEKLY) || frequency.equals(REPEATING_MONTHLY) ||
				frequency.equals(REPEATING_YEARLY)) {
			return true;
		} else {
			return false;
		}
	}

	// @@author A0145668R
	public String getFrequency() {
		return frequency;
	}
	
	// @@author A0145668R
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public String toString() {
		return "Event: " + this.taskName + 
				" Start: " + this.start + 
				" End: " + this.end + 
				" Repeating: " + this.isRepeating + 
				" freqency: " + this.frequency + 
				" Stop Date: " +this.stopDate;
	}
	// @@author
	
	
}
