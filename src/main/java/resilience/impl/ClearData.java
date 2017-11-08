package resilience.impl;

import model.api.Manager;
import resilience.api.Save;

public class ClearData extends SaveDecorator{
	private Manager db;
	
	public ClearData(Save save, Manager db) {
		super(save);
		this.db = db;
	}
	
	public ClearData(Save save) {
		super(save);
		this.db = null;
	}

	@Override
	public void write(String path){
		super.write(path);
		// db.watchlist()
	}
	
	@Override
	public void read(String path) {
		
	}

	public Manager getDb() {
		return db;
	}

	public void setDb(Manager db) {
		this.db = db;
	}
	
	
}
