package resilience.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.entity.User;
import resilience.api.Save;

/**
 * Header of a save file
 */
public class DatedHeader implements Save{
	private User u;
	private Date date;

	/**
	 * Creates a DatedHead
	 * @param u User
	 */
	public DatedHeader(User u){
		this.u = u;

		this.date = new Date();
	}

	/**
	 * Creates a DatedHead
	 * @param u User
	 */
	public DatedHeader(){
		this.u = new User();

		this.date = new Date();
	}
	
	/**
	 * Returns the header associated to the user <code>u</code>
	 * @return String Header of the save file
	 */
	@Override
	public String write() {
		return "<Header>\n" + "User [username=" + u.getNick() + ", id=" + u.getId() + "]" + "\n" + "Date [date=" + date + "]" + "\n" + "</Header>";//TODO: see if can use toString()
	}

	/**
	 * Initialises <code>date<code> and the user <code>u</code> thanks to the
	 *  string <code>s</code>
	 * @param s String to be analysed
	 * @return number of characters read
	 */
	@Override
	public int read(String s) {
		u = new User();
		String strComp1 = "username=", strComp2 = ", id=", strComp3 = "]\n";
		int UsernameBeging = s.indexOf(strComp1) + strComp1.length();
		int UsernameEnd = s.indexOf(strComp2);
		int idBegin = s.indexOf(strComp2) + strComp2.length();
		int idEnd = s.indexOf(strComp3);
		
		u.setId(s.substring(idBegin, idEnd));
		u.setNick(s.substring(UsernameBeging, UsernameEnd));
		
		String s2 = s.substring(s.indexOf(strComp3) + strComp3.length());
		
		String strComp4 = "Date [date=";
		
		int dateBeging = s2.indexOf(strComp4)+ strComp4.length();
		int dateEnd = s2.indexOf(strComp3);
		
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	    try {
			date =  df.parse(s2.substring(dateBeging, dateEnd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	    String end = "</Header>";
	    
		return s.indexOf(end) + end.length();
	}

	/**
	 * @return u User
	 */
	public User getU() {
		return u;
	}

	/**
	 * @return date Date
	 */
	public Date getDate() {
		return date;
	}
}
