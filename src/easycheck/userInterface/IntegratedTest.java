package easycheck.userInterface;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
        while (!commandResponse.equals("Floating:\n")) {
            ui.executeCommand("delete");
            commandResponse = ui.executeCommand("display");
        }
            
        
        
    }

    @Test
    public void testEmptyFile() {
        commandResponse = ui.executeCommand("display");
        assertEquals("Floating:\n", commandResponse);
    }

    @Test
    public void testAdd() {
        commandResponse = ui.executeCommand("add Plan Christmas Party");
        assertEquals("Added 1. Plan Christmas Party\n\n", commandResponse);
        // these tests will only work until Christmas #goodenough
        commandResponse = ui
                .executeCommand("add buy Christmas presents, 25 Dec 12:00");
        assertEquals(
                "Added 2. buy Christmas presents due on Fri 25 Dec 2015 at 12:00\n\n",
                commandResponse);
        commandResponse = ui
                .executeCommand("add Christmas Party, 25 Dec 12:00, 26 DEC 13:00");
        assertEquals(
                "Added 2. Christmas Party from Fri 25 Dec 2015 at 12:00 to Sat 26 Dec 2015 at 13:00\n\n",
                commandResponse);
    }

}
