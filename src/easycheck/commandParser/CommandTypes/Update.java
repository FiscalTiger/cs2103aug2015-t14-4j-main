package easycheck.commandParser.CommandTypes;
import org.joda.time.DateTime;

import easycheck.commandParser.Command;
/**
 * Edit Command Type represents a parsed command for Easy Check application.
 * To be called:
 * 
 * getTaskName()
 * getNewEvent()
 * 
 * @author A0126989H
 */
public class Update extends Command {
	private String idx;
	private String newName;
	private DateTime start = null;
	private DateTime end = null;
	
	public Update(String idx, String newName) {
		this.idx = idx;
		this.newName = newName;
	}
	
	public Update(String idx, String newName, DateTime end){
		this.idx = idx;
		this.newName = newName;
		this.end = end;
	}
	
	public Update(String idx, String newName, DateTime start, DateTime end){
		this.idx = idx;
		this.newName = newName;
		this.start = start;
		this.end = end;
	}
	
	public String getTaskIdx(){
		return idx;
	}
	public String getNewName(){
		return newName;
	}

	public DateTime getStart(){
		return start;
	}
	public DateTime getEnd() {
		return end;
	}
	public boolean hasStart() {
		return !(start == null);
	}
	public boolean hasEnd() {
		return !(end == null);
	}
	
	
}
