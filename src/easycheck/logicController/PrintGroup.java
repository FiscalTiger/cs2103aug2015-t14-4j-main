package easycheck.logicController;

import java.util.ArrayList;

import easycheck.eventlist.Event;

public class PrintGroup {
	public static final String NEWLINE = "\n";
	public static final String TAB = "\t";
	public static final String COLON = ":";
	
	private String heading;
	private ArrayList<Event> eventList;
	
	public PrintGroup(String heading) {
		this.heading = heading;
		eventList = new ArrayList<Event>();
	}
	
	public void addEntry(Event e) {
		eventList.add(e);
	}
	
	
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String toString() {
		String printGroup = heading + COLON + NEWLINE;
		for(Event e: eventList) {
			printGroup += TAB + e.toPrintGroupString();
		}
		return printGroup;
	}
}
