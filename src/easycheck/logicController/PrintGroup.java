package easycheck.logicController;

import java.util.ArrayList;

import easycheck.eventlist.Event;

public class PrintGroup {
	public static final String NEWLINE = "\n";
	public static final String TAB = "\t";
	
	private String heading;
	private ArrayList<Event> eventList;
	
	public PrintGroup(String heading) {
		this.heading = heading;
		eventList = new ArrayList<Event>();
	}
	
	public void addEntry(Event e) {
		eventList.add(e);
	}
	
	public String toString() {
		String printGroup = heading + NEWLINE;
		for(Event e: eventList) {
			printGroup += TAB + e;
		}
		return printGroup;
	}
}
