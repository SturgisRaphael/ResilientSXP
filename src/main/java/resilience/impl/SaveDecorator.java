package resilience.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import resilience.api.Save;

public abstract class SaveDecorator implements Save{
	protected Save save;
	
	public SaveDecorator(Save save) {
		this.save = save;
	}

	public void write(String path)
	{
		save.write(path);
				
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println("Data :");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read(String path){
		save.read(path);
	}
}
