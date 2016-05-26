package easycheck.commandParser.CommandTypes;

import easycheck.commandParser.CommandParser.*;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import easycheck.commandParser.Command;

//@@author A0126989H

public class Search extends Command {
	private static final int SUBSTRING_COMMAND_START = 4;
	private static final int ALL_COMMAND_END = 3;
	private static final int ZERO_CONSTANT = 0;
	private static final int ARGUMENT_SIZE = 1;
	
	private static final String SEARCH_CMD_SPECIALCOMMAND = "all";
	private static final String SEARCH_CMD_FREETIME_PHRASE = "freetime";
	private static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
	private static final String SPLIT_REGEXP = "\\s+";
	private static final String EMPTY_STRING = "";
	
	private String[] searchTerms;
	private DateTime date;

	public Search(String commandType, String[] arguments) {
		super(commandType, arguments);
		assert(arguments != null); // as if the arguments is null, the search
									// command should not exist
		searchTerms = arguments;
	}

	public String getArgument() {
		return searchTerms[ZERO_CONSTANT].toLowerCase();
	}

	public boolean isFreetimeSearch() throws IllegalArgumentException {
		String[] temp = getArgument().split(SPLIT_REGEXP);
		if (temp.length == ARGUMENT_SIZE && temp[ZERO_CONSTANT].toLowerCase().equals(SEARCH_CMD_FREETIME_PHRASE)) {
			return true;
		} else if (temp.length > ARGUMENT_SIZE && temp[ZERO_CONSTANT].toLowerCase().equals(SEARCH_CMD_FREETIME_PHRASE)) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(STANDARD_DATE_FORMAT);
			date = formatter.parseDateTime(temp[ARGUMENT_SIZE]);
			return true;
			
		} else {
			return false;
		}
	}

	public boolean hasDate() {
		return date != null;
	}

	public DateTime getDate() {
		return date;
	}

	public String getTaskNameAll() {
		if (getArgument().length() >= SUBSTRING_COMMAND_START)
			return getArgument().substring(SUBSTRING_COMMAND_START);
		else
			return EMPTY_STRING;
	}

	public boolean isSearchAll() {
		return getArgument().length() >= ALL_COMMAND_END
				&& getArgument().substring(ZERO_CONSTANT, ALL_COMMAND_END).equals(SEARCH_CMD_SPECIALCOMMAND);
	}
	public void showToUser(String message){
		System.out.println(message);
	}

}
