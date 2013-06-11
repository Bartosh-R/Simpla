
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Record;


public class Ask_Window {

	   public static JFrame f = new JFrame("SIMPLA");
   		public  Label label = new Label("Question here");
   		

	    public void go() {
	    	

	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        
	    	f.setVisible(true);
	        try {
				f.setIconImage(ImageIO.read(getClass().getResource("img/bu16.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        f.setPreferredSize(new Dimension(501, 100));
	        f.setAlwaysOnTop(true);
	        f.setLocation((dim.width/2)-200, (dim.height/2)-100);
	        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
	        
	        addLabel(f.getContentPane());
	        
	        f.pack();
  
	    }

	    public  void addLabel(Container pane) {
	    	Font font = new Font("Arial", Font.PLAIN, 35);
	    	
	    	
	        label.setAlignmentX(Component.CENTER_ALIGNMENT);
	        label.addMouseListener(new MouseListener() {
				
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
					if (e.getSource() == label) {
						int n = DataBase.currentRecord;
						
						String answer = DataBase.Records.get(n).getAnswer();
						if(label.getText().compareTo(answer) != 0)
						{
							label.setText(answer);
						}
						else
						{
							f.setVisible(false);
						}
					}
					
				}
			});
	        label.setPreferredSize(new Dimension(500, 99));
	        label.setFont(font);
	        pane.add(label);
	    }

		class Label extends JLabel
		{
			public Label(String str) {
				super(str);
			}

			@Override
			public void paint(Graphics g) {
				setSize(new Dimension(500, 50));
				super.paint(g);
			}
		}
	    
}
