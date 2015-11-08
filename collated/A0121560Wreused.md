# A0121560Wreused
###### src\easycheck\commandParser\CommandTypes\Update.java
``` java
    private static final String REPEATING_DAILY = "daily";
	private static final String REPEATING_WEEKLY = "weekly";
	private static final String REPEATING_BIWEEKLY = "biweekly";
	private static final String REPEATING_MONTHLY = "monthly";
	private static final String REPEATING_YEARLY = "yearly";
	
	private boolean isRepeating = false;
	private String complete = null;
	private DateTime stopDate = null;
	private String frequency = null;
```
###### src\easycheck\commandParser\CommandTypes\Update.java
``` java
	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean repeating) {
		this.isRepeating = repeating;
	}
	public boolean hasStopDate() {
    	return stopDate != null;
    }
	public static boolean isValidFrequency(String frequency) {
		if(frequency.equals(REPEATING_DAILY) || frequency.equals(REPEATING_WEEKLY) ||
				frequency.equals(REPEATING_BIWEEKLY) || frequency.equals(REPEATING_MONTHLY) ||
				frequency.equals(REPEATING_YEARLY)) {
			return true;
		} else {
			return false;
		}
	}


	
}
```
