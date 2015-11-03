package easycheck.commandParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Invalid;

/**
 * Simple JUnit Test for CommandParser
 * @@author A0124206W
 *
 */
public class ParserTest {
    public static DateTimeFormatter formatter = DateTimeFormat
            .forPattern("MM/dd/yyyy HH:mm");
    public static DateTime testStartDate = formatter
            .parseDateTime("11/12/2015 13:30");
    
    @Test
    public void init() {
        CommandParser parser = new CommandParser();
    }

    @Test
    public void addTask() {
        CommandParser parser = new CommandParser();
        Add add = (Add) parser.parseCommand("add task name without due date");
        assertEquals("task name without due date", add.getTaskName());
        assertEquals(false, add.hasStart());
        assertEquals(false, add.hasEnd());
    }

    @Test
    public void addTaskWithDeadline() {
        CommandParser parser = new CommandParser();
        Add add = (Add) parser.parseCommand("add task name, 11/12/2015 13:30");
        assertEquals("task name", add.getTaskName());
        assertEquals(testStartDate, add.getEnd());
        assertEquals(false, add.hasStart());
    }

    @Test
    // Tests parsing of flexi-words such as by
    public void addTaskWithFlexiWords() {
        CommandParser parser = new CommandParser();
        Add add = (Add) parser
                .parseCommand("add task name by 11/12/2015 13:30");
        assertEquals("task name", add.getTaskName());
        assertEquals(testStartDate, add.getEnd());
        assertEquals(false, add.hasStart());
    }

    // 13th month is a boundary value for month
    @Test
    public void addBadTaskThirteenthMonth() {
        CommandParser parser = new CommandParser();
        Command invalid = parser
                .parseCommand("add task name by 13/11/2016 13:30");
        assertTrue(invalid instanceof Invalid);
    }

    // 0th month is a boundary value for month
    @Test
    public void addBadTaskZeroMonth() {
        CommandParser parser = new CommandParser();
        Command invalid = parser
                .parseCommand("add task name by 0/11/2016 13:30");
        assertTrue(invalid instanceof Invalid);
    }

}
