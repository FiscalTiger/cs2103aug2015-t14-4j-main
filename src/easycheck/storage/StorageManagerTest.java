//@@author A0121560W
package easycheck.storage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import easycheck.logicController.LogicController;

public class StorageManagerTest {
	
	
	
	@Test
	public void testStorageManager() {
		String testFileName1 = "testFile1.txt";
		String testFileName2 = "testFile2.txt";
		File testFile1 = new File(testFileName1);
		try {
			testFile1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StorageManager testManager1 = new StorageManager(testFileName1);
		assertEquals(true, testFile1.exists());
		
		StorageManager testManager2 = new StorageManager(testFileName2);
		File testFile2 = new File(testFileName2);
		try {
			testFile2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		assertEquals(true, testFile2.exists());
		
		testFile1.delete();
		testFile2.delete();
	}

	@Test
	public void testReadDataFromEasyCheckFile() {
		// note, will have to manually delete testFile.txt if test fail because it will not continue to the deletion
		String testFileName = "testFile.txt";
		StorageManager testManager = new StorageManager(testFileName);
		assertEquals(true, testManager.readDataFromEasyCheckFile().isEmpty());
		LogicController testLogic = new LogicController(testFileName);
		testLogic.executeCommand("add firstTestTask");
		assertEquals(1, testManager.readDataFromEasyCheckFile().size());
		testLogic.executeCommand("add secondTestTask");
		assertEquals(2, testManager.readDataFromEasyCheckFile().size());
		testLogic.executeCommand("delete first");
		assertEquals(1, testManager.readDataFromEasyCheckFile().size());
		assertEquals(2, testManager.readDataFromEasyCheckFile().get(0).getEventIndex());
		assertEquals("secondTestTask", testManager.readDataFromEasyCheckFile().get(0).getEventName());
		File testFile = new File(testFileName);
		testFile.delete();
	}

	@Test
	public void testWriteDataToEasyCheckFile() {
		//this is currently tested by testReadData, using testLogic.executeCommand, of add type. change to manually add to file and read/write	
	}

}
