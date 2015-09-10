import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DateSelector extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel startDate;
	private JLabel currentDate;
	private JLabel endDate;
	
	private JButton addButton;
	private JButton minusButton;
	
	public DateSelector(final GUIState guiState) {
		this.setLayout(new GridLayout(5, 1, 20, 20));
		
		final Indicators ind = new Indicators();
		
		startDate = new JLabel("", JLabel.CENTER);
		currentDate = new JLabel("", JLabel.CENTER);
		endDate = new JLabel("", JLabel.CENTER);
		
		addButton = new JButton("Add 1 Day");
		minusButton = new JButton("Minus 1 Day");
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {              
				try {
					guiState.getCurrentDate().add(Calendar.DAY_OF_MONTH, ind.daysTillNextTrading(guiState.getSelectedCompany(), guiState.getCurrentDate()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   	  
			}              
		});
		
		minusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {              
				try {
					guiState.getCurrentDate().add(Calendar.DAY_OF_MONTH, -ind.daysTillLastTrading(guiState.getSelectedCompany(), guiState.getCurrentDate()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   	  
			}              
		});
		
		this.add(addButton);
		this.add(minusButton);
		
		this.setVisible(true);
		
	}

}
