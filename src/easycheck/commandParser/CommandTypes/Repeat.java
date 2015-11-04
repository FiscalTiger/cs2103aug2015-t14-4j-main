package easycheck.commandParser.CommandTypes;

import org.joda.time.DateTime;

import easycheck.commandParser.Command;

public class Repeat extends Command {
    private String taskName;
    private boolean isDaily;
    private boolean isWeekly;
    private boolean isBiweekly;
    private boolean isMonthly;
    private boolean isYearly;
    private DateTime endDate;

    public Repeat(String taskName) {
    	this.taskName = taskName;
    }
    
    public void isDaily() {
        // only one of the booleans can be true
        isDaily = true;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = false;
        isYearly = false;
    }

    public void isWeekly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = true;
        isBiweekly = false;
        isMonthly = false;
        isYearly = false;
    }
    
    public void isBiweekly() {
    	// only one of the booleans can be true
    	isDaily = false;
        isWeekly = false;
        isBiweekly = true;
        isMonthly = false;
        isYearly = false;
    }

    public void isMonthly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = true;
        isYearly = false;
    }
    
    public void isYearly() {
        // only one of the booleans can be true
        isDaily = false;
        isWeekly = false;
        isBiweekly = false;
        isMonthly = false;
        isYearly = true;
    }
}
