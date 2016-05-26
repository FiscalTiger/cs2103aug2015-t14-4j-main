package easycheck.eventlist;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * Class abstraction for floating events stored by the EasyCheck application.
 * Floating events do not have a start or end time.
 * @author A0145668R
 */

public abstract class Event implements Comparable<Event> {
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	
	private int eventIndex;
	private String eventName;
	private boolean complete;
	
	public Event() {}
	
	/**
	 * Event constructor
	 * @param eventIndex, the index of the event
	 * @param eventDesc, the description of the event
	 */
	public Event(int eventIndex, String eventName) {
		this.eventIndex = eventIndex;
		this.eventName = eventName;
		this.complete = false;
	}
	
	public Event(JSONObject jsonObj){
		Long eventIndex = (Long) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
	}
	
	
	/**
	 * Returns the event's index
	 * @return the event's index
	 */
	public int getEventIndex() {
		return eventIndex;
	}
	
	/**
	 * Returns the event's description
	 * @return the event's description
	 */
	public String getEventName() {
		return eventName;
	}
	
	/**
	 * Change the event's index
	 * @param newEventIndex the new event index
	 */
	public void setEventIndex(int newEventIndex) {
		this.eventIndex = newEventIndex;
	}
	
	/**
	 * Change the event's name
	 * @param newEventName the new event name
	 */
	public void setEventName(String newEventName) {
		this.eventName = newEventName;
	}
	
	public abstract boolean isDone();
	
	public abstract String toPrintGroupString();
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return eventIndex + ". " + eventName + "\n";
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	public abstract Event createCopy();
	
	public void setDone(){
		complete = true;
	}
	public void setUndone(){
		complete = false;
	}
	
	@Override
	public abstract int compareTo(Event e);
	
	public abstract String toJsonString();
	
	public void setRepeating(boolean repeating) {}

	public boolean hasStopDate() {
		return false;
	}
	
	public DateTime getStopDate() {
		return null;
	}

	public void setStopDate(DateTime stopDate) {}

	public String getFrequency() {
		return null;
	}

	public void setFrequency(String frequency) {}
	
}
