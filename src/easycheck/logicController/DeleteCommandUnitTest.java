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
import easycheck.commandParser.CommandTypes.Markdone;
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
	public void test_Delete_Case1() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);

		String[] deleteArgument = null;
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 1 Delete Without argument";
		String expected = "@|green Deleted \"Do homework\" successfully|@\n";
		testOneCommand(desc, expected, cmd);
	}

	// Case 2: when Command is "Delete 2"
	@Test
	public void test_Delete_Case2() {
		String argument = "Do homework";
		Add command = new Add(argument);
		cmdExe.executeCommand(command);
		
		String argument2 = "Presentation in two weeks";
		command = new Add(argument2);
		cmdExe.executeCommand(command);

		String[] deleteArgument = {"2"};
		Delete cmd = new Delete(deleteArgument);
		String desc = "test Case 2 Delete Presentation argument";
		String expected = "@|green Deleted \"Presentation in two weeks\" successfully|@\n";
		testOneCommand(desc, expected, cmd);
	}
	/* Case 3: 
	* Special Command : " delete all"
	*/ 
	@Test
	public void test_Delete_Case3() {
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
		String desc = "test Case 3 Delete all";
		String expected = "@|green Congratulations on completing all task! :)|@\n";
		testOneCommand(desc, expected, cmd);
	}
	// Delete Multiple matching String and "delete all + eventName"
	@Test
	public void test_Delete_Case3_Specific() {
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
		String desc = "test Case 3 Delete all CS2103";
		String expected = "@|red There are no such events!|@\n";
		testOneCommand(desc, expected, cmd);
	}
	// Case 4: Delete Done Tasks "delete done"
		@Test
		public void test_Delete_Done() {
			String[] argument = { "Read CS2103" };
			Add command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "ACC2002 Final starts on 26 Nov 2015 19:00";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			Markdone done = new Markdone(argument);
			cmdExe.executeCommand(done);
			
			String[] deleteArgument = {"done"};
			Delete cmd = new Delete(deleteArgument);
			String desc = "test Case 4 Delete done";
			String expected = "@|green Deleted all done tasks successfully|@\n";
			testOneCommand(desc, expected, cmd);
		}
	
	// Case 5: when Command is "Delete Laptop"
		@Test
		public void test_Delete_Case5() {
			String[] argument = { "Do homework" };
			Add command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Presentation in two weeks";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			String[] deleteArgument = {"Laptop"};
			Delete cmd = new Delete(deleteArgument);
			String desc = "test Case 5 Delete Laptop";
			String expected = "@|green Deleted \"Buy new laptop\" successfully|@\n";
			testOneCommand(desc, expected, cmd);
		}
	// Delete with specific keyword (lower or uppercase) which is a substring of the sentence
		@Test 
		public void test_Delete_Specific_KeyWords(){
			String[] argument = { "Do homework" };
			Add command = new Add("Do homework");
			cmdExe.executeCommand(command);
			
			argument[0] = "buy present for Mom's Birthday, 11/12/2015";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			argument[0] = "Buy new laptop";
			command = new Add(argument[0]);
			cmdExe.executeCommand(command);
			
			String[] deleteArgument = {"birthday"};
			Delete cmd = new Delete(deleteArgument);
			String desc = "test Case 6 Delete birthday";
			String expected = "@|green Deleted \"buy present for Mom's Birthday, 11/12/2015\" successfully|@\n";
			testOneCommand(desc, expected, cmd);
			
		}

	private void testOneCommand(String description, String expected, Command command) {
		assertEquals(description, expected, cmdExe.executeCommand(command));
	}
}
