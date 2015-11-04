 // @@author A0121560W
package easycheck.commandParser.CommandTypes;

import easycheck.commandParser.Command;



 

public class ReadFrom extends Command {
	private String readTarget;
	
	public String getReadTarget() {
		return readTarget;
	}

	private void setReadTarget(String readTarget) {
		this.readTarget = readTarget;
	}

	public ReadFrom(String target) {
		this.setReadTarget(target);
	};
	
}
