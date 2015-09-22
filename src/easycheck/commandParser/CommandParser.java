package easycheck.commandParser;

/**
 * Command Parser for Easy Check application.
 * Takes in a user command and parses it into a Command type Object for use by Storage.
 *
 */
public class CommandParser {
    private final String COMMAND_SPLITTER = " ";
    private final int COMMAND_ARRAY_LENGTH = 2;
    private final int PARAM_POSITION_COMMAND_TYPE = 0;
    private final int PARAM_POSITION_COMMAND_ARGUMENT = 1;
    //TODO add EXPECTED MIN MAX PARAM for all commands
    //TODO add flexi command keywords
    
    private Command parseCommand(String userCommand) {
        String[] commandArray = splitCommand(userCommand);
        Command command = createCommand(commandArray[PARAM_POSITION_COMMAND_TYPE], commandArray[PARAM_POSITION_COMMAND_ARGUMENT]);
        return command;
    }

    private String[] splitCommand(String userCommand) {
        return userCommand.split(COMMAND_SPLITTER, COMMAND_ARRAY_LENGTH);
    }
    
    private Command createCommand(String commandType, String commandArguments) {
        String[] arguments = getArguments(commandType, commandArguments);
        Command command = new Command(commandType, arguments);
        return null;
    }

    private String[] getArguments(String commandType, String commandArguments) {
        // TODO if not enough arguments, check for Flexi Commands
        return null;
    }

}
