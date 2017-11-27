package resilience.impl;

import java.util.Date;

import model.entity.User;
import resilience.api.Save;

public class DatedHeader implements Save{
	private User u;
	private Date date;

	public DatedHeader(User u){
		this.u = u;

		this.date = new Date();
	}
	
	public DatedHeader(){
		this.u = new User();

		this.date = new Date();
	}
	
	@Override
	public String write() {
		return "<Header>\n" + "User [username=" + u.getNick() + ", id=" + u.getId() + "]" + "\n" + "Date [date=" + date + "]" + "\n" + "</Header>";//TODO: see if can use toString()
	}

	@Override
	public int read(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	public User getU() {
		return u;
	}

	public Date getDate() {
		return date;
	}
}
