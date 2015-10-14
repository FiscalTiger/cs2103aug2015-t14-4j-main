package easycheck.commandParser;

/**
 * Command Parser for Easy Check application. Takes in a user command and parses
 * it into a Command type Object for use by Storage.
 * 
 * @@author A0124206W
 */
public class CommandParser {
    private final String COMMAND_SPLITTER = " ";
    private final String ARGUMENT_SPLITTER = ",";
    private final int COMMAND_ARRAY_LENGTH = 2;
    private final int PARAM_POSITION_COMMAND_TYPE = 0;
    private final int PARAM_POSITION_COMMAND_ARGUMENT = 1;

    // display messages
    private final String MESSAGE_UNRECOGNIZE = "Unrecognized command input!";
    private final String MESSAGE_INVALID_ARGUMENTS = "Invalid arguments!";

    // command types
    private final String COMMAND_TYPE_ADD = "add";
    private final String COMMAND_TYPE_ADD_EVENT = "addEvent";
    private final String COMMAND_TYPE_UPDATE = "update";
    private final String COMMAND_TYPE_DELETE = "delete";
    
    // private final String COMMAND_TYPE_ADD_REPEAT = "addRepeat";
    // private final String COMMAND_TYPE_COMPLETE = "complete";
    // private final String COMMAND_TYPE_LABEL = "label";
    // private final String COMMAND_TYPE_REMIND = "remind";
    // private final String COMMAND_TYPE_SEARCH = "search";
    private final String COMMAND_TYPE_DISPLAY = "display";
    // private final String COMMAND_TYPE_ARCHIVE = "archive";
    // private final String COMMAND_TYPE_ARCHIVE_DONE = "archiveDone";
    // private final String COMMAND_TYPE_DELETE_DONE = "deleteDone";
    // private final String COMMAND_TYPE_OVERDUE = "overdue ";
    // private final String COMMAND_TYPE_DELETE_TODAY = "deleteToday";
    // private final String COMMAND_TYPE_STORE_LOCATION = "storeLocation";
    // private final String COMMAND_TYPE_NEXT = "next";
    // private final String COMMAND_TYPE_UNDO = "undo";
    private final String COMMAND_TYPE_EXIT = "exit";

    // expected number of parameters for all commands
    private final int[] ARGUMENT_NUM_MIN_MAX_ADD = { 1, 2 };
    private final int[] ARGUMENT_NUM_MIN_MAX_ADD_EVENT = { 2, 3 };
    private final int[] ARGUMENT_NUM_MIN_MAX_UPDATE = { 2, 3 };
    private final int[] ARGUMENT_NUM_MIN_MAX_DELETE = { 1, 1 };
    
    // private final int[] ARGUMENT_NUM_MIN_MAX_ADD_REPEAT = { 1, 2 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_COMPLETE = { 1, 1 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_LABEL = { 2, 2 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_REMIND = { 2, 2 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_SEARCH = { 1, 1 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_ARCHIVE = { 1, 1 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_STORE_LOCATION = { 1, 1 };
    
    private final int[] ARGUMENT_NUM_MIN_MAX_DISPLAY = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_ARCHIVE_DONE = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_DELETE_DONE = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_OVERDUE = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_DELETE_TODAY = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_NEXT = { 0, 0 };
    // private final int[] ARGUMENT_NUM_MIN_MAX_UNDO = { 0, 0 };
    private final int[] ARGUMENT_NUM_MIN_MAX_EXIT = { 0, 0 };

    // flexi command keywords
    private final String[] FLEXI_KEYWORDS = {" by ", " to ", " until ", " for "};

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

    private Command createCommand(String commandType) {
        String[] arguments = null;
        if (commandType.equalsIgnoreCase(COMMAND_TYPE_DISPLAY)) {
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_EXIT)) {
        } else {
            System.err.println(MESSAGE_UNRECOGNIZE);
        }
        Command command = Command.createObject(commandType,arguments);
        return command;
        
    }

    private String[] splitCommand(String userCommand) {
        return userCommand.split(COMMAND_SPLITTER, COMMAND_ARRAY_LENGTH);
    }

    private Command createCommand(String commandType, String commandArguments) {
        String[] arguments = null;
        if (commandType.equalsIgnoreCase(COMMAND_TYPE_ADD)) {
            arguments = getArguments(commandArguments, ARGUMENT_NUM_MIN_MAX_ADD);
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_ADD_EVENT)) {
            arguments = getArguments(commandArguments,
                    ARGUMENT_NUM_MIN_MAX_ADD_EVENT);
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_DELETE)) {
            arguments = getArguments(commandArguments,
                    ARGUMENT_NUM_MIN_MAX_DELETE);
        } else if (commandType.equalsIgnoreCase(COMMAND_TYPE_UPDATE)) {
            arguments = getArguments(commandArguments,
                    ARGUMENT_NUM_MIN_MAX_UPDATE);
        } else {
            System.err.println(MESSAGE_UNRECOGNIZE);
        }
        assert (arguments != null);
        Command command = Command.createObject(commandType,arguments);
        return command;
    }

    private String[] getArguments(String commandArguments,
            int[] expectedArguments) {
        
        String[] arguments = checkFlexi(commandArguments);
        if (arguments.length < expectedArguments[0]) {
            throw new Error(MESSAGE_INVALID_ARGUMENTS);
        }
        if (arguments.length >= expectedArguments[0]
                && arguments.length <= expectedArguments[1]) {
            // correct number of arguments
            arguments = trimArguments(arguments, arguments.length);
        } else if (arguments.length > expectedArguments[1]) {
            //more than the maximum amount of arguments, truncate extra
            String[] truncatedArguments = trimArguments(arguments, expectedArguments[1]);
            return truncatedArguments;
        }

        return arguments;
    }

    private String[] trimArguments(String[] arguments, int expectedArguments) {
        String[] trimmedArguments = new String[expectedArguments];
        for (int i = 0; i < expectedArguments; i++) {
            trimmedArguments[i] = arguments[i].trim();
        }
        return trimmedArguments;
    }

    private String[] checkFlexi(String commandArguments) {
        for (int i = 0; i < FLEXI_KEYWORDS.length; i++) {
            commandArguments = commandArguments.replace(FLEXI_KEYWORDS[i], ARGUMENT_SPLITTER);
        }
        return commandArguments.split(ARGUMENT_SPLITTER);
    }
}
