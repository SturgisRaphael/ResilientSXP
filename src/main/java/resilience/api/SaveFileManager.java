package resilience.api;

import java.io.IOException;

/**
 * Methods used to link files and saves 
 */
public interface SaveFileManager {
	/**
	 * Inputs all user data into a file
	 * @throws IOException
	 */
	public void save() throws IOException;
	
	/**
	 * Reads a file and sorts the data
	 */
	public void load();
}
