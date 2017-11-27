package resilience.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.api.ContractSyncManager;
import model.api.EstablisherType;
import model.api.Status;
import model.api.Wish;
import model.entity.ContractEntity;
import model.entity.User;
import model.syncManager.ContractSyncManagerImpl;
import resilience.api.Save;

public class ContractsDecoratorTest {
	private static ContractSyncManager contracts;
	private static User u;
	private static Save DatedHeader;
	private static String testString;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		contracts = new ContractSyncManagerImpl();
		
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
		
		u = new User();
		u.setCreatedAt(new Date());
		u.setId("0001");
		u.setNick("username");
		u.setPasswordHash(null);
		
		DatedHeader = new DatedHeader(u);
		
		testString = "<Header>\nUser [username=username, id=0001]\nDate [date=Mon Nov 27 19:08:18 CET 2017]\n</Header>\n<contracts>\nContractEntity [id=48A206E7-603F-4936-A2CE-97C921950359, userid=0001, createdAt=Mon Nov 27 19:08:18 CET 2017, title=Titre_Contract, clauses=[clause1, clause2], parties=[0002, 0003], partiesNames={0002=Bob, 0003=Alice}, wish=ACCEPT, status=SIGNING, signatures={Bob=Miller, Alice=Signature}, establisherType=Sigma, establishementData=data]\n</contracts>";
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		contracts.close();
	}

	@Test
	public void testWrite() {
		Save contractDecorator = new ContractsDecorator(DatedHeader, u, contracts);
		System.out.print(contractDecorator.write());
		
		//TODO: Actual test for return value
	}

	@Test
	public void testRead() {
		ContractsDecorator contractDecorator = new ContractsDecorator(DatedHeader);
		
		contractDecorator.read(testString);
		
		Collection<ContractEntity> contractCollection = contractDecorator.getContracts().findAll();
		ContractEntity contract = null;
		for(ContractEntity c : contractCollection)
		{
			contract = c;
		}
		
		assertEquals(contract.getId(), "48A206E7-603F-4936-A2CE-97C921950359");
		assertEquals(contract.getUserid(), "0001");
		assertEquals(contract.getCreatedAt().toString(), "Mon Nov 27 19:08:18 CET 2017");
		assertEquals(contract.getTitle(), "Titre_Contract");
		assertEquals(contract.getClauses().toString(), "[clause1, clause2]");
		assertEquals(contract.getParties().toString(), "[0002, 0003]");
		assertEquals(contract.getPartiesNames().toString(), "{0002=Bob, 0003=Alice}");
		assertEquals(contract.getWish(), Wish.ACCEPT);
		assertEquals(contract.getStatus(), Status.SIGNING);
		assertEquals(contract.getWish(), Wish.ACCEPT);
		assertEquals(contract.getSignatures().toString(), "{Bob=Miller, Alice=Signature}");
		assertEquals(contract.getEstablisherType(), EstablisherType.Sigma);
		assertEquals(contract.getEstablishementData(), "data");
	}
}
