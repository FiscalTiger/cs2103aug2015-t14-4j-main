package easycheck.integratedTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import easycheck.userInterface.UserInterface;

public class IntegratedTest {
    String fileName = "integratedtestingfile.txt";
    UserInterface ui = new UserInterface(fileName);
    String commandResponse;
    
    //debugging code
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
            
        
        
    }

    @Test
    public void testDisplayEmpty() {
        commandResponse = ui.executeCommand("display");
        assertEquals("To Do:\n\n", commandResponse);
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
                .executeCommand("add Christmas Party, 25 Dec 12:00, 26 DEC 13:00");
        assertEquals(
                "Added 1. Christmas Party from Fri 25 Dec 2015 at 12:00 to Sat 26 Dec 2015 at 13:00\n\n",
                commandResponse);
    }

}
