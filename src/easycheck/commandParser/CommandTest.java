package easycheck.commandParser;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class CommandTest {

	@Test
	public void testExecuteCommand() throws IOException {
		String[] input1 = {"first task","tomorrow"};
		String[] input2 = {"2nd task","in two week"};
		String[] input3 = {"3rd task",""};
		String[] input4 = {"4th","next monday"};
		String[] input5 = {"5th task","in weeks"};
		String[] input6 = {"6th task",""};
		String[] input7 = {"7th task","tomorrow"};
		String[] input8 = {"8th task","in two week"};
		String[] input9 = {"9th task",""};
		//testing add
		testOneCommand("first task tomorrow","add",input1);
		testOneCommand("2nd task in two week","add",input2);
		testOneCommand("3rd task ","add",input3);
		testOneCommand("4th next monday","add",input4);
		testOneCommand("5th task in weeks","add",input5);
		testOneCommand("6th task ","add",input6);
		testOneCommand("7th task tomorrow","add",input7);
		testOneCommand("8th task in two week","add",input8);
		testOneCommand("9th task ","add",input9);
		
		//testing edit
		String[] input14 = {"first task","in 2 days"};
		String[] input15 = {"not inside yet","in two week"};
		testOneCommand("Updated Successfully","edit",input14);
		testOneCommand("Updated Successfully","edit",input15);
		
		//testing delete
		String[] input26 = {"in 2 days",""};
		String[] input27= {"2nd",""};
		testOneCommand("Delete Successfully","delete",input26);
		testOneCommand("Delete Successfully","delete",input27);
		
		
		//testing display
		testOneCommand("Display","display",input1);
		Command.checkCached();
	}
	
	private void testOneCommand(String expected, String command, String[] arguments) throws IOException {
		assertEquals(expected,Command.executeCommand(command,arguments));
	}

}
