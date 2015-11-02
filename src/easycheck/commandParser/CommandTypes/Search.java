package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.CommandParser.*;
import java.util.List;


import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import easycheck.commandParser.Command;

//@author A0126989H
public class Search extends Command{
	private static final String SPLIT_REGEXP= "\\s+";
	private String [] searchTerms;
	private DateTime date;
	
	
	
	public Search(String commandType, String[] arguments) {
		super(commandType, arguments);
		assert (arguments != null); // as if the arguments is null, the search command should not exist
		searchTerms = arguments;
	}
	
	public String getArgument(){
		return searchTerms[0].toLowerCase();
	}
	public boolean isFreetimeSearch(){
		String[] temp = getArgument().split(SPLIT_REGEXP);
		if (temp.length==1 && temp[0].toLowerCase().equals("freetime")){
			return true;
		} else if (temp.length>1 && temp[0].toLowerCase().equals("freetime")){
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
			date = formatter.parseDateTime(temp[1]);
			return true;
		} else {
			return false;
		}
	}
	public boolean hasDate(){
		return date!=null;
	}
	public DateTime getDate(){
		return date;
	}
	
	
}
