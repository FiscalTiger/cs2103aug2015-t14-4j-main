package easycheck.logicController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.eventlist.Event;

//@author A0126989H

public class DeleteCommandUnitTest {
	private ArrayList<Event> EventList;
	private CommandExecutor cmdExe;

	@Before
	public void setUp() {
		ArrayList<Event> EventList = new ArrayList<Event>();
		cmdExe = new CommandExecutor(new ArrayList<Event>());
		EventList.add(new Event(1, "Do homework"));
		EventList.add(new Event(2, "Test tomorrow"));
		EventList.add(new Event(3, "Presentation in two weeks"));
		EventList.add(new Event(4, "Buy new laptop"));
		EventList.add(new Event(5, "Birthday party this sunday"));
		EventList.add(new Event(6, "Final exam revision"));
		EventList.add(new Event(7, "CS2103 test"));
		EventList.add(new Event(8, "Check email"));
	}

	@Test
	public void testIsNumericFunction() {
		assertTrue("this is true", CommandExecutor.isNumeric("1"));
	}

	// Case 1: when Command is " Delete"
	@Test
	public void testDeleteCase1() {
		String[] argument = { "Do homework" };
		Add command = new Add("add", argument);
		cmdExe.executeCommand(command);

		String[] deleteArgument = null;
		Delete cmd = new Delete("delete", deleteArgument);
		String desc = "test Case 1 Delete Without argument";
		String expected = "Deleted Do homework Successfully\n";
		testOneCommand(desc, expected, cmd);
	}

	// Case 2: when Command is "Delete 2"
	@Test
	public void testDeleteCase2() {
		String[] argument = { "Do homework" };
		Add command = new Add("add", argument);
		cmdExe.executeCommand(command);
		
		argument[0] = "Presentation in two weeks";
		command = new Add("add", argument);
		cmdExe.executeCommand(command);

		String[] deleteArgument = {"2"};
		Delete cmd = new Delete("delete", deleteArgument);
		String desc = "test Case 2 Delete Presentation argument";
		String expected = "Deleted Presentation in two weeks Successfully\n";
		testOneCommand(desc, expected, cmd);
	}
	
	// Case 3: when Command is "Delete Laptop"
		@Test
		public void testDeleteCase3() {
			String[] argument = { "Do homework" };
			Add command = new Add("add", argument);
			cmdExe.executeCommand(command);
			
			argument[0] = "Presentation in two weeks";
			command = new Add("add", argument);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			command = new Add("add", argument);
			cmdExe.executeCommand(command);
			
			String[] deleteArgument = {"Laptop"};
			Delete cmd = new Delete("delete", deleteArgument);
			String desc = "test Case 2 Delete Presentation argument";
			String expected = "Deleted Buy new laptop Successfully\n";
			testOneCommand(desc, expected, cmd);
		}

	public void testReIndex() {

	}

	private void testOneCommand(String description, String expected, Command command) {
		assertEquals(description, expected, cmdExe.executeCommand(command));
	}

}
