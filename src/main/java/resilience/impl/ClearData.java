package resilience.impl;

import model.api.Manager;
import resilience.api.Save;

public class ClearData extends SaveDecorator{
	private Manager db;
	
	ClearData(Save save, Manager db) {
		super(save);
		this.db = db;
	}

	@Override
	public void write(String path){
		// db.watchlist()
	}
	
	@Override
	public void read(String path) {
		
	}
}
