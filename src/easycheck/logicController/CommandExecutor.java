package easycheck.logicController;

import java.util.ArrayList;
import java.util.Stack;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.*;
import easycheck.eventlist.Event;

public class CommandExecutor {
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command.\n";
	private static final String MESSAGE_ADD_CMD_RESPONSE = "Added %s\n";
	private static final String MESSAGE_DELETE_CMD_RESPONSE = "Deleted %s Successfully\n";
	private static final String MESSAGE_UPDATE_CMD_RESPONSE = "Updated %s to %s successfully\n";
	
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
	 */
    
    public String executeCommand(Command command) {
        if (command instanceof Add) {
	        return add((Add)command);
        } else if(command instanceof AddEvent) {
	        return addEvent((AddEvent)command);
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
	
	/* Add requires arguments to be of Events + Deadline
	 * if deadline is empty string, means its is a floating task
	 * @author A0126989H 
	 */
    private String add(Add cmd) {
    	return null;
	}

	private String addEvent(AddEvent cmd) {
		return null;
	}
	
	/* DISPLAY requires arguments to be of ""
	 * @author A0126989H
	 */
	private String display(Display cmd){
		return "";
	}
	
	/* UPDATE requires arguments to be of "Event name"+ "to" + "Updated Event" 
	 * @author A0126989H
	 */
	private String update(Edit cmd) {
		for (int i = 0; i<eventList.size(); i++){
			if (eventList.get(i).getEventName().contains(arguments[0])){
				eventList.get(i).setEventName(arguments[1]);
				break; //* @author A0126989H
			}
		}
		return String.format(MESSAGE_UPDATE_CMD_RESPONSE, arguments[0], arguments[1]);
	}
	
	/* DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(Delete cmd) {
		if (arguments == null) {
			return String.format(MESSAGE_DELETE_CMD_RESPONSE,eventList.remove(0).getEventName());
		} else {
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().contains(arguments[0])) {
					eventList.remove(i);
					break;
				}
			}
		}
		return String.format(MESSAGE_DELETE_CMD_RESPONSE, arguments[0]);
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
	private String exit(Exit cmd){
		System.exit(1);
		return "";
	}
	
	public ArrayList<Event> getEventList(){
		return eventList;
	}

}
