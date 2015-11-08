package easycheck.commandParser.CommandTypes;
import easycheck.commandParser.Command;

/**
 * @@author A0121560W
 */

public class Help extends Command{
	private String topic = null;
	public Help(){
		
	}
	public Help(String topic){
		this.topic = topic;
	} 
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public boolean hasTopic(){
		return !(this.topic == null);
	}
}
