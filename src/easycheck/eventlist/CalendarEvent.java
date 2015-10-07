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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CalendarEvent extends Event {
	/* implementation using JSONArray
	private static final int EVENT_INDEX_ARRAY_INDEX = 1;
	private static final int EVENT_NAME_ARRAY_INDEX = 2;
	private static final int EVENT_START_DATE_ARRAY_INDEX = 3;
	private static final int EVENT_END_DATE_ARRAY_INDEX = 4;
	*/

	private static final String DATE_AND_TIME_OUTPUT_FORMAT = "E dd.mm.yyyy 'at' hh:mm:ss a";
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MMM.yyyy HH:mm:ss";
	private static final String MESSAGE_JSON_INPUT_ERROR = "Error parsing JSON Object from File at event: ";
	private static final String MESSAGE_INVALID_DATE_STRING = "Error invalid date format";
	private static final String MESSAGE_END_DATE_INPUT_ERROR = "Error while parsing new end date and time";
	private static final String MESSAGE_START_DATE_INPUT_ERROR = "Error while parsing new start date and time";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE = "%S from %s to %s";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_START_DATE = "start";
	private static final String JSON_END_DATE = "end";
	
	private Date startDateAndTime;
	private Date endDateAndTime;
	
	public CalendarEvent(int eventIndex, String eventName, String startDateAndTimeString, String endDateAndTimeString) {
		SimpleDateFormat ft =
				new SimpleDateFormat(DATE_AND_TIME_INPUT_FORMAT);
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		try {
			startDateAndTime = ft.parse(startDateAndTimeString);
			endDateAndTime = ft.parse(endDateAndTimeString);
		} catch(ParseException e) {
			System.out.println(MESSAGE_INVALID_DATE_STRING);
		}
	}
	
	public CalendarEvent(JSONObject jsonObj) {
		SimpleDateFormat ft = 
			      new SimpleDateFormat (DATE_AND_TIME_INPUT_FORMAT);
		Integer eventIndex = (Integer) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		try {
		
		startDateAndTime = ft.parse((String)jsonObj.get(JSON_START_DATE));
		endDateAndTime = ft.parse((String)jsonObj.get(JSON_END_DATE));
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
		} catch(ParseException e) {
			System.out.println(MESSAGE_JSON_INPUT_ERROR + eventName);
		}
		/*
		JSONArray array=(JSONArray)obj;
		Integer eventIndex = (Integer) array.get(EVENT_INDEX_ARRAY_INDEX);
		String eventName = (String) array.get(EVENT_NAME_ARRAY_INDEX);
		try {
			startDateAndTime = ft.parse((String)array.get(EVENT_START_DATE_ARRAY_INDEX));
			endDateAndTime = ft.parse((String)array.get(EVENT_END_DATE_ARRAY_INDEX));
		} catch(ParseException e) {
			System.out.println(MESSAGE_JSON_INPUT_ERROR + eventIndex);
		}
		
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
		*/
	}
	
	/**
	 * Returns the start date and time of this event
	 * @return Date startDateAndTime
	 */
	public Date getStartDateAndTime() {
		return startDateAndTime;
	}
	
	/**
	 * Returns the end date and time of this event
	 * @return Date endDateAndTime
	 */
	public Date getEndDateAndTime() {
		return endDateAndTime;
	}
	
	/**
	 * Sets a new start date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setStartDateAndTime(String newDateString) {
		SimpleDateFormat ft = 
			      new SimpleDateFormat (DATE_AND_TIME_INPUT_FORMAT);
		try {
			startDateAndTime = ft.parse(newDateString);
		} catch(ParseException e) {
			System.out.println(MESSAGE_START_DATE_INPUT_ERROR);
		}
	}
	
	/**
	 * Sets a new end date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setEndDateAndTime(String newDateString) {
		SimpleDateFormat ft = 
			      new SimpleDateFormat (DATE_AND_TIME_INPUT_FORMAT);
		try {
			endDateAndTime = ft.parse(newDateString);
		} catch(ParseException e) {
			System.out.println(MESSAGE_END_DATE_INPUT_ERROR);
		}
	}
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		SimpleDateFormat ft = 
			      new SimpleDateFormat(DATE_AND_TIME_OUTPUT_FORMAT);
		return String.format(
				MESSAGE_TO_STRING_TEMPLATE, this.getEventName(), ft.format(startDateAndTime), ft.format(endDateAndTime));
	}
	
	/**
	 * Returns the JSON string to write to the file for this calendar object
	 * @return jsonString
	 */
	public String toJsonString() {
		SimpleDateFormat ft = 
			      new SimpleDateFormat(DATE_AND_TIME_INPUT_FORMAT);
		Map obj=new LinkedHashMap();
		obj.put(JSON_TYPE, "calendar");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		obj.put(JSON_START_DATE, ft.format(startDateAndTime));
		obj.put(JSON_END_DATE, ft.format(endDateAndTime));
		
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
}
