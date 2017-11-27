package resilience.impl;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.entity.User;

public class DatedHeaderTest {
	private static User u;
	private static DatedHeader datedHeader;
	private static String testString;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {u = new User();
		u.setCreatedAt(new Date());
		u.setId("0001");
		u.setNick("username");
		u.setPasswordHash(null);
		
		datedHeader = new DatedHeader(u);
		
		testString = "<Header>\nUser [username=username, id=0001]\nDate [date=Mon Nov 27 19:08:18 CET 2017]\n</Header>";
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testWrite() {
		System.out.print(datedHeader.write());
		
		//TODO: Actual test for return value
	}

	@Test
	public void testRead() {
		datedHeader = new DatedHeader();
		datedHeader.read(testString);
		
		assertEquals(datedHeader.getU().getNick(), "username");
		assertEquals(datedHeader.getU().getId(), "0001");
		assertEquals(datedHeader.getDate().toString(), "Mon Nov 27 19:08:18 CET 2017");
	}

}
