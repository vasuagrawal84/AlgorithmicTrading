import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

	
	
	public static void main(String[] args) throws ParseException {
		final Calendar currentDate = new GregorianCalendar(2012, 5, 16);
		//System.out.println(Indicators.getStockPrice("Anglo American PLC", currentDate));
		
		Indicators ind = new Indicators(); 
		System.out.println(ind.getBollingerBands("BP PLC", currentDate).getLowerBollingerBand());
		System.out.println(ind.read("BP PLC", currentDate, currentDate));
		//System.out.println(ind.getCCI("Anglo American PLC", startDate));
			//System.out.println("lower: " + ind.getKeltner("BP PLC", startDate).getLowerBollingerBand().toString());
			//System.out.println("higher: " + ind.getKeltner("BP PLC", startDate).getUpperBollingerBand().toString());
			//System.out.println(ind.getADX("BP PLC", startDate));
			//System.out.println(startDate.getTime().toString() + " " + ind.getMassIndex("BP PLC", startDate));
		ArrayList<String> companyNames = ind.getFiles();
		
//		for (String company : companyNames) {
//			System.out.println(company);
//			if(ind.getStockPrice(company, currentDate) > ind.getBollingerBands(company, currentDate).getUpperBollingerBand()) {
//				System.out.println("BUY: " + company);
//			}
//			if(ind.getStockPrice(company, currentDate) < ind.getBollingerBands(company, currentDate).getLowerBollingerBand()) {
//				System.out.println("SELL: " + company);
//			}
//			System.out.println("Done Analysing");
//		}
	}
}
