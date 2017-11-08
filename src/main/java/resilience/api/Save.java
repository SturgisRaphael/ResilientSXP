package resilience.api;

import controller.Application;

public interface Save {
	void write(String path);
	void read(String path);
}
