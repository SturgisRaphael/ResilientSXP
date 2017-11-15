package resilience.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Scanner;

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
		this.u = new User();
		this.date = new Date();
	}
	@Override
	public void write(String path) {
		//TODO : Remove old save file

		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file, false));
			out.write("<Header>");
	        out.newLine();
			out.write("User [username=" + u.getNick());
			out.write(", id=" + u.getId() + "]");
	        out.newLine();
			out.write("Date [date=" + date + "]");
	        out.newLine();
	        out.write("</Header>");
	        out.newLine();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
			System.out.println("Error");
			
			e1.printStackTrace();
		}
	}

	@Override
	public boolean read(String path) {
		Scanner in = new Scanner(path);
		String userLine;
		u = new User();
		
		if(!(userLine = in.nextLine()).contentEquals("<Header>")) {
			return false;
		}
		Scanner user = new Scanner(userLine);
		
		user.useDelimiter("[");
		if(user.next().contentEquals("User "))
			return false;
		
		Scanner userData = new Scanner(user.next());
		userData.useDelimiter(",");
		
		Scanner usernameScan = new Scanner(userData.next());
		usernameScan.useDelimiter("=");
		
		if(!usernameScan.next().contentEquals("username"))
			return false;
		u.setNick(usernameScan.next());
		
		Scanner idScan = new Scanner(user.next());
		idScan.useDelimiter("=");
		
		if(!idScan.next().contentEquals("id"))
			return false;
		u.setId(idScan.next());
		
		return true;
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
