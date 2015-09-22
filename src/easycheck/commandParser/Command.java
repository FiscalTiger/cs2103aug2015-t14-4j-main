package easycheck.commandParser;

/**
 * Command Type represents a parsed command for Easy Check application.
 *
 */
public class Command {
    String commandType, command;

    public Command(String commandType, String[] arguments) {
        // TODO Auto-generated constructor stub
    }
    
    // add <task_name >, <<due_date>>
    // add_event <event_name>, <start_date>, <<end_date>>
    // add_repeat <task_name>, <recurring_period>
    // update <task_index>, <task> <<new_due_date>>
    // delete <task_index>
    // complete <task_index>
    
    // ///////// not implemented for v0.1
    // label <task_index>, <label>
    // remind <task_index>, <reminder_date>
    // search <search_term>
    // display
    // archive <task_index>
    // archive_done
    // delete_done
    // overdue
    // delete_today
    // store_location
    // next
    // undo
    // exit


}
