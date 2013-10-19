package dane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Interpreter {

	 File PATH = new File(Interpreter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	 String FILE_PATH = PATH.getAbsolutePath()+"\\";
	 
	 
	ArrayList<String> commands = new ArrayList<String>();
	public static ArrayList<String> result = new ArrayList<String>();
	static Extractor ext;
	
	
	public Interpreter(String name) {
		FILE_PATH += name;
		load();
	}
	
	public void get(String word)
	{
		System.out.println(commands.get(0).replace("@", word));
		 ext = new Extractor(commands.get(0).replace("@", word), "UTF-8");
			
			//commands.remove(0);
			for(int i = 1; i < commands.size(); i++)
			{
				execute(commands.get(i));
			}
	}
	
	public void execute(String command)
	{
		switch(command.substring(0, command.indexOf(' ')))
		{
			case "NO":
				NO(command.substring(3));
				break;
			case "DEF":
				DEF(command.substring(4));
				break;
			case "EXT":
				EXT(command.substring(4));
				break;
			default:
				break;
		}
	}
	
	
	public void NO(String data)
	{
		//System.out.println("NO");
		ext.forbidden.add(data);
	}
	
	public void DEF(String data)
	{
		//System.out.print("DEF");
		
		String start = data.substring(0, data.indexOf('@')-1);
		String stop = data.substring(data.indexOf('@')+1);
		
		ext.define(start, stop);
	}
	
	public void EXT(String data)
	{
		result = ext.extract(data);
	}
	
    public  void load() {
    	commands = new ArrayList<String>();
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(FILE_PATH));
			String current;
			while ((current = br.readLine()) != null) {
				commands.add(current);
			}
			br.close();
		} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		 catch (IOException e) {e.printStackTrace();}
    }
    
    
   
	
}
