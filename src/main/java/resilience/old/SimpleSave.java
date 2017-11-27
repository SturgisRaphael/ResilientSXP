package resilience.old;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.entity.User;
import resilience.old.Save;

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

	@SuppressWarnings("deprecation")
	@Override
	public boolean read(String path) {
//		Scanner in = new Scanner(path);
//		String userLine;
//		u = new User();
//		userLine = in.nextLine();
//		System.out.println("userLine = " + userLine);
//		userLine = in.nextLine();
//		System.out.println("userLine = " + userLine);
//		if(!userLine.contentEquals("<Header>")) {
//			return false;
//		}
//		System.out.println("header ok");
//		
//		
//		Scanner user = new Scanner(userLine);
//		
//		user.useDelimiter("[");
//		if(user.next().contentEquals("User "))
//			return false;
//		System.out.println("user ok");
//		
//		Scanner userData = new Scanner(user.next());
//		userData.useDelimiter(",");
//		
//		Scanner usernameScan = new Scanner(userData.next());
//		usernameScan.useDelimiter("=");
//		
//		if(!usernameScan.next().contentEquals("username"))
//			return false;
//		System.out.println("username ok");
//		u.setNick(usernameScan.next());
//		
//		Scanner idScan = new Scanner(user.next());
//		idScan.useDelimiter("=");
//		
//		if(!idScan.next().contentEquals("id"))
//			return false;
//		System.out.println("id ok");
//		u.setId(idScan.next());
//		
//		System.out.println("----------------test scan : " + u);
		
		FileReader fileReader;
		try {
			fileReader = new FileReader(path);
						
			 BufferedReader bufferedReader = new BufferedReader(fileReader);
			 
			 if(!bufferedReader.readLine().contentEquals("<Header>"))
			 {
				 System.out.println("Error: header");
				 bufferedReader.close();
				 return false;
			 }
			 String userLine = bufferedReader.readLine();
			 String[] tmp = userLine.split("\\[");
			 if(!tmp[0].contentEquals("User ")){
				 System.out.println("Error: User");
				 bufferedReader.close();
				 return false;
			 }
			 
			 String[] tmp2 = tmp[1].split(", ");
			 String[] tmp3 = tmp2[0].split("=");
			 String[] tmp4 = tmp2[1].split("=");
			 
			 if(!tmp3[0].contentEquals("username"))
			 {
				 System.out.println("Error: username");
				 bufferedReader.close();
				 return false;
			 }
			 if(!tmp4[0].contentEquals("id"))
			 {
				 System.out.println("Error: id");
				 bufferedReader.close();
				 return false;
			 }
			 
			 u.setNick(tmp3[1]);
			 u.setId(tmp4[1].substring(0, tmp4[1].length() - 1));
			 
			 String dateLine = bufferedReader.readLine();
			 
			 tmp = dateLine.split("\\[");
			 if(!tmp[0].contentEquals("Date ")){
				 System.out.println("Error: date line");
				 bufferedReader.close();
				 return false;
			 }
			 tmp2 = tmp[1].split("=");
			 if(!tmp2[0].contentEquals("date")){
				 System.out.println("Error: date");
				 bufferedReader.close();
				 return false;
			 }
			 
			 DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy"); 
			 try {
			     date = df.parse(tmp2[1]);
			     String newDateString = df.format(date);
			 } catch (ParseException e) {
			     e.printStackTrace();
			 }
			 
			 bufferedReader.close();
			 
			 return true;
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
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
