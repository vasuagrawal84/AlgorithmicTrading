import java.util.Calendar;
import java.util.GregorianCalendar;


public class GUIState {
	
	private String selectedCompany;
	private String trader;
	private final Calendar startDate = new GregorianCalendar(2011, 5, 16);
	private final Calendar endDate = new GregorianCalendar(2011, 6, 15);
	private Calendar currentDate = endDate;
	
	public Calendar getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Calendar currentDate) {
		this.currentDate = currentDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public GUIState(String selectedCompany, String trader, boolean revalidate) {
		this.selectedCompany = selectedCompany;
		this.trader = trader;
	}

	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

}
