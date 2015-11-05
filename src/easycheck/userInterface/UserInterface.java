package easycheck.userInterface;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import easycheck.logicController.LogicController;

/*
* User Interface to take in User input and pass to Logic Controller.
*/

public class UserInterface {
	private static final String MESSAGE_ENTER_COMMAND = "@|magenta enter command: |@";
	private static final String MESSAGE_WELCOME = "@|green Welcome to Easy Check!\nHere are your current events and tasks.|@\n";
	public String userName;
	
	private Scanner sc = new Scanner(System.in);
	private LogicController logicController;

	public static void main(String[] args){
		AnsiConsole.systemInstall();
		String fileName;
		if(args.length == 0) {
			fileName = "myeasycheck.txt";
		} else {
			fileName = args[0];
		}
		UserInterface ui = new UserInterface(fileName);
		String userInput = "";
		String commandResponse = "";
        //quick hack to make the console display on startup
		ui.display(MESSAGE_WELCOME);
		ui.display(ui.executeCommand("display"));
		while(true) {
			ui.display(MESSAGE_ENTER_COMMAND);
			userInput = ui.getInput();
			commandResponse = ui.executeCommand(userInput);
			ui.display(commandResponse);
		}
	}
	
	public UserInterface(String easyCheckFileName) {
		logicController = new LogicController(easyCheckFileName);
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getInput() {
		String line = sc.nextLine();
		return line;
	}
	
	public String executeCommand(String userInput) {
		return logicController.executeCommand(userInput);
	}
	
	public void display(String msg) {
		System.out.print(ansi().render(msg));
		System.out.flush();
	}
}
