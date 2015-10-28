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

    public void isDaily(boolean isDaily) {
        this.isDaily = isDaily;
    }

    public void isWeekly(boolean isWeekly) {
        this.isWeekly = isWeekly;
    }

    public void isMonthly(boolean isMonthly) {
        this.isMonthly = isMonthly;
    }
}
