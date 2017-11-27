package resilience.api;

public interface Save {
	
	/**
	 * @return
	 */
	String write();
	
	/**
	 * 
	 * @return
	 */
	int read(String s);
}
