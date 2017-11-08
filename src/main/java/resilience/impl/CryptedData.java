package resilience.impl;

import model.api.Manager;
import resilience.api.Save;

public class CryptedData extends SaveDecorator{
	private Manager<?> db;
	
	CryptedData(Save save, Manager<?> db) {
		super(save);
		this.db = db;
	}

	@Override
	public void write(String path){
		super.write(path);
		// db.watchlist()
	}
	
	@Override
	public void read(String path) {
		
	}
}
