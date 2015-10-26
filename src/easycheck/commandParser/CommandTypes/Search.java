package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;
//@author A0126989H

public class Search extends Command{
	private String [] searchTerms;
	public Search(String commandType, String[] arguments) {
		super(commandType, arguments);
		assert (arguments != null); // as if the arguments is null, the search command should not exist
		searchTerms = arguments;
	}
	
	public String[] getArgument(){
		return searchTerms;
	}
}
