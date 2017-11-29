package resilience.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import model.api.MessageSyncManager;
import model.entity.ElGamalSignEntity;
import model.entity.Message;
import model.entity.Message.ReceptionStatus;
import model.entity.User;
import model.syncManager.MessageSyncManagerImpl;
import resilience.api.Save;

/**
 * Resilient messages
 */
public class MessagesDecorator extends SaveDecorator implements Save{
	private MessageSyncManager messages;
	
	/**
	 * Creates a MessagesDecorator
	 * @param save Save
	 * @param u User
	 * @param messages MessageSyncManager
	 */
	public MessagesDecorator(Save save, User u, MessageSyncManager messages) {
		super(save, u);
		this.messages = messages;
	}

	/**
	 * Get all messages from <code>message</code> and transforms them into a string
	 * @return String representing all messages in the MessageSyncManager <code>messages</code>
	 */
	@Override
	public String write() {
		Collection<Message> messagesCollection = messages.findAll();
		String result = new String();
		for(Message m : messagesCollection)
		{
			if(m.getReceiverId() == u.getId() || m.getSenderId() == u.getId()) {
				result += m.toString() + "\n";
			}
		}
		return save.write() + "\n" + "<message>" + "\n" + result + "</message>";
	}
	
	/**
	 * Inputs all items found in the string into the MessageSyncManager <code>messages</code>
	 * @param s String representing properties and values of messages
	 * @return number of characters read
	 */
	@Override
	public int read(String s) {
		int a = save.read(s);
		String str = s.substring(a);
		String messageBegin = "Message [";
		String messageEnd = "]]";
		System.out.println(str);
		
		messages.begin();
		
		int offset = str.indexOf(messageBegin);
		
		while (str.indexOf(messageBegin) != -1) {
			Message m = parseMessage(str.substring(str.indexOf(messageBegin) + messageBegin.length(), str.indexOf(messageEnd)));
			messages.persist(m);
			offset += str.indexOf(messageEnd)+ messageEnd.length();
			str = str.substring(str.indexOf(messageEnd)+ messageEnd.length());
		}
		
		
		String endOfMessages = "</message>";
		return offset + str.indexOf(endOfMessages) + endOfMessages.length();
	}

	/**
	 * Enables to parse a string and returns its appropriate Message 
	 * @param string to be parsed
	 * @return Message
	 */
	private Message parseMessage(String string) {
		Message m = new Message();
		
		String sendingDateBegin = ", sendingDate=";
		String senderIdBegin = ", senderId=";
		String senderNameBegin = ", senderName=";
		String receiverIdBegin = ", receiverId=";
		String receiverNameBegin = ", receiverName=";
		String messageContentBegin = ", messageContent=";
		String statusBegin = ", status=";
		String pbkeyBegin = ", pbkey=";
		String signature = ", signature=";
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	    try {
			date =  df.parse(string.substring(string.indexOf(sendingDateBegin) + sendingDateBegin.length(), string.indexOf(senderIdBegin)));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		m.setSendingDate(date);
		
		String id = string.substring(string.indexOf(senderIdBegin) + senderIdBegin.length(), string.indexOf(senderNameBegin));
		String name = string.substring(string.indexOf(senderNameBegin) + senderNameBegin.length(), string.indexOf(receiverIdBegin));
		m.setSender(id, name);
		
		String idR = string.substring(string.indexOf(receiverIdBegin) + receiverIdBegin.length(), string.indexOf(receiverNameBegin));
		String nameR = string.substring(string.indexOf(receiverNameBegin) + receiverNameBegin.length(), string.indexOf(messageContentBegin));
		m.setReceiver(idR, nameR);
		
		m.setMessageContent(string.substring(string.indexOf(messageContentBegin) + messageContentBegin.length(), string.indexOf(statusBegin)));
		m.setStatus(parseStatus(string.substring(string.indexOf(statusBegin) + statusBegin.length(), string.indexOf(pbkeyBegin))));
		m.setPbkey(new BigInteger(string.substring(string.indexOf(pbkeyBegin) + pbkeyBegin.length(), string.indexOf(signature))));
		m.setSignature(parseSignature(string.substring(string.indexOf(signature) + signature.length())));
		
		System.out.println(m.toString());
		return m;
	}

	/**
	 * Creates an ElGamal signature 
	 * @param substring String to be parsed
	 * @return ElGamalSignEntity
	 */
	private ElGamalSignEntity parseSignature(String substring) {		
		ElGamalSignEntity signature = new ElGamalSignEntity();
		System.out.println(substring);
		
		String rBegin = "ElGamalSignEntity [r="	;
		String sBegin = ", s=";
		
		signature.setR(new BigInteger(substring.substring(substring.indexOf(rBegin) + rBegin.length(), substring.indexOf(sBegin))));
		signature.setS(new BigInteger(substring.substring(substring.indexOf(sBegin) + sBegin.length())));
		
		return signature;
	}

	/**
	 * @param substring String to be parsed
	 * @return ReceptionStatus corresponding to <code>substring</code>
	 */
	private ReceptionStatus parseStatus(String substring) {
		return ReceptionStatus.valueOf(substring);
	}

	/**
	 * @return the MessageSyncManager attribute <code>message</code>
	 */
	public MessageSyncManager getMessages() {
		return messages;
	}
}
