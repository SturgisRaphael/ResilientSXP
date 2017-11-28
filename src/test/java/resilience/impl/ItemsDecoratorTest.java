package resilience.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.api.ItemSyncManager;
import model.entity.ElGamalSignEntity;
import model.entity.Item;
import model.entity.User;
import model.syncManager.ItemSyncManagerImpl;
import resilience.api.Save;

public class ItemsDecoratorTest {
	private static ItemSyncManager items;
	private static User u;
	private static DatedHeader datedHeader;
	private static String testString;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		items = new ItemSyncManagerImpl();
		
		items.begin();
		
		ElGamalSignEntity signature = new ElGamalSignEntity();
		signature.setR(new BigInteger("10000"));
		signature.setS(new BigInteger("10001"));

		u = new User();
		u.setCreatedAt(new Date());
		u.setId("0001");
		u.setNick("username");
		u.setPasswordHash(null);
		
		Item i = new Item();
		i.setTitle("Title");
		i.setDescription("description");
		i.setCreatedAt(new Date());
		i.setPbkey(new BigInteger("101"));
		i.setUsername("username");
		i.setUserid("0001");
		i.setSignature(signature);
		
		items.persist(i);
		
		datedHeader = new DatedHeader(u);
		
		testString = "<Header>\n" + 
				"User [username=username, id=0001]\n" + 
				"Date [date=Mon Nov 27 21:59:47 CET 2017]\n" + 
				"</Header>\n" + 
				"<items>\n" + 
				"Item [id=228337F7-77E8-403F-BBDA-AF26E418E6F5, title=Title, description=description, createdAt=Mon Nov 27 21:59:47 CET 2017, pbkey=101, username=username, userid=0001, signature=ElGamalSignEntity [r=10000, s=10001]]\n" + 
				"</items>";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testWrite() {
		Save itemsDecorator = new ItemsDecorator(datedHeader, u, items);
		System.out.print(itemsDecorator.write());
		
		//TODO: Actual test for return value
	}

	@Test
	public void testRead() {
		ItemsDecorator itemsDecorator = new ItemsDecorator(datedHeader);
		items.close();
		items = itemsDecorator.getItems();
		itemsDecorator.read(testString);
		Collection<Item> itemsCollection = items.findAll();
		items.end();
		Item item = null;
		for(Item i : itemsCollection)
		{
			item = i;
		}
		
		itemsDecorator.getItems().close();
		assertEquals(item.getTitle(), "Title");
		assertEquals(item.getDescription(), "description");
		assertEquals(item.getCreatedAt().toString(), "Mon Nov 27 21:59:47 CET 2017");
		assertEquals(item.getPbkey(), new BigInteger("101"));
		assertEquals(item.getUsername(), "username");
		assertEquals(item.getUserid(), "0001");
		assertEquals(item.getSignature().toString(), "ElGamalSignEntity [r=10000, s=10001]");
	}

}
