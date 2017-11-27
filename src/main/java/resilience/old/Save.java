package resilience.old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface Save {
	public void write(String path);
	public boolean read(String path);
}
