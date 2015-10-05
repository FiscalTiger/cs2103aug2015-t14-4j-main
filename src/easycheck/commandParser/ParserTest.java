package easycheck.commandParser;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void init() {
        CommandParser parser = new CommandParser();
    }
    
    @Test
    public void addTask() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name without due date");
        assertEquals("task name without due date", add.arguments[0]);
    }
    
    @Test
    public void addTaskWithDeadline() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name, due date");
        assertEquals("task name", add.arguments[0]);
        assertEquals("due date", add.arguments[1]);
    }
    
    @Test
    public void addTaskWithFlexiWords() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name by due date");
        assertEquals("task name", add.arguments[0]);
        assertEquals("due date", add.arguments[1]);
    }
}
