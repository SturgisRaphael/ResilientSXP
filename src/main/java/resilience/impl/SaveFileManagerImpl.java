package resilience.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import resilience.api.Save;
import resilience.api.SaveFileManager;

public class SaveFileManagerImpl implements SaveFileManager {
	private Save save;
	private String path;
	
	public SaveFileManagerImpl(Save save, String path) {
		super();
		this.save = save;
		this.path = path;
	}

	@Override
	public void save() throws IOException {
		FileWriter file = new FileWriter("test.txt");
		file.write (save.write());
		file.close();
	}

	@Override
	public void load() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append("\n");
		            line = br.readLine();
		        }
		        save.read(sb.toString());
		    }
			catch (IOException e1) {
			    System.out.println("Exception ");
			}
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    

}
