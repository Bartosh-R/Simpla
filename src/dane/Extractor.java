package dane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// This is what makes me good candidate for hell

public class Extractor {

	String myString;
	ArrayList<String> forbidden;
	
	
	
	public Extractor(String uri, String charset) {
		myString = urlGet(uri, charset);	
		forbidden = new ArrayList<String>();
	}
	
	public void define(String pattern, int start, int stop)
	{
		myString = find(pattern,myString, start, stop);
	}
	
	public void define(String start, String stop)
	{
		System.out.println(myString.length());
		int begin = myString.indexOf(start);
		int end = myString.indexOf(stop,begin+start.length());
		myString = myString.substring(begin,end);
	}

	
    public ArrayList<String> extract_dbl(String p1,String p2)
    {
    	ArrayList<String> result = new ArrayList<String>();
    	Pattern pattern1 = Pattern.compile(p1);
    	Pattern pattern2 = Pattern.compile(p2);
    	
    	int prefix_p1 = p1.indexOf(".*?");
    	int prefix_p2 = p2.indexOf(".*?");
    	
    	int postfix_p1 = (p1.length()-(prefix_p1+3));
    	int postfix_p2 =(p2.length()-(prefix_p2+3));
    	
    	Matcher m  = pattern1.matcher(myString);
    	while(m.find())
    	{
    		String helper = myString.substring(m.start()+prefix_p1,m.end());
    		Matcher m2 = pattern2.matcher(helper);
    		String wynik = "";
    		int licznik = 0;
    		while(m2.find())
    		{
    			String x = helper.substring(m2.start()+prefix_p2,m2.end()-postfix_p2);
    			if(check_list2(x) == false)result.add(x);
    			licznik++;
    		}		
    	}
		return result;
    }
    	
    
    public ArrayList<String> extract(String p)
    {
    	ArrayList<String> result = new ArrayList<String>();
    	Pattern pattern1 = Pattern.compile(p);
    	
    	int prefix_p = p.indexOf(".*?");
    	int postfix_p = (p.length()-(prefix_p+3));
    	
    	
    	Matcher m  = pattern1.matcher(myString);
    	while(m.find())
    	{
    		String x = myString.substring(m.start()+prefix_p,m.end()-postfix_p);
    		if(check_list2(x) == false)result.add(x);
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
 
	
	 public String urlGet(String urlString,String charset){ 
		 
		 if(charset == null) charset = "UTF-8";
		 
	     URLConnection urlConnection = null;
	     URL url = null;
	     String string = null;
	         
	        try {
	        	
	        	// make connection
	        	url = new URL(urlString);
	        	urlConnection = url.openConnection();
	    
	        	//preparing
	        	InputStream inputStream = urlConnection.getInputStream();
	        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream,charset);
	        	BufferedReader reader = new BufferedReader(inputStreamReader);
	    
	        	// reading from website
	        	StringBuffer stringBuffer = new StringBuffer();
	        	while((string = reader.readLine()) != null){
	        		stringBuffer.append(string + "\n");
	        	}
	        	
	        	//closing 
	        	inputStream.close();
	        	string = stringBuffer.toString();
	    
	    
	        } catch (MalformedURLException e) {
	        	e.printStackTrace();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } 
	   
	  return string;
	 }
	 
}
