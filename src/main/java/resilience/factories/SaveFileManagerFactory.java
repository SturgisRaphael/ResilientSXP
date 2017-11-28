package resilience.factories;

import resilience.api.Save;
import resilience.api.SaveFileManager;
import resilience.impl.SaveFileManagerImpl;

public class SaveFileManagerFactory {
	public SaveFileManager createDefaultSaveFileManager(Save save, String path) {
		return new SaveFileManagerImpl(save, path);
	}
}
