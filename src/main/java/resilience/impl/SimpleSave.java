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
	public void write(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(String path) {
		// TODO Auto-generated method stub
		
	}

}
