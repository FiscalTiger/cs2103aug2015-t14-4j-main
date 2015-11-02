package easycheck.logicController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandParser;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.eventlist.Event;

//@author A0126989H

public class DeleteCommandUnitTest {
	private CommandExecutor cmdExe;

	@Before
	public void setUp() {
		cmdExe = new CommandExecutor(new ArrayList<Event>());
	}

	@Test
	public void testIsNumericFunction() {
		assertTrue("this is true", CommandExecutor.isNumeric("1"));
	}

	// Case 1: when Command is " Delete"
	@Test
	public void testDeleteCase1() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);

		String[] deleteArgument = null;
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 1 Delete Without argument";
		String expected = "Deleted \"Do homework\" successfully\n";
		testOneCommand(desc, expected, cmd);
	}

	// Case 2: when Command is "Delete 2"
	@Test
	public void testDeleteCase2() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);
		
		String argument2 = "Presentation in two weeks";
		command = new Add(argument2);
		cmdExe.executeCommand(command);

		String[] deleteArgument = {"2"};
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 2 Delete Presentation argument";
		String expected = "Deleted \"Presentation in two weeks\" successfully\n";
		testOneCommand(desc, expected, cmd);
	}
	/* Case 3: 
	* Special Command : " delete all"
	* Delete Multiple matching String and "delete all + eventName"
	*/ 
	@Test
	public void testDeleteCase3() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);
		
		String argument2 = "Presentation in two weeks";
		command = new Add(argument2);
		cmdExe.executeCommand(command);
		
		String argument3 = "Read CS2103";
		command = new Add(argument3);
		cmdExe.executeCommand(command);
		

		String[] deleteArgument = {"all"};
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 2 Delete Presentation argument";
		String expected = "Congratulations on completing all task! :)\n";
		testOneCommand(desc, expected, cmd);
	}
	
	@Test
	public void testDeleteCase3_Specific() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);
		
		String argument2 = "Presentation in two weeks";
		command = new Add(argument2);
		cmdExe.executeCommand(command);
		
		String argument3 = "Read CS2103";
		command = new Add(argument3);
		cmdExe.executeCommand(command);
		
		String argument4 = "Prepare CS2103 Dev Guide";
		command = new Add(argument4);
		cmdExe.executeCommand(command);
		
		String argument5 = "Presentation for CS2103,12/11/2015";
		command = new Add(argument5);
		cmdExe.executeCommand(command);
		

		String[] deleteArgument = {"all CS2103"};
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 2 Delete Presentation argument";
		String expected = "Deleted \"all cs2103\" related tasks successfully\n";
		testOneCommand(desc, expected, cmd);
	}
	// Case 4: Delete Done Tasks "delete done"
	
	// Case 5: when Command is "Delete Laptop"
		@Test
		public void testDeleteCase5() {
			String[] argument = { "Do homework" };
			Add command = new Add("Do homework");
			cmdExe.executeCommand(command);
			
			argument[0] = "Presentation in two weeks";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			String[] deleteArgument = {"Laptop"};
			Delete cmd = new Delete(deleteArgument);
			String desc = "test Case 2 Delete Presentation argument";
			String expected = "Deleted \"Buy new laptop\" successfully\n";
			testOneCommand(desc, expected, cmd);
		}

	public void testReIndex() {

	}

	private void testOneCommand(String description, String expected, Command command) {
		assertEquals(description, expected, cmdExe.executeCommand(command));
	}
}
