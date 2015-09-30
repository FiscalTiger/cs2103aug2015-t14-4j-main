package easycheck.storage;

import easycheck.commandParser.Command;
import java.io.File;
import java.util.ArrayList;

/*
 * Storage manager for Easy Check application.
 * Handles the storage of application's file storage and cached data
 * @author Andrew Pouleson
 */
public class StorageManager {
	private File easyCheckFile;
	private ArrayList<EventInterface> easyCheckEvents;
	
	public StorageManager(String easyCheckFileName) {
		easyCheckFile = new File(easyCheckFileName);
		easyCheckEvents = readDataFromEasyCheckFile();
	}
	
	public String execute(Command cmd) {
		return null;
	}
	
	private ArrayList<EventInterface> readDataFromEasyCheckFile() {
		return null;
	}
	
}
