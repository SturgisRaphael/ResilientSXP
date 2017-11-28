package resilience.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import model.api.ContractSyncManager;
import model.api.EstablisherType;
import model.api.Status;
import model.api.Wish;
import model.entity.ContractEntity;
import model.entity.User;
import model.syncManager.ContractSyncManagerImpl;
import resilience.api.Save;

public class ContractsDecorator extends SaveDecorator implements Save{
	private ContractSyncManager contracts;
	
	public ContractsDecorator(Save save, User u, ContractSyncManager contracts) {
		super(save, u);
		this.contracts = contracts;
	}

	public ContractsDecorator(Save save) {
		super(save);
		this.contracts = new ContractSyncManagerImpl();
	}

	@Override
	public String write(){
		Collection<ContractEntity> contractCollection = contracts.findAll();
		contracts.end();
		String result = new String();
		for(ContractEntity c : contractCollection)
		{
			if(c.getUserid() == u.getId()) {
				result += c.toString() + "\n";
			}
		}

		return save.write() + "\n" + "<contracts>" + "\n" + result + "</contracts>";
	}
	
	@Override
	public int read(String s) {
		String str = s.substring(save.read(s));
		String contractBegin = "ContractEntity [";
		String contractEnd = "]\n";
		
		contracts.begin();
		
		int offset = str.indexOf(contractBegin);
		
		while (str.indexOf(contractBegin) != -1) {
			ContractEntity c = parseContract(str.substring(str.indexOf(contractBegin) + contractBegin.length(), str.indexOf(contractEnd) + contractEnd.length()));
			contracts.persist(c);
			offset += str.indexOf(contractEnd)+ contractEnd.length();
			str = str.substring(str.indexOf(contractEnd)+ contractEnd.length());
		}
		
		
		String endOfContracts = "</contracts>";
		return offset + str.indexOf(endOfContracts) + endOfContracts.length();
	}

	private ContractEntity parseContract(String string) {
		ContractEntity c = new ContractEntity();

		String userIdBegin= ", userid=";
		String createdAtBegin = ", createdAt=";
		String titleBegin = ", title=";
		String clausesBegin = ", clauses=";
		String partiesBegin = ", parties=";
		String partiesNamesBegin = ", partiesNames=";
		String wishBegin = ", wish=";
		String statusBegin = ", status=";
		String signaturesBegin = ", signatures=";
		String establisherTypeBegin = ", establisherType=";
		String establishementData = ", establishementData=";
		
		c.setUserid(string.substring(string.indexOf(userIdBegin) + userIdBegin.length(), string.indexOf(createdAtBegin)));
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	    try {
			date =  df.parse(string.substring(string.indexOf(createdAtBegin) + createdAtBegin.length(), string.indexOf(titleBegin)));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		c.setCreatedAt(date);
		
		c.setTitle(string.substring(string.indexOf(titleBegin) + titleBegin.length(), string.indexOf(clausesBegin)));
		c.setClauses(parseArrayList(string.substring(string.indexOf(clausesBegin) + clausesBegin.length(), string.indexOf(partiesBegin))));
		c.setParties(parseArrayList(string.substring(string.indexOf(partiesBegin) + partiesBegin.length(), string.indexOf(partiesNamesBegin))));
		c.setPartiesNames(parseHashMap(string.substring(string.indexOf(partiesNamesBegin) + partiesNamesBegin.length(), string.indexOf( wishBegin))));	
		c.setWish(parseWish(string.substring(string.indexOf(wishBegin) + wishBegin.length(), string.indexOf( statusBegin))));
		c.setStatus(parseStatus(string.substring(string.indexOf(statusBegin) + statusBegin.length(), string.indexOf( signaturesBegin))));
		c.setSignatures(parseHashMap(string.substring(string.indexOf(signaturesBegin) + signaturesBegin.length(), string.indexOf( establisherTypeBegin))));
		c.setEstablisherType(parseEstablisherType(string.substring(string.indexOf(establisherTypeBegin) + establisherTypeBegin.length(), string.indexOf( establishementData))));
		
		string = string.substring(string.indexOf(establishementData));
		
		c.setEstablishementData(string.substring(establishementData.length(), string.indexOf("]")));
		
		return c;
	}

	private EstablisherType parseEstablisherType(String substring) {
		return EstablisherType.valueOf(substring);
	}

	private Status parseStatus(String string) {
		return Status.valueOf(string);
	}

	private Wish parseWish(String string) {		
		return Wish.valueOf(string);
	}

	private HashMap<String, String> parseHashMap(String string) {
		// {0002=Bob, 0003=Alice}
		
		HashMap<String, String> hash = new HashMap<>();
		
		String delimiter1 = ", ";
		String delimiter2 = "=";
		
		string = string.substring(1); //cut first {
		while(string.indexOf(delimiter1) != -1)
		{
			String key = string.substring(0, string.indexOf(delimiter2));
			String value = string.substring(string.indexOf(delimiter2) + delimiter2.length(), string.indexOf(delimiter1));
			hash.put(key, value);
			string = string.substring(string.indexOf(delimiter1) + delimiter1.length());
		}
		String key = string.substring(0, string.indexOf(delimiter2));
		String value = string.substring(string.indexOf(delimiter2) + delimiter2.length(), string.indexOf("}"));
		hash.put(key, value);
		
		return hash;
	}

	private ArrayList<String> parseArrayList(String string) {
		ArrayList<String> clauses = new ArrayList<>();
		String delimiter = ", ";
		string = string.substring(1); //cut first [
		while(string.indexOf(delimiter) != -1)
		{
			clauses.add(string.substring(0, string.indexOf(delimiter)));
			string = string.substring(string.indexOf(delimiter) + delimiter.length());
		}
		clauses.add(string.substring(0, string.indexOf("]")));
		
		return clauses;
	}
	
	public ContractSyncManager getContracts() {
		return contracts;
	}
	
	
}
