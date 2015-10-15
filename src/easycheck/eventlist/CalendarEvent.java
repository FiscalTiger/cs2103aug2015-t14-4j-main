package easycheck.eventlist;

/*
 * This is a subclass of Event that deals with Calendar Events.
 * Calendar events are events with specific start and end times
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
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String MESSAGE_JSON_INPUT_ERROR = "Error parsing JSON Object from File at event: ";
	private static final String MESSAGE_INVALID_DATE_STRING = "Error invalid date format";
	private static final String MESSAGE_END_DATE_INPUT_ERROR = "Error while parsing new end date and time";
	private static final String MESSAGE_START_DATE_INPUT_ERROR = "Error while parsing new start date and time";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE = "%s from %s to %s\n";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_START_DATE = "start";
	private static final String JSON_END_DATE = "end";
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime startDateAndTime;
	private DateTime endDateAndTime;
	
	public CalendarEvent(int eventIndex, String eventName, String startDateAndTimeString, String endDateAndTimeString) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		startDateAndTime = fmt.parseDateTime(startDateAndTimeString);
		endDateAndTime = fmt.parseDateTime(endDateAndTimeString);
	}
	
	public CalendarEvent(JSONObject jsonObj) {
		Integer eventIndex = (Integer) jsonObj.get(JSON_EVENT_INDEX);
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
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		String startDateString = getFormattedStartDateString();
		String endDateString = getFormattedEndDateString();
		return String.format(
				MESSAGE_TO_STRING_TEMPLATE, this.getEventName(), startDateString, endDateString);
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
		Map obj=new LinkedHashMap();
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
		return false;
	}
	
	// TODO override compareTo method
	public int compareTo(Event e) {
		return 0;
	}
	
	public static void main(String[] args) {
		CalendarEvent se = new CalendarEvent(1, "Banging Head Against Wall", "7.10.2015 12:00", "8.10.2015 00:00");
		System.out.println(se);
	}
}
