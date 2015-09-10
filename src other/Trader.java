import java.text.ParseException;
import java.util.Calendar;


public class Trader {

	private String Name;
	private Double balance;
	private Double defaultBalance;
	private Double expenditureLimit;

	private Portfolio portfolio = new Portfolio();
	
	public int buy(String company, Double money, Calendar date) throws ParseException {
		int companyIndex = checkforCompany(company);
		Indicators tmpInd = new Indicators();
		
		int dayofWeek = date.get(Calendar.DAY_OF_WEEK);
		if(dayofWeek == Calendar.SATURDAY || dayofWeek == Calendar.SUNDAY) { 
			return -1;
		}
		else {
			double stockPrice = tmpInd.getStockPrice(company, date);
			int amount = (int) (money / stockPrice); //money trader wants to use to buy stock divided by stock price = number of stocks to buy
			Double remainder = money % stockPrice; //remainder of money not used to buy stock
			
			balance = balance - money + remainder;
			System.out.println("Balance: " + balance);
			
			if (balance < 0.00 | amount == 0) { //check that trader isn't buying more than their budget
				balance = balance + money;
				System.out.println("Not enought balance or not enough to buy one stock for " + company);
				return 1;
			}
			
			
			if (companyIndex == -1) { //if company doesn't exist in portfolio, then add
				System.out.println("Company doesnt exist in portfolio");
				if (portfolio.size() != 0) {
					portfolio.addnewCompany(portfolio.size()-1, company);
					portfolio.addnewAmount(portfolio.size()-1, amount);
					System.out.println(company + " bought! : "+ amount);
				}
				else {
					portfolio.addnewCompany(portfolio.size(), company);
					portfolio.addnewAmount(portfolio.size()-1, amount);
					System.out.println(company + " bought! : " + amount);
				}
			}
			else { //company does exist, so add amount to portfolio
				portfolio.addAmount(companyIndex, amount);		
				System.out.println(company + " bought! : " + amount);
			}
			System.out.println(portfolio.toString());
			return 0;
		}
		
	}
	
	public int sellAll(String company, Calendar date) throws ParseException {
		int dayofWeek = date.get(Calendar.DAY_OF_WEEK);
		if(dayofWeek == Calendar.SATURDAY || dayofWeek == Calendar.SUNDAY) { 
			return -1;
		}
		else {
			int companyIndex = checkforCompany(company);
			Indicators tmpInd = new Indicators();
			Double moneyToRemove = 0.00;
			if (companyIndex != -1) { //if company exists, remove it
				moneyToRemove = portfolio.getAmount(companyIndex)*tmpInd.getStockPrice(company, date);
				portfolio.removeCompany(companyIndex);
				portfolio.removeAmount(companyIndex);
			}
			else { System.out.println("No such sellAll option exists"); }
			System.out.println(company + " all sold!");
			System.out.println(portfolio.toString());
			balance = balance + moneyToRemove;
			return 0;
		}
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public int sell(String company, Double money, Calendar date) throws ParseException {
		int companyIndex = checkforCompany(company);
		Indicators tmpInd = new Indicators();
		double stockPrice = tmpInd.getStockPrice(company, date);
		int amount = (int) (money / stockPrice);
		Double remainder = money % stockPrice;
		System.out.println("Balance: " + balance);
		if(companyIndex != -1) {
			balance = balance + money - remainder;
			portfolio.subtractAmount(companyIndex, amount);
			System.out.println(company + " sold amount: " + amount);
			System.out.println(portfolio.toString());
		}
		companyIndex = checkforCompany(company);
		return 0;
	}
	
	public int checkforCompany(String company) {
		//System.out.println("Portfolio size:" + portfolio.size());
		if (portfolio.size() == 0) { return -1; }
		for(int i = 0; i<portfolio.size(); i++) {
			//System.out.println("Company requested: " + company);
			//System.out.println(portfolio.getCompanyName(i));
			if(company != null && company.equals(portfolio.getCompanyName(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Double getDefaultBalance() {
		return defaultBalance;
	}

	public void setDefaultBalance(Double defaultBalance) {
		this.defaultBalance = defaultBalance;
		balance = defaultBalance;
	}

	public Double getExpenditureLimit() {
		return expenditureLimit;
	}

	public void setExpenditureLimit(Double expenditureLimit) {
		this.expenditureLimit = expenditureLimit;
	}

	public Double getBalance() {
		return balance;
	}
}
