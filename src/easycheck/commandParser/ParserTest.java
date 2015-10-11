package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Simple JUnit Test for CommandParser,
 * @@author A0124206W
 *
 */
public class ParserTest {
    
    @Test
    public void init() {
        CommandParser parser = new CommandParser();
    }
    
    @Test
    public void addTask() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name without due date");
        assertEquals("task name without due date", add.getCommandArguments()[0]);
    }
    
    @Test
    public void addTaskWithDeadline() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name, due date");
        assertEquals("task name", add.getCommandArguments()[0]);
        assertEquals("due date", add.getCommandArguments()[1]);
    }
    
    @Test
    public void addTaskWithFlexiWords() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name by due date");
        assertEquals("task name", add.getCommandArguments()[0]);
        assertEquals("due date", add.getCommandArguments()[1]);
    }
}
