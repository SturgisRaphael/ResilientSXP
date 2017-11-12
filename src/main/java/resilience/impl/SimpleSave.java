package resilience.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import model.entity.User;
import resilience.api.Save;

public class SimpleSave implements Save{
	private User u;
	private Date date;

	public SimpleSave(User u){
		this.u = u;

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
			writer.println("Username : " + u.getNick());
			writer.println("Id : " + u.getId());
			writer.println("Date :" + date.toString());
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

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
