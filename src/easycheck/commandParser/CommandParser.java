package easycheck.commandParser;

/**
 * Command Parser for Easy Check application. Takes in a user command and parses
 * it into a Command type Object for use by Storage.
 * 
 * @@author A0124206W
 */

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    // private final String COMMAND_TYPE_SEARCH = "search";
    private final String COMMAND_TYPE_DISPLAY = "display";
    // private final String COMMAND_TYPE_DELETE_DONE = "deleteDone";
    // private final String COMMAND_TYPE_OVERDUE = "overdue ";
    // private final String COMMAND_TYPE_DELETE_TODAY = "deleteToday";
    // private final String COMMAND_TYPE_STORE_LOCATION = "storeLocation";
    // private final String COMMAND_TYPE_NEXT = "next";
    // private final String COMMAND_TYPE_UNDO = "undo";
    private final String COMMAND_TYPE_EXIT = "exit";
    private final String COMMAND_TYPE_INVALID = "invalid";

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
    // private final int NUM_ARGUMENTS_SEARCH = 1;
    // private final int NUM_ARGUMENTS_STORE_LOCATION = 1;

    private final int NUM_ARGUMENTS_DISPLAY = 0;
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
            command = Command.createObject(commandType, arguments);
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_EXIT)) {
            command = Command.createObject(commandType, arguments);
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
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
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
                arguments = getArguments(commandArguments, NUM_ARGUMENTS_DELETE);
            } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_UPDATE)) {
                arguments = getArguments(commandArguments, NUM_ARGUMENTS_UPDATE);
            } else {
                Command command = Command.createObject(COMMAND_TYPE_INVALID,
                        arguments);
                return command;
            }
        } catch (Exception e) {
            Command command = Command.createObject(COMMAND_TYPE_INVALID,
                    arguments);
            return command;
        }
        //at this point, arguments should have been pulled out.
        assert (arguments != null);
        Command command = Command.createObject(commandType, arguments);
        return command;
    }
    // get arguments for add type - supports flexi commands
    private String[] getArgumentsAdd(String commandArguments) {
        //check arguments for flexi commands, then trim them.
        String[] arguments = trimArguments(checkFlexi(commandArguments));
        return arguments;
    }
    
    private String[] getArguments(String commandArguments, int expectedArguments) throws Exception {
        //split arguments and then trim them.
        String[] arguments = trimArguments(commandArguments.split(ARGUMENT_SPLITTER));
        if (arguments.length < expectedArguments
                || arguments.length > expectedArguments) {
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
    
    //replaces the flexi keywords with ',' for parsing.
    private String[] checkFlexi(String commandArguments) {
        for (int i = 0; i < FLEXI_KEYWORDS.length; i++) {
            commandArguments = commandArguments.replace(FLEXI_KEYWORDS[i],
                    ARGUMENT_SPLITTER);
        }
        return commandArguments.split(ARGUMENT_SPLITTER);
    }
}
