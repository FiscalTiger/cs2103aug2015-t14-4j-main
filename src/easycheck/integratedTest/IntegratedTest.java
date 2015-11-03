package easycheck.integratedTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import easycheck.userInterface.UserInterface;
/**
 * Integrated JUnit Test for EasyCheck basic commands
 * @@author A0124206W
 *
 */
public class IntegratedTest {
    String fileName = "integratedtestingfile.txt";
    UserInterface ui = new UserInterface(fileName);
    String commandResponse;

    // debugging code
    public void printResponse() {
        System.out.println(commandResponse);
    }

    public void display() {
        String disp = ui.executeCommand("display");
        System.out.println(disp);
    }

    @Before
    public void clearFile() {
        commandResponse = ui.executeCommand("display");
        while (!commandResponse.equals("To Do:\n\n")) {
            ui.executeCommand("delete");
            commandResponse = ui.executeCommand("display");
        }
        commandResponse = null;
    }

    @Test
    public void testAddToDo() {
        commandResponse = ui.executeCommand("add Plan Christmas Party");
        assertEquals("Added 1. Plan Christmas Party\n\n", commandResponse);
    }

    @Test
    public void testAddDeadline() {
        commandResponse = ui
                .executeCommand("add buy Christmas presents, 25 Dec 12:00");
        assertEquals(
                "Added 1. buy Christmas presents due on Fri 25 Dec 2015 at 12:00\n\n",
                commandResponse);
    }

    @Test
    public void testAddEvent() {
        commandResponse = ui
                .executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        assertEquals(
                "Added 1. Christmas Party from Fri 25 Dec 2015 at 12:00 to Sat 26 Dec 2015 at 13:00\n\n",
                commandResponse);
    }

    @Test
    public void testDisplayEmpty() {
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }

    @Test
    public void testDisplayDifferentTasks() {
        String expectedResponse = "To Do:\n\t1. Plan Christmas Party\n\nFri 25 Dec 2015:\n\t2. Christmas Party from 12:00 to Sat 26 Dec 2015 at 13:00\n\t3. buy Christmas presents due at 12:00\n\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display");
        assertEquals(expectedResponse, commandResponse);
    }

    @Test
    public void testDisplayFloat() {
        String expectedResponse = "To Do:\n\t1. Plan Christmas Party\n\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display f");
        assertEquals(expectedResponse, commandResponse);
    }

    @Test
    public void testDisplayDate() {
        String expectedResponse = "Fri 25 Dec 2015:\n\t2. Christmas Party from 12:00 to Sat 26 Dec 2015 at 13:00\n\t3. buy Christmas presents due at 12:00\n\n";
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents, 25 Dec 12:00");
        ui.executeCommand("add Christmas Party, 25 Dec 12:00 to 26 Dec 13:00");
        commandResponse = ui.executeCommand("display d, 25 Dec");
        assertEquals(expectedResponse, commandResponse);
    }

    @Test
    public void testDisplayDone() {
        ui.executeCommand("add plan Christmas Party");
        ui.executeCommand("done plan");
        commandResponse = ui.executeCommand("display done");
        assertEquals("To Do:\n\t1. plan Christmas Party\n\n", commandResponse);
    }

    @Test
    public void testDeleteNotSpecified() {
        ui.executeCommand("add Plan Christmas Party");
        commandResponse = ui.executeCommand("delete");
        assertEquals("Deleted \"Plan Christmas Party\" successfully\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }

    @Test
    public void testDeleteSpecifiedKeyword() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec 12:00");
        commandResponse = ui.executeCommand("delete plan");
        assertEquals("Deleted \"Plan Christmas Party\" successfully\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals(
                "To Do:\n\nFri 25 Dec 2015:\n\t1. buy Christmas presents due at 12:00\n\n",
                commandResponse);
    }

    @Test
    public void testDeleteAll() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec");
        commandResponse = ui.executeCommand("delete all");
        assertEquals("Congratulations on completing all task! :)\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }

    @Test
    public void testDeleteMultipleKeyword() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents on 25th Dec");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete all christmas");
        assertEquals("Deleted \"all christmas\" related tasks successfully\n",
                commandResponse);
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\t1. Plan New Year's Party\n\n", commandResponse);
    }

    // 4 is a boundary value for a to do list with 3 tasks
    @Test
    public void testDeleteOverBounds() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete 4");
        assertEquals("There are no such events!\n", commandResponse);
    }

    // 0 is a boundary value for all to do lists as we use 1-indexing
    @Test
    public void testDeleteUnderBounds() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("add buy Christmas presents");
        ui.executeCommand("add Plan New Year's Party");
        commandResponse = ui.executeCommand("delete 0");
        assertEquals("There are no such events!\n", commandResponse);
    }

    @Test
    public void testMarkDone() {
        ui.executeCommand("add plan Christmas Party");
        ui.executeCommand("done plan");
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }
    
        @Test
    public void testUndoAdd() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("undo");
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }
    
    @Test
    public void testRedoAdd() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("undo");
        ui.executeCommand("redo");
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\t1. Plan Christmas Party\n\n", commandResponse);
    }
    
    @Test
    public void testUndoDelete() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("delete");
        ui.executeCommand("undo");
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\t1. Plan Christmas Party\n\n", commandResponse);
    }
    
    @Test
    public void testRedoDelete() {
        ui.executeCommand("add Plan Christmas Party");
        ui.executeCommand("delete");
        ui.executeCommand("undo");
        ui.executeCommand("redo");
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
    }

}
