package easycheck.commandParser;

public class Edit extends Command {
	private String task;
	private String newEvent;
	
	public Edit(String commandType, String[] arguments) {
		super(commandType, arguments);
		if (arguments.length==2){
			task = arguments[0];
			newEvent = arguments[1];
		} else if (arguments.length==1){
			task = arguments [0];
			newEvent = arguments[1];
		}
	}
	public String getTaskName(){
		return task;
	}
	public String getNewEvent(){
		return newEvent;
	}
}
