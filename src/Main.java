
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.dstjacques.jhotkeys.JHotKeys;
import com.dstjacques.jhotkeys.JHotKeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.jna.platform.FileMonitor.FileListener;
import com.sun.jna.platform.win32.Win32Exception;

import dane.Interpreter;


public class Main implements Runnable {
	
	//Address - translator database
	static Interpreter interpreter;

	// TrayIcon menu elements
	public PopupMenu popup = new PopupMenu();
	public MenuItem optItem = new MenuItem("Options");
    public MenuItem startItem = new MenuItem("Start");
    
    // Window
	public static Tip_Window tipWindow = new Tip_Window();
	public static Options_Window optionsWindow = new Options_Window();
    
	//Engine Bruuum ....Bruum ;-)
    public Timer timer = new Timer( ); 
    public static Boolean is_run = false;
    
    // Preferences 
   static Preferences prefs;
   static String NODENAME = "Simpla";
   static String NAMEFILE = "NameFile";
    
   // List of File with instructions
 	public static String fileList[]; 
    
	
	public static void main(String[] args) {

		final Main main = new Main();
		SwingUtilities.invokeLater(main);
		
		prefs = Preferences.userRoot().node(NODENAME);

		seachForFiles();
		showChooseDialog(new ArrayList<String>(Arrays.asList(fileList)));
		
		// search for saved file, if doesn't exist load fileList[0]
		String defaultFile = getPreference(fileList[0]);
		interpreter = new Interpreter(defaultFile);

		setHotKeys();
	}
	
	// Reload instruction file
	
	public static void reload()
	{
		String defaultFile = getPreference(fileList[0]);
		interpreter = new Interpreter(defaultFile);
	}
	
	// Preferences 
	
	  public static String getPreference(String deafult) {
		    prefs = Preferences.userRoot().node(NODENAME);
		    return prefs.get(NAMEFILE, deafult);
		  }
	  
	  public static void setPreference(String deafult) {
		    prefs = Preferences.userRoot().node(NODENAME);
		     prefs.put(NAMEFILE, deafult);
		  }
	
	// Main GUI elements 
	

	public static void setHotKeys()
	{
		JHotKeys hotkeys = new JHotKeys("../lib");
		try {
		    hotkeys.registerHotKey(0, JIntellitype.MOD_CONTROL, (int)'Q');
		    
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    "Could not load JIntellitype.dll from local file system");
		}
		
	    JHotKeyListener hotkeyListener = new JHotKeyListener(){
	         public void onHotKey(int id) {
	            if(id == 0)
	            {
	            	try {
	            		
	            		Selector foo = new Selector();
	            		String select = URLEncoder.encode(Selector.go(foo), "UTF-8");
	            		
	            		
						interpreter.get(select);
						ArrayList<String> elements = interpreter.result;
						
						
						 tipWindow.setBackground(new Color(0, 0, 0));
				         tipWindow.setOpacity(0.8f);
				         
						tipWindow.setInfo(elements);
						tipWindow.setVisible(true);

						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	         }
	      };

	      hotkeys.addHotKeyListener(hotkeyListener);
	}
		
	public void setMenu()
    {
        popup.add(optItem);
        popup.add(startItem);
        
        optItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
        		seachForFiles();
        		showChooseDialog(new ArrayList<String>(Arrays.asList(fileList)));
        		
            }
        });
        
        startItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
    }
	
	public void setTray()
	{
		try{
			Image img = ImageIO.read(getClass().getResource("img/bu16.png"));
			
		final TrayIcon trayIcon =
                new TrayIcon(img, "Simpla Application");
        final SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);

        trayIcon.setPopupMenu(popup);
        
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              System.out.println("END");
              System.exit(0);
            }
        });
        
		} catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
		catch (IOException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
	}
	
	//  Choose file option
	
    public static void seachForFiles()
    {
   	 File PATH = new File(Interpreter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
   	 
    	fileList = PATH.list(new DirFiler(".*?.mod"));
    	for(int i = 0; i < fileList.length; i++)
    	{
				System.out.print(fileList[i]+"\n");
    	}
    	
    }
    
    static class DirFiler implements FilenameFilter
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
    
    public static void showChooseDialog(ArrayList<String> list)
    {
		 optionsWindow.setBackground(new Color(0, 0, 0));
		 optionsWindow.setOpacity(0.8f);
         
		 optionsWindow.setInfo(list);
		 optionsWindow.setVisible(true);
    }
    
    // Run this shit

	@Override
	public void run() {
	      setMenu();
	      setTray();
	}

}
