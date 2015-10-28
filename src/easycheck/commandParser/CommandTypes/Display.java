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
	private static final String DATE_OUTPUT_FORMAT = "%s %d %s %d";

	private boolean dateFlag = false;
	private boolean floatingFlag = false;
	private boolean defaultFlag = false;
	private boolean indexFlag = false;
	private boolean notDoneFlag = false;
	private boolean doneFlag = false;
	
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

	public int getEventIndex() {
		return eventIndex;
	}

	public void setEventIndex(int eventIndex) {
		this.eventIndex = eventIndex;
	}

	public String getDisplayDate() {
		DateTime.Property pDayOfTheWeek = displayDate.dayOfWeek();
		DateTime.Property pMonthOfYear = displayDate.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				displayDate.getDayOfMonth(), pMonthOfYear.getAsShortText(), displayDate.getYear());
		return dateString;
	}

	public void setDisplayDate(DateTime displayDate) {
		this.displayDate = displayDate;
	}
}
