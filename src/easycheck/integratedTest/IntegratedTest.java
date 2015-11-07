package easycheck.integratedTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import easycheck.userInterface.UserInterface;

/**
 * Integrated JUnit Test for EasyCheck basic commands
 * 
 * @@author A0124206W
 *
 */
public class IntegratedTest {
    String fileName = "integratedtestingfile.txt";
    UserInterface ui = new UserInterface(fileName);
    String commandResponse;

    // debugging code to print display message from application.
    public void printResponse() {
        System.out.println(commandResponse);
    }

    // debugging code to display all tasks.
    public void display() {
        String disp = ui.executeCommand("display");
        System.out.println(disp);
    }

    // clears all tasks before each test to ensure correctness.
    @Before
    public void clearFile() {
        commandResponse = ui.executeCommand("display");
        display();
        while (!commandResponse.equals("@|red There aren't any events to display!|@\n")) {
            ui.executeCommand("delete");
            commandResponse = ui.executeCommand("display");
        }
        commandResponse = null;
    }

    // test adding of a to-do/floating type task
    @Test
    public void testAddToDo() {
        commandResponse = ui.executeCommand("add Plan Christmas Party");
        assertEquals("@|green Added|@ @|yellow 1. Plan Christmas Party\n|@\n",
                commandResponse);
    }

    // test adding of a task with deadline
    @Test
    public void testAddDeadline() {
        commandResponse = ui
                .executeCommand("add buy Christmas presents, 25 Dec 12:00");
        assertEquals(
                "@|green Added|@ @|red 1. buy Christmas presents due on Fri 25 Dec 2015 at 12:00|@\n\n",
                commandResponse);
    }

    // test adding of an event with a start and end time
    @Test
    public void testAddEvent() {
        commandResponse = ui
                .executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        assertEquals(
                "@|green Added|@ @|yellow 1. Christmas Party from Fri 25 Dec 2015 at 12:00 to Sat 26 Dec 2015 at 13:00|@\n\n",
                commandResponse);
    }
    
    // test adding of an event with a start and end time that repeats
    // @@author A0145668R
    @Test
    public void testAddRepeatingEvent() {
        commandResponse = ui
                .executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00 repeat yearly");
        assertEquals(
                "@|green Added|@ @|yellow 1. Christmas Party from Fri 25 Dec 2015 at 12:00 to Sat 26 Dec 2015 at 13:00 (Repeats yearly)|@\n\n",
                commandResponse);
    }
    //@@author

    // test display when there are no tasks
    @Test
    public void testDisplayEmpty() {
        commandResponse = ui.executeCommand("display");
        assertEquals("@|red There aren't any events to display!|@\n", commandResponse);
    }

    // test display when there is at least 1 task of each type
    @Test
    public void testDisplayDifferentTasks() {
        String expectedResponse = "@|cyan To Do:\n|@\t@|yellow 1. Plan Christmas Party\n|@\n@|cyan Fri 25 Dec 2015:\n|@\t@|yellow 2. Christmas Party from 12:00 to Sat 26 Dec 2015 at 13:00|@\n\t@|red 3. buy Christmas presents due at 12:00|@\n\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display");
        assertEquals(expectedResponse, commandResponse);
    }

    // test display of floating type tasks only
    @Test
    public void testDisplayFloat() {
        String expectedResponse = "@|cyan To Do:\n|@\t@|yellow 1. Plan Christmas Party\n|@\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display f");
        assertEquals(expectedResponse, commandResponse);
    }

    // test display of deadline type tasks only
    @Test
    public void testDisplayDate() {
        String expectedResponse = "@|cyan Fri 25 Dec 2015:\n|@\t@|yellow 2. Christmas Party from 12:00 to Sat 26 Dec 2015 at 13:00|@\n\t@|red 3. buy Christmas presents due at 12:00|@\n\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display d, 25 Dec");
        assertEquals(expectedResponse, commandResponse);
    }

    // test display of completed tasks only
    @Test
    public void testDisplayDone() {
        ui.executeCommand("add plan Christmas Party");
        ui.executeCommand("done plan");
        commandResponse = ui.executeCommand("display done");
        assertEquals(
                "@|cyan To Do:\n|@\t@|green 1. plan Christmas Party\n|@\n",
                commandResponse);
    }
    
    // test display all command (should display tasks regardless of completion status)
    // @@author A0145668R
    @Test
    public void testDisplayAll() {
    	ui.executeCommand("add plan party at on 25th Dec 12:00");
    	ui.executeCommand("add lunch at 1 pm 25th Dec to 2 pm 25th Dec");
    	ui.executeCommand("done plan");
    	commandResponse = ui.executeCommand("display all");
    	assertEquals(
                "@|cyan To Do:\n|@\n@|cyan Fri 25 Dec 2015:\n|@\t@|green 1. plan party due at 12:00 is complete|@\n\t@|yellow 2. lunch from 13:00 to 14:00|@\n\n",
                commandResponse);
    }
    // @@author

    // test deletion when task to delete not specified
    @Test
    public void testDeleteNotSpecified() {
        ui.executeCommand("add Plan Christmas Party");
        commandResponse = ui.executeCommand("delete");
        assertEquals(
                "@|green Deleted \"Plan Christmas Party\" successfully|@\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals("@|red There aren't any events to display!|@\n", commandResponse);
    }

    // test deletion when task to delete is specified
    @Test
    public void testDeleteSpecifiedKeyword() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec 12:00");
        commandResponse = ui.executeCommand("delete plan");
        assertEquals(
                "@|green Deleted \"Plan Christmas Party\" successfully|@\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals(
                "@|cyan To Do:\n|@\n@|cyan Fri 25 Dec 2015:\n|@\t@|red 1. buy Christmas presents due at 12:00|@\n\n",
                commandResponse);
    }

    // test deletion of all tasks
    @Test
    public void testDeleteAll() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec");
        commandResponse = ui.executeCommand("delete all");
        assertEquals("@|green Congratulations on completing all task! :)|@\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals("@|red There aren't any events to display!|@\n", commandResponse);
    }

    // test deletion of multiple tasks with a common keyword
    @Test
    public void testDeleteMultipleKeyword() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete all christmas");
        assertEquals(
                "@|green Deleted \"christmas\" related tasks successfully|@\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals(
                "@|cyan To Do:\n|@\t@|yellow 1. Plan New Year's Party\n|@\n",
                commandResponse);
    }

    // test erroneous use of delete, deleting non existent task
    // 4 is a boundary value for a to do list with 3 tasks
    @Test
    public void testDeleteOverBounds() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete 4");
        assertEquals("@|red There are no such events!|@\n", commandResponse);
    }

    // test erroneous use of delete, deleting non existent task 0
    // 0 is a boundary value for all to do lists as we use 1-indexing
    @Test
    public void testDeleteUnderBounds() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete 0");
        assertEquals("@|red There are no such events!|@\n", commandResponse);
    }

    // test marking an event type as done
    @Test
    public void testMarkDone() {
        ui.executeCommand("add plan Christmas Party");
        ui.executeCommand("done plan");
        commandResponse = ui.executeCommand("display");
        assertEquals("@|cyan To Do:\n|@\n", commandResponse);
    }

    // test undoing of add command
    @Test
    public void testUndoAdd() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("undo");
        commandResponse = ui.executeCommand("display");
        assertEquals("@|red There aren't any events to display!|@\n", commandResponse);
    }

    // test redoing of add command
    @Test
    public void testRedoAdd() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("undo");
        ui.executeCommand("redo");
        commandResponse = ui.executeCommand("display");
        assertEquals(
                "@|cyan To Do:\n|@\t@|yellow 1. Plan Christmas Party\n|@\n",
                commandResponse);
    }

    // test undoing of deletion
    @Test
    public void testUndoDelete() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("delete");
        ui.executeCommand("undo");
        commandResponse = ui.executeCommand("display");
        assertEquals(
                "@|cyan To Do:\n|@\t@|yellow 1. Plan Christmas Party\n|@\n",
                commandResponse);
    }

    // test redoing of deletion
    @Test
    public void testRedoDelete() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("delete");
        ui.executeCommand("undo");
        ui.executeCommand("redo");
        commandResponse = ui.executeCommand("display");
        assertEquals("@|red There aren't any events to display!|@\n", commandResponse);
    }
    
    // test making an event repeat
    // @@author A0145668R
    @Test
    public void testRepeatGoodFrequency() {
    	ui.executeCommand("add Plan Christmas Party at noon Dec 25th");
    	commandResponse = ui.executeCommand("repeat Plan Christmas Party, yearly");
    	assertEquals("@|green Successfully made Plan Christmas Party repeat yearly|@\n", commandResponse);
    }
    
    // test repeat with bad frequency
    @Test
    public void testRepeatBadFrequency() {
    	ui.executeCommand("add Plan Christmas Party at noon Dec 25th");
    	commandResponse = ui.executeCommand("repeat Plan Christmas Party, sdfsdf");
    	assertEquals("@|red Repeat: You have an invalid frequency.\n|@", commandResponse);
    }
    
    // test repeat with bad event name
    @Test
    public void testRepeatBadName() {
    	ui.executeCommand("add Plan Christmas Party at noon Dec 25th");
    	commandResponse = ui.executeCommand("repeat Plan Wrong Party, weekly");
    	assertEquals("@|red Repeat: There aren't any events with the name Plan Wrong Party|@\n", commandResponse);
    }
    
    // test repeat with incorrect index
    @Test
    public void testRepeatBadIndex() {
    	ui.executeCommand("add Plan Christmas Party at noon Dec 25th");
    	commandResponse = ui.executeCommand("repeat 2, weekly");
    	assertEquals("@|red Repeat: invalid event index 2.|@\n", commandResponse);
    }
    //@@author
}
