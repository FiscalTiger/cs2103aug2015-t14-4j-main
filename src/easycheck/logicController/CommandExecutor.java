package easycheck.logicController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import org.joda.time.DateTime;
import org.joda.time.Period;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.commandParser.CommandTypes.Exit;
import easycheck.commandParser.CommandTypes.Invalid;
import easycheck.commandParser.CommandTypes.Markdone;
import easycheck.commandParser.CommandTypes.Redo;
import easycheck.commandParser.CommandTypes.Repeat;
import easycheck.commandParser.CommandTypes.SaveAt;
import easycheck.commandParser.CommandTypes.Search;
import easycheck.commandParser.CommandTypes.Undo;
import easycheck.commandParser.CommandTypes.Update;
import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.Event;
import easycheck.eventlist.FloatingTask;
import easycheck.eventlist.ToDoEvent;

public class CommandExecutor {
	private static int ZERO_OFFSET = 1;

	private static final String MESSAGE_ADD_CMD_RESPONSE = "Added %s\n";
	private static final String MESSAGE_DELETE_CMD_EMPTY = "There aren't any events!\n";
	private static final String MESSAGE_DELETE_CMD_RESPONSE = "Deleted \"%s\" successfully\n";
	private static final String MESSAGE_UPDATE_FLOAT_RESPONSE = "Updated %s to %s successfully\n";
	private static final String MESSAGE_UPDATE_TODO_RESPONSE = "Updated %s successfully\n";
	private static final String MESSAGE_UPDATE_CAL_RESPONSE = "Updated %s successfully\n";
	private static final String MESSAGE_INVALID_CALENDAR_DATES = "The start date must be before the end date and after the current date and time.\n";
	private static final String MESSAGE_INVALID_TODO_DEADLINE = "The deadline must be after the current date and time.\n";
	private static final String MESSAGE_UNDO_EMPTY_STACK = "There is nothing to undo\n";
	private static final String MESSAGE_REDO_EMPTY_STACK = "There is nothing to redo\n";
	private static final String MESSAGE_UPDATE_INVALID_IDX = "%s is an invalid index.\n";
	private static final String PRINT_GROUP_HEADING_FLOATING = "To Do";

	// @@author A0126989H
	private static final String MESSAGE_SEARCH_CMD_EMPTY = "There aren't any events to search!\n";
	private static final String MESSAGE_SEARCH_CMD_NOTFOUND = "There are no such events!\n";
	private static final String MESSAGE_DELETE_CMD_NOTFOUND = "There are no such events!\n";
	private static final String MESSAGE_DELETE_CMD_ALL = "Congratulations on completing all task! :)\n";
	private static final String MESSAGE_DELETE_CMD_SPECIALCOMMAND = "all ";
	private static final String MESSAGE_DELETE_CMD_DONETASK = "Deleted all done tasks successfully\n";
	private static final String MESSAGE_DELETE_CMD_ALLTASKS = "Deleted \"%s\" related tasks successfully\n";

	private static final String MESSAGE_MARKDONE_CMD_RESPONSE = "Successfully mark %s as Done!\n";
	private static final String MESSAGE_MARDONE_CMD_EMPTY = "Your todoList is currently empty!\n";
	private static final String MESSAGE_MARKDONE_CMD_NOTFOUND = "There are no such events!\n";
	private static final String MESSAGE_MARKDONE_CMD_SPECIALCOMMAND = "all ";
	private static final String MESSAGE_MARKDONE_CMD_ALL = "Congratulations on finishing all tasks! :)\n";
	private static final String WHITESPACE_DELIMITER = "\\s+";
		
	private ArrayList<Event> eventList;
	private Stack<ArrayList<Event>> undoStack;
	private Stack<ArrayList<Event>> redoStack;

	public CommandExecutor(ArrayList<Event> initEventList) {
		eventList = initEventList;
		undoStack = new Stack<ArrayList<Event>>();
		redoStack = new Stack<ArrayList<Event>>();
	}

	/**
	 * Updates the cached version of the user's events and produces the response
	 * string to be printed to the user
	 * 
	 * @param command
	 * @return responseString
	 * @@author A0145668R
	 */
	public String executeCommand(Command command) {
		if (command instanceof Add) {
			return add((Add) command);
		} else if (command instanceof Display) {
			return display((Display) command);
		} else if (command instanceof Update) {
			return update((Update) command);
		} else if (command instanceof Delete) {
			return delete((Delete) command);
		} else if (command instanceof Markdone) {
			return markdone((Markdone) command);
		} else if (command instanceof Undo) {
			return undo((Undo) command);
		} else if (command instanceof Redo) {
			return redo((Redo) command);
		} else if (command instanceof Search) {
			return search((Search) command);
		} else if (command instanceof SaveAt) {
			return saveAt((SaveAt) command);
		} else if (command instanceof Exit) {
			return exit((Exit) command);
		} else if (command instanceof Invalid) {
			return Invalid((Invalid) command);
		} else {
			return command.toString();
		}
	}

	private String Invalid(Invalid command) {
		return command.getInvalidMessage();
	}

	/**
	 * Creates the correct type of event and adds it to eventList
	 * 
	 * @@author A0145668R
	 */
	private String add(Add cmd) {
		assert(cmd.getTaskName() != null);

		String response = "";
		Event newEvent;
		// has arguments for a calendar event
		if (cmd.hasStart() && cmd.hasEnd()) {
			if (CalendarEvent.areValidDates(cmd.getStart(), cmd.getEnd())) {
				int eventIndex = eventList.size() + 1;
				newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), cmd.getEnd());
				undoStack.push(new ArrayList<Event>(eventList));
				eventList.add(newEvent);
				sort();
				response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
			} else {
				response = MESSAGE_INVALID_CALENDAR_DATES;
			}
			// has arguments for a to do event
		} else if (!cmd.hasStart() && cmd.hasEnd()) {
			if (ToDoEvent.isValidDate(cmd.getEnd())) {
				int eventIndex = eventList.size() + 1;
				newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd());
				undoStack.push(new ArrayList<Event>(eventList));
				eventList.add(newEvent);
				sort();
				response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
			} else {
				response = MESSAGE_INVALID_TODO_DEADLINE;
			}
			// doesn't have time limits so it creates a floating task
		} else if (!cmd.hasStart() && !cmd.hasEnd()) {
			int eventIndex = eventList.size() + 1;
			newEvent = new FloatingTask(eventIndex, cmd.getTaskName());
			undoStack.push(new ArrayList<Event>(eventList));
			eventList.add(newEvent);
			sort();
			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);

		}
		// response should have a response by this point
		assert(!response.equals(""));
		return response;
	}

	private void sort() {
		Collections.sort(eventList);
		reIndex();
	}

	/**
	 * Displays all events
	 * 
	 * @@author A0145668R
	 */
	private String display(Display cmd) {
		String response = "";
		if (cmd.isIndex()) {
			response += eventList.get(cmd.getEventIndex() - 1);
		} else if (cmd.isFloating()) {
			response = getDisplayFloatingString();
		} else if (cmd.isDone()) {
			response = getDisplayDoneString();
		} else if (cmd.isDate()) {
			response = getDisplayDateString(cmd.getDisplayDate());
		} else if (cmd.isOverDue()) {
			response = getDisplayOverDueString();
		} else {
			response = getDefaultDisplayString();
		}
		// Response should not be empty
		assert(!response.equals(""));
		return response;
	}

	// Get Floating tasks for display string
	// @author A0145668R
	private String getDisplayFloatingString() {
		String response = "";
		PrintGroup printGroup = new PrintGroup(PRINT_GROUP_HEADING_FLOATING);
		for (Event e : eventList) {
			if (e instanceof FloatingTask) {
				printGroup.addEntry(e);
			}
		}
		response = printGroup.toString();
		return response;
	}
	
	// Get the tasks on a specific date for the display command
	// @author A0145668R
	private String getDisplayDateString(String dateText) {
		String response = "";
		PrintGroup dateGroup = new PrintGroup(dateText);
		for (Event e : eventList) {
			if (e instanceof CalendarEvent) {
				CalendarEvent cal = (CalendarEvent) e;
				if (dateGroup.getHeading().equals(cal.getStartDate())) {
					dateGroup.addEntry(cal);
				}
			} else if (e instanceof ToDoEvent && !e.isDone()) {
				ToDoEvent todo = (ToDoEvent) e;
				if (dateGroup.getHeading().equals(todo.getDeadlineDate())) {
					dateGroup.addEntry(todo);
				}
			}
		}

		response += dateGroup.toString();
		return response;
	}
	
	// Get done tasks for display string
	// @author A0145668R
	private String getDisplayDoneString() {
		String response = "";
		PrintGroup floatingGroup = new PrintGroup(PRINT_GROUP_HEADING_FLOATING);
		ArrayList<PrintGroup> dateGroups = new ArrayList<PrintGroup>();
		for (Event e : eventList) {
			if (e instanceof FloatingTask && e.isDone()) {
				floatingGroup.addEntry(e);
			} else if (e instanceof CalendarEvent && e.isDone()) {
				boolean isAdded = false;
				CalendarEvent cal = (CalendarEvent) e;
				for (PrintGroup dateGroup : dateGroups) {
					if (dateGroup.getHeading().equals(cal.getStartDate())) {
						dateGroup.addEntry(cal);
						isAdded = true;
						break;
					}
				}

				if (!isAdded) {
					PrintGroup temp = new PrintGroup(cal.getStartDate());
					dateGroups.add(temp);
					temp.addEntry(cal);
				}
			} else if (e instanceof ToDoEvent && e.isDone()) {
				boolean isAdded = false;
				ToDoEvent todo = (ToDoEvent) e;
				for (PrintGroup dateGroup : dateGroups) {
					if (dateGroup.getHeading().equals(todo.getDeadlineDate())) {
						dateGroup.addEntry(todo);
						isAdded = true;
						break;
					}
				}

				if (!isAdded) {
					PrintGroup temp = new PrintGroup(todo.getDeadlineDate());
					dateGroups.add(temp);
					temp.addEntry(todo);
				}
			}
		}

		response += floatingGroup.toString();
		for (PrintGroup dateGroup : dateGroups) {
			response += dateGroup.toString();
		}
		return response;
	}
	
	private String getDisplayOverDueString() {
		String response = "";
		ArrayList<PrintGroup> dateGroups = new ArrayList<PrintGroup>();
		for (Event e : eventList) {
			if (e instanceof ToDoEvent) {
				ToDoEvent todo = (ToDoEvent) e;
				if (todo.isOverDue()) {
					boolean isAdded = false;
					for (PrintGroup dateGroup : dateGroups) {
						if (dateGroup.getHeading().equals(todo.getDeadlineDate())) {
							dateGroup.addEntry(todo);
							isAdded = true;
							break;
						}
					}
	
					if (!isAdded) {
						PrintGroup temp = new PrintGroup(todo.getDeadlineDate());
						dateGroups.add(temp);
						temp.addEntry(todo);
					}
				}
			}
		}

		for (PrintGroup dateGroup : dateGroups) {
			response += dateGroup.toString();
		}
		return response;
	}
	
	// Get the default text for display string
	// @author A0145668R
	private String getDefaultDisplayString() {
		String response = "";
		PrintGroup floatingGroup = new PrintGroup(PRINT_GROUP_HEADING_FLOATING);
		ArrayList<PrintGroup> dateGroups = new ArrayList<PrintGroup>();
		for (Event e : eventList) {
			if (e instanceof FloatingTask && !e.isDone()) {
				floatingGroup.addEntry(e);
			} else if (e instanceof CalendarEvent && !e.isDone()) {
				boolean isAdded = false;
				CalendarEvent cal = (CalendarEvent) e;
				for (PrintGroup dateGroup : dateGroups) {
					if (dateGroup.getHeading().equals(cal.getStartDate())) {
						dateGroup.addEntry(cal);
						isAdded = true;
						break;
					}
				}

				if (!isAdded) {
					PrintGroup temp = new PrintGroup(cal.getStartDate());
					dateGroups.add(temp);
					temp.addEntry(cal);
				}
			} else if (e instanceof ToDoEvent && !e.isDone()) {
				boolean isAdded = false;
				ToDoEvent todo = (ToDoEvent) e;
				for (PrintGroup dateGroup : dateGroups) {
					if (dateGroup.getHeading().equals(todo.getDeadlineDate())) {
						dateGroup.addEntry(todo);
						isAdded = true;
						break;
					}
				}

				if (!isAdded) {
					PrintGroup temp = new PrintGroup(todo.getDeadlineDate());
					dateGroups.add(temp);
					temp.addEntry(todo);
				}
			}
		}

		response += floatingGroup.toString();
		for (PrintGroup dateGroup : dateGroups) {
			response += dateGroup.toString();
		}
		return response;
	}

	/*
	 * UPDATE requires arguments to be of "Event name" + "to" + "Updated Event"
	 * 
	 * @@author A0126989H
	 */
	private String update(Update cmd) {
		String idx = cmd.getTaskIdx();
		String newName = cmd.getNewName();
		String response = "";
		Event newEvent;
		int intIdx = 0;
		try {
			intIdx = Integer.parseInt(idx, 10);
		} catch (NumberFormatException e) {
			response = String.format(MESSAGE_UPDATE_INVALID_IDX, idx);
			return response;
		}
		if (intIdx > eventList.size() || intIdx <= 0) {
			response = String.format(MESSAGE_UPDATE_INVALID_IDX, idx);
		} else if (cmd.hasStart() && cmd.hasEnd()) {
			if (CalendarEvent.areValidDates(cmd.getStart(), cmd.getEnd())) {
				newEvent = new CalendarEvent(intIdx, newName, cmd.getStart(), cmd.getEnd());
				eventList.set(intIdx - 1, newEvent);
				response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
			} else {
				response = MESSAGE_INVALID_CALENDAR_DATES;
			}
		} else if (!cmd.hasStart() && cmd.hasEnd()) {
			if (ToDoEvent.isValidDate(cmd.getEnd())) {
				newEvent = new ToDoEvent(intIdx, newName, cmd.getEnd());
				eventList.set(intIdx - 1, newEvent);
				response = String.format(MESSAGE_UPDATE_TODO_RESPONSE, newName);
			} else {
				response = MESSAGE_INVALID_TODO_DEADLINE;
			}
		} else if (!cmd.hasStart() && !cmd.hasEnd()) {
			String originalName = eventList.get(intIdx - 1).getEventName();
			eventList.get(intIdx - 1).setEventName(newName);
			response = String.format(MESSAGE_UPDATE_FLOAT_RESPONSE, originalName, newName);
		}
		assert(!response.equals(""));
		sort();
		return response;

	}

	// @@author A0126989H
	private String markdone(Markdone cmd) {
		String arguments = cmd.getTaskName();
		// Case 1: When the command is "done"
		if (arguments == null) {
			return doneFirst(cmd);
			// Case 3: When the command is "done + index"
		} else if (isNumeric(arguments)) {
			return doneIndex(cmd);
			/*
			 * Case 2: Special Command : " done all" done Multiple matching String 
			 * and "done all + eventName"
			 */
		} else if (cmd.isDoneAll()) {
			return doneSpecial(cmd);
			// Case 4: When the command is "done + EventName"
		} else {
			return doneEvent(cmd);
		}
	}

	private String doneEvent(Markdone cmd) {
		String arguments = cmd.getTaskName();
		String doneEvent = "";
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEventName().toLowerCase().contains(arguments.toLowerCase())) {
				undoStack.push(new ArrayList<Event>(eventList));
				eventList.get(i).setDone();
				doneEvent = eventList.get(i).getEventName();
				break;
			}
		}
		if (doneEvent.equals(""))
			return MESSAGE_MARKDONE_CMD_NOTFOUND;
		else
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
	}

	private String doneSpecial(Markdone cmd) {
		String arguments = cmd.getTaskName();
		String doneEvent = "";
		if (arguments.equals(MESSAGE_MARKDONE_CMD_SPECIALCOMMAND.trim())) {
			undoStack.push(new ArrayList<Event>(eventList));
			for (int i = 0; i < eventList.size(); i++) {
				eventList.get(i).setDone();
			}
			return MESSAGE_MARKDONE_CMD_ALL;
		} else {
			undoStack.push(new ArrayList<Event>(eventList));
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().toLowerCase().contains(cmd.getTaskNameAll())) {
					eventList.get(i).setDone();
					doneEvent = cmd.getTaskNameAll();
				}
			}
			if (doneEvent.equals("")) {
				return MESSAGE_MARKDONE_CMD_NOTFOUND;
			}
			doneEvent = MESSAGE_MARKDONE_CMD_SPECIALCOMMAND + doneEvent;
		}
		return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
	}

	private String doneIndex(Markdone cmd) {
		String arguments = cmd.getTaskName();
		String doneEvent = "";
		int index = Integer.parseInt(arguments);
		if (eventList.size() < index || index < 1) {
			return MESSAGE_MARKDONE_CMD_NOTFOUND;
		} else if (eventList.size() != 0) {
			undoStack.push(new ArrayList<Event>(eventList));
			eventList.get(index - ZERO_OFFSET).setDone();
			doneEvent = eventList.get(index - ZERO_OFFSET).getEventName();
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
		} else {
			return MESSAGE_MARDONE_CMD_EMPTY;
		}
	}

	private String doneFirst(Markdone cmd) {
		String doneEvent = "";
		if (eventList.size() != 0) {
			undoStack.push(new ArrayList<Event>(eventList));
			eventList.get(0).setDone();
			doneEvent = eventList.get(0).getEventName();
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
		} else {
			return MESSAGE_MARDONE_CMD_EMPTY;
		}
	}

	// @ author A0126989H
	/*
	 * DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(Delete cmd) {
		String arguments = cmd.getTaskName();
		// Case 1: When the command is "delete"
		assert(!eventList.isEmpty());
		if (arguments == null) {
			return deleteFirst(cmd);
			// Case 2: When the command is "delete + index"
		} else if (isNumeric(arguments)) {
			return deleteIndex(cmd);
			/*
			 * Case 3: Special Command : " delete all" Delete Multiple matching
			 * String and "delete all + eventName"
			 */
		} else
			if (cmd.isDeleteAll()) {
			return deleteSpecial(cmd);
			// Case 4: Delete Done Tasks "delete done"
		} else if (cmd.isDeleteDone()) {
			return deleteDone(cmd);
			// Case 5: When the command is "delete + EventName"
		} else {
			return deleteEvent(cmd);
		}
	}

	// To be Implemented
	private String deleteDone(Delete cmd) {
		undoStack.push(new ArrayList<Event>(eventList));
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).isDone()) {
				eventList.remove(i);
				i--;
			}
		}
		reIndex();
		return MESSAGE_DELETE_CMD_DONETASK;
	}

	private String deleteEvent(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = "";
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEventName().toLowerCase().contains(arguments)) {
				undoStack.push(new ArrayList<Event>(eventList));
				removeEvent = eventList.remove(i).getEventName();
				reIndex();
				break;
			}
		}
		if (removeEvent.equals(""))
			return MESSAGE_DELETE_CMD_NOTFOUND;
		else
			return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
	}

	private String deleteSpecial(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = "";
		if (arguments.equals(MESSAGE_DELETE_CMD_SPECIALCOMMAND.trim())) {
			undoStack.push(new ArrayList<Event>(eventList));
			eventList.clear();
			return MESSAGE_DELETE_CMD_ALL;
		} else {
			undoStack.push(new ArrayList<Event>(eventList));
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().toLowerCase().contains(cmd.getTaskNameAll())) {
					eventList.remove(i).getEventName();
					removeEvent = cmd.getTaskNameAll();
					i--;
				}
			}
			if (removeEvent.equals("")) {
				return MESSAGE_DELETE_CMD_NOTFOUND;
			}
			reIndex();
		}
		return String.format(MESSAGE_DELETE_CMD_ALLTASKS, removeEvent);
	}

	private String deleteIndex(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = "";
		int index = Integer.parseInt(arguments);
		if (eventList.size() < index || index < 1) {
			return MESSAGE_DELETE_CMD_NOTFOUND;
		} else if (eventList.size() != 0) {
			undoStack.push(new ArrayList<Event>(eventList));
			removeEvent = eventList.remove(index - ZERO_OFFSET).getEventName();
			reIndex();
			return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
		} else {
			reIndex();
			return MESSAGE_DELETE_CMD_EMPTY;
		}
	}

	private String deleteFirst(Delete cmd) {
		String removeEvent = "";
		if (eventList.size() != 0) {
			undoStack.push(new ArrayList<Event>(eventList));
			removeEvent = eventList.remove(0).getEventName();
			reIndex();
			return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
		} else {
			reIndex();
			return MESSAGE_DELETE_CMD_EMPTY;
		}
	}

	// @@author A0126989H
	// ReIndexing all the event in the EventList
	public void reIndex() {
		for (int i = 0; i < eventList.size(); i++) {
			eventList.get(i).setEventIndex(i + 1);
		}
	}

	// @@author A0126989H
	// Checking if the delete argument is Index number
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	// Executes an Undo command
	// @author A0145668R
	private String undo(Undo cmd) {
		if (undoStack.isEmpty()) {
			return MESSAGE_UNDO_EMPTY_STACK;
		} else {
			redoStack.clear();
			redoStack.push(new ArrayList<Event>(eventList));
			eventList = undoStack.pop();
			reIndex();
		}
		Display disp = new Display();
		disp.setDefaultFlag(true);

		return display(disp);
	}
	
	// Executes an Redo command
	// @author A0145668R
	private String redo(Redo cmd) {
		if (redoStack.isEmpty()) {
			return MESSAGE_REDO_EMPTY_STACK;
		} else {
			undoStack.push(new ArrayList<Event>(eventList));
			eventList = redoStack.pop();
			reIndex();
		}

		Display disp = new Display();
		disp.setDefaultFlag(true);

		return display(disp);
	}

	// @@author A0126989H
	private String search(Search cmd) {
		String response = "";
		//Case 1: No Event to search at all;
		if (eventList.isEmpty()) {
			response = MESSAGE_SEARCH_CMD_EMPTY;
		//Case 2: Search freetime;
		} else if (cmd.isFreetimeSearch() && !cmd.hasDate()) {
			response = getFreeTime();
		//Case 3: Search freetime + Date;
		} else if (cmd.isFreetimeSearch() && cmd.hasDate()){
			response = getFreeTimeSpec(cmd);
		//Case 4: Search + EventName;
		} else {
			for (Event e : eventList) {
				if (e.getEventName().toLowerCase().contains(cmd.getArgument().toLowerCase())) {
					response += e;
				}
			}
		}
		if (response.equals("")) {
			response = MESSAGE_SEARCH_CMD_NOTFOUND;
		}
		// Response should not be empty
		assert(!response.equals(""));
		return response;
	}

	private String getFreeTimeSpec(Search cmd) {
		boolean[] freetime = new boolean[24];
		Arrays.fill(freetime, true);
		String indayEvent = "";
		String freeSlots ="";
		String response = "";
		int start=0;
		int end=0;
		DateTime tomorrow = cmd.getDate();

		for (Event e : eventList) {
			if (e instanceof CalendarEvent) {
				if (((CalendarEvent) e).isSameStartDay((tomorrow))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getStartDateAndTime().getHourOfDay()] = false;
				} else if (((CalendarEvent) e).isSameEndDay((tomorrow))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getEndDateAndTime().getHourOfDay()] = false;
				}
			} else if (e instanceof ToDoEvent) {
				if (((ToDoEvent) e).isSameDay((tomorrow))){
					indayEvent += e;
					freetime[((ToDoEvent) e).getDeadline().getHourOfDay()] = false;
				}
			}
		}
		
		for (int i = 7; i < 24; i++) {
			if (freetime[i]==true){
				start =i;
				while(i<24&&freetime[i]==true){
					i++;
				}
				end = i;
			}
			freeSlots += "Free from :  " + start + "---" +end + "\n";
		}
		response = indayEvent + "\n" + freeSlots + "\n";
		return response;
	}

	private String getFreeTime() {
		boolean[] freetime = new boolean[24];
		Arrays.fill(freetime, true);
		String indayEvent = "";
		String freeSlots ="";
		String response = "";
		int start=0;
		int end=0;
		DateTime today = new DateTime();
		DateTime tomorrow = today.plus(Period.days(1));

		for (Event e : eventList) {
			if (e instanceof CalendarEvent) {
				if (((CalendarEvent) e).isSameStartDay((tomorrow))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getStartDateAndTime().getHourOfDay()] = false;
				} else if (((CalendarEvent) e).isSameEndDay((tomorrow))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getEndDateAndTime().getHourOfDay()] = false;
				}
			} else if (e instanceof ToDoEvent) {
				if (((ToDoEvent) e).isSameDay((tomorrow))){
					indayEvent += e;
					freetime[((ToDoEvent) e).getDeadline().getHourOfDay()] = false;
				}
			}
		}
		
		for (int i = 7; i < 24; i++) {
			if (freetime[i]==true){
				start =i;
				while(i<24&&freetime[i]==true){
					i++;
				}
				end = i;
			}
			freeSlots += "Free from :  " + start + "---" +end + "\n";
		}
		response = indayEvent + "\n" + freeSlots + "\n";
		return response;
	}

	// TODO
	private String saveAt(SaveAt cmd) {
		return "Successfully Saved";
	}

	// TODO
	private String repeat(Repeat cmd) {
		return null;
	}

	private String exit(Exit cmd) {
		System.exit(1);
		return "";
	}

	public ArrayList<Event> getEventList() {
		return eventList;
	}

}
