package easycheck.logicController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import easycheck.commandParser.Command;
import easycheck.commandParser.CommandTypes.Add;
import easycheck.commandParser.CommandTypes.Delete;
import easycheck.commandParser.CommandTypes.Display;
import easycheck.commandParser.CommandTypes.Exit;
import easycheck.commandParser.CommandTypes.Invalid;
import easycheck.commandParser.CommandTypes.Redo;
import easycheck.commandParser.CommandTypes.Repeat;
import easycheck.commandParser.CommandTypes.Review;
import easycheck.commandParser.CommandTypes.SaveAt;
import easycheck.commandParser.CommandTypes.Search;
import easycheck.commandParser.CommandTypes.Undo;
import easycheck.commandParser.CommandTypes.Update;
import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.Event;
import easycheck.eventlist.FloatingTask;
import easycheck.eventlist.ToDoEvent;

public class CommandExecutor {
    private static int ZERO_OFFSET = 1;
    
	private static final String MESSAGE_ADD_CMD_RESPONSE = "Added %s\n";
	private static final String MESSAGE_DISPLAY_CMD_EMPTY = "There aren't any events to display!\n";
	private static final String MESSAGE_DELETE_CMD_EMPTY = "There aren't any events!\n";
	private static final String MESSAGE_DELETE_CMD_RESPONSE = "Deleted \"%s\" Successfully\n";
	private static final String MESSAGE_UPDATE_CMD_RESPONSE = "Updated %s to %s successfully\n";
	private static final String MESSAGE_INVALID_CALENDAR_DATES = "The start date must be before the end date and after the current date and time.\n";
	private static final String MESSAGE_INVALID_TODO_DEADLINE = "The deadline must be after the current date and time.\n";
	private static final String MESSAGE_UNDO_EMPTY_STACK = "There is nothing to undo\n";
	private static final String MESSAGE_REDO_EMPTY_STACK = "There is nothing to redo\n";
	
	private static final String PRINT_GROUP_HEADING_FLOATING = "Floating:";
	
	//@author A0126989H
	private static final String MESSAGE_SEARCH_CMD_EMPTY = "There aren't any events to search!\n";
	private static final String MESSAGE_SEARCH_CMD_NOTFOUND = "There are no such events!\n";
	
	
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
        } else if(command instanceof Update) {
	        return update((Update)command);
        } else if(command instanceof Delete) {
	        return delete((Delete)command);
        } else if(command instanceof Undo) {
	        return undo((Undo)command);
        } else if(command instanceof Redo) {
	        return redo((Redo)command);
        } else if(command instanceof Search) {
	        return search((Search)command);
        } else if(command instanceof Review) {
	        return review((Review)command);
        } else if(command instanceof SaveAt) {
	        return saveAt((SaveAt)command);
        } else if(command instanceof Exit) {
	        return exit((Exit)command);
        } else if(command instanceof Invalid) {
            return Invalid((Invalid)command);
        } else {
        	return command.toString();
	    }
    }
	
	private String Invalid(Invalid command) {
        return command.getInvalidMessage();
    }

    /**
	 * Creates the correct type of event
	 * and adds it to eventList
	 * @author A0145668R
	 */
    private String add(Add cmd) {
    	assert(cmd.getTaskName() != null);
    	
    	String response = "";
    	Event newEvent;
    	// has arguments for a calendar event
    	if (cmd.hasStart() && cmd.hasEnd()) {
    		if (CalendarEvent.areValidDates(cmd.getStart(), cmd.getEnd())) {
    			int eventIndex = eventList.size() + 1;
    			newEvent = new CalendarEvent(eventIndex, cmd.getTaskName(), cmd.getStart(), cmd.getEnd());
    			undoStack.push(new ArrayList<Event>(eventList));
    			eventList.add(newEvent);
    			sort();
    			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
    		} else {
    			response = MESSAGE_INVALID_CALENDAR_DATES;
    		}
    	  // has arguments for a to do event
    	} else if (!cmd.hasStart() && cmd.hasEnd()) {
    		if (ToDoEvent.isValidDeadline(cmd.getEnd())) {
    			int eventIndex = eventList.size() + 1;
    			newEvent = new ToDoEvent(eventIndex, cmd.getTaskName(), cmd.getEnd());
    			undoStack.push(new ArrayList<Event>(eventList));
    			eventList.add(newEvent);
    			sort();
    			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);
    		} else {
    			response = MESSAGE_INVALID_TODO_DEADLINE;
    		}
    	  // doesn't have time limits so it creates a floating task
    	} else if (!cmd.hasStart() && !cmd.hasEnd()) {
    		int eventIndex = eventList.size() + 1;
			newEvent = new FloatingTask(eventIndex, cmd.getTaskName());
			undoStack.push(new ArrayList<Event>(eventList));
			eventList.add(newEvent);
			sort();
			response = String.format(MESSAGE_ADD_CMD_RESPONSE, newEvent);

    	}
    	// response should have a response by this point
    	assert(!response.equals(""));
    	return response;
	}
	
	private void sort() {
		Collections.sort(eventList);
		reIndex();
	}

	/**
	 * Displays all events
	 * @author A0145668R
	 */
	private String display(Display cmd){
		String response = "";
		if(cmd.isIndex()) {
			response += eventList.get(cmd.getEventIndex() + 1);
		} else if(cmd.isFloating()) {
			PrintGroup printGroup = new PrintGroup(PRINT_GROUP_HEADING_FLOATING);
			for(Event e: eventList) {
				if(e instanceof FloatingTask) {
					printGroup.addEntry(e);
				}
			}
			response = printGroup.toString();
		} else if(cmd.isDone()) {
			
		} else {
			if(eventList.isEmpty()) {
				response = MESSAGE_DISPLAY_CMD_EMPTY;
			} else {
				for(Event e: eventList) {
					response += e;
				}
			}
		}
		//Response should not be empty
		assert(!response.equals(""));
		return response;
	}
	
	/* UPDATE requires arguments to be of "Event name" + "to" + "Updated Event" 
	 * @author A0126989H
	 */
	private String update(Update cmd) {
        for (int i = 0; i < eventList.size(); i++){
            if (eventList.get(i).getEventName().contains(cmd.getTaskName())){
                eventList.get(i).setEventName(cmd.getNewEvent());
            }
        }
        return String.format(MESSAGE_UPDATE_CMD_RESPONSE, cmd.getTaskName(), cmd.getNewEvent());

	}
	//@ author A0126989H
	/* DELETE requires arguments to be of "Event name" or "part of event name"
	 * 
	 */
	private String delete(Delete cmd) {
		String arguments = cmd.getTaskName();
		String removeEvent = "";
		// Case 1: When the command is "delete"
		if (arguments == null) {
			if (eventList.size() != 0) {
				undoStack.push(new ArrayList<Event>(eventList));
				removeEvent = eventList.remove(0).getEventName();
				reIndex();
				return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent );
			} else {
				reIndex();
				return MESSAGE_DELETE_CMD_EMPTY;
			}
		// Case 2: When the command is "delete + index"
		}else if (isNumeric(arguments)){
		    int index = Integer.parseInt(arguments);
            if (eventList.size() < index || index < 1) {
                return MESSAGE_SEARCH_CMD_NOTFOUND;
            } else if (eventList.size() != 0) {
            	undoStack.push(new ArrayList<Event>(eventList));
                removeEvent = eventList.remove(index - ZERO_OFFSET).getEventName();
                reIndex();
                return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
            } else {
                reIndex();
				return MESSAGE_DELETE_CMD_EMPTY;
			}
		// Case 3: When the command is "delete + EventName"
		} else {
			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getEventName().contains(arguments.toLowerCase())) {
					undoStack.push(new ArrayList<Event>(eventList));
					removeEvent = eventList.remove(i).getEventName();
					reIndex();
					break;
				}
			}
		}
		return String.format(MESSAGE_DELETE_CMD_RESPONSE, removeEvent);
	}
	// @author A0126989H
	// ReIndexing all the event in the EventList
	public void reIndex(){
		for(int i = 0; i < eventList.size(); i++) {
			eventList.get(i).setEventIndex(i + 1);
		}
	}
    // @author A0126989H
    // Checking if the delete argument is Index number
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
    
    

	private String undo(Undo cmd) {
		if(undoStack.isEmpty()) {
			return MESSAGE_UNDO_EMPTY_STACK;
		} else {
			redoStack.clear();
			redoStack.push(new ArrayList<Event>(eventList));
			eventList = undoStack.pop();
		}
		Display disp = new Display();
		disp.setDefaultFlag(true);
		
		return display(disp);
	}
	
	private String redo(Redo cmd) {
		if(redoStack.isEmpty()) {
			return MESSAGE_REDO_EMPTY_STACK;
		} else {
			undoStack.push(new ArrayList<Event>(eventList));
			eventList = redoStack.pop();
		}
		
		Display disp = new Display();
		disp.setDefaultFlag(true);
		
		return display(disp);
	}
	
	//@author A0126989H
	private String search(Search cmd) {
		String response = "";
		if(eventList.isEmpty()) {
			response = MESSAGE_SEARCH_CMD_EMPTY;
		} else {
			for(Event e: eventList) {
				if (e.getEventName().toLowerCase().contains(cmd.getArgument()[0].toLowerCase()))
					response += e;
			}
		}
		
		if (response.equals("")){
			response = MESSAGE_SEARCH_CMD_NOTFOUND ;
		}
		//Response should not be empty
		assert(!response.equals(""));
		return response;
	}
	//TODO
	private String review(Review cmd) {
		return "";
	}
	//TODO
	private String saveAt(SaveAt cmd) {
		return "Successfully Saved";
	}
	//TODO
    private String repeat(Repeat cmd) {
        return null;
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
