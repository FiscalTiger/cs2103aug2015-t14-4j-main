package easycheck.logicController;

import java.util.ArrayList;
import java.util.Stack;

import easycheck.commandParser.Command;
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
        switch (command.getCommandType()) {
	        case ADD:
	            return add(command.getCommandArguments());
	        case ADD_EVENT:
	            return addEvent(command.getCommandArguments());
	        case DISPLAY:
	            return display(command.getCommandArguments());
	        case UPDATE:
	            return update(command.getCommandArguments());
	        case DELETE:
	            return delete(command.getCommandArguments());
	        case UNDO:
	            return undo(command.getCommandArguments());
	        case SEARCH:
	            return search(command.getCommandArguments());
	        case REVIEW:
	            return review(command.getCommandArguments());
	        case SAVE_AT:
	            return saveAt(command.getCommandArguments());
	        default:
	            return MESSAGE_INVALID_COMMAND;
	        }
    }
	
	/* Add requires arguments to be of Events + Deadline
	 * if deadline is empty string, means its is a floating task
	 * 
	 */
    private String add(String[] arguments) {
    	int eventIndex = eventList.size();
		eventList.add(new Event(eventIndex, arguments[0]));
    	return String.format(MESSAGE_ADD_CMD_RESPONSE, arguments[0]);
	}

	private String addEvent(String[] arguments) {
		return null;
	}
	
	/* DISPLAY requires arguments to be of ""
	 * 
	 */
	private String display(String[] arguments){
		String responseTxt = "Your events:\n";
		for (int i = 0; i < eventList.size(); i++){
			responseTxt += eventList.get(i);
		}
		return responseTxt;
	}
	
	/* UPDATE requires arguments to be of "Event name" + "Updated Event" 
	 * 
	 */
	private String update(String[] arguments) {
		for (int i = 0; i<eventList.size(); i++){
			if (eventList.get(i).getEventName().contains(arguments[0])){
				eventList.get(i).setEventName(arguments[1]);
			}
		}
		return String.format(MESSAGE_UPDATE_CMD_RESPONSE, arguments[0], arguments[1]);
	}
	
	/* DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(String[] arguments) {
		for (int i = 0; i<eventList.size(); i++){
			if (eventList.get(i).getEventName().contains(arguments[0])){
				eventList.remove(i);
			}
		}
		return String.format(MESSAGE_DELETE_CMD_RESPONSE, arguments[0]);
	}

	private String undo(String[] arguments) {
		return "";
	}

	private String search(String[] arguments) {
		return "";
	}

	private String review(String[] arguments) {
		return "";
	}

	private String saveAt(String[] arguments) {
		return "Successfully Saved";
	}
	
	public ArrayList<Event> getEventList(){
		return eventList;
	}

}
