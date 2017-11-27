package resilience.impl;

import model.entity.User;
import resilience.api.Save;

public abstract class SaveDecorator implements Save{
	protected Save save;
	protected User u;
	
	public SaveDecorator(Save save, User u) {
		super();
		this.save = save;
		this.u = u;
	}

	public SaveDecorator(Save save) {
		super();
		this.save = save;
		this.u = new User();
	}

	@Override
	public String write() {
		return save.write();
	}

	@Override
	public int read(String s) {
		return save.read(s);
	}

	public Save getSave() {
		return save;
	}

	public User getU() {
		return u;
	}
}
