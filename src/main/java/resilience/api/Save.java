package resilience.api;

public interface Save {
	
	/**
	 * @return String representing all data of a SyncManager
	 */
	String write();
	
	/**
	 * Inputs all data in a SyncManager
	 * @param s String containing all properties and their value
	 * @return number of characters read
	 */
	int read(String s);
}
