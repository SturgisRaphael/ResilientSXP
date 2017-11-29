package resilience.api;

import java.io.IOException;

public interface SaveFileManager {
	public void save() throws IOException;
	public void load();
}
