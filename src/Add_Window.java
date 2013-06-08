

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;


/* ListDemo.java requires no other files. */
public class Add_Window extends JPanel implements ListSelectionListener {
	
    private JList<String> list;
    private DefaultListModel<String> listModel;
    
     public static JFrame f = new JFrame("SIMPLA");
    public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    private static final String hireString = "Add";
    private static final String fireString = "Delete";
    private JButton fireButton;
    private JButton hireButton;
    private JTextField addField;

    public Add_Window() {
        super(new BorderLayout());

        //listModel = new DefaultListModel<String>();
        listModel = DataBase.toArray();

        //Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);

        hireButton = new JButton(hireString);
        HireListener hireListener = new HireListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(hireListener);
        hireButton.setEnabled(false);

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.setEnabled(false);
        fireButton.addActionListener(new FireListener());

        addField = new JTextField(10);
        addField.addActionListener(hireListener);
        addField.getDocument().addDocumentListener(hireListener);


        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(addField);
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            
            String p = listModel.get(index);
            String question = p.substring(0,p.indexOf("-")-1);
            String answer = p.substring(p.indexOf("-")+2);
            
            System.out.println("Question = "+question);
            System.out.println("Answer = "+answer);
            
            DataBase.erase(question, answer, "file.dat");
            
            
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                fireButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = addField.getText();
            String question = name.substring(0, name.indexOf("/"));
            String answer = name.substring(name.indexOf("/")+1,name.length());
            
            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(question+" - "+answer) || !check(name)) {
                Toolkit.getDefaultToolkit().beep();
                addField.requestFocusInWindow();
                addField.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
  
            listModel.insertElementAt(question+" - "+answer, index);
            DataBase.insert(question, answer);
            
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            addField.requestFocusInWindow();
            addField.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            try {
				check(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            try {
				check(e.getDocument().getText(0, e.getDocument().getLength()));
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
        		
        }


        private boolean check(String s) {
        	Pattern p = Pattern.compile(".*?/.*?");
        	
				if (!p.matcher(s).matches()) {
				    button.setEnabled(false);
				    return false;
				}
				else
				{
					button.setEnabled(true);
		            return true;
				}
        }
        
        
      
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                fireButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                fireButton.setEnabled(true);
            }
        }
    }


    public static void go() {
        //Create and set up the window.
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(501, 220));
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.setIconImage((new ImageIcon("img/bu48.png").getImage()));
        f.setLocation((dim.width/2)-200, (dim.height/2)-100);
        //Create and set up the content pane.
        JComponent newContentPane = new Add_Window();
        newContentPane.setOpaque(true); //content panes must be opaque
        f.setContentPane(newContentPane);

        //Display the window.
        f.pack();
        f.setVisible(true);
    }

}