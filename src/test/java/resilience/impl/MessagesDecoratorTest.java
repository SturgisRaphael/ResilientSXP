package resilience.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.api.MessageSyncManager;
import model.entity.ElGamalSignEntity;
import model.entity.Message;
import model.entity.Message.ReceptionStatus;
import model.entity.User;
import model.syncManager.MessageSyncManagerImpl;
import resilience.api.Save;

public class MessagesDecoratorTest {
	private static MessageSyncManager messages;
	private static User u;
	private static Save DatedHeader;
	private static String testString;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		messages = new MessageSyncManagerImpl();
		
		testString = "<Header>\n" + 
				"User [username=username, id=0001]\n" + 
				"Date [date=Tue Nov 28 14:34:31 CET 2017]\n" + 
				"</Header>\n" + 
				"<message>\n" + 
				"Message [id=E982119F-FB00-4FB7-8C32-DF7773F03C65, sendingDate=Tue Nov 28 14:34:31 CET 2017, senderId=0001, senderName=username, receiverId=0002, receiverName=Eric, messageContent=message, status=RECEIVED, pbkey=101, signature=ElGamalSignEntity [r=10000, s=10001]]\n" + 
				"</message>";
		
		u = new User();
		u.setCreatedAt(new Date());
		u.setId("0001");
		u.setNick("username");
		u.setPasswordHash(null);
		
		DatedHeader = new DatedHeader(u);
		
		ElGamalSignEntity signature = new ElGamalSignEntity();
		signature.setR(new BigInteger("10000"));
		signature.setS(new BigInteger("10001"));
		
		messages.begin();
		
		Message m = new Message();
		m.setSendingDate(new Date());
		m.setSender("0001", "username");
		m.setReceiver("0002", "Eric");
		m.setMessageContent("message");
		m.setStatus(ReceptionStatus.RECEIVED);
		m.setPbkey(new BigInteger("101"));
		m.setSignature(signature);
		
		messages.persist(m);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		messages.close();
	}

	@Test
	public void testWrite() {
		Save messagesDecorator = new MessagesDecorator(DatedHeader, u, messages);
		System.out.print(messagesDecorator.write());
	}

	@Test
	public void testRead() {
		MessagesDecorator messagesDecorator = new MessagesDecorator(DatedHeader, u, messages);
		messagesDecorator.read(testString);
		messages = messagesDecorator.getMessages();
		Collection<Message> messagesCollection = messages.findAll();
		messages.end();
		Message message = null;
		for(Message m : messagesCollection)
		{
			message = m;
		}
		
		assertEquals(message.getSendingDate().toString(), "Tue Nov 28 14:34:31 CET 2017");
		assertEquals(message.getSenderId(), "0001");
		assertEquals(message.getSenderName(), "username");
		assertEquals(message.getReceiverName(), "Eric");
		assertEquals(message.getReceiverId(), "0002");
		assertEquals(message.getMessageContent(), "message");
		assertEquals(message.getStatus(), Message.ReceptionStatus.RECEIVED);
		assertEquals(message.getPbkey(), new BigInteger("101"));
		assertEquals(message.getSignature().toString(), "ElGamalSignEntity [r=10000, s=10001]");
	}

}
