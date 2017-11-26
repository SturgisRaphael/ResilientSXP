package resilience.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import org.eclipse.persistence.jpa.rs.util.metadatasources.ItemLinksMetadataSource;

import model.api.ContractSyncManager;
import model.api.ItemSyncManager;
import model.api.Manager;
import model.api.MessageSyncManager;
import model.entity.ContractEntity;
import model.entity.Item;
import model.entity.Message;
import model.entity.User;
import resilience.api.Save;

public class ClearData extends SaveDecorator{
	private ContractSyncManager contracts;
	private ItemSyncManager items;
	private MessageSyncManager messages;
	private User u;
	
	public ClearData(Save save, ContractSyncManager contracts, ItemSyncManager items, MessageSyncManager messages, User u) {
		super(save);
		this.contracts = contracts;
		this.items = items;
		this.messages = messages;
		this.u = u;
	}

	private void writeContracts(String path) {
		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file,true));
			out.write("<contracts>");
			out.newLine();
			Collection<ContractEntity> contractCollection = contracts.findAll();
			for(ContractEntity c : contractCollection)
			{
				if(c.getUserid() == u.getId()) {
					out.write(c.toString());
					out.newLine();
				}
			}
			out.write("</contracts>");
			out.newLine();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void writeItems(String path) {
		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file,true));
			out.write("<items>");
			out.newLine();
			Collection<Item> itemsCollection = items.findAll();
			for(Item i : itemsCollection)
			{
				if(i.getUserid() == u.getId()) {
					out.write(i.toString());
					out.newLine();
				}
			}
			out.write("</items>");
			out.newLine();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void writeMessages(String path) {
		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file,true));
			out.write("<message>");
			out.newLine();
			Collection<Message> messagesCollection = messages.findAll();
			for(Message m : messagesCollection)
			{
				if(m.getReceiverId() == u.getId() || m.getSenderId() == u.getId()) {
					out.write(m.toString());
					out.newLine();
				}
			}
			out.write("</message>");
			out.newLine();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
			
	public ClearData(Save save) {
		super(save);
	}

	@Override
	public void write(String path){
		super.write(path);
		
		writeContracts(path);
		writeItems(path);
		writeMessages(path);
		
		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file,true));
			out.write("</data>");
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public boolean read(String path) {
		
		return super.read(path);
	}

	public ContractSyncManager getContracts() {
		return contracts;
	}

	public ItemSyncManager getItems() {
		return items;
	}

	public MessageSyncManager getMessages() {
		return messages;
	}
	
	
}
