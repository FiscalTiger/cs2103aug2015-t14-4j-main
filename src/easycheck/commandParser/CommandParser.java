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
import easycheck.commandParser.CommandTypes.Markdone;
import easycheck.commandParser.CommandTypes.Redo;
import easycheck.commandParser.CommandTypes.Repeat;
import easycheck.commandParser.CommandTypes.Undo;
import easycheck.commandParser.CommandTypes.Update;

public class CommandParser {
    private final String COMMAND_SPLITTER = " ";
    private final String ARGUMENT_SPLITTER = ",";
    private final int COMMAND_ARRAY_LENGTH = 2;
    private final int PARAM_POSITION_COMMAND_TYPE = 0;
    private final int PARAM_POSITION_COMMAND_ARGUMENT = 1;

    private final int DATE_GROUP_ONE_DATE = 1;
    private final int DATE_GROUP_TWO_DATES = 2;
    private final int FIRST_PARSED_DATE_TEXT = 0;
    private final int SECOND_PARSED_DATE_TEXT = 1;

    // command types
    private final String COMMAND_TYPE_ADD = "add";
    private final String COMMAND_TYPE_UPDATE = "update";
    // private final String COMMAND_TYPE_UPDATE_SPECIFIC = "updateSpecific";
    private final String COMMAND_TYPE_DELETE = "delete";
    // private final String COMMAND_TYPE_REPEAT = "repeat";
    private final String COMMAND_TYPE_SEARCH = "search";
    private final String COMMAND_TYPE_DISPLAY = "display";
    private final String COMMAND_TYPE_MARKDONE = "done";
    // private final String COMMAND_TYPE_STORE_LOCATION = "storeLocation";
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
    private static final String MESSAGE_INVALID_ADD_DATES = "Add: Couldn't parse the date text.\n";
    private static final String MESSAGE_INVALID_ADD_NUM_OF_ARGS = "Add: too many arguments\n";
    private static final String MESSAGE_INVALID_ADD_FREQUENCY = "Add: You have used the Repeat flag but entered an invalid frequency.\n";
    
    private static final int ADD_ARGS_NUM_FLOATING = 1;
    private static final int ADD_ARGS_NUM_EVENT_WITH_DATES = 2;
    private static final int ADD_ARGS_NUM_REPEATING_EVENT_WITHOUT_STOP = 3;
    private static final int ADD_ARGS_NUM_FLOATING_EVENT_WITH_STOP = 4;

    private static final String ADD_FLAG_REPEAT = " repeat ";
    private static final String ADD_FLAG_STOP = " stop ";
    private static final boolean REPEATING = true;

    private static final String MESSAGE_INVALID_UPDATE_DATE = "Update: Couldn't parse the date \"%s\"s\n";
    private static final String MESSAGE_INVALID_UPDATE_NUM_OF_ARGS = "Update: too many arguments\n";
    private static final String DISPLAY_FLAG_FLOATING = "f";
    private static final String DISPLAY_FLAG_DONE = "done";
    private static final String DISPLAY_FLAG_OVER_DUE = "overdue";
    private static final String DISPLAY_FLAG_DATE = "d";
    private static final String DISPLAY_FLAG_INDEX = "i";

    // expected number of parameters for all other commands
    private final int NUM_ARGUMENTS_DELETE = 1;
    private final int NUM_ARGUMENTS_SEARCH = 1;

    // Date Time Formats accepted
    private static final String DATE_SPLITTER_SLASH = "/";
    private static final String DATE_SPLITTER_DOT = ".";
    private static final String DATE_SPLITTER_COLON = ":";
    private static final String DATE_INPUT_FORMAT = "MM.dd.yyyy";
    private static final String DATE_AND_TIME_INPUT_FORMAT = "MM.dd.yyyy HH:mm";
    private static final int NUM_CHAR_DATE_INPUT = DATE_INPUT_FORMAT.length();
    private static final int NUM_CHAR_DATE_TIME_INPUT = DATE_AND_TIME_INPUT_FORMAT
            .length();
    private DateTimeFormatter timeFormatter = DateTimeFormat
            .forPattern(DATE_AND_TIME_INPUT_FORMAT);

    // flexi command keywords
    private final String[] FLEXI_KEYWORDS = { " by ", " at ", " on ", " due " };
    private final String FLEXI_KEYWORD_EVENT_SPLITTER = " to ";
    private final String DUMMY_TIME = " 23:59";
    // parses the arguments and calls the appropriate create command.
    public Command parseCommand(String userCommand) {
        String[] commandArray = splitCommand(userCommand);
        if (commandArray.length == 1) {
            Command command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE]);
            return command;
        } else {
            Command command = createCommand(
                    commandArray[PARAM_POSITION_COMMAND_TYPE],
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
                arguments = getArguments(commandArguments, NUM_ARGUMENTS_SEARCH);
                command = Command.createObject(commandType, arguments);
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DISPLAY)) {
                arguments = getDisplayArguments(commandArguments);
                command = createDisplayCommand(arguments);
            } else {
                // if command type not recognized
                command = new Invalid(MESSAGE_INVALID_COMMAND);
            }
        } catch (Exception e) {
            // catch any exceptions thrown by creation of commands
            command = new Invalid(MESSAGE_INVALID_COMMAND);
        }
        // at this point, arguments should have been pulled out.
        assert (arguments != null);
        assert (command != null);
        return command;
    }

    // get arguments for add type - supports flexi commands
    private String[] getArgumentsAdd(String commandArguments) {
    	commandArguments = commandArguments.replace(ADD_FLAG_REPEAT, ARGUMENT_SPLITTER);
    	commandArguments = commandArguments.replace(ADD_FLAG_STOP, ARGUMENT_SPLITTER);
        // check arguments for flexi commands, then trim them.
        String[] arguments = trimArguments(checkFlexi(commandArguments));
        return arguments;
    }

    private String[] getArgumentsUpdate(String commandArguments) {
        String[] arguments = trimArguments(commandArguments
                .split(ARGUMENT_SPLITTER));
        return arguments;
    }

    // @author A0145668R
    private Command createAddCommand(String[] arguments) {
        Add cmd = null;
        String taskName;
        DateTime start;
        DateTime end;
        DateTime[] parsedDates;
        try {

            if (arguments.length == ADD_ARGS_NUM_FLOATING) {
            	taskName = arguments[0];
                cmd = new Add(taskName);
            } else if (arguments.length == ADD_ARGS_NUM_EVENT_WITH_DATES) {
            	taskName = arguments[0];
                parsedDates = parseDateText(arguments[1]);
                if (parsedDates.length == 1) {
        		    end = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    if (end != null) {
        		        cmd = new Add(taskName, end);
        		    } else {
        		        return new Invalid(String.format(
        		                MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		    }
        		} else if (parsedDates.length == 2) {
        		    start = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    end = parsedDates[SECOND_PARSED_DATE_TEXT];
        		    if (start != null && end != null) {
        		        cmd = new Add(taskName, start, end);
        		    } else {
       		            return new Invalid(String.format(
       		                    MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		       
        		    }
        		} else {
        			return new Invalid(MESSAGE_INVALID_ADD_DATES);
        		}
            } else if (arguments.length == ADD_ARGS_NUM_REPEATING_EVENT_WITHOUT_STOP) {
            	String frequency = arguments[2];
            	taskName = arguments[0];
                parsedDates = parseDateText(arguments[1]);
                if (parsedDates.length == 1) {
        		    end = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    if (end != null) {
        		        cmd = new Add(taskName, end);
        		    } else {
        		        return new Invalid(String.format(
        		                MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		    }
        		} else if (parsedDates.length == 2) {
        		    start = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    end = parsedDates[SECOND_PARSED_DATE_TEXT];
        		    if (start != null && end != null) {
        		        cmd = new Add(taskName, start, end);
        		    } else {
       		            return new Invalid(String.format(
       		                    MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		    }
        		} else {
        			return new Invalid(MESSAGE_INVALID_ADD_DATES);
        		}
                
                cmd.setRepeating(REPEATING);
                
            	if(Add.isValidFrequency(frequency)) {
            		cmd.setFrequency(frequency);
            	} else {
	    			return new Invalid(MESSAGE_INVALID_ADD_FREQUENCY);
            	}
        	} else if (arguments.length == 4) {
        		String frequency = arguments[2];
        		DateTime stopDate = parseDateText(arguments[3])[FIRST_PARSED_DATE_TEXT];
        		if(stopDate ==  null) {
        			return new Invalid(String.format(MESSAGE_INVALID_ADD_DATE, arguments[3]));
        		}
            	taskName = arguments[0];
                parsedDates = parseDateText(arguments[1]);
                if (parsedDates.length == 1) {
        		    end = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    if (end != null) {
        		        cmd = new Add(taskName, end);
        		    } else {
        		        return new Invalid(String.format(
        		                MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		    }
        		} else if (parsedDates.length == 2) {
        		    start = parsedDates[FIRST_PARSED_DATE_TEXT];
        		    end = parsedDates[SECOND_PARSED_DATE_TEXT];
        		    if (start != null && end != null) {
        		        cmd = new Add(taskName, start, end);
        		    } else {
       		            return new Invalid(String.format(
       		                    MESSAGE_INVALID_ADD_DATE, arguments[1]));
        		    }
        		} else {
        			return new Invalid(MESSAGE_INVALID_ADD_DATES);
        		}
                
                cmd.setRepeating(REPEATING);
                cmd.setStopDate(stopDate);
                
                if(Add.isValidFrequency(frequency)) {
            		cmd.setFrequency(frequency);
            	} else {
	    			return new Invalid(MESSAGE_INVALID_ADD_FREQUENCY);
            	}
        	}
        } catch (Exception e) {
            return new Invalid(String.format(
                    MESSAGE_INVALID_ADD_DATE, arguments[1]));
        }
        assert (cmd != null);

        return cmd;
    }

    private Command createUpdateCommand(String[] arguments) {
        Command cmd = null;
        if (arguments.length < 2) {
            cmd = new Invalid(
                    String.format(MESSAGE_INVALID_LESS_ARGS, "update"));
            return cmd;
        }

        String idx = arguments[0];
        String newName = arguments[1];
        DateTime start;
        DateTime end;

        if (arguments.length == 2) {
            cmd = new Update(idx, newName);
        } else if (arguments.length == 3) {
            try {
                end = parseDateText(arguments[2])[FIRST_PARSED_DATE_TEXT];
                if (end != null) {
                    cmd = new Update(idx, newName, end);
                } else {
                    cmd = new Invalid(String.format(
                            MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                }
            } catch (Exception e) {
                cmd = new Invalid(MESSAGE_INVALID_UPDATE_NUM_OF_ARGS);
            }

        } else if (arguments.length == ADD_ARGS_NUM_FLOATING_EVENT_WITH_STOP) {
            try {
                start = parseDateText(arguments[2])[FIRST_PARSED_DATE_TEXT];
                end = parseDateText(arguments[3])[SECOND_PARSED_DATE_TEXT];

                if (start != null && end != null) {
                    cmd = new Update(idx, newName, start, end);
                } else {
                    if (start == null) {
                        cmd = new Invalid(String.format(
                                MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                    }
                    if (end == null) {
                        cmd = new Invalid(String.format(
                                MESSAGE_INVALID_UPDATE_DATE, arguments[3]));
                    }
                }
            } catch (Exception e) {
                cmd = new Invalid(MESSAGE_INVALID_UPDATE_NUM_OF_ARGS);
            }
        } else {
            cmd = new Invalid(MESSAGE_INVALID_DISPLAY_DATE);
        }
        assert (cmd != null);

        return cmd;
    }

    // @@author A0145668R
    private String[] getDisplayArguments(String commandArguments) {
        // split arguments and then trim them.
        String[] arguments = trimArguments(commandArguments
                .split(ARGUMENT_SPLITTER));
        return arguments;
    }

    // @@author A0145668R
    private Command createDisplayCommand(String[] arguments) {
        Display disp = new Display();
        try {
            if (arguments == null) {
                disp.setDefaultFlag(true);
            } else if (arguments.length == 1) {
                if (arguments[0].equals(DISPLAY_FLAG_FLOATING)) {
                    disp.setFloatingFlag(true);
                } else if (arguments[0].equals(DISPLAY_FLAG_OVER_DUE)) {
                    disp.setOverDueFlag(true);
                } else if (arguments[0].equals(DISPLAY_FLAG_DONE)) {
                    disp.setDoneFlag(true);
                } else {
                    return new Invalid(String.format(
                            MESSAGE_INVALID_DISPLAY_ARGS, arguments[0]));
                }
            } else if (arguments.length == 2) {
                if (arguments[0].equals(DISPLAY_FLAG_DATE)) {
                    DateTime displayDate = parseDateText(arguments[1])[FIRST_PARSED_DATE_TEXT];
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
                    return new Invalid(String.format(
                            MESSAGE_INVALID_DISPLAY_ARGS, arguments[0]));
                }
            } else {
                return new Invalid(MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS);
            }
        } catch (Exception e) {
            return new Invalid(MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS);
        }
        return disp;
    }

    // @@author A0124206W
    private Command createDeleteCommand(String[] arguments) {
        return new Delete(arguments);
    }

    // @@author A0126989H
    private Command createMarkDoneCommand(String[] arguments) {
        return new Markdone(arguments);
    }

    // @@author
    // feel free to change it haha
    private Command createRepeatCommand(String[] arguments) {
        return new Repeat(arguments[PARAM_POSITION_COMMAND_ARGUMENT]);
    }

    // @@author A0145668R
    public DateTime[] parseDateText(String dateString) throws Exception {
        isValidExplicitDate(dateString);
        Parser dateParser = new Parser();
        DateTime[] parsedDates;
        List<DateGroup> dateGroups = dateParser.parse(dateString);
        if (dateGroups.size() == 1) {
            DateGroup dateGroup = dateGroups.get(0);
            if (dateGroup.getDates().size() == 1) {
                parsedDates = new DateTime[DATE_GROUP_ONE_DATE];
                parsedDates[0] = new DateTime(dateGroup.getDates().get(0));
            } else if (dateGroup.getDates().size() == 2) {
                parsedDates = new DateTime[DATE_GROUP_TWO_DATES];
                parsedDates[0] = new DateTime(dateGroup.getDates().get(0));
                parsedDates[1] = new DateTime(dateGroup.getDates().get(1));
            } else {
                parsedDates = new DateTime[DATE_GROUP_ONE_DATE];
                parsedDates[0] = null;
            }

        } else {
            parsedDates = new DateTime[DATE_GROUP_ONE_DATE];
            parsedDates[0] = null;
        }
        return parsedDates;
    }

    private String[] getArguments(String commandArguments, int expectedArguments)
            throws Exception {
        // split arguments and then trim them.
        String[] arguments = trimArguments(commandArguments
                .split(ARGUMENT_SPLITTER));
        if (arguments.length < expectedArguments
                || arguments.length > expectedArguments) {
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
            commandArguments = commandArguments.replace(FLEXI_KEYWORDS[i],
                    ARGUMENT_SPLITTER);
        }
        return commandArguments.split(ARGUMENT_SPLITTER);
    }

    // checks if explicit dates (MM/dd/yyyy and MM/dd/yyyy HH:mm) are valid.
    private boolean isValidExplicitDate(String dateString) throws Exception {
        dateString = dateString.replace(DATE_SPLITTER_SLASH, DATE_SPLITTER_DOT);
        if (!dateString.contains(DATE_SPLITTER_DOT)) {
            // is not an explicit date
            return true;
        }
        // check if date has time and if there is exactly one date
        if (dateString.contains(DATE_SPLITTER_COLON)
                && (dateString.length() <= NUM_CHAR_DATE_TIME_INPUT)) {
            timeFormatter.parseDateTime(dateString);
        } else if (dateString.length() <= NUM_CHAR_DATE_INPUT) {
            timeFormatter.parseDateTime(dateString + DUMMY_TIME);
        } else {
            // there is more than 1 date (should be 2)
            String[] dateStringArray = splitDates(dateString);
            if (dateStringArray.length != 2) {
                throw new Exception();
            } else {
                isValidExplicitDate(dateStringArray[FIRST_PARSED_DATE_TEXT]);
                isValidExplicitDate(dateStringArray[SECOND_PARSED_DATE_TEXT]);
            }
        }
        return true;
    }
    
    // splits and trims date strings in the form of "date to date"
    private String[] splitDates(String dateString) {
        String[] dateStrings = trimArguments(dateString
                .split(FLEXI_KEYWORD_EVENT_SPLITTER));
        return dateStrings;
    }
}
