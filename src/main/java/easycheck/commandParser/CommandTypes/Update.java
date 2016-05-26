package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;

import easycheck.commandParser.Command;
/**
 * Edit Command Type represents a parsed command for Easy Check application.
 * To be called:
 * 
 * getTaskName()
 * getNewEvent()
 * 
 * @@author A0121560W
 */
public class Update extends Command {
	private static final String UPDATE_COMMAND_TYPE_START = "start";
    private static final String UPDATE_COMMAND_TYPE_END = "end";
    //private static final String UPDATE_COMMAND_TYPE_NAME = "name";
    //private static final String UPDATE_COMMAND_TYPE_TYPE = "type";
    //@@author A0121560W-reused
    private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private boolean isRepeating = false;
	private String complete = null;
	private DateTime stopDate = null;
	private String frequency = null;
	//@@author A0121560W
	
	private String idx = null;
	private String newName = null;
	private DateTime start = null;
	private DateTime end = null;
	private String type = null;
	
	
	

	public Update(String idx, String newName) {
		this.idx = idx;
		this.newName = newName;
	}
	
	public Update(String idx, String newName, DateTime end){
		this.idx = idx;
		this.newName = newName;
		this.end = end;
	}
	
	public Update(String idx, String newName, DateTime start, DateTime end){
		this.idx = idx;
		this.newName = newName;
		this.start = start;
		this.end = end;
	}
	public Update(String idx, DateTime date, String type){
		this.idx = idx;
		this.type = type;
		if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_END)){
			this.end = date;
		} else if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_START)){
			this.start = date;
		}
	}
	public Update(String idx, String type, String name){
		this.idx = idx;
		this.type = type;
		this.newName = name;
	}
	
	public String getTaskIdx(){
		return idx;
	}
	public String getNewName(){
		return newName;
	}

	public DateTime getStart(){
		return start;
	}
	public DateTime getEnd() {
		return end;
	}
	public String getType() {
		return type;
	}
	public boolean hasStart() {
		return !(start == null);
	}
	public boolean hasEnd() {
		return !(end == null);
	}
	public boolean hasType() {
		return !(type == null);
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public DateTime getStopDate() {
		return stopDate;
	}

	public void setStopDate(DateTime stopDate) {
		this.stopDate = stopDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	//@@author A0121560W-reused
	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean repeating) {
		this.isRepeating = repeating;
	}
	public boolean hasStopDate() {
    	return stopDate != null;
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


	
}
