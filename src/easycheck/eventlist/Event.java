package easycheck.eventlist;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONValue;

/*
 * Class abstraction for events stored by the EasyCheck application
 * @author Andrew Pouleson
 */
public class Event implements Comparable<Event> {
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	
	private int eventIndex;
	private String eventName;
	
	public Event() {}
	
	/**
	 * Event constructor
	 * @param eventIndex, the index of the event
	 * @param eventDesc, the description of the event
	 */
	public Event(int eventIndex, String eventName) {
		this.eventIndex = eventIndex;
		this.eventName = eventName;
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
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return eventIndex + ". " + eventName + "\n";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) {
			return false;
		} else {
			Event e = (Event) obj;
			return eventIndex == e.getEventIndex() && eventName.equals(e.getEventName());
		}
	}
	
	@Override
	public int compareTo(Event e) {
		return eventName.compareTo(e.getEventName());
	}
	
	
	public String toJsonString() {
		Map obj=new LinkedHashMap();
		obj.put(JSON_TYPE, "base");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		
		StringWriter out = new StringWriter();
	    try {
			JSONValue.writeJSONString(obj, out);
		} catch (IOException e) {
			System.out.println(MESSAGE_JSON_STRING_ERROR);
		}
	    return out.toString();
	}
}
