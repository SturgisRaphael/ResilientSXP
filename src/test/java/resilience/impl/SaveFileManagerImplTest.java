package resilience.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import model.api.ContractSyncManager;
import model.api.EstablisherType;
import model.api.ItemSyncManager;
import model.api.MessageSyncManager;
import model.api.Status;
import model.api.Wish;
import model.entity.ContractEntity;
import model.entity.ElGamalSignEntity;
import model.entity.Item;
import model.entity.Message;
import model.entity.User;
import model.entity.Message.ReceptionStatus;
import model.syncManager.ContractSyncManagerImpl;
import model.syncManager.ItemSyncManagerImpl;
import model.syncManager.MessageSyncManagerImpl;
import resilience.api.Save;
import resilience.api.SaveFileManager;
import resilience.factories.SaveFactory;
import resilience.factories.SaveFileManagerFactory;

public class SaveFileManagerImplTest {
	private static Save save;
	private static ContractSyncManager contracts;
	private static ItemSyncManager items;
	private static MessageSyncManager messages;
	private static User u;
	private static DatedHeader datedHeader;
	private static String testString;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		contracts = new ContractSyncManagerImpl();
		items = new ItemSyncManagerImpl();
		messages = new MessageSyncManagerImpl();
		

		
		u = new User();
		u.setCreatedAt(new Date());
		u.setId("0001");
		u.setNick("username");
		u.setPasswordHash(null);
		
		items.begin();
		
		ElGamalSignEntity signature = new ElGamalSignEntity();
		signature.setR(new BigInteger("10000"));
		signature.setS(new BigInteger("10001"));
		
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
		
		ElGamalSignEntity signature1 = new ElGamalSignEntity();
		signature1.setR(new BigInteger("10002"));
		signature1.setS(new BigInteger("10003"));
		
		messages.begin();
		
		Message m = new Message();
		m.setSendingDate(new Date());
		m.setSender("0001", "username");
		m.setReceiver("0002", "Eric");
		m.setMessageContent("message");
		m.setStatus(ReceptionStatus.RECEIVED);
		m.setPbkey(new BigInteger("101"));
		m.setSignature(signature1);
		
		messages.persist(m);
		
		contracts.begin();
		
		ContractEntity c = new ContractEntity();
		c.setUserid("0001");
		c.setCreatedAt(new Date());
		c.setTitle("Titre_Contract");
		
		ArrayList<String> clauses = new ArrayList<>();
		ArrayList<String> parties = new ArrayList<>();
		HashMap<String, String> partiesNames = new HashMap<>();
		HashMap<String, String> signatures = new HashMap<>();
		
		clauses.add("clause1");
		clauses.add("clause2");
		
		parties.add("0002");
		parties.add("0003");
		
		partiesNames.put("0002", "Bob");
		partiesNames.put("0003", "Alice");
		
		signatures.put("Bob", "Miller");
		signatures.put("Alice", "Signature");
		
		c.setClauses(clauses);
		c.setParties(parties);
		c.setPartiesNames(partiesNames);
		c.setSignatures(signatures);
		
		c.setWish(Wish.ACCEPT);
		c.setStatus(Status.SIGNING);
		c.setEstablisherType(EstablisherType.Sigma);
		c.setEstablishementData("data");
		
		contracts.persist(c);
		
		save = new SaveFactory().createDatedHeaderContractsItemsMessages(u, contracts, items, messages);
	}
	
	@Test
	public void test() throws IOException {
		SaveFileManager fileManager = new SaveFileManagerFactory().createDefaultSaveFileManager(save, "test.txt");
		fileManager.save();
		fileManager.load();
	}
	

}
