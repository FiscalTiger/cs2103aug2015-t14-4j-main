package easycheck.logicController;

import java.util.ArrayList;
import java.util.Stack;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.*;
import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.Event;
import easycheck.eventlist.ToDoEvent;

public class CommandExecutor {
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command.\n";
	private static final String MESSAGE_ADD_CMD_RESPONSE = "Added %s\n";
	private static final String MESSAGE_DISPLAY_CMD_EMPTY = "There aren't any events to display!\n";
	private static final String MESSAGE_DELETE_CMD_RESPONSE = "Deleted %s Successfully\n";
	private static final String MESSAGE_UPDATE_CMD_RESPONSE = "Updated %s to %s successfully\n";
	private static final String MESSAGE_INVALID_CALENDAR_DATES = "The start date must be before the end date and after the current date and time.\n";
	private static final String MESSAGE_INVALID_TODO_DEADLINE = "The deadline must be after the current date and time.\n";
	
	private ArrayList<Event> eventList;
	private Stack<ArrayList<Event>> undoStack;
	private Stack<ArrayList<Event>> redoStack;
	
	public CommandExecutor(ArrayList<Event> initEventList) {
		eventList = initEventList;
		undoStack = new Stack<ArrayList<Event>>();
		redoStack = new Stack<ArrayList<Event>>();
	}
	
	/**
	 * Updates the cached version of the user's events and 
	 * produces the response string to be printed to the user
	 * @param command
	 * @return responseString
	 * @author A0145668R
	 */
    
    public String executeCommand(Command command) {
        if (command instanceof Add) {
	        return add((Add)command);
        } else if(command instanceof Display) {
        	return display((Display)command);
        } else if(command instanceof Edit) {
	        return update((Edit)command);
        } else if(command instanceof Delete) {
	        return delete((Delete)command);
        } else if(command instanceof Undo) {
	        return undo((Undo)command);
        } else if(command instanceof Search) {
	        return search((Search)command);
        } else if(command instanceof Review) {
	        return review((Review)command);
        } else if(command instanceof SaveAt) {
	        return saveAt((SaveAt)command);
        } else if(command instanceof Exit) {
	        return exit((Exit)command);
        } else {
        	return MESSAGE_INVALID_COMMAND;
	    }
    }
	
	/**
	 * Creates the correct type of event
	 * and adds it to eventList
	 * @author A0145668R
	 */
    private String add(Add cmd) {
    	String response = "";
    	Event newEvent;
    	// has arguments for a calendar event
    	if (cmd.hasStart() && cmd.hasEnd()) {
    		if (CalendarEvent.areValidDates(cmd.getStart(), cmd.getEnd())) {
    			int eventIndex = eventList.size();
    			newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), cmd.getEnd());
    			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
    			undoStack.push(eventList);
    			eventList.add(newEvent);
    		} else {
    			response = MESSAGE_INVALID_CALENDAR_DATES;
    		}
    	  // has arguments for a to do event
    	} else if (!cmd.hasStart() && cmd.hasEnd()) {
    		if (ToDoEvent.isValidDeadline(cmd.getEnd())) {
    			int eventIndex = eventList.size();
    			newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd());
    			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
    			undoStack.push(eventList);
    			eventList.add(newEvent);
    		} else {
    			response = MESSAGE_INVALID_TODO_DEADLINE;
    		}
    	  // doesn't have time limits so it creates a floating task
    	} else if (!cmd.hasStart() && !cmd.hasEnd()) {
    		int eventIndex = eventList.size();
			newEvent = new Event(eventIndex, cmd.getTaskName());
			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
			undoStack.push(eventList);
			eventList.add(newEvent);
    	}
    	// response should have a response by this point
    	assert(!response.equals(""));
    	return response;
	}
	
	/* DISPLAY requires arguments to be of ""
	 * @author A0145668R
	 */
	private String display(Display cmd){
		String response = "";
		if(eventList.isEmpty()) {
			response = MESSAGE_DISPLAY_CMD_EMPTY;
		} else {
			for(Event e: eventList) {
				response += e;
			}
		}
		//Response should not be empty
		assert(!response.equals(""));
		return response;
	}
	
	/* UPDATE requires arguments to be of "Event name"+ "to" + "Updated Event" 
	 * @author A0126989H
	 */
	private String update(Edit cmd) {
		
		return null;
	}
	
	/* DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(Delete cmd) {
		return null;
	}

	private String undo(Undo cmd) {
		return "";
	}

	private String search(Search cmd) {
		return "";
	}

	private String review(Review cmd) {
		return "";
	}

	private String saveAt(SaveAt cmd) {
		return "Successfully Saved";
	}
	
	/* DISPLAY requires arguments to be of ""
	 * 
	 */
	private String exit(Exit cmd) {
		System.exit(1);
		return "";
	}
	
	public ArrayList<Event> getEventList(){
		return eventList;
	}

}
