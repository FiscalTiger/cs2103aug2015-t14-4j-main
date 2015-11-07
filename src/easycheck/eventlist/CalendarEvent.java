package easycheck.eventlist;

/*
 * This is a subclass of Event that deals with Calendar Events.
 * Calendar events are events with specific start and end times
 * @author A0145668R
 */

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CalendarEvent extends Event {
	private static final String DATE_AND_TIME_OUTPUT_FORMAT = "%s %d %s %d at %02d:%02d";
	private static final String DATE_OUTPUT_FORMAT = "%s %d %s %d";
	private static final String TIME_OUTPUT_FORMAT = "%02d:%02d";
	private static final String DATE_AND_TIME_INPUT_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String MESSAGE_JSON_STRING_ERROR = "Error in toJsonString method, most likely coding error";
	private static final String MESSAGE_TO_STRING_TEMPLATE = "@|yellow %d. %s from %s to %s%s|@\n";
	private static final String MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE = "@|yellow %d. %s from %s to %s%s|@\n";
	private static final String MESSAGE_REPEATING_ADDITION = " (Repeats %s)";
	
	private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private static final String JSON_TYPE = "type";
	private static final String JSON_EVENT_INDEX = "index";
	private static final String JSON_EVENT_NAME = "name";
	private static final String JSON_START_DATE = "start";
	private static final String JSON_END_DATE = "end";
	private static final String JSON_REPEAT = "repeating";
	private static final String JSON_FREQUENCY = "frequency";
	private static final String JSON_STOPDATE = "stopdate";
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_AND_TIME_INPUT_FORMAT);
	private DateTime startDateAndTime;
	private DateTime endDateAndTime;
	private DateTime stopDate;
	private boolean repeating;
	private String frequency;
	
	public CalendarEvent(int eventIndex, String eventName, DateTime startDateAndTime, DateTime endDateAndTime) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		this.startDateAndTime = startDateAndTime;
		this.endDateAndTime = endDateAndTime;
		this.setRepeating(false);
		this.setFrequency(null);
		this.setStopDate(null);
	}
	
	public CalendarEvent(CalendarEvent e) {
		this.setEventIndex(e.getEventIndex());
		this.setEventName(e.getEventName());
		this.setStartDateAndTime(e.getStartDateAndTime());
		this.setEndDateAndTime(e.getEndDateAndTime());
		this.setRepeating(e.isRepeating());
		this.setFrequency(e.getFrequency());
		this.stopDate = e.stopDate;
	}
	
	public CalendarEvent(int eventIndex, String eventName, DateTime startDateAndTime, DateTime endDateAndTime,
			boolean repeating, String frequency) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		this.startDateAndTime = startDateAndTime;
		this.endDateAndTime = endDateAndTime;
		this.setRepeating(repeating);
		this.setFrequency(frequency);
		this.setStopDate(null);
	}
	
	public CalendarEvent(int eventIndex, String eventName, DateTime startDateAndTime, DateTime endDateAndTime,
			boolean repeating, String frequency, DateTime stopDate) {
		this.setEventIndex(eventIndex);
		this.setEventName(eventName);
		this.startDateAndTime = startDateAndTime;
		this.endDateAndTime = endDateAndTime;
		this.setRepeating(repeating);
		this.setFrequency(frequency);
		this.setStopDate(stopDate);
	}
	
	public CalendarEvent(JSONObject jsonObj) {
		Long eventIndex = (Long) jsonObj.get(JSON_EVENT_INDEX);
		String eventName = (String) jsonObj.get(JSON_EVENT_NAME);
		startDateAndTime = fmt.parseDateTime((String)jsonObj.get(JSON_START_DATE));
		endDateAndTime = fmt.parseDateTime((String)jsonObj.get(JSON_END_DATE));
		repeating = ((Boolean)jsonObj.get(JSON_REPEAT)).booleanValue();
		
		if(repeating) {
			if(jsonObj.get(JSON_STOPDATE) != null) {
				stopDate = fmt.parseDateTime((String)jsonObj.get(JSON_STOPDATE));
			} else {
				stopDate = (DateTime)jsonObj.get(JSON_STOPDATE);
			}
			frequency = (String) jsonObj.get(JSON_FREQUENCY);
		}
		
		this.setEventIndex(eventIndex.intValue());
		this.setEventName(eventName);
	}
	
	/**
	 * Returns the start date and time of this event
	 * @return DateTime startDateAndTime
	 */
	public DateTime getStartDateAndTime() {
		if(this.isRepeating() && !this.isUpdated()) {
			update();
		}
		return startDateAndTime;
	}
	
	/**
	 * Returns the end date and time of this event
	 * @return DateTime endDateAndTime
	 */
	public DateTime getEndDateAndTime() {
		if(this.isRepeating() && !this.isUpdated()) {
			update();
		}
		return endDateAndTime;
	}
	
	public Duration getDuration() {
		return new Duration(startDateAndTime, endDateAndTime);
	}
	
	public String getStartDate() {
		DateTime.Property pDayOfTheWeek = getStartDateAndTime().dayOfWeek();
		DateTime.Property pMonthOfYear = getStartDateAndTime().monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				getStartDateAndTime().getDayOfMonth(), pMonthOfYear.getAsShortText(), getStartDateAndTime().getYear());
		return dateString;
	}
	
	public String getStartTime() {
		String timeString = String.format(TIME_OUTPUT_FORMAT, getStartDateAndTime().getHourOfDay(), getStartDateAndTime().getMinuteOfHour());
		return timeString;
	}
	
	public String getEndDate() {
		DateTime.Property pDayOfTheWeek = getEndDateAndTime().dayOfWeek();
		DateTime.Property pMonthOfYear = getEndDateAndTime().monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				getEndDateAndTime().getDayOfMonth(), pMonthOfYear.getAsShortText(), getEndDateAndTime().getYear());
		return dateString;
	}
	
	public String getEndTime() {
		String timeString = String.format(TIME_OUTPUT_FORMAT, getEndDateAndTime().getHourOfDay(), getEndDateAndTime().getMinuteOfHour());
		return timeString;
	}
	
	/**
	 * Sets a new start date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setStartDateAndTime(DateTime newDate) {
		// If end date is not set yet we can't assert
		// this is true but the other assert in
		// setEndDateAndTime will pick it up
		if(endDateAndTime != null) {
			assert(newDate.isBefore(endDateAndTime));
		}
		startDateAndTime = newDate;
	}
	
	/**
	 * Sets a new end date and time
	 * @param newDateString in format of "E MM.dd.yyyy 'at' hh:mm:ss a zzz"
	 * See Documentation for java.text.SimpleDateFormat
	 */
	public void setEndDateAndTime(DateTime newDate) {
		assert(startDateAndTime.isBefore(newDate));
		endDateAndTime = newDate;
	}
	
	public boolean isSameDay() {
		return this.getStartDate().equals(this.getEndDate());
	}
	
	public boolean isSameStartDay(DateTime date){
		DateTime.Property pDayOfTheWeek = date.dayOfWeek();
		DateTime.Property pMonthOfYear = date.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				date.getDayOfMonth(), pMonthOfYear.getAsShortText(), date.getYear());
		return dateString.equals(getStartDate());
	}
	
	public boolean isSameEndDay(DateTime date){
		DateTime.Property pDayOfTheWeek = date.dayOfWeek();
		DateTime.Property pMonthOfYear = date.monthOfYear();
		String dateString = String.format(DATE_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				date.getDayOfMonth(), pMonthOfYear.getAsShortText(), date.getYear());
		return dateString.equals(getEndDate());
	}
	
	public boolean isDone() {
		if(hasStopDate()) {
			if(repeating && !isUpdated()) {
				update();
			}
			return stopDate.isBefore(endDateAndTime);
		}
		return getEndDateAndTime().isBeforeNow();
	}
	
	private void update() {
		switch(frequency) {
			case REPEATING_DAILY:
				startDateAndTime = startDateAndTime.plusDays(1);
				endDateAndTime = endDateAndTime.plusDays(1);
				break;
			case REPEATING_WEEKLY:
				startDateAndTime = startDateAndTime.plusWeeks(1);
				endDateAndTime = endDateAndTime.plusWeeks(1);
				break;
			case REPEATING_BIWEEKLY:
				startDateAndTime = startDateAndTime.plusWeeks(2);
				endDateAndTime = endDateAndTime.plusWeeks(2);
				break;
			case REPEATING_MONTHLY:
				startDateAndTime = startDateAndTime.plusMonths(1);
				endDateAndTime = endDateAndTime.plusMonths(1);
				break;
			case REPEATING_YEARLY:
				startDateAndTime = startDateAndTime.plusYears(1);
				endDateAndTime = endDateAndTime.plusYears(1);
				break;
			default:
				try {
					throw new Exception("Got to defualt case in update. Something is wrong!");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
	}

	public boolean isUpdated() {
		return endDateAndTime.isAfterNow();
	}
	
	/**
	 * Returns the string form of this calendar event
	 */
	public String toString() {
		String startDateString = getFormattedStartDateString();
		String endDateString = getFormattedEndDateString();
		String repeatAddition = "";
		
		if(repeating) {
			repeatAddition = String.format(MESSAGE_REPEATING_ADDITION, frequency);
		}
		return String.format(
				MESSAGE_TO_STRING_TEMPLATE, this.getEventIndex(), this.getEventName(), 
				startDateString, endDateString, repeatAddition);
	}
	
	public String toPrintGroupString() {
		String repeatAddition = "";
		
		if(repeating) {
			repeatAddition = String.format(MESSAGE_REPEATING_ADDITION, frequency);
		}
		if (this.isSameDay()) {
			return String.format(MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE, this.getEventIndex(), 
					this.getEventName(), this.getStartTime(), this.getEndTime(), repeatAddition);
		} else {
			return String.format(MESSAGE_TO_PRINT_GROUP_STRING_TEMPLATE, this.getEventIndex(), 
					this.getEventName(), this.getStartTime(), this.getFormattedEndDateString(), repeatAddition);
		}
	}

	private String getFormattedStartDateString() {
		DateTime.Property pDayOfTheWeek = getStartDateAndTime().dayOfWeek();
		DateTime.Property pMonthOfYear = getStartDateAndTime().monthOfYear();
		return String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				getStartDateAndTime().getDayOfMonth(), pMonthOfYear.getAsShortText(), getStartDateAndTime().getYear(),
				getStartDateAndTime().getHourOfDay(), getStartDateAndTime().getMinuteOfHour());
	}
	
	private String getFormattedEndDateString() {
		DateTime.Property pDayOfTheWeek = getEndDateAndTime().dayOfWeek();
		DateTime.Property pMonthOfYear = getEndDateAndTime().monthOfYear();
		return String.format(DATE_AND_TIME_OUTPUT_FORMAT, pDayOfTheWeek.getAsShortText(),
				getEndDateAndTime().getDayOfMonth(), pMonthOfYear.getAsShortText(), getEndDateAndTime().getYear(),
				getEndDateAndTime().getHourOfDay(), getEndDateAndTime().getMinuteOfHour());
	}
	
	/**
	 * Returns the JSON string to write to the file for this calendar object
	 * @return jsonString
	 */
	public String toJsonString() {
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		obj.put(JSON_TYPE, "calendar");
		obj.put(JSON_EVENT_INDEX, new Integer(this.getEventIndex()));
		obj.put(JSON_EVENT_NAME, this.getEventName());
		obj.put(JSON_START_DATE, fmt.print(getStartDateAndTime()));
		obj.put(JSON_END_DATE, fmt.print(getEndDateAndTime()));
		obj.put(JSON_REPEAT, new Boolean(repeating));
		if(repeating) {
			obj.put(JSON_FREQUENCY, frequency);
			if(hasStopDate()) {
				obj.put(JSON_STOPDATE, fmt.print(stopDate));
			} else {
				obj.put(JSON_STOPDATE, null);
			}
		}
		
		StringWriter out = new StringWriter();
	    try {
			JSONValue.writeJSONString(obj, out);
		} catch (IOException e) {
			System.out.println(MESSAGE_JSON_STRING_ERROR);
		}
	    return out.toString();
	}
	
	// TODO override equals method
	public boolean equals(Object obj) {
		if (!(obj instanceof CalendarEvent)) {
			return false;
		} else {
			CalendarEvent e = (CalendarEvent) obj;
			return this.getEventIndex() == e.getEventIndex() && this.getEventName().equals(e.getEventName()) &&
					this.getStartDateAndTime().equals(e.getStartDateAndTime()) && 
					this.endDateAndTime.equals(e.getEndDateAndTime());
		}
	}
	
	/**
	 * Used to compare to Event subclasses
	 * @return int
	 */
	public int compareTo(Event e) {
		if(e instanceof FloatingTask) {
			return 1;
		} else if (e instanceof CalendarEvent) {
			CalendarEvent cal = (CalendarEvent)e;
			int result = this.getStartDateAndTime().compareTo(cal.getStartDateAndTime());
			if (result == 0) {
				result = this.getEndDateAndTime().compareTo(cal.getEndDateAndTime());
				if(result == 0) {
					result = this.getEventName().compareTo(cal.getEventName());
				}
			}
			return result;
		} else if (e instanceof ToDoEvent) {
			ToDoEvent todo = (ToDoEvent)e;
			int result = this.getStartDateAndTime().compareTo(todo.getDeadline());
			if (result == 0) {
				result = this.getEventName().compareTo(todo.getEventName());
			}
			return result;
		}
		return 0;
	}
	
	/*
	 * Checks if dates are valid for this date and time
	 * and if end is after start
	 */
	public static boolean areValidDates(DateTime start, DateTime end) {
		return (start.isBefore(end) && start.isAfterNow());
	}
	// @@author A0145668R
	
	public void setDone(){
		//method stub for polymorthism
	}
	
	// @author A0145668R
	public boolean hasStopDate() {
		return stopDate != null;
	}
	
	public DateTime getStopDate() {
		return stopDate;
	}

	public void setStopDate(DateTime stopDate) {
		this.stopDate = stopDate;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public Event createCopy() {
		return new CalendarEvent(this);
	}
	// @@author A0145668R
}
