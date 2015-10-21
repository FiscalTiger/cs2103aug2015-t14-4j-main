package easycheck.commandParser;
import static org.junit.Assert.assertEquals;

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
        Command add = parser.parseCommand("add task name, 11.12.2015 13:30");
        assertEquals("task name", add.getCommandArguments()[0]);
        assertEquals("11.12.2015 13:30", add.getCommandArguments()[1]);
    }
    
    @Test
    public void addTaskWithFlexiWords() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name by 11.12.2015 13:30");
        assertEquals("task name", add.getCommandArguments()[0]);
        assertEquals("11.12.2015 13:30", add.getCommandArguments()[1]);
    }
    
    //13th month is a boundary value for month
    @Test (expected = org.joda.time.IllegalFieldValueException.class)
    public void addBadTaskThirteenthMonth() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name by 11.13.2015 13:30");
    }
    
    //0th month is a boundary value for month
    @Test (expected = org.joda.time.IllegalFieldValueException.class)
    public void addBadTaskZeroMonth() {
        CommandParser parser = new CommandParser();
        Command add = parser.parseCommand("add task name by 11.0.2015 13:30");
    }
}
