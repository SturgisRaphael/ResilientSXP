package resilience.impl;

import model.api.Manager;
import resilience.api.Save;

public class ClearData extends SaveDecorator{
	private 
	
	ClearData(Save save, Manager db) {
		super(save);
		// TODO Auto-generated constructor stub
	}
}
