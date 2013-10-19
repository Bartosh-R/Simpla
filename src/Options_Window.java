

 

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;







import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.geom.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Options_Window extends JFrame implements MouseWheelListener{
	
	Label display = new Label("Hakuna matata");
   	Font font = new Font("Arial", Font.ITALIC, 35);
   	
   	Color white = new Color(255,255,255);
   	Color yellow = new Color(255,234,0);
   	
   	// indicate current displayed translation
   	 int cursor; 

   	 // contains informations about translations
   	ArrayList<String> elements;
   	
   	  
    public Options_Window() {
        super("SIMPLA");
        setDisplay();

        setUndecorated(true);

        setContentPane(new ContentPane());
        getContentPane().setBackground(Color.BLACK);
        getContentPane().setLayout(null);
               
        addMouseWheelListener(this);
        
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
     
        try{
        	setIconImage(ImageIO.read(getClass().getResource("img/bu32.png")));
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
        
        add(display);
        pack();

    }
    
    
    @Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
    	if(!elements.isEmpty())super.setVisible(b);
	}

    
    public void setInfo(ArrayList<String> elements)
    {
    	//set color which depends on preferences
    	String text = elements.get(cursor);
    	 display.setForeground(text.compareTo(Main.getPreference("")) == 0? yellow : white);
    	
    	cursor = 0; // set on start
    	this.elements = elements;
    	
    	if(!elements.isEmpty())
    	{
    		display.setText(elements.get(cursor));
    	}
    	else
    	{
    		display.setText("Nie Bangla");
    		playSound();
    	}

    }
    
    public void playSound() {
    
    	      try {
    	        Clip clip = AudioSystem.getClip();
    	        
    	        InputStream audioSrc = getClass().getResourceAsStream("msc/knock.wav");
    	      //add buffer for mark/reset support
    	      InputStream bufferedIn = new BufferedInputStream(audioSrc);
    	      AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
    	        
    	        clip.open(inputStream);
    	        clip.start(); 
    	      } catch (Exception e) {
    	        JOptionPane.showMessageDialog(null,
    				    e.getMessage());
    	      }
    }
   

    
    public void setDisplay()
    {

        display.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				switch (e.getButton()) {
				case 1:
					setVisible(false);
					break;
				case 3:
					display.setForeground(yellow);
					// Setting one true default file
					Main.setPreference(elements.get(cursor)); 
					Main.reload();
					break;
				default:
					break;
				}
				
			}
		}); 
        display.setFont(font);
    }


    
    
    @Override
    public void paint(Graphics g) {
        setShape(new Rectangle.Double(0,0,getWidth(),getHeight()));
		
        Graphics2D g2d = (Graphics2D) g.create();
        
        
        //Adjust display size
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(display.getText(), font, frc);
        Rectangle2D bounds = layout.getBounds();
       
        
        int width = (int)bounds.getWidth()+300;
        int heigth = (int)bounds.getHeight()+60;
        
        setSize(width,heigth);
        
        // Setting frame location
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width/2)-(width/2), (dim.height/2)-(heigth/2));
        
    	Dimension size = getSize();
    	int x = (size.width/2)-(display.getPreferredSize().width/2);
    	int y = (size.height/2)-(display.getPreferredSize().height/2);
    	
    	display.setBounds(x, y,width+10, heigth+10);
    	   	
    	super.paint(g);
    }
    
	class Label extends JLabel
	{
		public Label(String str) {
			super(str);
		}

		@Override
		public void setText(String arg0) {
			super.setText(arg0);
			repaint();
		}

		@Override
		public void paint(Graphics g) {
            // Apply our own painting effect	
			
            Graphics2D g2d = (Graphics2D) g.create();
			
            //Adjust display size
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout layout = new TextLayout(display.getText(), font, frc);
            Rectangle2D bounds = layout.getBounds();
            
          //  System.out.println("Width ->>" + bounds.getWidth());
          //  System.out.print("Heigth ->>" + bounds.getHeight());
            
			setSize(new Dimension((int)bounds.getWidth()+10, (int)bounds.getHeight()+10));
			super.paint(g);
		}
	}
    
    public class ContentPane extends JPanel {

        public ContentPane() {

            setOpaque(false);

        }

        @Override
        protected void paintComponent(Graphics g) {

            // Allow super to paint
            super.paintComponent(g);

            // Apply our own painting effect
            Graphics2D g2d = (Graphics2D) g.create();
           	
            // 50% 	 Alpha
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            g2d.setColor(getBackground());
            g2d.fill(getBounds());
            
            g2d.dispose();

        }
    }
    
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		 int notches = e.getWheelRotation();
	        if (notches < 0) {
	        	//  mouse-up
	           if(cursor < elements.size()-1)cursor++;
	           String text = elements.get(cursor);
	           
	           display.setText(text);
	           display.setForeground(text.compareTo(Main.getPreference("")) == 0? yellow : white);
	           
	           
	           repaint();
	        } else {
	        //  mouse-down
	        	if(cursor > 0)cursor--;
	        	String text = elements.get(cursor);
	        			
		        display.setText(text);
	        	display.setForeground(text.compareTo(Main.getPreference("")) == 0? yellow : white);
	        
		        repaint();
	        }
	}
    
}