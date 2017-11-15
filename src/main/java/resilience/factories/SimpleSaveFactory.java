package resilience.factories;

import model.api.ContractSyncManager;
import model.api.ItemSyncManager;
import model.api.Manager;
import model.api.MessageSyncManager;
import model.entity.User;
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
	
	public void writeSave(User user, ContractSyncManager  contracts, ItemSyncManager items, MessageSyncManager messages) {
		header = new SimpleSave(user);
		data = new ClearData(header, contracts, items, messages, user);
		save = data;
		save.write(pathToSaveFile);
	}
	
	public void readSave() {
		header = new SimpleSave();
		data = new ClearData(header);
		save = data;
		
		save.read(pathToSaveFile);
	}

	public SimpleSave getHeader() {
		return header;
	}

	public void setHeader(SimpleSave header) {
		this.header = header;
	}

	public ClearData getData() {
		return data;
	}

	public void setData(ClearData data) {
		this.data = data;
	}

	public Save getSave() {
		return save;
	}

	public void setSave(Save save) {
		this.save = save;
	}

	public String getPathToSaveFile() {
		return pathToSaveFile;
	}

	public void setPathToSaveFile(String pathToSaveFile) {
		this.pathToSaveFile = pathToSaveFile;
	}
	
	
}
