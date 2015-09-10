import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.ui.RefineryUtilities;

public class Main {

	
	
	public static void main(String[] args) throws ParseException {
		final Calendar startDate = new GregorianCalendar(2012, 5, 16);
		final Calendar endDate = new GregorianCalendar(2012, 6, 15);
		//System.out.println(Indicators.getStockPrice("Anglo American PLC", startDate));
		
		/*Indicators ind = new Indicators();
		while(startDate.before(endDate)) { 
			System.out.println(ind.getAroonUp("BP PLC", startDate) + "," + ind.getAroonDown("BP PLC", startDate));
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}*/
		
		Indicators ind = new Indicators();
		while(startDate.before(endDate)) { 
			//System.out.println(ind.getCCI("Anglo American PLC", startDate));
			//System.out.println("lower: " + ind.getKeltner("BP PLC", startDate).getLowerBollingerBand().toString());
			//System.out.println("higher: " + ind.getKeltner("BP PLC", startDate).getUpperBollingerBand().toString());
			//System.out.println(ind.getADX("BP PLC", startDate));
			//System.out.println(startDate.getTime().toString() + " " + ind.getMassIndex("BP PLC", startDate));
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		//traderOne(startDate, endDate);
		//traderTwo(startDate, endDate);
		//traderThree(startDate, endDate);
		
		/*Interface gui = new Interface();
		gui.pack();
		RefineryUtilities.centerFrameOnScreen(gui);
		gui.setVisible(true);*/
		/*gui.showCompanyCombobox();
		gui.showBeta();*/
	}

	private static void traderOne(Calendar startDate, Calendar endDate) throws ParseException {
		Trader traderOne = new Trader();
		Indicators traderOneInd = new Indicators();
		Calendar currentDate = startDate;
		
		traderOne.setName("John Doe");
		System.out.println("Trader Name: " + traderOne.getName());
		
		traderOne.setDefaultBalance(1000000.00);
		System.out.println("Starting Balance: " + traderOne.getDefaultBalance());
		
		ArrayList<String> companyNames = traderOneInd.getFiles();
		
		/*while(currentDate.before(endDate)) { 
			for(int i=0; i<companyNames.size(); i++) {
				if(traderOneInd.getStockPrice(companyNames.get(i), currentDate) > traderOneInd.getBollingerBands(companyNames.get(i), currentDate).getUpperBollingerBand()) {
					traderOne.sellAll(companyNames.get(i), currentDate);
				}
				if(traderOneInd.getStockPrice(companyNames.get(i), currentDate) < traderOneInd.getBollingerBands(companyNames.get(i), currentDate).getLowerBollingerBand()) {
					traderOne.buy(companyNames.get(i), 5000.00, currentDate);
				}
			}
			
			int days = traderOneInd.daysTillNextTrading("FTSE100", currentDate);
			currentDate.add(Calendar.DAY_OF_MONTH, days);
		}*/
		
		Double previousCCI;
		
		while(startDate.before(endDate)) { 
			for(int i=0; i<companyNames.size(); i++) {
				Calendar previousDay = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH));
				previousDay.add(Calendar.DAY_OF_MONTH, -1);
				previousCCI = traderOneInd.getCCI(companyNames.get(i), previousDay);
				if(previousCCI > 100 && traderOneInd.getCCI(companyNames.get(i), currentDate) < 100) {
					traderOne.sellAll(companyNames.get(i), currentDate);
				}
				if(previousCCI < -100 && traderOneInd.getCCI(companyNames.get(i), currentDate) > -100) {
					traderOne.buy(companyNames.get(i), 5000.00, currentDate);
				}
			}
			int days = traderOneInd.daysTillNextTrading("FTSE100", currentDate);
			//System.out.println(days);
			currentDate.add(Calendar.DAY_OF_MONTH, days);
			//System.out.println(currentDate.getTime().toString());
		}
		
		Double finalbalance = traderOne.getBalance()+traderOneInd.getPortfolioValue(traderOne.getPortfolio(), currentDate);
		
		System.out.println("final balance: " + finalbalance);
		
		Double percentageChange = traderOneInd.getMarketRateOfReturn(endDate);
		
		System.out.println(percentageChange);
		
		
		
		//System.out.println(traderOneInd.getRelativeStrengthIndex("BP PLC", endDate));
		
			
		
		
		
	}
	
	private static void traderTwo(Calendar startDate, Calendar endDate) throws ParseException {
		Trader traderTwo = new Trader();
		Indicators traderTwoInd = new Indicators();
		Calendar currentDate = startDate;
		
		traderTwo.setName("John Doe");
		System.out.println("Trader Name: " + traderTwo.getName());
		
		traderTwo.setDefaultBalance(1000000.00);
		System.out.println("Starting Balance: " + traderTwo.getDefaultBalance());
		
		
		ArrayList<String> companyNames = traderTwoInd.getFiles();
		
		while(currentDate.before(endDate)) {
			for(int i=0; i<companyNames.size(); i++) {
				//System.out.println(companyNames.get(i));
				if(traderTwoInd.getMACD(companyNames.get(i), currentDate) == "Buy") {
					System.out.println("buy: " + companyNames.get(i));
					traderTwo.buy(companyNames.get(i), 3000.00, currentDate);
				}
				if(traderTwoInd.getMACD(companyNames.get(i), currentDate) == "Sell") {
					System.out.println("buy: " + companyNames.get(i));
					traderTwo.sellAll(companyNames.get(i), currentDate);
				}
			}
			int days = traderTwoInd.daysTillNextTrading(companyNames.get(0), currentDate);
			currentDate.add(Calendar.DAY_OF_MONTH, days);
		}
		
		Double finalbalance = traderTwo.getBalance()+traderTwoInd.getPortfolioValue(traderTwo.getPortfolio(), currentDate);
		
		System.out.println("final balance: " + finalbalance);
		
		Double percentageChange = traderTwoInd.getMarketRateOfReturn(endDate);
		
		System.out.println(percentageChange);
		
	}
	
	private static void traderThree(Calendar startDate, Calendar endDate) throws ParseException {
		Trader traderThree = new Trader();
		Indicators traderThreeInd = new Indicators();
		Calendar currentDate = startDate;
		
		traderThree.setName("John Doe");
		System.out.println("Trader Name: " + traderThree.getName());
		
		traderThree.setDefaultBalance(1000000.00);
		System.out.println("Starting Balance: " + traderThree.getDefaultBalance());
		String topBeta = null;
		//ArrayList<String> companies = traderThreeInd.getFiles();
		//System.out.println(companies.get(0) + " rate of return " + traderThreeInd.getStockRateOfReturn(companies.get(0), startDate));
		
		String betaValue = "Beta: " + traderThreeInd.getBeta(startDate).get(0).getValue().toString();
		System.out.println(betaValue);
		
		/*ArrayList<StringAndDouble> betaValues = new ArrayList<StringAndDouble>();
		betaValues = traderThreeInd.getBeta(currentDate);
		
		for(int i=0; i<betaValues.size();i++) {
			System.out.println(betaValues.get(i).getCompany()+" " + betaValues.get(i).getValue());
		}*/
		
				
		
		
		while(currentDate.before(endDate)) {
			ArrayList<StringAndDouble> betaValues = new ArrayList<StringAndDouble>();
			betaValues = traderThreeInd.getBeta(currentDate);
			traderThree.buy(betaValues.get(0).getCompany(), 2000.00, currentDate);
			if(topBeta!=null && !topBeta.equals(betaValues.get(0).getCompany())) {
				System.out.println("Top beta has changed");
				traderThree.sell(topBeta, 1000.00, currentDate);
			}
			topBeta = betaValues.get(0).getCompany();
			System.out.println(currentDate.getTime());
			System.out.println(traderThreeInd.daysTillNextTrading(betaValues.get(0).getCompany(), currentDate));
			System.out.println();
			
			int days = traderThreeInd.daysTillNextTrading(betaValues.get(0).getCompany(), currentDate);
			currentDate.add(Calendar.DAY_OF_MONTH, days);
		}
		
		Double finalbalance = traderThree.getBalance()+traderThreeInd.getPortfolioValue(traderThree.getPortfolio(), currentDate);
		
		System.out.println("final balance: " + finalbalance);
		
		Double percentageChange = traderThreeInd.getMarketRateOfReturn(endDate);
		
		System.out.println(percentageChange);
	}

}
