
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
 
import model.Record;;
 
public class DataBase {
 
	Vector<String> FileNames;
	static ArrayList<Record> Records;
	static int currentRecord;

    public DataBase() {
    	File path = new File(".");
    	String list[];
    	list = path.list(new DirFiler(".*?.dat"));
    	for(int i = 0; i < list.length; i++)
    	{
				System.out.print(list[i]+"\n");
    	}
    	loadFiles();
    }
 
    public static boolean loadFiles()  {
    	Records = select("file.dat");
    	return true;
    }
 
 
    public static DefaultListModel<String> toArray()
    {
    	loadFiles();
    	
    	DefaultListModel<String> result = new DefaultListModel<String>();
    	for(int i = 0; i < Records.size(); i++)
    	{
    		result.addElement(Records.get(i).toString());
    	}
		return result;
    }
    
    public static boolean insert(String question, String answer){
        FileWriter fw;
		try {
			fw = new FileWriter("file.dat",true);
		       fw.write(question + "^" + answer+"\n");
		        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadFiles();
        return true;
    }
    
    public static void erase(String question, String answer, String path)
    {
    	String c = getFileString(path);
    	String replace = question+"^"+answer+"\n";
    	int start = c.indexOf(replace);
    	
    	//System.out.print("?"+c+"?"+replace+"?");
    	
    	//System.out.print(c.charAt(0)+"<"+start+">");
    	
        FileWriter fw;
		try {
			fw = new FileWriter("file.dat",false);
		       fw.write(c.substring(0,start)+c.substring(start+replace.length()));
		       System.out.println(c.substring(0,start));
		        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
 
    
    public static String getFileString(String path)
    {
        BufferedReader br;
		String c;
		String r = "";
		
		try {
			br = new BufferedReader(new FileReader(path));
			while ((c = br.readLine()) != null) {
				r = r + (c + "\n");
			}
			br.close();
		} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		 catch (IOException e) {e.printStackTrace();}
		return r;
    }
 
    public static Record getRandom()
    {
    	Brandom g = new Brandom();
    	currentRecord = g.nextInt(Records.size());
    	return Records.get(currentRecord);
    }
    
    
    public static ArrayList<Record> select(String path) {
        ArrayList<Record> records = new ArrayList<Record>();
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String current;
			String question;
			String answer;
			while ((current = br.readLine()) != null) {
				 question = current.substring(0, current.indexOf("^"));
				 answer =  current.substring(current.indexOf("^")+1,current.length());
				//System.out.println(question+" - "+answer);
				records.add(new Record(question, answer));
			}
			br.close();
		} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		 catch (IOException e) {e.printStackTrace();}
		
        return records;
    }
    
    class DirFiler implements FilenameFilter
    {
    	private Pattern pat;
    	
    	public DirFiler(String regex) {
    		pat = Pattern.compile(regex);
		}
    	
		@Override
		public boolean accept(File dir, String name) {
			return pat.matcher(name).matches();
		}
    	
    }
    
   static class Brandom extends Random
    {
    	static int noagain = -1;

    	public Brandom() {
			super();
		}
    	
    	
		@Override
		public int nextInt(int n) {
			
			if(n == 1) return 0;
			
			 int c = super.nextInt(n);
			 if(c == noagain) 
				 {
				 return nextInt(n);
				 }
			 else
			 {
				 noagain = c;
				 return c;
			 }
		}	
    }

}

