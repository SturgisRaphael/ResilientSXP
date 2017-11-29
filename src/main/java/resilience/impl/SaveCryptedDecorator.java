package resilience.impl;

import model.entity.User;
import resilience.api.Save;

/**
 * Resilient encrypted data
 */
public class SaveCryptedDecorator extends SaveDecorator implements Save{

	/**
	 * Creates a SaveCryptedDecorator
	 * @param save Save
	 * @param u User
	 */
	public SaveCryptedDecorator(Save save, User u) {
		super(save, u);
	}
	
	/**
	 * @return String with the encrypted content of the save 
	 */
	@Override
	public String write() {
		return crypt(save.write());
	}

	/**
	 * To be implemented
	 * @param write
	 * @return
	 */
	private String crypt(String write) {
		return write;
	}

	/**
	 * 
	 * @param s String representing the encrypted content of the save
	 * @return
	 */
	@Override
	public int read(String s) {
		return save.read(decrypt(s));
	}

	/**
	 * To be implemented
	 * @param s
	 * @return
	 */
	private String decrypt(String s) {
		return s;
	}
	
}
