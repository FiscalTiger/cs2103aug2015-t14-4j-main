package easycheck.commandParser;

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
import easycheck.commandParser.CommandTypes.ReadFrom;
import easycheck.commandParser.CommandTypes.Redo;
import easycheck.commandParser.CommandTypes.Repeat;
import easycheck.commandParser.CommandTypes.SaveAt;
import easycheck.commandParser.CommandTypes.Undo;
import easycheck.commandParser.CommandTypes.Update;

/**
 * Command Parser for Easy Check application. Takes in a user command and parses
 * it into a Command type Object for use by Logic.
 * 
 * @@author A0124206W
 */
public class CommandParser {
    private static final String COMMAND_SPLITTER = " ";
    private static final String ARGUMENT_SPLITTER = ",";
    private static final int COMMAND_ARRAY_LENGTH = 2;

    // locations for the arguments in command arrays
    private static final int PARAM_POSITION_COMMAND_TYPE = 0;
    private static final int PARAM_POSITION_COMMAND_ARGUMENT = 1;

    // number of expected arguments in date arrays
    private static final int DATE_GROUP_ONE_DATE = 1;
    private static final int DATE_GROUP_TWO_DATE = 2;
    // locations for the arguments in parsed date arrays
    private static final int PARSED_DATE_TEXT_FIRST = 0;
    private static final int PARSED_DATE_TEXT_SECOND = 1;

    // command types supported
    private static final String COMMAND_TYPE_ADD = "add";
    private static final String COMMAND_TYPE_UPDATE = "update";
    private static final String COMMAND_TYPE_DELETE = "delete";
    private static final String COMMAND_TYPE_REPEAT = "repeat";
    private static final String COMMAND_TYPE_SEARCH = "search";
    private static final String COMMAND_TYPE_DISPLAY = "display";
    private static final String COMMAND_TYPE_MARKDONE = "done";
    private static final String COMMAND_TYPE_UNDO = "undo";
    private static final String COMMAND_TYPE_REDO = "redo";
    private static final String COMMAND_TYPE_EXIT = "exit";
    private static final String COMMAND_TYPE_SAVE_AT = "save_at";
    private static final String COMMAND_TYPE_READ_FROM = "read_from";

    // display messages for invalid commands
    private static final String MESSAGE_INVALID_COMMAND = "Invalid Command\n";
    private static final String MESSAGE_INVALID_LESS_ARGS = "Too little arguments for command type \"%s\" \n";
    // @@author
    private static final String MESSAGE_INVALID_DISPLAY_ARGS = "Display: Invalid flag \"%s\"\n";
    private static final String MESSAGE_INVALID_DISPLAY_DATE = "Display: Couldn't parse the date \"%s\"\n";
    private static final String MESSAGE_INVALID_DISPLAY_INDEX = "Display: Invalid index \"%s\"\n";
    private static final String MESSAGE_INVALID_DISPLAY_NUM_OF_ARGS = "Display: too many arguments\n";

    private static final String MESSAGE_INVALID_ADD_DATE = "Add: Couldn't parse the date \"%s\"s\n";
    private static final String MESSAGE_INVALID_ADD_DATES = "Add: Couldn't parse the date text.\n";
    private static final String MESSAGE_INVALID_ADD_FREQUENCY = "Add: You have used the Repeat flag but entered an invalid frequency.\n";

    private static final String MESSAGE_INVALID_REPEAT_NUM_OF_ARGS = "Repeat: too many arguments\n";
    private static final String MESSAGE_INVALID_REPEAT_FREQUENCY = "Repeat: You have an invalid frequency.\n";
    private static final String MESSAGE_INVALID_REPEAT_DATE = "Display: Couldn't parse the date \"%s\"\n";
    private static final int REPEAT_ARGS_EVENT = 0;
    private static final int REPEAT_ARGS_FREQUENCY = 1;
    private static final int REPEAT_ARGS_STOP = 2;

    private static final int ADD_ARGS_NUM_FLOATING = 1;
    private static final int ADD_ARGS_NUM_EVENT_WITH_DATES = 2;
    private static final int ADD_ARGS_NUM_REPEATING_EVENT_WITHOUT_STOP = 3;
    private static final int ADD_ARGS_NUM_FLOATING_EVENT_WITH_STOP = 4;

    private static final String ADD_FLAG_REPEAT = " repeat ";
    private static final String ADD_FLAG_STOP = " stop ";
    private static final boolean REPEATING = true;

    private static final String MESSAGE_INVALID_UPDATE_DATE = "Update: Couldn't parse the date \"%s\"s\n";
    private static final String MESSAGE_INVALID_UPDATE_NUM_OF_ARGS = "Update: too many arguments\n";
    private static final String DISPLAY_FLAG_ALL = "all";
    private static final String DISPLAY_FLAG_FLOATING = "f";
    private static final String DISPLAY_FLAG_DONE = "done";
    private static final String DISPLAY_FLAG_OVER_DUE = "overdue";
    private static final String DISPLAY_FLAG_DATE = "d";
    private static final String DISPLAY_FLAG_INDEX = "i";

    // expected number of parameters for all other commands
    private static final int NUM_ARGUMENTS_EMPTY_COMMAND_ARRAY = 1;
    private static final int NUM_ARGUMENTS_DELETE = 1;
    private static final int NUM_ARGUMENTS_SEARCH = 1;
    private static final int NUM_ARGUMENTS_SAVE_AT = 1;
    private static final int NUM_ARGUMENTS_READ_FROM = 1;
    // @@author A0124206W
    // Date Time Formats accepted for parsing and validation
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

    // @@author A0121560W
    // Update command parsing
    private static final String UPDATE_COMMAND_TYPE_START = "start";
    private static final String UPDATE_COMMAND_TYPE_END = "end";
    private static final String UPDATE_COMMAND_TYPE_NAME = "name";
    private static final String UPDATE_COMMAND_TYPE_TYPE = "type";
    private static final String MESSAGE_UPDATE_INVALID_ARGS = "Invalid number of arguments for update command";

    // flexi command keywords
    private static final String[] FLEXI_KEYWORDS = { " by ", " at ", " on ",
            " due ", " for " };
    private static final String FLEXI_KEYWORD_EVENT_SPLITTER = " to ";
    private static final String DUMMY_TIME = " 23:59";

    // @@author A0124206W
    // parses the arguments and calls the appropriate create command.
    public Command parseCommand(String userCommand) {
        Command command;
        String[] commandArray = splitCommand(userCommand);
        if (commandArray.length == NUM_ARGUMENTS_EMPTY_COMMAND_ARRAY) {
            // command array has no arguments
            command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE]);

        } else {
            // command array has at least 1 argument
            command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE],
                    commandArray[PARAM_POSITION_COMMAND_ARGUMENT]);
        }
        // at this point, a command should have been created.
        assert (command instanceof Command);
        return command;
    }

    // @@author A0124206W
    // splits the user-input string into an array of command type and arguments
    private String[] splitCommand(String userCommand) {
        String[] splitArray = userCommand.split(COMMAND_SPLITTER,
                COMMAND_ARRAY_LENGTH);
        return splitArray;
    }

    // @@author A0124206W
    // creates a command type object for user commands with no arguments.
    private Command createCommand(String commandType) {
        String[] arguments = null;
        Command command;
        try {
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
        } catch (Exception e) {
            // catch any exceptions thrown by creation of commands
            // and handle by creating an invalid command
            command = new Invalid(MESSAGE_INVALID_COMMAND);
        }
        // at this point, command should already have been created.
        assert (command != null);
        return command;
    }

    // @@author A0124206W
    // creates Command for commands with >0 arguments.
    private Command createCommand(String commandType, String commandArguments) {
        String[] arguments = null;
        Command command = null;
        try {
            if (commandType.equalsIgnoreCase(COMMAND_TYPE_ADD)) {
                arguments = getArgumentsAdd(commandArguments);
                command = createAddCommand(arguments);
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_REPEAT)) {
                arguments = getArgumentsRepeat(commandArguments);
                command = createRepeatCommand(arguments);
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
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_SAVE_AT)) {
                arguments = getArguments(commandArguments,
                        NUM_ARGUMENTS_SAVE_AT);
                command = createSaveAtCommand(arguments);
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_READ_FROM)) {
                arguments = getArguments(commandArguments,
                        NUM_ARGUMENTS_READ_FROM);
                command = createReadFromCommand(arguments);
            } else {
                // if command type not recognized
                return new Invalid(MESSAGE_INVALID_COMMAND);
            }
        } catch (Exception e) {
            // catch any exceptions thrown by creation of commands
            // and handle by creating an invalid command
            return new Invalid(MESSAGE_INVALID_COMMAND);
        }
        // arguments should have been parsed
        assert (arguments != null);
        // command should have been created
        assert (command != null);
        return command;
    }

    // @@author
    // get arguments for add type - supports flexi commands
    private String[] getArgumentsAdd(String commandArguments) {
        commandArguments = commandArguments.replace(ADD_FLAG_REPEAT,
                ARGUMENT_SPLITTER);
        commandArguments = commandArguments.replace(ADD_FLAG_STOP,
                ARGUMENT_SPLITTER);
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
                    end = parsedDates[PARSED_DATE_TEXT_FIRST];
                    if (end != null) {
                        cmd = new Add(taskName, end);
                    } else {
                        return new Invalid(String.format(
                                MESSAGE_INVALID_ADD_DATE, arguments[1]));
                    }
                } else if (parsedDates.length == 2) {
                    start = parsedDates[PARSED_DATE_TEXT_FIRST];
                    end = parsedDates[PARSED_DATE_TEXT_SECOND];
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
                    end = parsedDates[PARSED_DATE_TEXT_FIRST];
                    if (end != null) {
                        cmd = new Add(taskName, end);
                    } else {
                        return new Invalid(String.format(
                                MESSAGE_INVALID_ADD_DATE, arguments[1]));
                    }
                } else if (parsedDates.length == 2) {
                    start = parsedDates[PARSED_DATE_TEXT_FIRST];
                    end = parsedDates[PARSED_DATE_TEXT_SECOND];
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

                if (Add.isValidFrequency(frequency)) {
                    cmd.setFrequency(frequency);
                } else {
                    return new Invalid(MESSAGE_INVALID_ADD_FREQUENCY);
                }
            } else if (arguments.length == 4) {
                String frequency = arguments[2];
                DateTime stopDate = parseDateText(arguments[3])[PARSED_DATE_TEXT_FIRST];
                if (stopDate == null) {
                    return new Invalid(String.format(MESSAGE_INVALID_ADD_DATE,
                            arguments[3]));
                }
                taskName = arguments[0];
                parsedDates = parseDateText(arguments[1]);
                if (parsedDates.length == 1) {
                    end = parsedDates[PARSED_DATE_TEXT_FIRST];
                    if (end != null) {
                        cmd = new Add(taskName, end);
                    } else {
                        return new Invalid(String.format(
                                MESSAGE_INVALID_ADD_DATE, arguments[1]));
                    }
                } else if (parsedDates.length == 2) {
                    start = parsedDates[PARSED_DATE_TEXT_FIRST];
                    end = parsedDates[PARSED_DATE_TEXT_SECOND];
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

                if (Add.isValidFrequency(frequency)) {
                    cmd.setFrequency(frequency);
                } else {
                    return new Invalid(MESSAGE_INVALID_ADD_FREQUENCY);
                }
            }
        } catch (Exception e) {
            return new Invalid(String.format(MESSAGE_INVALID_ADD_DATE,
                    arguments[1]));
        }
        assert (cmd != null);

        return cmd;
    }

    private String[] getArgumentsRepeat(String commandArguments) {
        // split arguments and then trim them.
        String[] arguments = trimArguments(commandArguments
                .split(ARGUMENT_SPLITTER));
        return arguments;
    }

    // @@author A0121560W
    private Command createUpdateCommand(String[] arguments) {
        Command cmd = null;
        String idx = null;
        String newName = null;
        DateTime start;
        DateTime end;
        String type = null;

        if (arguments.length < 2) {
            cmd = new Invalid(
                    String.format(MESSAGE_INVALID_LESS_ARGS, "update"));
            return cmd;
        }

        idx = arguments[0];

        if (arguments[1].equalsIgnoreCase(UPDATE_COMMAND_TYPE_END)
                || arguments[1].equalsIgnoreCase(UPDATE_COMMAND_TYPE_START)) {
            type = arguments[1];
            if (arguments.length != 3) {
                cmd = new Invalid(MESSAGE_UPDATE_INVALID_ARGS);
                return cmd;
            } else {
                if (arguments[1].equalsIgnoreCase(UPDATE_COMMAND_TYPE_END)) {
                    try {
                        end = parseDateText(arguments[2])[PARSED_DATE_TEXT_FIRST];
                        if (end != null) {
                            cmd = new Update(idx, end, type);
                        } else {
                            cmd = new Invalid(String.format(
                                    MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                        }
                    } catch (Exception e) {
                        cmd = new Invalid(String.format(
                                MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                    }
                } else {
                    try {
                        start = parseDateText(arguments[2])[PARSED_DATE_TEXT_FIRST];
                        if (start != null) {
                            cmd = new Update(idx, start, type);
                        } else {
                            cmd = new Invalid(String.format(
                                    MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                        }
                    } catch (Exception e) {
                        cmd = new Invalid(String.format(
                                MESSAGE_INVALID_UPDATE_DATE, arguments[2]));
                    }
                }
            }
        } else if (arguments[1].equalsIgnoreCase(UPDATE_COMMAND_TYPE_NAME)
                || arguments[1].equalsIgnoreCase(UPDATE_COMMAND_TYPE_TYPE)) {
            type = arguments[1];
            if (arguments.length != 3) {
                cmd = new Invalid(MESSAGE_UPDATE_INVALID_ARGS);
                return cmd;
            }
            newName = arguments[2];
            cmd = new Update(idx, type, newName);
        } else {
            newName = arguments[1];

            if (arguments.length == 2) {
                cmd = new Update(idx, newName);
            } else if (arguments.length == 3) {
                try {
                    end = parseDateText(arguments[2])[PARSED_DATE_TEXT_FIRST];
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
                    start = parseDateText(arguments[2])[PARSED_DATE_TEXT_FIRST];
                    end = parseDateText(arguments[3])[PARSED_DATE_TEXT_SECOND];

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
                } else if (arguments[0].equals(DISPLAY_FLAG_ALL)) {
                    disp.setAllFlag(true);
                } else {
                    return new Invalid(String.format(
                            MESSAGE_INVALID_DISPLAY_ARGS, arguments[0]));
                }
            } else if (arguments.length == 2) {
                if (arguments[0].equals(DISPLAY_FLAG_DATE)) {
                    DateTime displayDate = parseDateText(arguments[1])[PARSED_DATE_TEXT_FIRST];
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
    // Creates a Delete command with the user input arguments.
    private Command createDeleteCommand(String[] arguments) {
        return new Delete(arguments);
    }

    // @@author A0126989H
    private Command createMarkDoneCommand(String[] arguments) {
        return new Markdone(arguments);
    }

    // @@author A0145668R
    private Command createRepeatCommand(String[] arguments) {
        if (!Repeat.isValidFrequency(arguments[REPEAT_ARGS_FREQUENCY])) {
            return new Invalid(MESSAGE_INVALID_REPEAT_FREQUENCY);
        }

        if (arguments.length == 2) {
            return new Repeat(arguments[REPEAT_ARGS_EVENT],
                    arguments[REPEAT_ARGS_FREQUENCY]);
        } else if (arguments.length == 3) {
            DateTime stop;
            try {
                stop = parseDateText(arguments[REPEAT_ARGS_STOP])[PARSED_DATE_TEXT_FIRST];
                return new Repeat(arguments[REPEAT_ARGS_EVENT],
                        arguments[REPEAT_ARGS_FREQUENCY], stop);
            } catch (Exception e) {
                return new Invalid(String.format(MESSAGE_INVALID_REPEAT_DATE,
                        arguments[REPEAT_ARGS_STOP]));
            }

        } else {
            return new Invalid(MESSAGE_INVALID_REPEAT_NUM_OF_ARGS);
        }
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
                parsedDates = new DateTime[DATE_GROUP_TWO_DATE];
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

    // @@author A0124206W
    // gets and trims arguments from a user-inputed string
    // throws exception if insufficient or too many arguments
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

    // @@author A0124206W
    // removes leading and trailing whitespace from arguments
    private String[] trimArguments(String[] arguments) {
        String[] trimmedArguments = new String[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            trimmedArguments[i] = arguments[i].trim();
        }
        return trimmedArguments;
    }

    // @@author A0124206W
    // replaces the flexi keywords with ',' for argument parsing.
    private String[] checkFlexi(String commandArguments) {
        for (int i = 0; i < FLEXI_KEYWORDS.length; i++) {
            commandArguments = commandArguments.replace(FLEXI_KEYWORDS[i],
                    ARGUMENT_SPLITTER);
        }
        return commandArguments.split(ARGUMENT_SPLITTER);
    }

    // @@author A0124206W
    // checks if explicit mentioned dates in the form of MM/dd/yyyy and
    // MM/dd/yyyy HH:mm are valid.
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
            if (dateStringArray.length != DATE_GROUP_TWO_DATE) {
                throw new Exception();
            } else {
                isValidExplicitDate(dateStringArray[PARSED_DATE_TEXT_FIRST]);
                isValidExplicitDate(dateStringArray[PARSED_DATE_TEXT_SECOND]);
            }
        }
        return true;
    }

    // @@author A0124206W
    // splits and trims dates from user-inputed strings in the form of
    // "start date TO end date" to two separate dates strings in an array
    private String[] splitDates(String dateString) {
        String[] dateStrings = trimArguments(dateString
                .split(FLEXI_KEYWORD_EVENT_SPLITTER));
        return dateStrings;
    }

    // @@author A0121560W
    // only one argument expected, handled by getArguments
    private Command createSaveAtCommand(String[] target) {
        return new SaveAt(target[0]);
    }

    private Command createReadFromCommand(String[] target) {
        return new ReadFrom(target[0]);
    }

}
