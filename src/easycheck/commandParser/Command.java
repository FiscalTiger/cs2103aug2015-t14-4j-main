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

            enum COMMAND_TYPE {
                ADD, ADD_EVENT, EDIT, DELETE, UNDO, SEARCH, REVIEW, SAVE_AT
            };

        public static String add(String[] arguments){
            return "";
        }
        public static String addEvent(String[] arguments){
            return "";
        }
        public static String edit(String[] arguments){
            return "";
        }
        public static String delete(String[] arguments){
            return "";
        }
        public static String undo(String[] arguments){
            return "";
        }
        public static String search(String[] arguments){
            return "";
        }
        public static String review(String[] arguments){
            return "";
        }
        public static String saveAt(String[] arguments){
            return "Successfully Saved";
        }
        }