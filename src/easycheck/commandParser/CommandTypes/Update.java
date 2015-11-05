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
 * @author A0121560W
 */
public class Update extends Command {
	private static final String UPDATE_COMMAND_TYPE_START = "start";
    private static final String UPDATE_COMMAND_TYPE_END = "end";
    private static final String UPDATE_COMMAND_TYPE_NAME = "name";
    private static final String UPDATE_COMMAND_TYPE_TYPE = "type";
	
	private String idx;
	private String newName;
	private DateTime start = null;
	private DateTime end = null;
	private String type = null;
	
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
	public Update(String idx, DateTime date, String type){
		this.idx = idx;
		this.type = type;
		if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_END)){
			this.end = date;
		} else if (type.equalsIgnoreCase(UPDATE_COMMAND_TYPE_START)){
			this.start = date;
		}
	}
	public Update(String idx, String type, String name){
		this.idx = idx;
		this.type = type;
		this.newName = name;
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
	public String getType() {
		return type;
	}
	public boolean hasStart() {
		return !(start == null);
	}
	public boolean hasEnd() {
		return !(end == null);
	}
	public boolean hasType() {
		return !(type == null);
	}
	
	
}
