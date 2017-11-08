package resilience.impl;

import resilience.api.Save;

public abstract class SaveDecorator implements Save{
	protected Save save;
	
	public SaveDecorator(Save save) {
		this.save = save;
	}

	public void write(String path)
	{
		save.write(path);
	}
	
	public void read(String path){
		save.read(path);
	}
}
