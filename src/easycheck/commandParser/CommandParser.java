package easycheck.commandParser;

/**
 * Command Parser for Easy Check application. Takes in a user command and parses
 * it into a Command type Object for use by Storage.
 * 
 * @@author A0124206W
 */

import java.util.List;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import easycheck.commandParser.CommandTypes.*;

public class CommandParser {
	private final String COMMAND_SPLITTER = " ";
	private final String ARGUMENT_SPLITTER = ",";
	private final int COMMAND_ARRAY_LENGTH = 2;
	private final int PARAM_POSITION_COMMAND_TYPE = 0;
	private final int PARAM_POSITION_COMMAND_ARGUMENT = 1;

	// command types
	private final String COMMAND_TYPE_ADD = "add";
	private final String COMMAND_TYPE_UPDATE = "update";
	private final String COMMAND_TYPE_DELETE = "delete";
	// private final String COMMAND_TYPE_ADD_REPEAT = "addRepeat";
	// private final String COMMAND_TYPE_COMPLETE = "complete";
	// private final String COMMAND_TYPE_REMIND = "remind";
	private final String COMMAND_TYPE_SEARCH = "search";
	private final String COMMAND_TYPE_DISPLAY = "display";
	// private final String COMMAND_TYPE_DELETE_DONE = "deleteDone";
	// private final String COMMAND_TYPE_OVERDUE = "overdue ";
	// private final String COMMAND_TYPE_DELETE_TODAY = "deleteToday";
	// private final String COMMAND_TYPE_STORE_LOCATION = "storeLocation";
	// private final String COMMAND_TYPE_NEXT = "next";
	// private final String COMMAND_TYPE_UNDO = "undo";
	private final String COMMAND_TYPE_EXIT = "exit";
	private final String COMMAND_TYPE_INVALID = "invalid";
	
	private static final String MESSAGE_INVALID_DISPLAY_ARGS = "Display: Invalid flag \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_DATE = "Display: Couldn't parse the date \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_INDEX = "Display: Invalid index \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS = "Display: too many arugments\n";
	
	private static final String MESSAGE_INVALID_ADD_DATE = "Add: Couldn't parse the date \"%s\"s\n";
	private static final String MESSAGE_INVALID_ADD_NUM_OF_ARGS = "Add: too many arugments\n";
	
	private final String DISPLAY_FLAG_FLOATING = "f";
	private final String DISPLAY_FLAG_DONE = "done";
	private final String DISPLAY_FLAG_DUE = "due";
	private final String DISPLAY_FLAG_DATE = "d";
	private final String DISPLAY_FLAG_INDEX = "i";

	// expected number of parameters for add commands
	private final int NUM_ARGUMENTS_ADD = 1;
	private final int NUM_ARGUMENTS_ADD_TODO_EVENT = 2;
	private final int NUM_ARGUMENTS_ADD_CALENDAR_EVENT = 2;
	// private final int NUM_ARGUMENTS_ADD_REPEAT = 2;

	// expected number of parameters for all other commands
	private final int NUM_ARGUMENTS_UPDATE = 2;
	private final int NUM_ARGUMENTS_DELETE = 1;
	// private final int NUM_ARGUMENTS_COMPLETE = 1;
	// private final int NUM_ARGUMENTS_REMIND = 2;
	private final int NUM_ARGUMENTS_SEARCH = 1;
	// private final int NUM_ARGUMENTS_STORE_LOCATION = 1;

	private final int NUM_MAX_ARGUMENTS_DISPLAY = 2;
	// private final int NUM_ARGUMENTS_DELETE_DONE = 0;
	// private final int NUM_ARGUMENTS_OVERDUE = 0;
	// private final int NUM_ARGUMENTS_DELETE_TODAY = 0;
	// private final int NUM_ARGUMENTS_NEXT = 0;
	// private final int NUM_ARGUMENTS_UNDO = 0;
	private final int NUM_ARGUMENTS_EXIT = 0;
	

	// flexi command keywords
	private final String[] FLEXI_KEYWORDS = { " by ", " at ", " to ", " for " };

	public Command parseCommand(String userCommand) {
		String[] commandArray = splitCommand(userCommand);
		if (commandArray.length == 1) {
			Command command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE]);
			return command;
		} else {
			Command command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE],
					commandArray[PARAM_POSITION_COMMAND_ARGUMENT]);
			return command;
		}
	}

	private String[] splitCommand(String userCommand) {
		return userCommand.split(COMMAND_SPLITTER, COMMAND_ARRAY_LENGTH);
	}

	// creates a command type object for user commands with no arguments.
	private Command createCommand(String commandType) {
		String[] arguments = null;
		Command command;
		if (commandType.equalsIgnoreCase(COMMAND_TYPE_DISPLAY)) {
			command = createDisplayCommand(arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_EXIT)) {
			command = Command.createObject(commandType, arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
			command = Command.createObject(commandType, arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_SEARCH)) {
			command = Command.createObject(commandType, arguments);
		} else {
			command = Command.createObject(COMMAND_TYPE_INVALID, arguments);
		}
		return command;
	}

	// creates Command for commands with >0 arguments.
	private Command createCommand(String commandType, String commandArguments) {
		String[] arguments = null;
		try {
			if (commandType.equalsIgnoreCase(COMMAND_TYPE_ADD)) {
				arguments = getArgumentsAdd(commandArguments);
				return createAddCommand(arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_DELETE);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_UPDATE)) {
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_UPDATE);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_SEARCH)) {
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_SEARCH);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DISPLAY)) {
				arguments = getDisplayArguments(commandArguments);
				return createDisplayCommand(arguments);
			} else {
				Command command = Command.createObject(COMMAND_TYPE_INVALID, arguments);
				return command;
			}
		} catch (Exception e) {
			Command command = Command.createObject(COMMAND_TYPE_INVALID, arguments);
			return command;
		}
		// at this point, arguments should have been pulled out.
		assert(arguments != null);
		Command command = Command.createObject(commandType, arguments);
		return command;
	}
	
	//@author A0145668R
	private String[] getDisplayArguments(String commandArguments) {
		// split arguments and then trim them.
		String[] arguments = trimArguments(commandArguments.split(ARGUMENT_SPLITTER));
		return arguments;
	}
	
	//@author A0145668R
	private Command createAddCommand(String[] arguments) {
		Command cmd = null;
		String taskName = arguments[0];
		DateTime start;
		DateTime end;
		
		if (arguments.length == 1) {
			cmd = new Add(taskName);
		} else if (arguments.length==2) {
			end = parseDateText(arguments[1]);
			if(end != null) {
				cmd = new Add(taskName, end);
			} else {
				cmd = new Invalid(String.format(MESSAGE_INVALID_ADD_DATE, arguments[1]));
			}
		} else if (arguments.length==3) {
			start = parseDateText(arguments[1]);
			end = parseDateText(arguments[2]);
			if(start != null && end != null) {
				cmd = new Add(taskName, start, end);
			} else {
				if(start == null) {
					cmd = new Invalid(String.format(MESSAGE_INVALID_ADD_DATE, arguments[1]));
				}
				if(end == null) {
					cmd = new Invalid(String.format(MESSAGE_INVALID_ADD_DATE, arguments[2]));
				}
			}
		} else {
			cmd = new Invalid(MESSAGE_INVALID_ADD_NUM_OF_ARGS);
		}
		assert(cmd != null);
		
		return cmd;
	}
	
	//@author A0145668R
	private Command createDisplayCommand(String[] arguments) {
		Display disp = new Display();
		if(arguments == null) {
			disp.setDefaultFlag(true);
		} else if(arguments.length == 1) {
			if(arguments[0].equals(DISPLAY_FLAG_FLOATING)) {
				disp.setFloatingFlag(true);
			} else if(arguments[0].equals(DISPLAY_FLAG_DUE)) {
				disp.setNotDoneFlag(true);
			} else if(arguments[0].equals(DISPLAY_FLAG_DONE)) {
				disp.setDoneFlag(true);
			} else {
				return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_ARGS, arguments[0]));
			}
		} else if(arguments.length == 2) {
			if(arguments[0].equals(DISPLAY_FLAG_DATE)) {
				DateTime displayDate = parseDateText(arguments[1]);
				if(displayDate != null) {
					disp.setDateFlag(true);
					disp.setDisplayDate(displayDate);
				} else {
					return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_DATE, arguments[1]));
				}
			} else if(arguments[0].equals(DISPLAY_FLAG_INDEX)) {
				try {
					int eventIndex = Integer.parseInt(arguments[1]);
					disp.setEventIndex(eventIndex);
					disp.setIndexFlag(true);
				} catch(NumberFormatException e) {
					return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_INDEX, arguments[1]));
				}
			} else {
				return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_ARGS, arguments[0]));
			}
		} else {
			return new Invalid(MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS);
		}
		
		return disp;
	}
	
	//@author A0145668R
	public DateTime parseDateText(String dateString) {
		Parser dateParser = new Parser();
		List<DateGroup> dateGroups = dateParser.parse(dateString);
		if (dateGroups.size() != 1) {
			return null;
		} else {
		  DateGroup dateGroup = dateGroups.get(0);
		  if (dateGroup.getDates().size() != 1) {
			  return null;
		  }
		  return new DateTime(dateGroup.getDates().get(0));
		}
	}

	// get arguments for add type - supports flexi commands
	private String[] getArgumentsAdd(String commandArguments) {
		// check arguments for flexi commands, then trim them.
		String[] arguments = trimArguments(checkFlexi(commandArguments));
		return arguments;
	}

	private String[] getArguments(String commandArguments, int expectedArguments) throws Exception {
		// split arguments and then trim them.
		String[] arguments = trimArguments(commandArguments.split(ARGUMENT_SPLITTER));
		if (arguments.length < expectedArguments || arguments.length > expectedArguments) {
			throw new Exception();
		}
		return arguments;
	}

	private String[] trimArguments(String[] arguments) {
		String[] trimmedArguments = new String[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			trimmedArguments[i] = arguments[i].trim();
		}
		return trimmedArguments;
	}

	// replaces the flexi keywords with ',' for parsing.
	private String[] checkFlexi(String commandArguments) {
		for (int i = 0; i < FLEXI_KEYWORDS.length; i++) {
			commandArguments = commandArguments.replace(FLEXI_KEYWORDS[i], ARGUMENT_SPLITTER);
		}
		return commandArguments.split(ARGUMENT_SPLITTER);
	}
}
