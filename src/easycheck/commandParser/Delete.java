package easycheck.commandParser;

public class Delete extends Command{
	private String task;
	
	public Delete(String commandType, String[] arguments) {
		super(commandType, arguments);
		task=arguments[0];
		// TODO Auto-generated constructor stub
	}
	public String getTaskName(){
		return task;
	}
}
