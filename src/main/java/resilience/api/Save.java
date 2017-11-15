package resilience.api;

public interface Save {
	void write(String path);
	boolean read(String path);
}
