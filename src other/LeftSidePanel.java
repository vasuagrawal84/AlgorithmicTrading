import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Timer;


public class LeftSidePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel companySelectorPanel;
	private JLabel MACDSignal;
	private JLabel lowerBollinger;
	private JLabel upperBollinger;
	private JLabel relativeStrengthIndex;
	private JLabel beta;
	private JLabel aroonup;
	private JLabel aroondown;
	private JLabel CCI;
	private JLabel DMI;
	private JLabel keltner;
	private JLabel massindex;
	private JLabel ASI;
	private Indicators ind = new Indicators();
	
	
	ArrayList<String> companyNames = ind.getFiles();
	
	public LeftSidePanel(final GUIState guiState) throws ParseException {
		this.setLayout(new GridLayout(7, 1, 20, 20));
	    
	    /*companiesDropdown = new DefaultComboBoxModel<String>();
		companiesCombo = new JComboBox<String>(companiesDropdown); 
		companiesListScrollPane = new JScrollPane(companiesCombo);
		setupCompanySelector();	*/
		
		companySelectorPanel = new CompanySelector(guiState);
		//selectedCompany = companySelectorPanel.
		
		MACDSignal = new JLabel("", JLabel.CENTER);
		lowerBollinger = new JLabel("", JLabel.CENTER);
		upperBollinger = new JLabel("", JLabel.CENTER);
		relativeStrengthIndex = new JLabel("", JLabel.CENTER);
		beta = new JLabel("", JLabel.CENTER);
		aroonup = new JLabel("", JLabel.CENTER);
		aroondown = new JLabel("", JLabel.CENTER);
		CCI = new JLabel("", JLabel.CENTER);
		DMI = new JLabel("", JLabel.CENTER);
		keltner = new JLabel("", JLabel.CENTER);
		massindex = new JLabel("", JLabel.CENTER);
		ASI = new JLabel("", JLabel.CENTER);
		
		int delay = 1000; //milliseconds

		ActionListener taskPerformer = new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    try {
					setupIndicators(guiState, guiState.getCurrentDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			  }
		};
			
		Timer time = new Timer(delay, taskPerformer);
			
		time.start();
		
		this.add(companySelectorPanel);
		this.add(MACDSignal);
		this.add(lowerBollinger);
		this.add(upperBollinger);
		this.add(relativeStrengthIndex);
		this.add(beta);
		this.add(aroonup);
		this.add(aroondown);
		this.add(CCI);
		this.add(DMI);
		this.add(keltner);
		this.add(massindex);
		this.add(ASI);
		this.revalidate();
				
		this.setVisible(true);
		
	}
		
	private void setupIndicators(GUIState guiState, Calendar date) throws ParseException {
		String macdValue = "MACD Signal: " + ind.getMACD(guiState.getSelectedCompany(), date);
		System.out.println(macdValue);
		
		MACDSignal.setText(macdValue);
		
		String lowerBollingerValue = "Lower Bollinger Band: " + ind.getBollingerBands(guiState.getSelectedCompany(), date).getLowerBollingerBand().toString();
		
		lowerBollinger.setText(lowerBollingerValue);
		
		String upperBollingerValue = "Upper Bollinger Band: " + ind.getBollingerBands(guiState.getSelectedCompany(), date).getUpperBollingerBand().toString();
		
		upperBollinger.setText(upperBollingerValue);
		
		String RSI = "RSI: " + ind.getRelativeStrengthIndex(guiState.getSelectedCompany(), date).toString();
		
		relativeStrengthIndex.setText(RSI);
		
		ArrayList<StringAndDouble> betaList = new ArrayList<StringAndDouble>();
		betaList = ind.getBeta(date);
		
		String betaValue = "Beta: " + betaList.get(0).getValue().toString();
		System.out.println(betaValue);
		
		beta.setText(betaValue);
		
		String aroonup = "Aroon Up: " + ind.getAroonUp().toString();
		
		//aroonup
		
		//String aroondown = "Aroon Up: " + ind.getAroonDown().toString();
		
		//aroondown.setText(betaValue);
		
		
	}	
}
