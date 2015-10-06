package easycheck.eventlist;

import org.json.simple.JSONObject;

/*
 * Class abstraction for events stored by the EasyCheck application
 * @author Andrew Pouleson
 */
public class Event implements Comparable<Event> {
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
	public String toString() {
		return eventIndex + ". " + eventName + "\n";
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) {
			return false;
		} else {
			Event e = (Event) obj;
			return eventIndex == e.getEventIndex() && eventName.equals(e.getEventName());
		}
	}
	
	public int compareTo(Event e) {
		return eventName.compareTo(e.getEventName());
	}
}
