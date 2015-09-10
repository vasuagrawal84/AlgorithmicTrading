import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Calendar;


public class MainPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel leftSidePanel;
	private JPanel rightSidePanel;
	
	public MainPanel() throws ParseException {
		final GUIState guiState = new GUIState("Aberdeen Asset Management PLC", "One", false);
		setLayout(new GridLayout(1, 2, 0, 0));
		/*Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DAY_OF_MONTH, -203);*/
		leftSidePanel = new LeftSidePanel(guiState);
		rightSidePanel = new RightSidePanel(guiState);
		
		
		
		
		this.add(leftSidePanel);
		this.add(rightSidePanel);
		
		this.setVisible(true);
		
	}

}
