package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;

import easycheck.commandParser.Command;

/**
 * DISPLAY Command Type represents a parsed command for Easy Check application.
 * 
 * 
 * @author A0145668R
 */

public class Display extends Command {
	private boolean dateFlag = false;
	private boolean floatingFlag = false;
	private boolean defaultFlag = false;
	private boolean indexFlag = false;
	private boolean notDoneFlag = false;
	private boolean doneFlag = false;
	private boolean invalidFlag = false;
	
	private String invalidMessage = "";
	private int eventIndex;
	private DateTime displayDate;
	
	
	public Display() {}
	
	public boolean isDate() {
		return dateFlag;
	}

	public void setDateFlag(boolean dateFlag) {
		this.dateFlag = dateFlag;
	}

	public boolean isFloating() {
		return floatingFlag;
	}

	public void setFloatingFlag(boolean floatingFlag) {
		this.floatingFlag = floatingFlag;
	}

	public boolean isDefault() {
		return defaultFlag;
	}

	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public boolean isIndex() {
		return indexFlag;
	}

	public void setIndexFlag(boolean indexFlag) {
		this.indexFlag = indexFlag;
	}

	public boolean isNotDone() {
		return notDoneFlag;
	}

	public void setNotDoneFlag(boolean notDoneFlag) {
		this.notDoneFlag = notDoneFlag;
	}

	public boolean isDone() {
		return doneFlag;
	}

	public void setDoneFlag(boolean doneFlag) {
		this.doneFlag = doneFlag;
	}

	public boolean isInvalid() {
		return invalidFlag;
	}

	public void setInvalidFlag(boolean invalidFlag) {
		this.invalidFlag = invalidFlag;
	}

	public String getInvalidMessage() {
		return invalidMessage;
	}

	public void setInvalidMessage(String invalidMessage) {
		this.invalidMessage = invalidMessage;
	}

	public int getEventIndex() {
		return eventIndex;
	}

	public void setEventIndex(int eventIndex) {
		this.eventIndex = eventIndex;
	}

	public DateTime getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(DateTime displayDate) {
		this.displayDate = displayDate;
	}
}
