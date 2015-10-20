package easycheck.commandParser.CommandTypes;

import easycheck.commandParser.Command;

/**
 * Invalid Command Type represents an invalid command for Easy Check
 * application. (Error is handled by command executor, not parser.)
 * 
 * @@author A0124206W
 */

public class Invalid extends Command {

    public Invalid(String commandType, String[] arguments) {
        super(commandType, arguments);
    }

}
