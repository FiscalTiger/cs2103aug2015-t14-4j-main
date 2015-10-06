package easycheck.storage;

import easycheck.commandParser.Command;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

/*
 * Storage manager for Easy Check application.
 * Handles the storage of application's file storage and cached data
 * @author Andrew Pouleson
 */
public class StorageManager {
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

	public String execute(Command cmd) {
		return null;
	}
	
	private ArrayList<Event> readDataFromEasyCheckFile() {
		return null;
	}
	
}
