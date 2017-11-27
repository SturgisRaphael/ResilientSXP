package resilience.old;

import model.api.Manager;
import resilience.old.Save;

public class CryptedData extends SaveDecoratorOld{
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
	public boolean read(String path) {
		return false;
		
	}
}
