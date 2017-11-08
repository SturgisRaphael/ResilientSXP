package resilience.impl;

import java.util.Date;

import resilience.api.Save;

public class SimpleSave implements Save{
	private String username;
	private Date date;
	
	SimpleSave(String username){
		this.username = username;
		
		this.date = new Date();
	}
	
	@Override
	public String read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

}
