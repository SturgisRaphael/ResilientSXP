package resilience.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import resilience.api.Save;

public class SimpleSave implements Save{
	private String username;
	private Date date;
	
	public SimpleSave(String username){
		this.username = username;
		
		this.date = new Date();
	}
	
	public SimpleSave() {
		this.date = new Date();
	}
	@Override
	public void write(String path) {
		//TODO : Remove old save file
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("Header:");
			writer.println("Username : " + username);
			writer.println("Date : :" + date.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void read(String path) {
		// TODO Auto-generated method stub
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
