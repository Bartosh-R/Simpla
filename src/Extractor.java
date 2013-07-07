import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extractor {

	String myString;
	
	ArrayList<String> subjects;
	
	ArrayList<String> forbidden;
	ArrayList<Integer> hole;
	
	
	public Extractor(String uri) {
		myString = urlGet(uri);
		forbidden = new ArrayList<String>();
		forbidden.add(".*?</I>.*?");
		subjects = extract();
		
		if(subjects.size() == 0) // if can't match by default setting
		{
			String thing = find("class=\"a1 result\">.*?</div>", 18, 0);
			String r = find("<BR>.*?</div>", thing, 4, -6);
			subjects.add(r);
		}
	}
	

	
    public ArrayList<String> extract()
    {
    	ArrayList<String> result = new ArrayList<String>();
    	Pattern p = Pattern.compile("class=\"a1 result\">.*?</div");
    	Pattern p2 = Pattern.compile("</B>.*?<BR>");
    	Matcher m  = p.matcher(myString);
    	while(m.find())
    	{
    		String helper = myString.substring(m.start()+18,m.end());
    		Matcher m2 = p2.matcher(helper);
    		String wynik = "";
    		int licznik = 0;
    		while(m2.find())
    		{
    			String x = helper.substring(m2.start()+4,m2.end()-4);
    			if(check_list2(x) == false)result.add(x);
    			licznik++;
    		}
    		
    	}
		return result;
    }
    

	
    public boolean check_list2(String check)
    {
    	for(String forb: forbidden)
    	{
    		Pattern p = Pattern.compile(forb);
    		Matcher m = p.matcher(check);
    		if(m.matches()) return true;
    	}
    	return false;
    }
	
    public String find(String pattern, int start, int stop)
    {  	
    	Pattern p = Pattern.compile(pattern);
    	Matcher m  = p.matcher(myString);
    	m.find();	
    	return myString.substring(m.start()+start,m.end()+stop);
    }
   
    public String find(String pattern, String base, int start, int stop)
    {  	
    	Pattern p = Pattern.compile(pattern);
    	Matcher m  = p.matcher(base);
    	m.find();	
    	return base.substring(m.start()+start,m.end()+stop);
    }
 
	
	 public String urlGet(String urlString){
	      
	     URLConnection urlConnection = null;
	        URL url = null;
	        String string = null;
	         
	        try {
	   url = new URL(urlString);
	   urlConnection = url.openConnection();
	    
	   InputStream inputStream = urlConnection.getInputStream();
	   InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	   BufferedReader reader = new BufferedReader(inputStreamReader);
	    
	       
	   StringBuffer stringBuffer = new StringBuffer();
	    
	   while((string = reader.readLine()) != null){
	    stringBuffer.append(string + "\n");
	   }
	   inputStream.close();
	    
	   string = stringBuffer.toString();
	    
	    
	  } catch (MalformedURLException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  } 
	   
	  return string;
	    }
	 
	 public ArrayList<String> getBaseOfLinks(){
		 String kopia = myString;
		   ArrayList<String> baza = new ArrayList<String>();
		   	 int start = kopia.indexOf("<a href=\"");
		   	 int stop = kopia.indexOf("\" target");
		   	 while(start != -1 && stop != -1){
			   	 baza.add(kopia.substring(start+9,stop));
			   	 kopia = kopia.substring(stop+8);
			   	 start = kopia.indexOf("<a href=\"");
			   	 stop = kopia.indexOf("\" target");
		   	 }
		     return baza;
	 }
	
	   public ArrayList<String> getBaseOfNames(){
		   String kopia = myString;
		   ArrayList<String> baza = new ArrayList<String>();
		   	 int start = kopia.indexOf("\"plan\">");
		   	 int stop = kopia.indexOf("</a>");
		   	 while(start != -1 && stop != -1){
			   	 baza.add(kopia.substring(start+7,stop));
			   	 kopia = kopia.substring(stop+4);
			   	 start = kopia.indexOf("\"plan\">");
			   	 stop = kopia.indexOf("</a>");
		   	 }
		     return baza;
		    }  
}
