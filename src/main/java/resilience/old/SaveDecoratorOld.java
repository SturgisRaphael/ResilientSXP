package resilience.old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import resilience.old.Save;

public abstract class SaveDecoratorOld implements Save{
	protected Save save;
	
	public SaveDecoratorOld(Save save) {
		this.save = save;
	}

	public void write(String path)
	{
		save.write(path);
		File file = new File (path);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file, true));
			out.write("<data>");
	        out.newLine();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean read(String path){
		return save.read(path);
	}
}