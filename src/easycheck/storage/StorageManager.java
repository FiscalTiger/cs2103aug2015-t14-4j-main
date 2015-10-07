package easycheck.storage;

import easycheck.commandParser.Command;
import easycheck.eventlist.Event;
import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.ToDoEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Storage manager for Easy Check application.
 * Handles the storage of application's file storage and cached data
 * @author Andrew Pouleson
 */
public class StorageManager {
	private static final String EVENT_TYPE_CALENDAR_KEY = "calendar";
	private static final String EVENT_TYPE_TODO_KEY = "todo";
	private static final String JSON_TYPE = "type";
	
	private File easyCheckFile;
	private ArrayList<Event> easyCheckEvents;
	
	public StorageManager(String easyCheckFileName) {
		if (!checkFileExists(easyCheckFileName)){
			createFile(easyCheckFileName);
		}
		this.easyCheckFile = new File(easyCheckFileName);
		easyCheckEvents = readDataFromEasyCheckFile();
	}
	
	private boolean checkFileExists(String easyCheckFileName) {
		File newEasyCheckFile = new File(easyCheckFileName);
		if (newEasyCheckFile.exists()){
			return true; 
		}
		return false;
	}

	private void createFile(String easyCheckFileName) {
		File newEasyCheckFile = new File(easyCheckFileName);
		try {
			newEasyCheckFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	public ArrayList<Event> readDataFromEasyCheckFile() {
		Scanner scanner;
		JSONParser parser = new JSONParser();
		try {
			scanner = new Scanner(this.easyCheckFile);
			while (scanner.hasNextLine()){
				Object obj = parser.parse(scanner.nextLine());
				JSONObject jsonObj = (JSONObject)obj;
				String jsonObjType = (String) jsonObj.get(JSON_TYPE);
				if (jsonObjType == EVENT_TYPE_CALENDAR_KEY){
					easyCheckEvents.add(new CalendarEvent(jsonObj));
				} else if (jsonObjType == EVENT_TYPE_TODO_KEY){
					easyCheckEvents.add(new ToDoEvent(jsonObj));
				}
				// TODO handle if type is unrecognised
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// for parsing obj to jsonObj
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void writeDataToEasyCheckFile(ArrayList<Event> eventList){
		for (Event event: eventList){
			System.out.println(event.toJsonString());
		}
	}
}
