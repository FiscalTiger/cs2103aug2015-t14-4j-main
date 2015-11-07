package easycheck.commandParser.CommandTypes;

import easycheck.commandParser.Command;

/**
 * @@author A0124206W 
 * 
 * Invalid Command Type represents an invalid command for
 * Easy Check application.
 */

public class Invalid extends Command {
	private String invalidMessage;
	
    public Invalid(String invalidMessage) {
    	this.setInvalidMessage(invalidMessage);
    }

	public String getInvalidMessage() {
		return invalidMessage;
	}

	public void setInvalidMessage(String invalidMessage) {
		this.invalidMessage = invalidMessage;
	}
	
	public String toString() {
		return "@|red " + getInvalidMessage() +"|@";
	}

}
