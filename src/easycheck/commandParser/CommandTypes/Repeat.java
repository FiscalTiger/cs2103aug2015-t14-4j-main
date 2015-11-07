package easycheck.commandParser.CommandTypes;

import org.joda.time.DateTime;

import easycheck.commandParser.Command;

public class Repeat extends Command {
	//@@author A0145668R
	private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
    private String task;
    private String frequency;
    private DateTime endDate;

    public Repeat(String task, String frequency) {
    	this(task, frequency, null);
    }
    
    public Repeat(String taskName, String frequency, DateTime endDate) {
    	this.setTask(task);
    	this.setFrequency(frequency);
    	this.setEndDate(endDate);
    }
  

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
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
	// @@author
}
