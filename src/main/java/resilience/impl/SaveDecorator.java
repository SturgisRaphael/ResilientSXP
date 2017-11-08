package resilience.impl;

import resilience.api.Save;

public abstract class SaveDecorator implements Save{
	protected Save save;
	
	public SaveDecorator(Save save) {
		this.save = save;
	}

	public void write()
	{
		save.write();
	}
	
	public String read(){
		return save.read();
	}
}
