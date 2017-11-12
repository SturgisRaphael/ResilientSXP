package resilience.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collection;

import crypt.api.encryption.Encrypter;
import crypt.impl.encryption.ElGamalSerpentEncrypter;
import model.api.ContractSyncManager;
import model.api.ItemSyncManager;
import model.api.Manager;
import model.api.MessageSyncManager;
import model.entity.ContractEntity;
import model.entity.ElGamalKey;
import model.entity.Item;
import model.entity.Message;
import model.entity.User;
import resilience.api.Save;

public class EncryptedData extends SaveDecorator{
	private ContractSyncManager contracts;
	private ItemSyncManager items;
	private MessageSyncManager messages;
	private User u;
	private ElGamalKey key;
	private Encrypter <ElGamalKey> encrypter;
	
	public EncryptedData(Save save, ContractSyncManager contracts, ItemSyncManager items, MessageSyncManager messages, User u) {
		super(save);
		this.contracts = contracts;
		this.items = items;
		this.messages = messages;
		this.u = u;
		this.key = new ElGamalKey();
		key.setPrivateKey(new BigInteger("1"));
		key.setPublicKey(new BigInteger("2"));
		encrypter = new ElGamalSerpentEncrypter();
		encrypter.setKey(key);
	}
	

	private void writeContracts(String path) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("Contracts :");
			Collection<ContractEntity> contractCollection = contracts.findAll();
			for(ContractEntity c : contractCollection)
			{
				if(c.getUserid() == u.getId()) {
					writer.println(encrypter.encrypt(c.toString().getBytes()));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeItems(String path) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("Item :");
			Collection<Item> itemsCollection = items.findAll();
			for(Item c : itemsCollection)
			{
				if(c.getUserid() == u.getId()) {
					writer.println(encrypter.encrypt(c.toString().getBytes()));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeMessages(String path) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("Item :");
			Collection<Message> messagesCollection = messages.findAll();
			for(Message c : messagesCollection)
			{
				if(c.getReceiverId() == u.getId() || c.getSenderId() == u.getId()) {
					writer.println(encrypter.encrypt(c.toString().getBytes()));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void write(String path){
		super.write(path);
		
		writeContracts(path);
		writeItems(path);
		writeMessages(path);
	}
	
	@Override
	public void read(String path) {
		
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
