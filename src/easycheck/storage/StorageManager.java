package easycheck.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import easycheck.eventlist.CalendarEvent;
import easycheck.eventlist.Event;
import easycheck.eventlist.FloatingTask;
import easycheck.eventlist.ToDoEvent;

/*
 * Storage manager for Easy Check application.
 * Handles the storage of application's file storage and cached data
 * @author Andrew Pouleson
 * 
 */
public class StorageManager {
	private static final String EVENT_TYPE_CALENDAR_KEY = "calendar";
	private static final String EVENT_TYPE_TODO_KEY = "todo";
	private static final String EVENT_TYPE_BASE_KEY = "floating";
	private static final String JSON_TYPE = "type";
	
	
	private File easyCheckFile;
	
	public StorageManager(String easyCheckFileName) {
		if (!checkFileExists(easyCheckFileName)){
			createFile(easyCheckFileName);
		}
		easyCheckFile = new File(easyCheckFileName);
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
		ArrayList<Event> easyCheckEvents = new ArrayList<Event>();
		JSONParser parser = new JSONParser();
		try {
			scanner = new Scanner(easyCheckFile);
			while (scanner.hasNextLine()){
				Object obj = parser.parse(scanner.nextLine());
				JSONObject jsonObj = (JSONObject)obj;
				String jsonObjType = (String) jsonObj.get(JSON_TYPE);
				if (jsonObjType.equals(EVENT_TYPE_CALENDAR_KEY)){
					easyCheckEvents.add(new CalendarEvent(jsonObj));
				} else if (jsonObjType.equals(EVENT_TYPE_TODO_KEY)){
					easyCheckEvents.add(new ToDoEvent(jsonObj));
				} else if (jsonObjType.equals(EVENT_TYPE_BASE_KEY)){
					easyCheckEvents.add(new FloatingTask(jsonObj));
					
				}
				// TODO handle if type is unrecognised
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// for parsing obj to jsonObj
			e.printStackTrace();
		} 
		
		return easyCheckEvents;
	}
	
	public void writeDataToEasyCheckFile(ArrayList<Event> eventList) throws IOException{
		FileWriter writer = new FileWriter(easyCheckFile);
		for (Event event: eventList){
			writer.write(event.toJsonString() +"\n");
			
		}
		writer.close();
	}
}
