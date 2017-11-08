package resilience.factories;

import model.api.Manager;
import resilience.api.Save;
import resilience.impl.ClearData;
import resilience.impl.SimpleSave;

public class SimpleSaveFactory {
	private SimpleSave header;
	private ClearData data;
	private Save save;
	
	private String pathToSaveFile;
	
	public SimpleSaveFactory(String pathToSaveFile) {
		this.pathToSaveFile = pathToSaveFile;
	}
	
	public void writeSave(String username, Manager db) {
		header = new SimpleSave(username);
		data = new ClearData(header, db);
		save = data;
		save.write(pathToSaveFile);
	}
	
	public void readSave() {
		header = new SimpleSave();
		data = new ClearData(header);
		save = data;
		
		save.read(pathToSaveFile);
	}	
}
