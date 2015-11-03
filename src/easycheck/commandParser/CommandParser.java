package easycheck.commandParser;

/**
 * Command Parser for Easy Check application. Takes in a user command and parses
 * it into a Command type Object for use by Storage.
 * 
 * @@author A0124206W
 */

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.commandParser.CommandTypes.Exit;
import easycheck.commandParser.CommandTypes.Invalid;
import easycheck.commandParser.CommandTypes.Redo;
import easycheck.commandParser.CommandTypes.Repeat;
import easycheck.commandParser.CommandTypes.Undo;
import easycheck.commandParser.CommandTypes.Update;
import easycheck.commandParser.CommandTypes.Markdone;

public class CommandParser {
	private final String COMMAND_SPLITTER = " ";
	private final String ARGUMENT_SPLITTER = ",";
	private final int COMMAND_ARRAY_LENGTH = 2;
	private final int PARAM_POSITION_COMMAND_TYPE = 0;
	private final int PARAM_POSITION_COMMAND_ARGUMENT = 1;

	// command types
	private final String COMMAND_TYPE_ADD = "add";
	private final String COMMAND_TYPE_UPDATE = "update";
	// private final String COMMAND_TYPE_UPDATE_SPECIFIC = "updateSpecific";
	private final String COMMAND_TYPE_DELETE = "delete";
	// private final String COMMAND_TYPE_ADD_REPEAT = "addRepeat";
	// private final String COMMAND_TYPE_COMPLETE = "complete";
	// private final String COMMAND_TYPE_REMIND = "remind";
	private final String COMMAND_TYPE_SEARCH = "search";
	private final String COMMAND_TYPE_DISPLAY = "display";
	private final String COMMAND_TYPE_MARKDONE = "done";
	// private final String COMMAND_TYPE_DELETE_DONE = "deleteDone";
	// private final String COMMAND_TYPE_OVERDUE = "overdue ";
	// private final String COMMAND_TYPE_DELETE_TODAY = "deleteToday";
	// private final String COMMAND_TYPE_STORE_LOCATION = "storeLocation";
	// private final String COMMAND_TYPE_NEXT = "next";
	private final String COMMAND_TYPE_UNDO = "undo";
	private final String COMMAND_TYPE_REDO = "redo";
	private final String COMMAND_TYPE_EXIT = "exit";

	private final String MESSAGE_INVALID_COMMAND = "Invalid Command\n";
	private final String MESSAGE_INVALID_LESS_ARGS = "Too little arguments for command type \"%s\" \n";

	private static final String MESSAGE_INVALID_DISPLAY_ARGS = "Display: Invalid flag \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_DATE = "Display: Couldn't parse the date \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_INDEX = "Display: Invalid index \"%s\"\n";
	private static final String MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS = "Display: too many arguments\n";
	
	private static final String MESSAGE_INVALID_ADD_DATE = "Add: Couldn't parse the date \"%s\"s\n";
	private static final String MESSAGE_INVALID_ADD_NUM_OF_ARGS = "Add: too many arugments\n";
	
	private static final String MESSAGE_INVALID_UPDATE_DATE = "Update: Couldn't parse the date \"%s\"s\n";
	private static final String MESSAGE_INVALID_UPDATE_NUM_OF_ARGS = "Update: too many arugments\n";
	private final String DISPLAY_FLAG_FLOATING = "f";
	private final String DISPLAY_FLAG_DONE = "done";
	private final String DISPLAY_FLAG_DUE = "due";
	private final String DISPLAY_FLAG_DATE = "d";
	private final String DISPLAY_FLAG_INDEX = "i";

	// expected number of parameters for all other commands
	private final int NUM_ARGUMENTS_DELETE = 1;
	private final int NUM_ARGUMENTS_SEARCH = 1;

	// flexi command keywords
	private final String[] FLEXI_KEYWORDS = { " by ", " at ", " to ", " for " };

	// parses the arguments and calls the appropriate create command.
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
			command = new Exit();
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
			command = createDeleteCommand(arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_SEARCH)) {
			command = Command.createObject(commandType, arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_MARKDONE)) {
			command = createMarkDoneCommand(arguments);
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_UNDO)) {
			command = new Undo();
		} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_REDO)) {
			command = new Redo();
		} else {
		    command = new Invalid(MESSAGE_INVALID_COMMAND);
		}
		return command;
	}

	// creates Command for commands with >0 arguments.
	private Command createCommand(String commandType, String commandArguments) {
		String[] arguments = null;
		Command command = null;
		try {
			if (commandType.equalsIgnoreCase(COMMAND_TYPE_ADD)) {
				arguments = getArgumentsAdd(commandArguments);
				command = createAddCommand(arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_DELETE);
				command = createDeleteCommand(arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_MARKDONE)) {
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_DELETE);
				command = createMarkDoneCommand(arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_UPDATE)) {
				arguments = getArgumentsUpdate(commandArguments);
				command = createUpdateCommand(arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_SEARCH)) {
			    //TODO
				arguments = getArguments(commandArguments, NUM_ARGUMENTS_SEARCH);
				command = Command.createObject(commandType, arguments);
			} else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DISPLAY)) {
				arguments = getDisplayArguments(commandArguments);
				command = createDisplayCommand(arguments);
			} else {
			    //if command type not recognized
				command = new Invalid(MESSAGE_INVALID_COMMAND);
			}
		} catch (Exception e) {
		    // catch any exceptions thrown by creation of commands
			command = new Invalid(MESSAGE_INVALID_COMMAND);
		}
		// at this point, arguments should have been pulled out.
		assert(arguments != null);
		assert(command != null);
		return command;
	}
	
	// get arguments for add type - supports flexi commands
    private String[] getArgumentsAdd(String commandArguments) {
    	// check arguments for flexi commands, then trim them.
    	String[] arguments = trimArguments(checkFlexi(commandArguments));
    	return arguments;
    }
    
    private String[] getArgumentsUpdate(String commandArguments){
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
	
	private Command createUpdateCommand(String[] arguments){
		Command cmd = null;
		if (arguments.length <2){
			cmd = new Invalid(String.format(MESSAGE_INVALID_LESS_ARGS, "update"));
			return cmd;
		}
		
		String idx = arguments[0];
		String newName = arguments[1];
		DateTime start;
		DateTime end;
		if (arguments.length == 2) {
			cmd = new Update(idx, newName);
		} else if (arguments.length==3) {
			end = parseDateText(arguments[2]);
			if(end != null) {
				cmd = new Update(idx, newName, end);
			} else {
				cmd = new Invalid(String.format(MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
			}
		} else if (arguments.length==4) {
			start = parseDateText(arguments[2]);
			end = parseDateText(arguments[3]);
			if(start != null && end != null) {
				cmd = new Update(idx, newName, start, end);
			} else {
				if(start == null) {
					cmd = new Invalid(String.format(MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
				}
				if(end == null) {
					cmd = new Invalid(String.format(MESSAGE_INVALID_UPDATE_DATE, arguments[3]));
				}
			}
		} else {
			cmd = new Invalid(MESSAGE_INVALID_UPDATE_NUM_OF_ARGS);
		}
		assert(cmd != null);
		
		return cmd;
	}
	
	//@author A0145668R
    private String[] getDisplayArguments(String commandArguments) {
    	// split arguments and then trim them.
    	String[] arguments = trimArguments(commandArguments.split(ARGUMENT_SPLITTER));
    	return arguments;
    }

    //@author A0145668R
    private Command createDisplayCommand(String[] arguments) {
        Display disp = new Display();
        if (arguments == null) {
            disp.setDefaultFlag(true);
        } else if (arguments.length == 1) {
            if (arguments[0].equals(DISPLAY_FLAG_FLOATING)) {
                disp.setFloatingFlag(true);
            } else if (arguments[0].equals(DISPLAY_FLAG_DUE)) {
                disp.setNotDoneFlag(true);
            } else if (arguments[0].equals(DISPLAY_FLAG_DONE)) {
                disp.setDoneFlag(true);
            } else {
                return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_ARGS,
                        arguments[0]));
            }
        } else if (arguments.length == 2) {
            if (arguments[0].equals(DISPLAY_FLAG_DATE)) {
                DateTime displayDate = parseDateText(arguments[1]);
                if (displayDate != null) {
                    disp.setDateFlag(true);
                    disp.setDisplayDate(displayDate);
                } else {
                    return new Invalid(String.format(
                            MESSAGE_INVALID_DISPLAY_DATE, arguments[1]));
                }
            } else if (arguments[0].equals(DISPLAY_FLAG_INDEX)) {
                try {
                    int eventIndex = Integer.parseInt(arguments[1]);
                    disp.setEventIndex(eventIndex);
                    disp.setIndexFlag(true);
                } catch (NumberFormatException e) {
                    return new Invalid(String.format(
                            MESSAGE_INVALID_DISPLAY_INDEX, arguments[1]));
                }
            } else {
                return new Invalid(String.format(MESSAGE_INVALID_DISPLAY_ARGS,
                        arguments[0]));
            }
        } else {
            return new Invalid(MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS);
        }

        return disp;
    }

    // @@author A0124206
    private Command createDeleteCommand(String[] arguments) {
        return new Delete(arguments);
    }
    // @author A0126989H
    private Command createMarkDoneCommand(String[] arguments) {
        return new Markdone(arguments);
    }
    // @author 
    // feel free to change it haha
    private Command createRepeatCommand(String[] arguments) {
        return new Repeat(arguments[PARAM_POSITION_COMMAND_ARGUMENT]);
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

	private String[] getArguments(String commandArguments, int expectedArguments) throws Exception {
		// split arguments and then trim them.
		String[] arguments = trimArguments(commandArguments.split(ARGUMENT_SPLITTER));
		if (arguments.length < expectedArguments || arguments.length > expectedArguments) {
			throw new Exception();
		}
		return arguments;
	}

    // removes leading and trailing whitespace from arguments
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
