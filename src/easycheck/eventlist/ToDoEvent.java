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
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE_IS_NOT_COMPLETE = "%d. %s due on %s\n";
	private static final String MESSAGE_TO_STRING_TEMPLATE_IS_COMPLETE = "%d. %s due on %s is complete\n";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_DUE_DATE = "due";
	private static final String JSON_COMPLETE = "completed";
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime deadline;
	private boolean complete;
	
	public ToDoEvent(int eventIndex, String eventName, DateTime dueDateAndTime) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		deadline = dueDateAndTime;
		complete = false;
	}
	
	public ToDoEvent(JSONObject jsonObj) {
		Integer eventIndex = (Integer) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		deadline = fmt.parseDateTime((String)jsonObj.get(JSON_DUE_DATE));
		complete = ((Boolean)jsonObj.get(JSON_COMPLETE)).booleanValue();
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
	}
	
	/**
	 * Returns the due date and time of this event
	 * @return DateTime deadline
	 */
	public DateTime getDeadline() {
		return deadline;
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
	
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		DateTime.Property pDayOfTheWeek = deadline.dayOfWeek();
		DateTime.Property pMonthOfYear = deadline.monthOfYear();
		String deadlineString = String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				deadline.getDayOfMonth(), pMonthOfYear.getAsShortText(), deadline.getYear(),
				deadline.getHourOfDay(), deadline.getMinuteOfHour());
		if(complete) {
			return String.format(
					MESSAGE_TO_STRING_TEMPLATE_IS_COMPLETE, this.getEventIndex(), this.getEventName(), deadlineString);
		} else {
			return String.format(
					MESSAGE_TO_STRING_TEMPLATE_IS_NOT_COMPLETE, this.getEventIndex(), this.getEventName(), deadlineString);
		}
	}
	
	/**
	 * Returns the JSON string to write to the file for this calendar object
	 * @return jsonString
	 */
	public String toJsonString() {
		Map obj=new LinkedHashMap();
		obj.put(JSON_TYPE, "todo");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		obj.put(JSON_DUE_DATE, fmt.print(deadline));
		obj.put(JSON_COMPLETE, new Boolean(complete));
		
		StringWriter out = new StringWriter();
	    try {
			JSONValue.writeJSONString(obj, out);
		} catch (IOException e) {
			System.out.println(MESSAGE_JSON_STRING_ERROR);
		}
	    return out.toString();
	}
	
	// TODO override equals method
	public boolean equals(Object obj) {
		return false;
	}
	
	// TODO override compareTo method
	public int compareTo(Event e) {
		return 0;
	}
	
	public static boolean isValidDeadline(DateTime deadline) {
		return deadline.isAfterNow();
	}
}
