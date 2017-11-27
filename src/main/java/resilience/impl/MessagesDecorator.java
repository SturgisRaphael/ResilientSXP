package resilience.impl;

import java.util.Collection;

import model.api.MessageSyncManager;
import model.entity.Message;
import model.entity.User;
import resilience.api.Save;

public class MessagesDecorator extends SaveDecorator implements Save{
	private MessageSyncManager messages;
	
	public MessagesDecorator(Save save, User u, MessageSyncManager messages) {
		super(save, u);
		this.messages = messages;
	}

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
	
	@Override
	public int read(String s) {
		return save.read(s);
	}
}
