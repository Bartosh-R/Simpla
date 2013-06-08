
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import model.Record;


public class Main implements Runnable {

	public PopupMenu popup = new PopupMenu();
	public MenuItem addItem = new MenuItem("Add");
    public MenuItem startItem = new MenuItem("Start");
    
    public Timer timer = new Timer( ); 
    
    private Boolean is_run = false;
    
	
	public static void main(String[] args) {

		final Main main = new Main();
		SwingUtilities.invokeLater(main);

		DataBase b = new DataBase();
		
	}

	
	protected static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);
         
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
	
	public void setMenu()
    {
        popup.add(addItem);
        popup.add(startItem);
        
        addItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("ADD");
            	Add_Window.go();
            }
        });
        
        startItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startAsk();
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

	public void startAsk()
	{
		if(is_run == false)
		{
		final Ask_Window se = new Ask_Window();
		se.go();
		//	if(timer == null) System.out.print("EEEEEE .... Working bad");
        	timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("ASK_WINDOW");
				se.label.setText(DataBase.getRandom().getQuestion());
				se.f.setVisible(true);
			}
        	}, 0,5*60*1000);
        	is_run = true;
			startItem.setLabel("Stop");
		}
		else
		{
			startItem.setLabel("Start");
			timer.cancel();
			timer = new Timer();
			is_run = false;
		}
	}
	
	@Override
	public void run() {
	      setMenu();
	      setTray();
	}

}
