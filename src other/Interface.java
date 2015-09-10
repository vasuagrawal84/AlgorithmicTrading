import java.awt.event.*;
import java.text.ParseException;
import javax.swing.*;

public class Interface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame mainFrame;
	private JPanel mainPanel;
	
	public Interface() throws ParseException {
		prepareGUI();
	}
	
	private void prepareGUI() throws ParseException{
	      mainFrame = new JFrame("Algorithmic Trading");
	      
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
	      
	      mainPanel = new MainPanel();
	      
	      mainFrame.add(mainPanel);
	      
	      mainFrame.setVisible(true);  
	   }
	
	/*public void showCompanyCombobox(){                                    
	      headerLabel.setText("Control in action: JComboBox"); 

	      final DefaultComboBoxModel<String> companiesDropdown = new DefaultComboBoxModel<String>();
	      
	      for(int i=0; i<companyNames.size(); i++) {
	    	  companiesDropdown.addElement(companyNames.get(i));
	      }

	      final JComboBox<String> companiesCombo = new JComboBox<String>(companiesDropdown);    
	      companiesCombo.setSelectedIndex(0);

	      JScrollPane companiesListScrollPane = new JScrollPane(companiesCombo);    

	      JButton showButton = new JButton("Show");

	      showButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            //String data = "";
	            if (companiesCombo.getSelectedIndex() != -1) {                     
	               data = "Company Selected: " + companiesCombo.getItemAt(companiesCombo.getSelectedIndex());
	               //selectedCompany = companiesCombo.getItemAt(companiesCombo.getSelectedIndex());
	            }              
	            statusLabel.setText(data);
	         }
	      }); 
	      companyNamePanel.add(companiesListScrollPane);          
	      companyNamePanel.add(showButton);    
	      mainFrame.setVisible(true);             
	   }
	
	public void showBeta() throws ParseException{ 

	      JLabel label  = new JLabel("", JLabel.CENTER);        
	      label.setText("Beta Value: ");
	      label.setOpaque(true);
	      label.setBackground(Color.GRAY);
	      label.setForeground(Color.WHITE);
	      
	      
	      
	      JLabel betaValue  = new JLabel("", JLabel.RIGHT);
	      
	      //System.out.println(getValue());
	      betaValue.setText(getValue());
	      
	      betaPanel.add(label);
	      betaPanel.add(betaValue);

	      mainFrame.setVisible(true);  
	}
	
	private String getValue() throws ParseException {
		Calendar date = Calendar.getInstance();
	    date.add(Calendar.DAY_OF_MONTH, -202);
	    //System.out.println("hello: " + ind.getMACD(data.substring(17), date));
	    String company = data.substring(17);
	    System.out.println(company);
		String value = ind.getMACD(data.substring(17), date);
		return value;
		
	}*/
}
