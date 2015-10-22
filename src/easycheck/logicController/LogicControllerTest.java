package easycheck.logicController;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class LogicControllerTest {

	@Test
	public void testLogicController() {
		String testFileName = "testFile.txt";
		LogicController testLogic = new LogicController(testFileName);
		File testFile = new File(testFileName);
		assertEquals(true, testFile.exists());
		testFile.delete();
		
		
	}

	@Test
	public void testExecuteCommand() {
		String testFileName = "testFile.txt";
		LogicController testLogic = new LogicController(testFileName);
		File testFile = new File(testFileName);
		assertEquals("Added 1. testTask\n\n", testLogic.executeCommand("add testTask"));
		assertEquals("Deleted testTask Successfully\n", testLogic.executeCommand("delete testTask"));
		testFile.delete();
		// TODO add more test cases more more command types
		
	}

}
