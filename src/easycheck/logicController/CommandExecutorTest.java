package easycheck.logicController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.Event;
import easycheck.eventlist.FloatingTask;
import easycheck.eventlist.ToDoEvent;

public class CommandExecutorTest {
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";

	private static final String GOOD_START_DATE = "11.12.2100 12:00";
	private static final String GOOD_END_DATE = "11.12.2100 13:00";
	private static final String OLD_START_DATE = "11.12.1999 12:00";
	private static final String BAD_START_DATE = "11.12.2100 14:00";
	private static final String BAD_END_DATE = "11.12.1999 12:00";
	
	private static final String MESSAGE_INVALID_CALENDAR_DATES = "The start date must be before the end date and after the current date and time.\n";
	private static final String MESSAGE_INVALID_TODO_DEADLINE = "The deadline must be after the current date and time.\n";

	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	
	private DateTime goodStart;
	private DateTime goodEnd;
	private DateTime oldStart;
	private DateTime badStart;
	private DateTime badDeadline;
	private CommandExecutor cmdExe;
	
	
	@Before public void setUp() {
		goodStart = fmt.parseDateTime(GOOD_START_DATE);
		goodEnd = fmt.parseDateTime(GOOD_END_DATE);
		oldStart = fmt.parseDateTime(OLD_START_DATE);
		badStart = fmt.parseDateTime(BAD_START_DATE);
		badDeadline = fmt.parseDateTime(BAD_END_DATE);
		cmdExe = new CommandExecutor(new ArrayList<Event>());
	}
	
	@Test
	public void testExecuteCommand() {
		testAddCommandGoodCal();
		testAddCommandBadCal();
		testAddCommandOldCal();
		testAddCommandGoodToDo();
		testAddCommandOldToDo();
		testAddCommandGoodFloating();
		testDeleteCommand();
	}
	//@author A0126989H
	public void testDeleteCommand(){
		String[] temp = {"1"};
		assertTrue("this is true",CommandExecutor.isNumeric("1"));
	}
	
	public void testAddCommandGoodCal() {
		Command cmd;
		String desc;
		String expected;
		
		// Good Calendar Event
		CalendarEvent goodCal = new CalendarEvent(1, "Good Cal Event", goodStart, goodEnd);
		cmd = new Add("Good Cal Event", goodStart, goodEnd);
		desc = "Creates good calendar event";
		expected = "Added " + goodCal + "\n";
		testOneCommand(desc, expected, cmd);
	}

	public void testAddCommandGoodFloating() {
		Command cmd;
		String desc;
		String expected;
		
		// Good Floating task
		Event goodFloating = new FloatingTask(1, "Good floating task");
		cmd = new Add("Good floating task");
		desc = "Creates good floating task";
		expected = "Added " + goodFloating + "\n";
		testOneCommand(desc, expected, cmd);
	}

	public void testAddCommandOldToDo() {
		Command cmd;
		String desc;
		String expected;
		
		// Old To Do Event
		cmd = new Add("Old To Do Event", badDeadline);
		desc = "Tries to make a to do event where the due date already past";
		expected = MESSAGE_INVALID_TODO_DEADLINE;
		testOneCommand(desc, expected, cmd);
	}

	public void testAddCommandGoodToDo() {
		Command cmd;
		String desc;
		String expected;
		
		// Good To Do Event
		ToDoEvent goodToDo = new ToDoEvent(2, "Good To Do Event", goodEnd);
		cmd = new Add("Good To Do Event", goodEnd);
		desc = "Creates good to do event";
		expected = "Added " + goodToDo + "\n";
		testOneCommand(desc, expected, cmd);
	}

	public void testAddCommandOldCal() {
		Command cmd;
		String desc;
		String expected;
		
		// Old Calendar Event
		String[] oldCalCommandArgs = {"Old Cal Event", OLD_START_DATE, GOOD_END_DATE};
		cmd = new Add("Old Cal Event", oldStart, goodEnd);
		desc = "Tries to make a calendar event where the start date already past";
		expected = MESSAGE_INVALID_CALENDAR_DATES;
		testOneCommand(desc, expected, cmd);
	}

	public void testAddCommandBadCal() {
		Command cmd;
		String desc;
		String expected;
		
		// Bad Calendar Event
		cmd = new Add("Bad Cal Event", badStart, goodEnd);
		desc = "Tries to make a bad calendar event";
		expected = MESSAGE_INVALID_CALENDAR_DATES;
		testOneCommand(desc, expected, cmd);
	}
	
	private void testOneCommand(String description, String expected, Command command) {
		assertEquals(description, expected, cmdExe.executeCommand(command)); 
	}

}
