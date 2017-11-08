package resilience.api;

public interface Save {
	void write(String path);
	void read(String path);
}
