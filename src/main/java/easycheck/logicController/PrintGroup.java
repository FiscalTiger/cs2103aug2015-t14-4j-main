package easycheck.logicController;
/*
 * This file helps display the events in a nice format
 * @@author A0145668R
 */

import java.util.ArrayList;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import easycheck.eventlist.Event;

public class PrintGroup {
	public static final String NEWLINE = "\n";
	public static final String TAB = "\t";
	public static final String COLON = ":";
	public static final String HEADING_COLOR = "@|cyan %s|@";
	
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
		String printGroup = String.format(HEADING_COLOR, heading + COLON + NEWLINE);
		for(Event e: eventList) {
			printGroup += TAB + e.toPrintGroupString();
		}
		return printGroup + NEWLINE;
	}
}
