package easycheck.eventlist;

/*
 * This is a subclass of Event that deals with to do events.
 * To do events are events with a specific deadline
 * @author A0145668R
 */

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ToDoEvent extends Event {	
	private static final String DATE_AND_TIME_OUTPUT_FORMAT = "%s %d %s %d at %02d:%02d";
	private static final String DATE_OUTPUT_FORMAT = "%s %d %s %d";
	private static final String TIME_OUTPUT_FORMAT = "%02d:%02d";
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";
	
	private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE_IS_NOT_COMPLETE = "@|red %d. %s due on %s %s|@\n";
	private static final String MESSAGE_TO_STRING_TEMPLATE_IS_COMPLETE = "@|green %d. %s due on %s is complete %s|@\n";
	private static final String MESSAGE_REPEATING_ADDITION = " (Repeats %s)";
	
	private static final String MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE_IS_NOT_COMPLETE = "@|red %d. %s due at %s|@\n";
	private static final String MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE_IS_COMPLETE = "@|yellow %d. %s due at %s is complete|@\n";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_DUE_DATE = "due";
	private static final String JSON_COMPLETE = "completed";
	private static final String JSON_REPEAT = "repeating";
	private static final String JSON_FREQUENCY = "frequency";
	private static final String JSON_STOPDATE = "stopdate";
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime deadline;
	private DateTime stopDate;
	private boolean complete;
	private boolean repeating;
	private String frequency;
	
	public ToDoEvent(int eventIndex, String eventName, DateTime dueDateAndTime) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		deadline = dueDateAndTime;
		this.complete = false;
		this.setRepeating(false);
		this.setFrequency(null);
		this.stopDate = null;
	}
	
	public ToDoEvent(ToDoEvent e) {
		this.setEventIndex(e.getEventIndex());
		this.setEventName(e.getEventName());
		this.deadline = e.getDeadline();
		this.complete = e.isDone();
		this.setRepeating(e.isRepeating());
		this.setFrequency(e.getFrequency());
		this.stopDate = e.stopDate;
	}
	
	public ToDoEvent(int eventIndex, String eventName, DateTime dueDateAndTime, boolean repeating, String frequency) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		deadline = dueDateAndTime;
		this.complete = false;
		this.setRepeating(repeating);
		this.setFrequency(frequency);
		this.stopDate = null;
	}
	
	public ToDoEvent(int eventIndex, String eventName, DateTime dueDateAndTime, boolean repeating, String frequency, DateTime stopDate) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		deadline = dueDateAndTime;
		this.complete = false;
		this.setRepeating(repeating);
		this.setFrequency(frequency);
		this.setStopDate(stopDate);
	}
	
	public ToDoEvent(JSONObject jsonObj) {
		Long eventIndex = (Long) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		deadline = fmt.parseDateTime((String)jsonObj.get(JSON_DUE_DATE));
		complete = ((Boolean)jsonObj.get(JSON_COMPLETE)).booleanValue();
		repeating = ((Boolean)jsonObj.get(JSON_REPEAT)).booleanValue();
		
		if(repeating) {
			if(jsonObj.get(JSON_STOPDATE) != null) {
				stopDate = fmt.parseDateTime((String)jsonObj.get(JSON_STOPDATE));
			} else {
				stopDate = (DateTime)jsonObj.get(JSON_STOPDATE);
			}
			frequency = (String) jsonObj.get(JSON_FREQUENCY);
		}
		
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
	}
	
	/**
	 * Returns the due date and time of this event
	 * @return DateTime deadline
	 */
	public DateTime getDeadline() {
		if(this.isRepeating() && !this.isUpdated()) {
			try {
				update();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return deadline;
	}
	
	private void update() throws Exception {
		switch(frequency) {
			case REPEATING_DAILY:
				deadline.plusDays(1);
				break;
			case REPEATING_WEEKLY:
				deadline.plusWeeks(1);
				break;
			case REPEATING_BIWEEKLY:
				deadline.plusWeeks(2);
				break;
			case REPEATING_MONTHLY:
				deadline.plusMonths(1);
				break;
			case REPEATING_YEARLY:
				deadline.plusYears(1);
				break;
			default:
				throw new Exception("Got to defualt case in update. Something is wrong!");
		}
		if(hasStopDate()) {
			if(deadline.isAfter(stopDate)) {
				setDone();
			}
		}
	}

	/**
	 * Returns the due date in day of week dd Month YYYY format
	 * @return String
	 */
	public String getDeadlineDate() {
		DateTime.Property pDayOfTheWeek = this.getDeadline().dayOfWeek();
		DateTime.Property pMonthOfYear = this.getDeadline().monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				this.getDeadline().getDayOfMonth(), pMonthOfYear.getAsShortText(), 
				this.getDeadline().getYear());
		return dateString;
	}
	
	public String getDeadlineTime() {
		String timeString = String.format(TIME_OUTPUT_FORMAT, this.getDeadline().getHourOfDay(), 
				this.getDeadline().getMinuteOfHour());
		return timeString;
	}
	
	/**
	 * Sets a new due date and time
	 * @param newDate
	 */
	public void setDueDateAndTime(DateTime newDate) {
		assert(newDate.isAfterNow());
		deadline = newDate;
	}
	
	public void markComplete() {
		complete = true;
	}
	
	public void unMarkComplete() {
		complete = false;
	}
	
	public boolean isDone() {
		return complete;
	}
	
	public boolean isOverDue() {
		return !complete && deadline.isBeforeNow();
	}
	
	public boolean isUpdated() {
		return deadline.isAfterNow() && !complete;
	}
	
	public boolean isSameDay(DateTime date){
		DateTime.Property pDayOfTheWeek = date.dayOfWeek();
		DateTime.Property pMonthOfYear = date.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				date.getDayOfMonth(), pMonthOfYear.getAsShortText(), date.getYear());
		return dateString.equals(getDeadlineDate());
	}
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		String repeatAddition = "";
		DateTime.Property pDayOfTheWeek = this.getDeadline().dayOfWeek();
		DateTime.Property pMonthOfYear = this.getDeadline().monthOfYear();
		String deadlineString = String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				this.getDeadline().getDayOfMonth(), pMonthOfYear.getAsShortText(), 
				this.getDeadline().getYear(), this.getDeadline().getHourOfDay(), 
				this.getDeadline().getMinuteOfHour());
		
		if(repeating) {
			repeatAddition = String.format(MESSAGE_REPEATING_ADDITION, frequency);
		}
		
		if(complete) {
			return String.format(
					MESSAGE_TO_STRING_TEMPLATE_IS_COMPLETE, this.getEventIndex(), this.getEventName(), deadlineString, repeatAddition);
		} else {
			return String.format(
					MESSAGE_TO_STRING_TEMPLATE_IS_NOT_COMPLETE, this.getEventIndex(), this.getEventName(), deadlineString, repeatAddition);
		}
	}
	
	public String toPrintGroupString() {
		if(complete) {
			return String.format(
					MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE_IS_COMPLETE, this.getEventIndex(), this.getEventName(),
					this.getDeadlineTime());
		} else {
			return String.format(
					MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE_IS_NOT_COMPLETE, this.getEventIndex(), this.getEventName(),
					this.getDeadlineTime());
		}
	}
	
	/**
	 * Returns the JSON string to write to the file for this calendar object
	 * @return jsonString
	 */
	public String toJsonString() {
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		obj.put(JSON_TYPE, "todo");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		obj.put(JSON_DUE_DATE, fmt.print(this.getDeadline()));
		obj.put(JSON_COMPLETE, new Boolean(complete));
		obj.put(JSON_REPEAT, new Boolean(repeating));
		if(repeating) {
			obj.put(JSON_FREQUENCY, frequency);
			if(hasStopDate()) {
				obj.put(JSON_STOPDATE, fmt.print(stopDate));
			} else {
				obj.put(JSON_STOPDATE, null);
			}
		}
		
		StringWriter out = new StringWriter();
	    try {
			JSONValue.writeJSONString(obj, out);
		} catch (IOException e) {
			System.out.println(MESSAGE_JSON_STRING_ERROR);
		}
	    return out.toString();
	}
	
	//@author A0145668R 
	//override equals method
	public boolean equals(Object obj) {
		if (!(obj instanceof ToDoEvent)) {
			return false;
		} else {
			ToDoEvent e = (ToDoEvent) obj;
			return this.getEventIndex() == e.getEventIndex() && this.getEventName().equals(e.getEventName()) &&
					this.getDeadline().equals(e.getDeadline());
		}
	}
	
	//@author A0145668R 
	//override compareTo method
	public int compareTo(Event e) {
		if(e instanceof FloatingTask) {
			return 1;
		} else if (e instanceof CalendarEvent) {
			CalendarEvent cal = (CalendarEvent)e;
			int result = this.getDeadline().compareTo(cal.getStartDateAndTime());
			if (result == 0) {
				result = this.getEventName().compareTo(cal.getEventName());
			}
			return result;
		} else if (e instanceof ToDoEvent) {
			ToDoEvent todo = (ToDoEvent)e;
			int result = this.getDeadline().compareTo(todo.getDeadline());
			if (result == 0) {
				result = this.getEventName().compareTo(todo.getEventName());
			}
			return result;
		}
		return 0;
	}
	
	// @author A0126989H
	public void setDone(){
		this.complete = true;
	}
	public void setUndone(){
		this.complete = false;
	}
	
	//@author A0145668R
	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public Event createCopy() {
		return new ToDoEvent(this);
	}
	
	public static boolean isValidDate(DateTime date) {
		return date.isAfterNow();
	}
	
}
