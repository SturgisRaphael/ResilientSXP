package resilience.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import model.api.ItemSyncManager;
import model.entity.ElGamalSignEntity;
import model.entity.Item;
import model.entity.User;
import model.syncManager.ItemSyncManagerImpl;
import resilience.api.Save;
/**
 * Resilient items
 */
public class ItemsDecorator extends SaveDecorator implements Save{
	private ItemSyncManager items;
	
	/**
	 * Creates an ItemsDecorator
	 * @param save Save
	 * @param u User
	 * @param items ItemSyncManager
	 */
	public ItemsDecorator(Save save, User u, ItemSyncManager items) {
		super(save, u);
		this.items = items;
	}
	
	/**
	 * Creates an ItemsDecorator
	 * @param save Save
	 */
	public ItemsDecorator(Save save) {
		super(save);
		items = new ItemSyncManagerImpl();
	}

	/**
	 * @return String representing all items in the ItemSyncManager <code>items</code>
	 */
	@Override
	public String write() {
		Collection<Item> itemsCollection = items.findAll();
		String result = new String();
		for(Item i : itemsCollection)
		{
			if(i.getUserid() == u.getId()) {
				result += i.toString() + "\n";
			}
		}
		items.end();
		return save.write() + "\n" + "<items>" + "\n" + result + "</items>";
	}

	/**
	 * Reads a string and adds each item found in the ItemSyncManager <code>items</code>
	 * @param s String representing a list of items with their properties
	 * @return number of character read
	 */
	@Override
	public int read(String s) {
		int offset = save.read(s);
		String str = s.substring(offset) ;
		String itemBegin = "Item [";
		String itemEnd = "]]";
		
		items.begin();
		
		offset = str.indexOf(itemBegin);
		
		while (str.indexOf(itemBegin) != -1) {
			Item i = parseItem(str.substring(str.indexOf(itemBegin) + itemBegin.length(), str.indexOf(itemEnd)));
			items.persist(i);
			offset += str.indexOf(itemEnd)+ itemEnd.length();
			str = str.substring(str.indexOf(itemEnd)+ itemEnd.length());
		}
		
		
		String endOfItems = "</items>";
		return s.indexOf(endOfItems) + endOfItems.length();
	}

	/**
	 * Parses a string and returns its appropriate item
	 * @param substring String to be parsed
	 * @return Item
	 */
	private Item parseItem(String substring) {
		Item i = new Item();
		String titleBegin = ", title=";
		String descriptionBegin = ", description=";
		String createdAtBegin = ", createdAt=";
		String pbkeyBegin = ", pbkey=";
		String usernameBegin = ", username=";
		String userIdBegin = ", userid=";
		String signatureBegin = ", signature=";
		
		String title = substring.substring(substring.indexOf(titleBegin) + titleBegin.length(), substring.indexOf(descriptionBegin));
		i.setTitle(title);
		
		String description = substring.substring(substring.indexOf(descriptionBegin) + descriptionBegin.length(), substring.indexOf(createdAtBegin));
		i.setDescription(description);
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	    try {
			date =  df.parse(substring.substring(substring.indexOf(createdAtBegin) + createdAtBegin.length(), substring.indexOf(pbkeyBegin)));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	    i.setCreatedAt(date);
	    
	    String key = substring.substring(substring.indexOf(pbkeyBegin) + pbkeyBegin.length(), substring.indexOf(usernameBegin));
	    BigInteger pbKey = new BigInteger(key);
	    i.setPbkey(pbKey);

	    String username = substring.substring(substring.indexOf(usernameBegin) + usernameBegin.length(), substring.indexOf(userIdBegin));
	    i.setUsername(username);
	    
	    String userId = substring.substring(substring.indexOf(userIdBegin) + userIdBegin.length(), substring.indexOf(signatureBegin));
	    i.setUserid(userId);
	    
	    ElGamalSignEntity signature = parseSignature(substring.substring(substring.indexOf(signatureBegin) + signatureBegin.length()));
		i.setSignature(signature);
		
		return i;
		
	}

	/**
	 * Creates an ElGamal signature 
	 * @param substring String to be parsed
	 * @return ElGamalSignEntity
	 */
	private ElGamalSignEntity parseSignature(String substring) {
		ElGamalSignEntity signature = new ElGamalSignEntity();
		
		String rBegin = "ElGamalSignEntity [r="	;
		String sBegin = ", s=";
		
		signature.setR(new BigInteger(substring.substring(substring.indexOf(rBegin) + rBegin.length(), substring.indexOf(sBegin))));
		signature.setS(new BigInteger(substring.substring(substring.indexOf(sBegin) + sBegin.length())));
		
		return signature;
	}

	/**
	 * 
	 * @return the ItemSyncManager attribute <code>items</code>
	 */
	public ItemSyncManager getItems() {
		return items;
	}
}
