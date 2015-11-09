package easycheck.logicController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.*;

import org.joda.time.DateTime;
import org.joda.time.Period;

import org.fusesource.jansi.AnsiConsole;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.commandParser.CommandTypes.Exit;
import easycheck.commandParser.CommandTypes.Help;
import easycheck.commandParser.CommandTypes.Invalid;
import easycheck.commandParser.CommandTypes.Markdone;
import easycheck.commandParser.CommandTypes.ReadFrom;
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
import easycheck.storage.StorageManager;

public class CommandExecutor {
	//@@author A0126989H
	private static int ZERO_OFFSET = 1;
	private static Logger logger = Logger.getLogger("CommandExecutor");
	private static String LOGGERNAME = "CommandExecutor.log";
	private static ConsoleHandler consoleH = new ConsoleHandler();
	//@@author

	private static final String MESSAGE_ADD_CMD_RESPONSE = "@|green Added|@ %s\n";
	private static final String MESSAGE_DISPLAY_CMD_EMPTY = "@|red There aren't any events to display!|@\n";
	private static final String MESSAGE_DELETE_CMD_EMPTY = "@|red There aren't any events!|@\n";
	private static final String MESSAGE_DELETE_CMD_RESPONSE = "@|green Deleted \"%s\" successfully|@\n";
	private static final String MESSAGE_UPDATE_FLOAT_RESPONSE = "@|green Updated %s to %s successfully|@\n";
	private static final String MESSAGE_UPDATE_TODO_RESPONSE = "@|green Updated %s successfully|@\n";
	private static final String MESSAGE_UPDATE_CAL_RESPONSE = "@|green Updated %s successfully|@\n";
	private static final String MESSAGE_UPDATE_TYPE_RESPONSE = "@|green %s is now a %s type task! |@ \n";
	private static final String MESSAGE_INVALID_CALENDAR_DATES = "@|red The start date must be before the end date and after the current date and time.|@\n";
	private static final String MESSAGE_INVALID_STOP_DATE = "@|red The stop date has to be after the end date.|@\n";
	private static final String MESSAGE_INVALID_TODO_DEADLINE = "@|red The deadline must be after the current date and time.|@\n";
	private static final String MESSAGE_UNDO_EMPTY_STACK = "@|red There is nothing to undo|@\n";
	private static final String MESSAGE_REDO_EMPTY_STACK = "@|red There is nothing to redo|@\n";
	private static final String MESSAGE_UPDATE_INVALID_IDX = "@|red%s is an invalid index.|@\n";
	private static final String MESSAGE_REPEAT_INVALID_EVENT_NAME = "@|red Repeat: There aren't any events with the name %s|@\n";
	private static final String MESSAGE_REPEAT_SUCCESS = "@|green Successfully made %s repeat %s|@\n";
	private static final String MESSAGE_STOP_REPEAT_SUCCESS = "@|green Successfully made %s stop repeating|@\n";
	private static final String MESSAGE_REPEAT_INVALID_INDEX = "@|red Repeat: invalid event index %d.|@\n";
	private static final String PRINT_GROUP_HEADING_FLOATING = "To Do";

	// @@author A0126989H
	private static final String NEWLINE = "\n";
	private static final String EMPTY_STRING ="";
	private static final String INVALID_DATE_EXCEPTION = "@|red Please use date format as DD/MM/YYYY|@\n";

	private static final String MESSAGE_SEARCH_CMD_EMPTY = "@|red There aren't any events to search!|@\n";
	private static final String MESSAGE_SEARCH_CMD_NOTFOUND = "@|red There are no such events!|@\n";
	private static final String MESSAGE_SEARCH_CMD_RESPONSE = "@|green Here is your search result: |@\n";
	private static final String MESSAGE_SEARCH_CMD_FREESLOT = "@|green Free from %s to %s |@\n";
	private static final String MESSAGE_SEARCH_CMD_TASK_IN_DAY = "@|red Work to be done today: |@\n";
	private static final String MESSAGE_SEARCH_CMD_NO_TASK = "@| None |@\n";
	
	private static final String MESSAGE_DELETE_CMD_NOTFOUND = "@|red There are no such events!|@\n";
	private static final String MESSAGE_DELETE_CMD_ALL = "@|green Congratulations on completing all task! :)|@\n";
	private static final String MESSAGE_DELETE_CMD_SPECIALCOMMAND = "all ";
	private static final String MESSAGE_DELETE_CMD_DONETASK = "@|green Deleted all done tasks successfully|@\n";
	private static final String MESSAGE_DELETE_CMD_ALLTASKS = "@|green Deleted \"%s\" related tasks successfully|@\n";

	private static final String MESSAGE_MARKDONE_CMD_RESPONSE = "@|green Successfully mark %s as Done!|@\n";
	private static final String MESSAGE_MARDONE_CMD_EMPTY = "@|green Your todoList is currently empty!|@\n";
	private static final String MESSAGE_MARKDONE_CMD_NOTFOUND = "@|red There are no such events!|@\n";
	private static final String MESSAGE_MARKDONE_CMD_SPECIALCOMMAND = "all ";
	private static final String MESSAGE_MARKDONE_CMD_ALL = "@|green Congratulations on finishing all tasks! :)|@\n";
	private static final String WHITESPACE_DELIMITER = "\\s+";
	

	// @@author A0121560W
	private static final String MESSAGE_SAVE_AT_SUCCESS = "@|green File has been save at %s successfully!|@ \n";
	private static final String MESSAGE_SAVE_AT_IO_EXCEPTION = "@|red File could not be saved at %s!|@ \n";
	private static final String UPDATE_COMMAND_TYPE_START = "start";
	private static final String UPDATE_COMMAND_TYPE_END = "end";
	private static final String UPDATE_COMMAND_TYPE_NAME = "name";
	private static final String UPDATE_COMMAND_TYPE_TYPE = "type";
	private static final String UPDATE_EVENT_TYPE_TODO = "todo";
	private static final String UPDATE_EVENT_TYPE_CALENDAR = "calendar";
	private static final String UPDATE_EVENT_TYPE_FLOATING = "floating";
	private static final String MESSAGE_UPDATE_INVALID_CAST = "@|red %s task type cannot be changed to %s task type!|@ \n";
	private static final String MESSAGE_UPDATE_INVALID_TYPE = "@|red %s is not a valid type! |@ \n";
	private static final String MESSAGE_UPDATE_INVALID_START = "@|red A task cannot just have a start date/time! |@ \n";
	private static final String MESSAGE_UPDATE_INVALID_END = "@|red The end date must be after the start date! |@ \n";
	private static final String MESSAGE_UPDATE_NAME_RESPONSE = "@|green Task %s has been renamed to %s |@ \n";
	// various help topics, based off command types
	private static final String HELP_TOPIC_ADD = "add";
	private static final String HELP_TOPIC_DELETE = "delete";
	private static final String HELP_TOPIC_DISPLAY = "display";
	private static final String HELP_TOPIC_EXIT = "exit";
	private static final String HELP_TOPIC_HELP = "help";
	private static final String HELP_TOPIC_DONE = "done";
	private static final String HELP_TOPIC_READ_FROM = "read_from";
	private static final String HELP_TOPIC_REDO = "redo";
	private static final String HELP_TOPIC_REPEAT = "repeat";
	private static final String HELP_TOPIC_SAVE_AT = "save_at";
	private static final String HELP_TOPIC_SEARCH = "search";
	private static final String HELP_TOPIC_UNDO = "undo";
	private static final String HELP_TOPIC_UPDATE = "update";
	// various help messages
	private static final String MESSAGE_HELP_DEFAULT = "For more information on a specific command, type HELP command-name\n"
			+ "@|cyan Example: help update |@\n\n"
			+ "@|red Note: Dates in this program follow a mm/dd/yyyy format |@\n\n"
			+ "@|green Available command types: |@\n"
			+ "@|yellow "+HELP_TOPIC_ADD+"\t\t "+HELP_TOPIC_DELETE+"\t\t "+HELP_TOPIC_DISPLAY+"\t\t "+HELP_TOPIC_DONE+"\t\t\n"
			+HELP_TOPIC_EXIT+"\t\t "+HELP_TOPIC_HELP+"\t\t "+HELP_TOPIC_READ_FROM+"\t\t "+HELP_TOPIC_REDO+"\t\t\n"
			+HELP_TOPIC_REPEAT+"\t\t "+HELP_TOPIC_SAVE_AT+"\t "+HELP_TOPIC_SEARCH+"\t\t\t "+HELP_TOPIC_UNDO+"\t\t\n"
			+HELP_TOPIC_UPDATE+"\t\t |@\n";
	
	private static final String MESSAGE_HELP_ADD = "@|green ADD: Adds tasks/events to the record:\n|@"
			+ "@|yellow To add tasks to the record: |@\n"
			+ "@|green add <task_name> |@ \n"
			+ "@|cyan Example: add CS2103 Homework |@\n\n"
			
			+ "@|yellow To add ToDo tasks to the record: |@\n"
			+ "@|green add <ToDo_task_name>, <due_date w/o time> |@\n"
			+ "@|green add <ToDo_task_name> at <due_date w/o time> |@\n"
			+ "@|green add <ToDo_task_name> due <due_date w/o time> |@\n"
			+ "@|cyan Example: add project v0.5 due at noon tomorrow |@\n\n"
			
			+ "@|yellow To add Events to the record: |@\n"
			+ "@|green add <event_name> from <start_date w/o time> to <end_date w/o time> |@\n"
			+ "@|cyan Example: add Meeting with HR from 12/9/2015 18:00 to 19:00|@\n\n"
			
			+ "@|yellow To add repeating ToDo or Event to the record:|@\n"
			+ "@|green add <task_name> (at,due,for,by) <due_date> repeat (daily, weekly, biweekly, monthly, yearly)|@\n"
			+ "@|green add <task_name> (at,due,for,by) <start_date> to <end_date> repeat (daily, weekly, biweekly, monthly, yearly)|@\n"
			+ "@|cyan Example ToDo: add Laundry by Monday 11:59 repeat weekly |@\n"
			+ "@|cyan Example Event: add My birthday at midnight november 27th to 11:59pm november 27th repeat yearly |@\n"
			+ "@|yellow The repeating period can be specified as well by appending \"stop <date>\" at the end|@\n"
			+ "@|cyan add Software Engineering Quiz by Tuesday 11:59 repeat weekly stop 11/10/2015 11:59 |@\n"
			+ "@|cyan add Software Egineering Lecture at Friday 2 pm to Friday 4 pm repeat weekly stop 11/6/2015 4 pm|@\n";

	private static final String MESSAGE_HELP_DELETE = "@|green DELETE: Deletes tasks/events from the record:\n"
			+ "@|yellow To delete the first task:|@\n"
			+ "@|cyan Example: delete |@\n\n"
			
			+ "@|yellow To delete a task by index:|@\n"
			+ "@|green delete <index> |@\n"
			+ "@|cyan Example: delete 3 |@\n\n"
			
			+ "@|yellow To delete a task by name (whole or part of): |@\n"
			+ "@|green delete <name> |@\n"
			+ "@|cyan Example: delete presentation |@\n\n"
			
			+ "@|yellow To delete all tasks:|@\n"
			+ "@|green delete all |@\n\n"
			
			+ "@|yellow To delete all related tasks containing a phrase:|@\n"
			+ "@|green delete all <phrase>|@\n"
			+ "@|cyan Example: delete all cs2103 |@\n";
	
	private static final String MESSAGE_HELP_DISPLAY = "@|green DISPLAY: Displays tasks/events from the record: |@\n"
			+ "@|yellow To display all uncompleted tasks: |@\n"
			+ "@|cyan Example: display |@\n\n"
			
			+ "@|yellow To display all done tasks: |@\n"
			+ "@|cyan Example: display done |@\n\n"

			+ "@|yellow To display all floating tasks: |@\n"
			+ "@|cyan Example: display f |@\n\n"

			+ "@|yellow To display all events: |@\n"
			+ "@|cyan Example: display all |@\n\n"

			+ "@|yellow To display all events on a day: |@\n"
			+ "@|green display d, <date>|@\n"
			+ "@|cyan Example: display d, November 6th|@\n\n"

			+ "@|yellow To display a single event: |@\n"
			+ "@|green display i, <index>|@\n"
			+ "@|cyan Example: display i, 13|@\n";
	
	private static final String MESSAGE_HELP_EXIT = "@|green EXIT: Exits the program: |@\n"
			+ "@|cyan Example: exit |@\n";
	
	private static final String MESSAGE_HELP_HELP = "@|green . . . _ _ _ . . . |@\n";
	
	private static final String MESSAGE_HELP_DONE = "@|green DONE: Marks tasks/events in the record as done: |@\n"
			+ "@|yellow To mark the first task as done:|@\n"
			+ "@|cyan Example: done|@\n\n"
			
			+ "@|yellow To mark tasks as Done by index: |@\n"
			+ "@|green done <index> |@\n"
			+ "@|cyan Example: done 5 |@\n\n"
			
			+ "@|yellow To mark all tasks as Done: |@\n"
			+ "@|cyan Example: done all |@\n\n"
			
			+ "@|yellow To mark all related tasks containing a phrase as Done: |@\n"
			+ "@|green done all <phrase> |@\n"
			+ "@|cyan Example: done all submit |@\n\n"
			
			+ "@|yellow To mark a single task by name/phrase as Done: |@\n"
			+ "@|green done <phrase> |@\n"
			+ "@|cyan Example: done presentation |@\n";
	
	private static final String MESSAGE_HELP_READ_FROM = "@|green READ_FROM: Read and Write to a record file: |@\n"
			+ "@|red this command handles absolute and relative file paths |@\n"
			+ "@|green read_from <file> |@\n"
			+ "@|cyan Example: read_from benjamin_schedule.txt |@\n";
	
	private static final String MESSAGE_HELP_REDO = "@|green REDO: Re-does last action that altered the record: |@\n"
			+ "@|cyan Example: redo |@\n";
	
	private static final String MESSAGE_HELP_REPEAT = "@|green REPEAT: Changes an existing task's repeating status: |@\n"
			+ "@|yellow To make and existing task repeating: |@\n"
			+ "@|green repeat <index/name>, <frequency> |@\n"
			+ "@|cyan Example: repeat 5, weekly |@\n"
			+ "@|cyan Example: repeat lunch, daily |@\n\n"
			
			+ "@|yellow To make an existing task repeating with a stop date: |@\n"
			+ "@|green repeat <index/name>, <frequency>, <date/time>|@\n"
			+ "@|cyan Example: repeat 4, weekly, 12/1/2016 |@\n"
			+ "@|cyan Example: repeat order pizza, daily, 3/1/2016 |@\n\n"
			
			+ "@|yellow To make an existing repeating task stop repeating: |@\n"
			+ "@|green repeat <index/name>, none |@\n"
			+ "@|cyan Example: repeat 5, none |@\n"
			+ "@|cyan Example: repeat pizza, none |@\n";
	
	private static final String MESSAGE_HELP_SAVE_AT = "@|green SAVE_AT: Saves a copy of the current record at the specified location: |@\n"
			+ "@|red this command handles absolute and relative file paths |@\n"
			+ "@|green save_at <file> |@\n"
			+ "@|cyan Example: save_at benjamin_schedule.txt |@\n";
	
	private static final String MESSAGE_HELP_SEARCH = "@|green SEARCH: Searches the record of occurences of a phrase |@\n"
			+ "@|green search <phrase> |@\n"
			+ "@|cyan Example: search homework |@\n";
	
	private static final String MESSAGE_HELP_UNDO = "@|green UNDO: Un-does the last action that altered the record |@\n"
			+ "@|cyan Example: undo |@\n";
	
	private static final String MESSAGE_HELP_UPDATE = "@|green UPDATE: Updates the record |@\n"
			+ "@|yellow Update a specific part of a record: |@\n"
			+ "@|green update <index>, <type>, <arguments> |@\n"
			+ "@|cyan Updating name: update 1, name, prepare presentation and slides |@\n"
			+ "@|cyan Updating start time: update 2, start, 11/11/2015 |@\n"
			+ "@|cyan Updating end time: update 3, end, 12/12/2015 |@\n"
			+ "@|cyan Updating type: update 6, type, (floating/todo/calendar) |@\n\n"
			
			+ "@|yellow Update multiple parts concurrently: |@\n"
			+ "@|red tasks will change to different types with this command if parameters are not provided. Use with caution! |@\n"
			+ "@|red No change in task types|@ \n@|green update <index>, <name> |@\n"
			+ "@|red Changes floating tasks to ToDo tasks|@ \n@|green update <index>, <name>, <end> |@\n"
			+ "@|red Changes floating and ToDo tasks to Calendar Events|@ \n@|green update <index>, <name>, <start>, <end> |@\n"
			+ "@|cyan update 4, do assignment six too |@\n"
			+ "@|cyan update 5, do assignment six too, 11/11/2015 |@\n"
			+ "@|cyan update 6, do assignment six too, 11/11/2015, 11/13/2015 |@\n";

	private static final String MESSAGE_HELP_INVALID = "@|red %s is not a valid command! |@\n";
	
	
	private static final String SECURITY_EXCEPTION = "@|red Permission denied |@ \n";
	private static final String IO_EXCEPTION = "@|red Invalid Input name|@ \n";
	
	private static final String LOG_ADDING = "Adding %s to List";
	private static final String LOG_ADD_FAILED = "Add command failed: %s";

	private ArrayList<Event> eventList;
	private Stack<ArrayList<Event>> undoStack;
	private Stack<ArrayList<Event>> redoStack;

	public CommandExecutor(ArrayList<Event> initEventList) {
		eventList = initEventList;
		undoStack = new Stack<ArrayList<Event>>();
		redoStack = new Stack<ArrayList<Event>>();
		//@@author A0126989H
		consoleH.setLevel(Level.SEVERE);
		logger.setUseParentHandlers(false);
		logger.addHandler(consoleH);
		logger.setLevel(Level.FINE); 
		logger.log(Level.FINE, "Going to start CommandExecutor");
		//@@author
	}

	private void showToUser(String message) {
		System.out.println(message);
	}

	/**
	 * Updates the cached version of the user's events and produces the response
	 * string to be printed to the user
	 * 
	 * @param command
	 * @return responseString
	 * @@@author A0145668R
	 */
	public String executeCommand(Command command) {
		if (command instanceof Add) {
			redoStack.clear();
			return add((Add) command);
		} else if (command instanceof Repeat) { 
			redoStack.clear();
			return repeat((Repeat)command);
		} else if (command instanceof Display) {
			return display((Display) command);
		} else if (command instanceof Update) {
			redoStack.clear();
			return update((Update) command);
		} else if (command instanceof Delete) {
			redoStack.clear();
			return delete((Delete) command);
		} else if (command instanceof Markdone) {
			redoStack.clear();
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
		} else if (command instanceof ReadFrom) {
			return readFrom((ReadFrom) command);
		} else if (command instanceof Help) {
			return help((Help) command);
		} else {
			return command.toString();
		}
	}

	private String Invalid(Invalid command) {
		return command.toString();
	}

	/**
	 * Creates the correct type of event and adds it to eventList
	 * @return String with the action that took place
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
				if (cmd.isRepeating() && cmd.hasStopDate()) {
					if (CalendarEvent.areValidDates(cmd.getEnd(), cmd.getStopDate())) {
						newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), 
								cmd.getEnd(), true, cmd.getFrequency(), cmd.getStopDate());
					} else {
						logger.log(Level.FINE, String.format(LOG_ADD_FAILED, cmd));
						return MESSAGE_INVALID_STOP_DATE;
					}
				} else if(cmd.isRepeating()) {
					newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), 
							cmd.getEnd(), true, cmd.getFrequency());
				} else {
					newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), cmd.getEnd());
				}
				undoStack.push(cloneEventList());
				logger.log(Level.FINE, String.format(LOG_ADDING, newEvent));
				eventList.add(newEvent);
				sort();
				response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
			} else {
				logger.log(Level.FINE, String.format(LOG_ADD_FAILED, cmd));
				response = MESSAGE_INVALID_CALENDAR_DATES;
			}
			// has arguments for a to do event
		} else if (!cmd.hasStart() && cmd.hasEnd()) {
			if (ToDoEvent.isValidDate(cmd.getEnd())) {
				int eventIndex = eventList.size() + 1;
				if (cmd.isRepeating() && cmd.hasStopDate()) {
					if(cmd.getEnd().isBefore(cmd.getStopDate())) {
						newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd(), true, cmd.getFrequency(), cmd.getStopDate());
					} else {
						logger.log(Level.FINE, String.format(LOG_ADD_FAILED, cmd));
						return MESSAGE_INVALID_STOP_DATE;
					}
				} else if(cmd.isRepeating()) {
					newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd(), true, cmd.getFrequency());
				} else {
					newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd());
				}
				undoStack.push(cloneEventList());
				logger.log(Level.FINE, String.format(LOG_ADDING, newEvent));
				eventList.add(newEvent);
				sort();
				response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
			} else {
				logger.log(Level.FINE, String.format(LOG_ADD_FAILED, cmd));
				response = MESSAGE_INVALID_TODO_DEADLINE;
			}
			// doesn't have time limits so it creates a floating task
		} else if (!cmd.hasStart() && !cmd.hasEnd()) {
			int eventIndex = eventList.size() + 1;
			newEvent = new FloatingTask(eventIndex, cmd.getTaskName());
			undoStack.push(cloneEventList());
			logger.log(Level.FINE, String.format(LOG_ADDING, newEvent));
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
	 * Displays events according to flags set by user
	 * 
	 * @@author A0145668R
	 */
	private String display(Display cmd) {
		String response = "";
		if(eventList.isEmpty()) {
			response = MESSAGE_DISPLAY_CMD_EMPTY;
		} else if (cmd.isIndex()) {
			response += eventList.get(cmd.getEventIndex() - 1);
		} else if (cmd.isFloating()) {
			response = getDisplayFloatingString();
		} else if (cmd.isDone()) {
			response = getDisplayDoneString();
		} else if (cmd.isDate()) {
			response = getDisplayDateString(cmd.getDisplayDate());
		} else if (cmd.isOverDue()) {
			response = getDisplayOverDueString();
		} else if (cmd.isAllFlag()) {
			response = getDisplayAllString();
		} else {
			response = getDefaultDisplayString();
		}
		// Response should not be empty
		assert(!response.equals(""));
		return response;
	}

	// Get Floating tasks for display string
	// @@author A0145668R
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
	// @@author A0145668R
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
	// @@author A0145668R
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

	private String getDisplayAllString() {
		String response = "";
		PrintGroup floatingGroup = new PrintGroup(PRINT_GROUP_HEADING_FLOATING);
		ArrayList<PrintGroup> dateGroups = new ArrayList<PrintGroup>();
		for (Event e : eventList) {
			if (e instanceof FloatingTask) {
				floatingGroup.addEntry(e);
			} else if (e instanceof CalendarEvent) {
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
			} else if (e instanceof ToDoEvent) {
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

	// Get the default text for display string
	// @@author A0145668R
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
	 * @@author A0121560W
	 */
	private String update(Update cmd) {
		String idx = cmd.getTaskIdx();
		String newName = cmd.getNewName();
		DateTime start = cmd.getStart();
		DateTime end = cmd.getEnd();
		String type = cmd.getType();
		String taskType = null;
		Event task;
		String response = "";
		Event newEvent;
		int intIdx = 0;
		try {
			intIdx = Integer.parseInt(idx, 10);
		} catch (NumberFormatException e) {
			response = String.format(MESSAGE_UPDATE_INVALID_IDX, idx);
			return response;
		}
		int adjustedIdx = intIdx - 1;
		if (intIdx > eventList.size() || intIdx <= 0) {
			response = String.format(MESSAGE_UPDATE_INVALID_IDX, idx);
		}

		task = eventList.get(adjustedIdx);
		taskType = getInstance(task);

		if (cmd.hasType()) {
			if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_NAME)) {
				int taskIdx = task.getEventIndex();
				setEventListName(adjustedIdx, newName);
				response = String.format(MESSAGE_UPDATE_NAME_RESPONSE, taskIdx, newName);
			} else if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_TYPE)) {
				if (newName.equalsIgnoreCase(UPDATE_EVENT_TYPE_CALENDAR)) {
					response = String.format(MESSAGE_UPDATE_INVALID_CAST, taskType, newName);
				} else if (newName.equalsIgnoreCase(UPDATE_EVENT_TYPE_TODO)) {
					if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)
							|| taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_TODO)) {
						response = String.format(MESSAGE_UPDATE_INVALID_CAST, taskType, newName);
					} else {
						eventList.set(adjustedIdx, castToDo((CalendarEvent) task));
						response = String.format(MESSAGE_UPDATE_TYPE_RESPONSE, newName, UPDATE_EVENT_TYPE_TODO);
					}
				} else if (newName.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)) {
					if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)) {
						response = String.format(MESSAGE_UPDATE_INVALID_CAST, taskType, newName);
					} else {
						eventList.set(adjustedIdx, castFloating(task));
						response = String.format(MESSAGE_UPDATE_TYPE_RESPONSE, newName, UPDATE_EVENT_TYPE_FLOATING);
					}
				} else {
					response = String.format(MESSAGE_UPDATE_INVALID_TYPE, newName);
				}

			} else if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_END)) {
				if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)) {
					if (!cmd.hasEnd() || !ToDoEvent.isValidDate(end)) {
						response = MESSAGE_INVALID_TODO_DEADLINE;
					} else {	
						eventList.set(adjustedIdx, castToDoUp((FloatingTask) task, end));
						response = String.format(MESSAGE_UPDATE_TODO_RESPONSE, newName);
					}
				} else {
					newName = task.getEventName();
					if (!cmd.hasEnd() || !ToDoEvent.isValidDate(end)) {
						response = MESSAGE_INVALID_TODO_DEADLINE;
					} else {
						if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_CALENDAR)) {
							start = ((CalendarEvent) task).getStartDateAndTime();
							if (!CalendarEvent.areValidDates(start, end)) {
								response = MESSAGE_INVALID_CALENDAR_DATES;
							} else {
								((CalendarEvent) eventList.get(adjustedIdx)).setEndDateAndTime(end);
								response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
							}
						} else {
							((ToDoEvent) eventList.get(adjustedIdx)).setDueDateAndTime(end);
							response = String.format(MESSAGE_UPDATE_TODO_RESPONSE, newName);
						}
					}
				}
			} else if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_START)) {
				if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)) {
					response = MESSAGE_UPDATE_INVALID_START;
				} else if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_TODO)) {
					end = ((ToDoEvent) task).getDeadline();
					if (!cmd.hasStart() || !CalendarEvent.areValidDates(start, end)) {
						response = MESSAGE_INVALID_CALENDAR_DATES;
					} else {
						eventList.set(adjustedIdx, castCalendarUp((ToDoEvent) task, start));
						response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
					}
				} else if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_CALENDAR)) {
					end = ((CalendarEvent) task).getEndDateAndTime();
					if (!cmd.hasStart() || !CalendarEvent.areValidDates(start, end)) {
						response = MESSAGE_INVALID_CALENDAR_DATES;
					} else {
						((CalendarEvent) eventList.get(adjustedIdx)).setStartDateAndTime(start);
						response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
					}
				}
			}
		} else if (cmd.hasStart() && cmd.hasEnd()) {
			if (CalendarEvent.areValidDates(cmd.getStart(), cmd.getEnd())) {
				if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_TODO)){
					DateTime stopDate = task.getStopDate();
					String frequency = task.getFrequency();
					boolean repeating = ((ToDoEvent) task).isRepeating();
					newEvent = new CalendarEvent(intIdx, newName, start, end);
					newEvent.setStopDate(stopDate);
					newEvent.setFrequency(frequency);
					newEvent.setRepeating(repeating);
					if (task.isDone()){
						newEvent.setDone();
					}
					eventList.set(adjustedIdx, newEvent);
					response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
				} else if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)){
					newEvent = new CalendarEvent(intIdx, newName, start, end);
					if (task.isDone()){
						newEvent.setDone();
					}
					eventList.set(adjustedIdx, newEvent);
					response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
				} else {
					task.setStopDate(end);
					task.setEventName(newName);
					((CalendarEvent) task).setStartDateAndTime(start);
					response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
				}
				
			} else {
				response = MESSAGE_INVALID_CALENDAR_DATES;
			}
		} else if (!cmd.hasStart() && cmd.hasEnd()) {
			if (ToDoEvent.isValidDate(cmd.getEnd())) {
				if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_FLOATING)){
					newEvent = new ToDoEvent(intIdx, newName, cmd.getEnd());
					if (task.isDone()){
						newEvent.setDone();
					}
					eventList.set(adjustedIdx, newEvent);
					response = String.format(MESSAGE_UPDATE_FLOAT_RESPONSE, newName);
				} else if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_TODO))  {
					((ToDoEvent) eventList.get(adjustedIdx)).setDueDateAndTime(end);
					eventList.get(adjustedIdx).setEventName(newName);
					response = String.format(MESSAGE_UPDATE_TODO_RESPONSE, newName);
				} else if (taskType.equalsIgnoreCase(UPDATE_EVENT_TYPE_CALENDAR)){
					start = ((CalendarEvent) task).getStartDateAndTime();
					if (CalendarEvent.areValidDates(start, end)){
						eventList.get(adjustedIdx).setEventName(newName);
						((CalendarEvent) eventList.get(adjustedIdx)).setEndDateAndTime(end);
						response = String.format(MESSAGE_UPDATE_CAL_RESPONSE, newName);
					} else {
						response = MESSAGE_UPDATE_INVALID_END;
					}
				}
				
			} else {
				response = MESSAGE_INVALID_TODO_DEADLINE;
			}
		} else if (!cmd.hasStart() && !cmd.hasEnd()) {
			String originalName = eventList.get(adjustedIdx).getEventName();
			eventList.get(adjustedIdx).setEventName(newName);
			response = String.format(MESSAGE_UPDATE_FLOAT_RESPONSE, originalName, newName);
		}
		assert(!response.equals(""));
		sort();
		return response;

	}
	
	private String getInstance(Event task){
		String taskType = null;
		if (task instanceof ToDoEvent) {
			taskType = UPDATE_EVENT_TYPE_TODO;
		} else if (task instanceof CalendarEvent) {
			taskType = UPDATE_EVENT_TYPE_CALENDAR;
		} else if (task instanceof FloatingTask) {
			taskType = UPDATE_EVENT_TYPE_FLOATING;
		}
		return taskType;
	}
	
	private void setEventListName(int idx, String newName){
		eventList.get(idx).setEventName(newName);
	}
	
	// casting helpers only cast down the hierarchy, unless specified in method name CalendarEvent>ToDoEvent>FloatingTask
	private ToDoEvent castToDo(CalendarEvent task){
		DateTime end = ((CalendarEvent) task).getEndDateAndTime();
		String newName = task.getEventName();
		int taskIdx = task.getEventIndex();
		DateTime stopDate = task.getStopDate();
		String frequency = task.getFrequency();
		boolean repeating = ((CalendarEvent) task).isRepeating();
		ToDoEvent castedEvent = new ToDoEvent(taskIdx, newName, end);
		castedEvent.setStopDate(stopDate);
		castedEvent.setFrequency(frequency);
		castedEvent.setRepeating(repeating);
		return castedEvent;
	}
	
	private FloatingTask castFloating(Event task){
		String newName = task.getEventName();
		int taskIdx = task.getEventIndex();
		FloatingTask castedEvent = new FloatingTask(taskIdx, newName);
		if (task.isDone()){
			castedEvent.markComplete();
		}
		return castedEvent;
	}
	
	private ToDoEvent castToDoUp(FloatingTask task, DateTime end){
		String newName = task.getEventName();
		int taskIdx = task.getEventIndex();
		ToDoEvent castedEvent = new ToDoEvent(taskIdx, newName, end);
		if (task.isDone()){
			castedEvent.markComplete();
		}
		return castedEvent;
	}
	
	private CalendarEvent castCalendarUp(ToDoEvent task, DateTime start){
		String newName = task.getEventName();
		int taskIdx = task.getEventIndex();
		DateTime end = task.getDeadline();
		DateTime stopDate = task.getStopDate();
		String frequency = task.getFrequency();
		boolean repeating = ((ToDoEvent) task).isRepeating();
		CalendarEvent castedEvent = new CalendarEvent(taskIdx, newName, start, end);
		castedEvent.setStopDate(stopDate);
		castedEvent.setFrequency(frequency);
		castedEvent.setRepeating(repeating);
		return castedEvent;
	}

	// @@author A0126989H
	private String markdone(Markdone cmd) {
		logger.log(Level.INFO, "Execute Markdone command: ");
		String arguments = cmd.getTaskName();
		// Case 1: When the command is "done"
		if (arguments == null) {
			return doneFirst(cmd);
			// Case 2: When the command is "done + index"
		} else if (isNumeric(arguments)) {
			return doneIndex(cmd);
			/*
			 * Case 3: Special Command : " done all" done Multiple matching
			 * String Or "done all + eventName"
			 */
		} else if (cmd.isDoneAll()) {
			return doneSpecial(cmd);
			// Case 4: When the command is "done + EventName"
		} else {
			return doneEvent(cmd);
		}
	}

	private String doneEvent(Markdone cmd) {
		assert(cmd.getTaskName()!=null);
		String arguments = cmd.getTaskName();
		String doneEvent = EMPTY_STRING;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEventName().toLowerCase().contains(arguments.toLowerCase())) {
				undoStack.push(cloneEventList());
				eventList.get(i).setDone();
				doneEvent = eventList.get(i).getEventName();
				break;
			}
		}
		if (doneEvent.equals(EMPTY_STRING)) {
			return MESSAGE_MARKDONE_CMD_NOTFOUND;
		} else {
			sort();
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
		}
	}

	private String doneSpecial(Markdone cmd) {
		assert(cmd.getTaskName()!=null);
		String arguments = cmd.getTaskName();
		String doneEvent = EMPTY_STRING;
		if (arguments.equals(MESSAGE_MARKDONE_CMD_SPECIALCOMMAND.trim())) {
			undoStack.push(cloneEventList());
			for (int i = 0; i < eventList.size(); i++) {
				eventList.get(i).setDone();
			}
			return MESSAGE_MARKDONE_CMD_ALL;
		} else {
			undoStack.push(cloneEventList());
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().toLowerCase().contains(cmd.getTaskNameAll())) {
					eventList.get(i).setDone();
					doneEvent = cmd.getTaskNameAll();
				}
			}
			if (doneEvent.equals(EMPTY_STRING)) {
				return MESSAGE_MARKDONE_CMD_NOTFOUND;
			}
			doneEvent = MESSAGE_MARKDONE_CMD_SPECIALCOMMAND + doneEvent;
		}
		sort();
		return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
	}

	private String doneIndex(Markdone cmd) {
		assert(cmd.getTaskName()!=null);
		String arguments = cmd.getTaskName();
		String doneEvent = EMPTY_STRING;
		int index = Integer.parseInt(arguments);
		if (eventList.size() < index || index < 1) {
			return MESSAGE_MARKDONE_CMD_NOTFOUND;
		} else if (eventList.size() != 0) {
			undoStack.push(cloneEventList());
			eventList.get(index - ZERO_OFFSET).setDone();
			doneEvent = eventList.get(index - ZERO_OFFSET).getEventName();
			sort();
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
		} else {
			return MESSAGE_MARDONE_CMD_EMPTY;
		}
	}

	private String doneFirst(Markdone cmd) {
		assert(cmd.getTaskName()==null);
		String doneEvent = EMPTY_STRING;
		if (eventList.size() != 0) {
			undoStack.push(cloneEventList());
			eventList.get(0).setDone();
			doneEvent = eventList.get(0).getEventName();
			sort();
			return String.format(MESSAGE_MARKDONE_CMD_RESPONSE, doneEvent);
		} else {
			return MESSAGE_MARDONE_CMD_EMPTY;
		}
	}

	// @@ author A0126989H
	/*
	 * DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(Delete cmd) {
		String arguments = cmd.getTaskName();
		// Case 1: When the command is "delete"
		logger.log(Level.INFO, "Execute Delete Command: ");
		if (arguments == null) {
			return deleteFirst(cmd);
			// Case 2: When the command is "delete + index"
		} else if (isNumeric(arguments)) {
			return deleteIndex(cmd);
			/*
			 * Case 3: Special Command : " delete all" Delete Multiple matching
			 * String and "delete all + eventName"
			 */
		} else if (cmd.isDeleteAll()) {
			return deleteSpecial(cmd);
			// Case 4: Delete Done Tasks "delete done"
		} else if (cmd.isDeleteDone()) {
			return deleteDone(cmd);
			// Case 5: When the command is "delete + EventName"
		} else {
			return deleteEvent(cmd);
		}
	}

	private String deleteDone(Delete cmd) {
		undoStack.push(cloneEventList());
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
		assert(cmd.getTaskName()!=null);
		String arguments = cmd.getTaskName();
		String removeEvent = EMPTY_STRING;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEventName().toLowerCase().contains(arguments)) {
				undoStack.push(cloneEventList());
				removeEvent = eventList.remove(i).getEventName();
				reIndex();
				break;
			}
		}
		if (removeEvent.equals(EMPTY_STRING))
			return MESSAGE_DELETE_CMD_NOTFOUND;
		else
			return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
	}

	private String deleteSpecial(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = EMPTY_STRING;
		if (arguments.equals(MESSAGE_DELETE_CMD_SPECIALCOMMAND.trim())) {
			undoStack.push(cloneEventList());
			eventList.clear();
			return MESSAGE_DELETE_CMD_ALL;
		} else {
			undoStack.push(cloneEventList());
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().toLowerCase().contains(cmd.getTaskNameAll())) {
					eventList.remove(i).getEventName();
					removeEvent = cmd.getTaskNameAll();
					i--;
				}
			}
			if (removeEvent.equals(EMPTY_STRING)) {
				return MESSAGE_DELETE_CMD_NOTFOUND;
			}
			reIndex();
		}
		return String.format(MESSAGE_DELETE_CMD_ALLTASKS, removeEvent);
	}

	private String deleteIndex(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = EMPTY_STRING;
		int index = Integer.parseInt(arguments);
		if (eventList.size() < index || index < 1) {
			return MESSAGE_DELETE_CMD_NOTFOUND;
		} else if (eventList.size() != 0) {
			undoStack.push(cloneEventList());
			removeEvent = eventList.remove(index - ZERO_OFFSET).getEventName();
			reIndex();
			return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
		} else {
			reIndex();
			return MESSAGE_DELETE_CMD_EMPTY;
		}
	}

	private String deleteFirst(Delete cmd) {
		String removeEvent = EMPTY_STRING;
		if (eventList.size() != 0) {
			undoStack.push(cloneEventList());
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
	
	// @@author A0145668R
	public ArrayList<Event> cloneEventList() {
		ArrayList<Event> temp = new ArrayList<Event>();
		for (Event e : eventList) {
			temp.add(e.createCopy());
		}
		return temp;
	}


	// @@author A0126989H
	// Checking if the argument of DeleteCommand and Markdone is an Index number
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
			assert(d>=0.0);
		} catch (NumberFormatException nfe) {
			logger.log(Level.WARNING, "Formmating number error ");
			return false;
		}
		return true;
	}

	// Executes an Undo command
	// @@author A0145668R
	private String undo(Undo cmd) {
		if (undoStack.isEmpty()) {
			return MESSAGE_UNDO_EMPTY_STACK;
		} else {
			redoStack.push(cloneEventList());
			eventList = undoStack.pop();
		}
		Display disp = new Display();
		disp.setDefaultFlag(true);

		return display(disp);
	}

	// Executes an Redo command
	// @@author A0145668R
	private String redo(Redo cmd) {
		if (redoStack.isEmpty()) {
			return MESSAGE_REDO_EMPTY_STACK;
		} else {
			undoStack.push(cloneEventList());
			eventList = redoStack.pop();
		}

		Display disp = new Display();
		disp.setDefaultFlag(true);

		return display(disp);
	}

	// @@author A0126989H
	private String search(Search cmd) {
		assert(cmd instanceof Search);
		logger.log(Level.INFO, "Execute Search Command: ");
		String response = MESSAGE_SEARCH_CMD_RESPONSE;
		// Case 1: No Event to search at all;
		// return empty search message;
		assert(cmd.getArgument() != null);
		try {
			if (eventList.isEmpty()) {
				response += MESSAGE_SEARCH_CMD_EMPTY;
				// Case 2: Search freetime;
				// return freeslots for tomorrow

			} else if (cmd.isFreetimeSearch() && !cmd.hasDate()) {
				response += searchFreeTime();
				// Case 3: Search freetime + Date;
				// return freeslots on a specific date
			} else if (cmd.isFreetimeSearch() && cmd.hasDate()) {
				response += searchFreeTimeSpec(cmd);
				// Case 4: Search + EventName;
				// return all related task with same eventName;
			} else {
				response += searchEvent(cmd);
			}
		} catch (IllegalArgumentException iae) {
			response = (INVALID_DATE_EXCEPTION);
			logger.log(Level.WARNING, "Invalid Date Exception throws in search");
		}
		if (response.equals(MESSAGE_SEARCH_CMD_RESPONSE)) {
			response = MESSAGE_SEARCH_CMD_NOTFOUND;
		}
		assert(!response.equals(EMPTY_STRING));
		return response;
	}

	private String searchEvent(Search cmd) {
		String response = EMPTY_STRING;
		for (Event e : eventList) {
			if (e.getEventName().toLowerCase().contains(cmd.getArgument().toLowerCase())) {
				response += e;
			}
		}
		return response;
	}

	private String computFreeSlot(boolean[] freetime, String freeSlots) {
		logger.log(Level.INFO, "Trying to arrange all freetime in the day");
		int start = 0;
		int end = 0;
		for (int i = 7; i < 24; i++) {
			if (freetime[i] == true) {
				start = i;
				while (i < 24 && freetime[i] == true) {
					i++;
				}
				end = i;
			}
			freeSlots += String.format(MESSAGE_SEARCH_CMD_FREESLOT, start, end);
		}

		return freeSlots;
	}

	private String searchFreeTimeSpec(Search cmd) {
		boolean[] freetime = new boolean[24];
		Arrays.fill(freetime, true);
		String indayEvent = EMPTY_STRING;
		String freeSlots = EMPTY_STRING;
		String response = EMPTY_STRING;

		DateTime date = cmd.getDate();

		for (Event e : eventList) {
			if (e instanceof CalendarEvent) {
				if (((CalendarEvent) e).isSameStartDay((date))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getStartDateAndTime().getHourOfDay()] = false;
				} else if (((CalendarEvent) e).isSameEndDay((date))) {
					indayEvent += e;
					freetime[((CalendarEvent) e).getEndDateAndTime().getHourOfDay()] = false;
				}
			} else if (e instanceof ToDoEvent) {
				if (((ToDoEvent) e).isSameDay((date))) {
					indayEvent += e;
					freetime[((ToDoEvent) e).getDeadline().getHourOfDay()] = false;
				}
			}
		}
		freeSlots = computFreeSlot(freetime, freeSlots);
		response = MESSAGE_SEARCH_CMD_TASK_IN_DAY + indayEvent + NEWLINE + freeSlots + NEWLINE;
		return response;
	}

	private String searchFreeTime() {
		boolean[] freetime = new boolean[24];
		Arrays.fill(freetime, true);
		String indayEvent = EMPTY_STRING;
		String freeSlots = EMPTY_STRING;
		String response = EMPTY_STRING;
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
				if (((ToDoEvent) e).isSameDay((tomorrow))) {
					indayEvent += e;
					freetime[((ToDoEvent) e).getDeadline().getHourOfDay()] = false;
				}
			}
		}

		freeSlots = computFreeSlot(freetime, freeSlots);
		response = MESSAGE_SEARCH_CMD_TASK_IN_DAY + indayEvent + NEWLINE + freeSlots + NEWLINE;
		return response;
	}

	// @@author A0145668R
	private String repeat(Repeat cmd) {
		int eventId;
		String eventName;
		Event e = null;
		
		if(isNumeric(cmd.getTask())) {
			eventId = Integer.parseInt(cmd.getTask());
			if(eventId < 1 || eventId > eventList.size()) {
				return String.format(MESSAGE_REPEAT_INVALID_INDEX, eventId);
			}
			e = eventList.get(eventId - 1);
			eventName = e.getEventName();
			undoStack.push(cloneEventList());
			if(cmd.getFrequency().equals("none")) {
				e.setRepeating(false);
				e.setFrequency(null);
				e.setStopDate(null);
				return String.format(MESSAGE_STOP_REPEAT_SUCCESS,
						eventName);
			} else {
				e.setRepeating(true);
				e.setFrequency(cmd.getFrequency());
				e.setStopDate(cmd.getEndDate());
				return String.format(MESSAGE_REPEAT_SUCCESS,
						eventName, e.getFrequency());
			}
		} else {
			eventName = cmd.getTask();
			for (Event temp: eventList) {
				if (temp.getEventName().toLowerCase().contains(eventName.toLowerCase())) {
					e = temp;
					break;
				}
			}
			if(e != null) {
				undoStack.push(cloneEventList());
				if(cmd.getFrequency().equals("none")) {
					e.setRepeating(false);
					e.setFrequency(null);
					e.setStopDate(null);
				} else {
					e.setRepeating(true);
					e.setFrequency(cmd.getFrequency());
					e.setStopDate(cmd.getEndDate());
				}
				return String.format(MESSAGE_REPEAT_SUCCESS,
						eventName, e.getFrequency());
			} else {
				return String.format(MESSAGE_REPEAT_INVALID_EVENT_NAME, eventName);
			}
		}
	}

	private String exit(Exit cmd) {
		AnsiConsole.systemUninstall();
		System.exit(1);
		return EMPTY_STRING;
	}

	public ArrayList<Event> getEventList() {
		return eventList;
	}

	// @@author A0121560W
	private String saveAt(SaveAt cmd) {
		String target = cmd.getTarget();
		StorageManager saveTarget = new StorageManager(target);
		try {
			saveTarget.writeDataToEasyCheckFile(this.getEventList());
		} catch (IOException e) {
			return String.format(MESSAGE_SAVE_AT_IO_EXCEPTION, target);
		}
		return String.format(MESSAGE_SAVE_AT_SUCCESS, target);
	}

	private String readFrom(ReadFrom cmd) {
		return cmd.getReadTarget();
	}
	
	private String help(Help cmd) {
		String response = null;
		String topic = cmd.getTopic();
		if (!cmd.hasTopic()){
			response = MESSAGE_HELP_DEFAULT;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_ADD)){
			response = MESSAGE_HELP_ADD;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_DELETE)){
			response = MESSAGE_HELP_DELETE;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_DISPLAY)){
			response = MESSAGE_HELP_DISPLAY;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_EXIT)){
			response = MESSAGE_HELP_EXIT;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_HELP)){
			response = MESSAGE_HELP_HELP;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_DONE)){
			response = MESSAGE_HELP_DONE;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_READ_FROM)){
			response = MESSAGE_HELP_READ_FROM;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_REDO)){
			response = MESSAGE_HELP_REDO;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_REPEAT)){
			response = MESSAGE_HELP_REPEAT;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_SAVE_AT)){
			response = MESSAGE_HELP_SAVE_AT;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_SEARCH)){
			response = MESSAGE_HELP_SEARCH;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_UNDO)){
			response = MESSAGE_HELP_UNDO;
		} else if (topic.equalsIgnoreCase(HELP_TOPIC_UPDATE)){
			response = MESSAGE_HELP_UPDATE;
		} else {
			response = MESSAGE_HELP_INVALID;
		}
		
		
		assert(response != null);
		return response;
	}

}
