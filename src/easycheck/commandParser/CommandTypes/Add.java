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
	private boolean isRepeating;
	private boolean isDaily;
    private boolean isWeekly;
    private boolean isBiweekly;
    private boolean isMonthly;
    private boolean isYearly;
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

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
	}

	public void isDaily() {
        // only one of the booleans can be true
        isDaily = true;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = false;
        isYearly = false;
    }

    public void isWeekly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = true;
        isBiweekly = false;
        isMonthly = false;
        isYearly = false;
    }
    
    public void isBiweekly() {
    	// only one of the booleans can be true
    	isDaily = false;
        isWeekly = false;
        isBiweekly = true;
        isMonthly = false;
        isYearly = false;
    }

    public void isMonthly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = true;
        isYearly = false;
    }
    
    public void isYearly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = false;
        isYearly = true;
    }
}
