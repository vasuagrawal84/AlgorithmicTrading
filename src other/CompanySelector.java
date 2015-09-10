import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class CompanySelector extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> companiesDropdown;
	private JComboBox<String> companiesCombo;
	private JScrollPane companiesListScrollPane;
	private JButton showButton;
	private String selectedCompany;
	
	Indicators ind = new Indicators();
    ArrayList<String> companyNames = ind.getFiles();
    //String companyName = "";
    
	public CompanySelector(GUIState guiState) {
		/*this.setSize(200, 100);
		this.selectedCompany = selectedCompany;*/
		companiesDropdown = new DefaultComboBoxModel<String>();
		companiesCombo = new JComboBox<String>(companiesDropdown); 
		companiesListScrollPane = new JScrollPane(companiesCombo);  
		
		setupCompanySelector(guiState);	
		
	}
	
	private void setupCompanySelector(final GUIState guiState) {
	      
	    for(int i=0; i<companyNames.size(); i++) {
    	  companiesDropdown.addElement(companyNames.get(i));
	    }

	       
	    this.companiesCombo.setSelectedIndex(0);
	    guiState.setSelectedCompany(companiesCombo.getItemAt(companiesCombo.getSelectedIndex()));
	    
		this.showButton = new JButton("Show");
		
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
			      //String data = "";
			      if (companiesCombo.getSelectedIndex() != -1) {                     
			    	  selectedCompany = companiesCombo.getItemAt(companiesCombo.getSelectedIndex());
			    	  System.out.println(selectedCompany);
			    	  guiState.setSelectedCompany(selectedCompany);		    	  
			      }              
			   }
		});
	      
	      this.add(companiesListScrollPane);
	      this.add(showButton);
	      this.setVisible(true);
	}

	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}	

}
