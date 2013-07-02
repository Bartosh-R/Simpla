

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import javax.naming.ldap.Rdn;
import javax.swing.*;

import java.awt.geom.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class Ask_Window extends JFrame {
	
	Label display = new Label("Hakuna matata");
   	Font font = new Font("Arial", Font.ITALIC, 35);

    public Ask_Window() {
        super("SIMPLA");
        setDisplay();

        setUndecorated(true);
        
        setContentPane(new ContentPane());
        getContentPane().setBackground(Color.BLACK);
        getContentPane().setLayout(null);
               
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        add(display);
        pack();

    }
    
    public  void print(String s)
    {
    	System.out.print(s);
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
				if (e.getSource() == display) {
					int n = DataBase.currentRecord;
					
					String answer = DataBase.Records.get(n).getAnswer();
					if(display.getText().compareTo(answer) != 0)
					{
						display.setText(answer);
						repaint();
					}
					else
					{
						setVisible(false);
					}
				}
				
			}
		});
        display.setForeground(new Color(255,255,255));   
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
        
        int width = (int)bounds.getWidth();
        int heigth = (int)bounds.getHeight();
        
        setSize(width+60,heigth+60);
        
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
            
            
            System.out.println("Width ->>" + bounds.getWidth());
            System.out.print("Heigth ->>" + bounds.getHeight());
            
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
          
            // 50% transparent Alpha
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            g2d.setColor(getBackground());
            g2d.fill(getBounds());

            g2d.dispose();

        }

    }
    
}