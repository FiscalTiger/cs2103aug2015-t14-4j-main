package easycheck.eventlist;

/*
 * This is a subclass of Event that deals with Calendar Events.
 * Calendar events are events with specific start and end times
 * @author A0145668R
 */

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CalendarEvent extends Event {
	private static final String DATE_AND_TIME_OUTPUT_FORMAT = "%s %d %s %d at %02d:%02d";
	private static final String DATE_OUTPUT_FORMAT = "%s %d %s %d";
	private static final String TIME_OUTPUT_FORMAT = "%02d:%02d";
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE = "%d. %s from %s to %s\n";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_START_DATE = "start";
	private static final String JSON_END_DATE = "end";
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime startDateAndTime;
	private DateTime endDateAndTime;
	
	public CalendarEvent(int eventIndex, String eventName, DateTime startDateAndTime, DateTime endDateAndTime) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		this.startDateAndTime = startDateAndTime;
		this.endDateAndTime = endDateAndTime;
	}
	
	public CalendarEvent(JSONObject jsonObj) {
		Long eventIndex = (Long) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		startDateAndTime = fmt.parseDateTime((String)jsonObj.get(JSON_START_DATE));
		endDateAndTime = fmt.parseDateTime((String)jsonObj.get(JSON_END_DATE));
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
	}
	
	/**
	 * Returns the start date and time of this event
	 * @return DateTime startDateAndTime
	 */
	public DateTime getStartDateAndTime() {
		return startDateAndTime;
	}
	
	/**
	 * Returns the end date and time of this event
	 * @return DateTime endDateAndTime
	 */
	public DateTime getEndDateAndTime() {
		return endDateAndTime;
	}
	
	public String getStartDate() {
		DateTime.Property pDayOfTheWeek = startDateAndTime.dayOfWeek();
		DateTime.Property pMonthOfYear = startDateAndTime.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				startDateAndTime.getDayOfMonth(), pMonthOfYear.getAsShortText(), startDateAndTime.getYear());
		return dateString;
	}
	
	public String getStartTime() {
		String timeString = String.format(TIME_OUTPUT_FORMAT, startDateAndTime.getHourOfDay(), startDateAndTime.getMinuteOfHour());
		return timeString;
	}
	
	public String getEndDate() {
		DateTime.Property pDayOfTheWeek = endDateAndTime.dayOfWeek();
		DateTime.Property pMonthOfYear = endDateAndTime.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				endDateAndTime.getDayOfMonth(), pMonthOfYear.getAsShortText(), endDateAndTime.getYear());
		return dateString;
	}
	
	public String getEndTime() {
		String timeString = String.format(TIME_OUTPUT_FORMAT, endDateAndTime.getHourOfDay(), endDateAndTime.getMinuteOfHour());
		return timeString;
	}
	
	/**
	 * Sets a new start date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setStartDateAndTime(DateTime newDate) {
		assert(newDate.isBefore(endDateAndTime));
		startDateAndTime = newDate;
	}
	
	/**
	 * Sets a new end date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setEndDateAndTime(DateTime newDate) {
		assert(startDateAndTime.isBefore(newDate));
		endDateAndTime = newDate;
	}
	
	public boolean isSameDay() {
		return this.getStartDate().equals(this.getEndDate());
	}
	
	public boolean isDone() {
		return endDateAndTime.isBeforeNow();
	}
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		String startDateString = getFormattedStartDateString();
		String endDateString = getFormattedEndDateString();
		return String.format(
				MESSAGE_TO_STRING_TEMPLATE, this.getEventIndex(), this.getEventName(), startDateString, endDateString);
	}
	
	public String toPrintGroupString() {
		if (this.isSameDay()) {
			return String.format(MESSAGE_TO_STRING_TEMPLATE, this.getEventIndex(), this.getEventName(),
					this.getStartTime(), this.getEndTime());
		} else {
			return String.format(MESSAGE_TO_STRING_TEMPLATE, this.getEventIndex(), this.getEventName(),
					this.getStartTime(), this.getFormattedEndDateString());
		}
	}

	private String getFormattedStartDateString() {
		DateTime.Property pDayOfTheWeek = startDateAndTime.dayOfWeek();
		DateTime.Property pMonthOfYear = startDateAndTime.monthOfYear();
		return String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				startDateAndTime.getDayOfMonth(), pMonthOfYear.getAsShortText(), startDateAndTime.getYear(),
				startDateAndTime.getHourOfDay(), startDateAndTime.getMinuteOfHour());
	}
	
	private String getFormattedEndDateString() {
		DateTime.Property pDayOfTheWeek = endDateAndTime.dayOfWeek();
		DateTime.Property pMonthOfYear = endDateAndTime.monthOfYear();
		return String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				endDateAndTime.getDayOfMonth(), pMonthOfYear.getAsShortText(), endDateAndTime.getYear(),
				endDateAndTime.getHourOfDay(), endDateAndTime.getMinuteOfHour());
	}
	
	/**
	 * Returns the JSON string to write to the file for this calendar object
	 * @return jsonString
	 */
	public String toJsonString() {
		Map obj = new LinkedHashMap();
		obj.put(JSON_TYPE, "calendar");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		obj.put(JSON_START_DATE, fmt.print(startDateAndTime));
		obj.put(JSON_END_DATE, fmt.print(endDateAndTime));
		
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
		if (!(obj instanceof CalendarEvent)) {
			return false;
		} else {
			CalendarEvent e = (CalendarEvent) obj;
			return this.getEventIndex() == e.getEventIndex() && this.getEventName().equals(e.getEventName()) &&
					this.startDateAndTime.equals(e.getStartDateAndTime()) && 
					this.endDateAndTime.equals(e.getEndDateAndTime());
		}
	}
	
	// TODO override compareTo method
	public int compareTo(Event e) {
		if(e instanceof FloatingTask) {
			return 1;
		} else if (e instanceof CalendarEvent) {
			CalendarEvent cal = (CalendarEvent)e;
			int result = this.startDateAndTime.compareTo(cal.getStartDateAndTime());
			if (result == 0) {
				result = this.endDateAndTime.compareTo(cal.getEndDateAndTime());
				if(result == 0) {
					result = this.getEventName().compareTo(cal.getEventName());
				}
			}
			return result;
		} else if (e instanceof ToDoEvent) {
			ToDoEvent todo = (ToDoEvent)e;
			int result = this.startDateAndTime.compareTo(todo.getDeadline());
			if (result == 0) {
				result = this.getEventName().compareTo(todo.getEventName());
			}
			return result;
		}
		return 0;
	}
	
	public static boolean areValidDates(DateTime start, DateTime end) {
		return (start.isBefore(end) && start.isAfterNow());
	}
}
