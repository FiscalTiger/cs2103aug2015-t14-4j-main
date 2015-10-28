package easycheck.commandParser.CommandTypes;

import org.joda.time.DateTime;

import easycheck.commandParser.Command;

public class Repeat extends Command {
    private String taskName;
    private boolean isDaily;
    private boolean isWeekly;
    private boolean isMonthly;
    private DateTime endDate;

    public Repeat(String arguments) {
        this.taskName = arguments;
    }
    
    public void isDaily() {
        // only one of the booleans can be true
        isDaily = true;
        isWeekly = false;
        isMonthly = false;
    }

    public void isWeekly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = true;
        isMonthly = false;
    }

    public void isMonthly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = false;
        isMonthly = true;
    }
}
