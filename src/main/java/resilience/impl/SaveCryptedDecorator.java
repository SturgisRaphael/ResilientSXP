package resilience.impl;

import model.entity.User;
import resilience.api.Save;

public class SaveCryptedDecorator extends SaveDecorator implements Save{

	public SaveCryptedDecorator(Save save, User u) {
		super(save, u);
	}
	
	@Override
	public String write() {
		return crypt(save.write());
	}

	private String crypt(String write) {
		return write;
	}

	@Override
	public int read(String s) {
		return save.read(decrypt(s));
	}

	private String decrypt(String s) {
		return s;
	}
	
}
