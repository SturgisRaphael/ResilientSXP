package resilience.impl;

import java.util.Collection;

import model.api.ItemSyncManager;
import model.entity.Item;
import model.entity.User;
import model.syncManager.ItemSyncManagerImpl;
import resilience.api.Save;

public class ItemsDecorator extends SaveDecorator implements Save{
	private ItemSyncManager items;
	
	public ItemsDecorator(Save save, User u, ItemSyncManager items) {
		super(save, u);
		this.items = items;
	}
	
	public ItemsDecorator(Save save) {
		super(save);
		items = new ItemSyncManagerImpl();
	}

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
		return save.write() + "\n" + "<items>" + "\n" + result + "</items>";
	}

	@Override
	public int read(String s) {
		return save.read(s);
	}

	public ItemSyncManager getItems() {
		return items;
	}
}
