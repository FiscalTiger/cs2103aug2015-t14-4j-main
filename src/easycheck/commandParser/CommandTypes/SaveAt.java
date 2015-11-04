package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;
public class SaveAt extends Command{
	private String target;
	
	public SaveAt(String target) {
		this.target = target;
	}
	public String getTarget() {
		return target;
	}

}
