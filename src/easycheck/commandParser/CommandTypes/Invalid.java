package easycheck.commandParser.CommandTypes;

import easycheck.commandParser.Command;

/**
 * @@author A0124206W
 * 
 *          Invalid Command Type represents an invalid command for Easy Check
 *          application.
 */

public class Invalid extends Command {
    private String invalidMessage;

    public Invalid(String invalidMessage) {
        // invalid should always be created with an error message.
        assert (invalidMessage != null);
        this.setInvalidMessage(invalidMessage);
    }

    // returns the error message
    public String getInvalidMessage() {
        return invalidMessage;
    }

    // changes the error message
    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }

    // print the error message
    public String toString() {
        return "@|red " + getInvalidMessage() + "|@";
    }

}
