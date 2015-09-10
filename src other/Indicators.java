import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;


public class Indicators {
	
	//private Double blackScholes;
	//private Double annualVolatility;
	private ArrayList<DateAndPrice> stockPrice;
	private ArrayList<DateAndPrice> data;
	private double previousEMA10 = 0.0;
	private double previousEMA9 = 0.0;
	private double previousEMAEMA = 0.0;
	
	
	
	public ArrayList<DateAndPrice> getStockPriceBetween(String company, Calendar beginDate, Calendar endDate) throws ParseException {
		return read(company, beginDate, endDate);
		
	}
	
	public Double getStockPrice(String company, Calendar date) throws ParseException {
		stockPrice = read(company, date, date);
		//System.out.println(stockPrice.size());
		while(stockPrice.size() == 0) {
			Calendar tmpDate = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
			tmpDate.add(Calendar.DAY_OF_MONTH, 1);
			stockPrice = read(company, tmpDate, tmpDate);
		}
		return stockPrice.get(0).getPrice();
		
	}
	
	public int daysTillNextTrading(String companyName, Calendar date) throws ParseException {
		Calendar upperLimit = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		upperLimit.add(Calendar.DAY_OF_MONTH, 7);
		ArrayList<DateAndPrice> nextSevenDays = read(companyName, date, upperLimit);
		//System.out.println("next trading day: " + nextSevenDays.get((nextSevenDays.size()-2)).getDate().getTime());
		long diff = nextSevenDays.get(nextSevenDays.size()-2).getDate().getTimeInMillis() - date.getTimeInMillis();
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		return (int) diffDays;
	}
	
	public int daysTillLastTrading(String companyName, Calendar date) throws ParseException {
		Calendar lowerLimit = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		lowerLimit.add(Calendar.DAY_OF_MONTH, -7);
		ArrayList<DateAndPrice> previousSevenDays = read(companyName, lowerLimit, date);
		//System.out.println("next trading day: " + nextSevenDays.get((nextSevenDays.size()-2)).getDate().getTime());
		long diff = date.getTimeInMillis() - previousSevenDays.get(1).getDate().getTimeInMillis();
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		return (int) diffDays;
	}
	
	public Double getStockRateOfReturn(String company, Calendar date) throws ParseException {
		Calendar startDate = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		startDate.add(Calendar.MONTH, -6);
		//System.out.println("date requested: " + date.getTime().toString());
		//System.out.println("6 months ago: " + startDate.getTime().toString());	
		//System.out.println(startDate.getTime().toString());
		ArrayList<DateAndPrice> prices = getStockPriceBetween(company, startDate, date);
		//get the prices of FTSE100 between today and 6 months ago
		Double rateOfReturn = ((prices.get(prices.size()-1).getPrice()-prices.get(0).getPrice())/prices.get(0).getPrice())*100;
		return rateOfReturn;
	}
	
	public Double getMarketRateOfReturn(Calendar date) throws ParseException {
		Calendar startDate = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		startDate.add(Calendar.MONTH, -1);
		//System.out.println("FTSE date requested: " + date.getTime().toString());
		//System.out.println("FTSE 6 months ago: " + startDate.getTime().toString());	
		//System.out.println(startDate.getTime().toString());
		ArrayList<String> companyNames = getFiles();
		double startPrice[] = new double[companyNames.size()];
		double endPrice[] = new double[companyNames.size()];
		//System.out.println(companyNames.size());
		
		for(int i=0; i<companyNames.size(); i++) {
			ArrayList<DateAndPrice> tmp = getStockPriceBetween(companyNames.get(i), startDate, date);
			startPrice[i] = tmp.get(tmp.size()-1).getPrice();
			endPrice[i] = tmp.get(0).getPrice();
		}
		
		double averageStart = calculateAverage(startPrice);
		double averageEnd = calculateAverage(endPrice);
		
		//ArrayList<DateAndPrice> prices = getStockPriceBetween("FTSE100", startDate, date);
		//get the prices of FTSE100 between today and 6 months ago
		Double rateOfReturn = ((averageEnd-averageStart)/averageStart)*100;
		return rateOfReturn;
	}
	
	public ArrayList<StringAndDouble> getBeta(Calendar date) throws ParseException {
		ArrayList<String> companies = getFiles();
		ArrayList<StringAndDouble> beta = new ArrayList<StringAndDouble>();
		//System.out.println("No of beta values: " + companies.size());
		for(int i=0; i<companies.size(); i++) {
			//System.out.println("Date requested by Beta: " + date.getTime().toString());
			Double diff1 = getStockRateOfReturn(companies.get(i), date) - getRiskFreeRate(date);
			Double diff2 = getFTSERateOfReturn(date) - getRiskFreeRate(date);
			StringAndDouble nameAndBeta = new StringAndDouble(companies.get(i), diff1/diff2);
			beta.add(i, nameAndBeta);
			//System.out.println(companies.get(i) + ": " + diff1/diff2);
			//System.out.println("");
		}
		Collections.sort(beta, StringAndDouble.ObjectValueComparator);
		return beta;
	}
	
	private Double getFTSERateOfReturn(Calendar date) throws ParseException {
		Calendar startDate = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		startDate.add(Calendar.MONTH, -6);
		//System.out.println("date requested: " + date.getTime().toString());
		//System.out.println("6 months ago: " + startDate.getTime().toString());	
		//System.out.println(startDate.getTime().toString());
		ArrayList<DateAndPrice> prices = getStockPriceBetween("FTSE100", startDate, date);
		//get the prices of FTSE100 between today and 6 months ago
		Double rateOfReturn = ((prices.get(prices.size()-1).getPrice()-prices.get(0).getPrice())/prices.get(0).getPrice())*100;
		return rateOfReturn;
	}

	public Double getRiskFreeRate(Calendar date) {
		Double riskFreeRate = 0.5;
		
		return riskFreeRate;
	}
	
	public Double getPortfolioValue(Portfolio portfolio, Calendar date) throws ParseException {
		Double portfolioValue = 0.00;
		for(int i = 0; i<portfolio.size(); i++) {
			portfolioValue = portfolioValue + portfolio.getAmount(i)*getStockPrice(portfolio.getCompanyName(i), date);
		}
		
		return portfolioValue;
		
	}
	
	public Double getRelativeStrengthIndex(String company, Calendar date) throws ParseException {
		Double RSI = null;
		double[] upwardMovement = new double[14];
		double[] downwardMovement = new double[14];
		Calendar fifteenTradingDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar thirtyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		thirtyDaysBefore.add(Calendar.DAY_OF_MONTH, -30);
		ArrayList<DateAndPrice> closePrices = read(company, thirtyDaysBefore, date);
		
		fifteenTradingDaysBefore = closePrices.get(14).getDate();
		
		closePrices = read(company, fifteenTradingDaysBefore, date);
		
		for (int i = 1; i<closePrices.size(); i++) {
			
			Double tmpDiff = closePrices.get(i).getPrice() - closePrices.get(i-1).getPrice();
			//System.out.println(tmpDiff);
			
			if (tmpDiff < 0) {
				downwardMovement[i-1] = tmpDiff*-1;
				upwardMovement[i-1] = 0.0;
			}
			if (tmpDiff > 0) {
				upwardMovement[i-1] = tmpDiff;
				downwardMovement[i-1] = 0.0;
			}
			
		}
		
		Double averageUpward = calculateAverage(upwardMovement);
		Double averageDownward = calculateAverage(downwardMovement);
		
		Double relativeStrength = averageUpward/averageDownward;
		
		RSI = 100 - (100/(relativeStrength+1));
		
		return RSI;
		
	}
	
	public String getMACD(String company, Calendar date) throws ParseException {
		int fastEMAPeriod = 12;
		int slowEMAPeriod = 26;
		int signalPeriod = 9;
		
		int totalPeriod = slowEMAPeriod+signalPeriod;
		
		double[] fastEMA = new double[totalPeriod-fastEMAPeriod+1];
		double[] slowEMA = new double[totalPeriod-slowEMAPeriod+1];
		
		double[] twelvePrices = new double[fastEMAPeriod];
		double[] twentysixPrices = new double[slowEMAPeriod];
		double[] nineSignals = new double[signalPeriod];
		
		double fastFactor = (2.0/(fastEMAPeriod+1));
		double slowFactor = (2.0/(slowEMAPeriod+1));
		double signalFactor = (2.0/(signalPeriod+1));
		
		Calendar thirtyfiveTradingDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar eightyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		
		eightyDaysBefore.add(Calendar.DAY_OF_MONTH, -80);
		//System.out.println(sixtyDaysBefore.getTime().toString());
		
		ArrayList<DateAndPrice> closePrices = read(company, eightyDaysBefore, date);
		
		while(closePrices.size() < totalPeriod) {
			closePrices.clear();
			eightyDaysBefore.add(Calendar.DAY_OF_MONTH, -1);
			closePrices = read(company, eightyDaysBefore, date);
		}		
		
		//System.out.println(date.getTime().toString() + ": " + company + ": "  + closePrices.size());
		
		//System.out.println(closePrices.get(totalPeriod+1).getDate().getTime().toString());
		
		thirtyfiveTradingDaysBefore = closePrices.get(totalPeriod+1).getDate();
		closePrices.clear();
		closePrices = read(company, thirtyfiveTradingDaysBefore, date);
		Collections.reverse(closePrices);
		
		for(int i=0; i<fastEMAPeriod; i++) {
			twelvePrices[i] = closePrices.get(i).getPrice();
			//System.out.println("first 12 prices: " + twelvePrices[i]);
		}
		//get first 12 close prices into array
		
		Double previousTwelveDayEMA = calculateAverage(twelvePrices);
		//calc average of prices
		
		fastEMA[0] = previousTwelveDayEMA;
		//first EMA is just moving average
		
		for(int i=1; i<fastEMA.length; i++) {
			fastEMA[i] = ((closePrices.get(i+fastEMAPeriod-1).getPrice() - previousTwelveDayEMA)*fastFactor)+previousTwelveDayEMA;
			//calc EMA for rest of totalPeriod and put in array
			previousTwelveDayEMA = fastEMA[i];
		}
		
		//do the same for slowEMA
		for(int i=0; i<slowEMAPeriod; i++) {
			twentysixPrices[i] = closePrices.get(i).getPrice();
		}
		
		Double previousTwentySixDayEMA = calculateAverage(twentysixPrices);
		slowEMA[0] = previousTwentySixDayEMA;
		
		for(int i=1; i<slowEMA.length; i++) {
			slowEMA[i] = ((closePrices.get(i+slowEMAPeriod-1).getPrice() - previousTwentySixDayEMA)*slowFactor)+previousTwentySixDayEMA;
			previousTwentySixDayEMA = slowEMA[i];
		}
		
		//calc difference
		Double[] difference = new Double[slowEMA.length];
		
		for(int i=0; i<difference.length; i++) {
			difference[i] = fastEMA[i+fastEMA.length-slowEMA.length] - slowEMA[i];
			//System.out.println(difference[i]);
		}
		
		if (difference[difference.length-1] > 0.0 & difference[difference.length-2] < 0.0) {
			return "Buy";
		}
		
		//moving average for difference = signal
		for(int i=0; i<signalPeriod; i++) {
			nineSignals[i] = difference[i];
		}
		
		Double previousSignal = calculateAverage(nineSignals);
		
		Double signal = ((difference[difference.length-1] - previousSignal)*signalFactor)+previousSignal;
		
		Double histogramYday = difference[difference.length-2] - previousSignal;
		Double histogramToday = difference[difference.length-1] - signal;
		
		if(histogramYday > 0.0 && histogramToday < 0.0) {
			return "Sell";
		}
		else {
			return "Do Nothing";
		}
	}
	
	private double calculateAverage(double[] twentyPrices) {
	    if (twentyPrices == null) {
	        return 0;
	    }

	    double sum = 0;
	    for (int i = 0; i<twentyPrices.length; i++) {
	        sum = sum + twentyPrices[i];
	    }

	    return sum / twentyPrices.length;
	}
	
	public BollingerBands getBollingerBands(String company, Calendar date) throws ParseException {
		BollingerBands bands = new BollingerBands(null, null);
		
		int movingAveragePeriod = 20;
		
		double[] twentyPrices = new double[movingAveragePeriod];
		
		Calendar fortyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar twentyTradingDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		
		fortyDaysBefore.add(Calendar.DAY_OF_MONTH, -40);
		
		ArrayList<DateAndPrice> closePrices = read(company, fortyDaysBefore, date);
		
		while(closePrices.size() < movingAveragePeriod) {
			closePrices.clear();
			fortyDaysBefore.add(Calendar.DAY_OF_MONTH, -1);
			closePrices = read(company, fortyDaysBefore, date);
		}		
		
		//System.out.println(date.getTime().toString() + ": " + company + ": "  + closePrices.size());
		
		//System.out.println(closePrices.get(totalPeriod+1).getDate().getTime().toString());
		
		twentyTradingDaysBefore = closePrices.get(movingAveragePeriod-1).getDate();
		closePrices.clear();
		closePrices = read(company, twentyTradingDaysBefore, date);
		Collections.reverse(closePrices);
		
		for(int i=0; i<movingAveragePeriod; i++) {
			twentyPrices[i] = closePrices.get(i).getPrice();
			//System.out.println("first 12 prices: " + twelvePrices[i]);
		}
		
		Double movingAverage = calculateAverage(twentyPrices);
		
		StandardDeviation dev = new StandardDeviation();
		
		Double stdev = dev.evaluate(twentyPrices);
		
		bands.setUpperBollingerBand(movingAverage+(stdev*2));
		bands.setLowerBollingerBand(movingAverage-(stdev*2));
		
		return bands;
	}
	
	public ArrayList<DateAndPrice> read(String companyName, Calendar beginDate, Calendar endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String csvFile = "C:/Users/Vasu/workspace/AlgoTrading/src/data/" + companyName + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		data = new ArrayList<DateAndPrice>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				
			        // use comma as separator
				String[] lineData = line.split(cvsSplitBy);
				
				Calendar tmp = Calendar.getInstance();
				//System.out.println(sdf.format(tmp.getTime()));
				tmp.setTime(sdf.parse(lineData[0]));
				
				//System.out.println(sdf.format(beginDate.getTime()));
				
				
				if ((tmp.equals(beginDate) | tmp.after(beginDate) && tmp.before(endDate)) |  tmp.equals(endDate)) {
					DateAndPrice readvalues = new DateAndPrice(tmp, lineData[4]);
					//System.out.println(readvalues.getPrice());
					data.add(readvalues);
				}
				
				//System.out.println("Date= " + country[0] + " , Open=" + country[1]);
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		//System.out.println("Done");
		return data;
	  }
	
	public ArrayList<DateAndPrice> readHigh(String companyName, Calendar beginDate, Calendar endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String csvFile = "C:/Users/Vasu/workspace/AlgoTrading/src/data/" + companyName + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		data = new ArrayList<DateAndPrice>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				
			        // use comma as separator
				String[] lineData = line.split(cvsSplitBy);
				
				Calendar tmp = Calendar.getInstance();
				//System.out.println(sdf.format(tmp.getTime()));
				tmp.setTime(sdf.parse(lineData[0]));
				
				//System.out.println(sdf.format(beginDate.getTime()));
				
				
				if ((tmp.equals(beginDate) | tmp.after(beginDate) && tmp.before(endDate)) |  tmp.equals(endDate)) {
					DateAndPrice readvalues = new DateAndPrice(tmp, lineData[2]);
					//System.out.println(readvalues.getPrice());
					data.add(readvalues);
				}
				
				//System.out.println("Date= " + country[0] + " , Open=" + country[1]);
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		//System.out.println("Done");
		return data;
	  }
	
	public ArrayList<DateAndPrice> readLow(String companyName, Calendar beginDate, Calendar endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String csvFile = "C:/Users/Vasu/workspace/AlgoTrading/src/data/" + companyName + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		data = new ArrayList<DateAndPrice>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				
			        // use comma as separator
				String[] lineData = line.split(cvsSplitBy);
				
				Calendar tmp = Calendar.getInstance();
				//System.out.println(sdf.format(tmp.getTime()));
				tmp.setTime(sdf.parse(lineData[0]));
				
				//System.out.println(sdf.format(beginDate.getTime()));
				
				
				if ((tmp.equals(beginDate) | tmp.after(beginDate) && tmp.before(endDate)) |  tmp.equals(endDate)) {
					DateAndPrice readvalues = new DateAndPrice(tmp, lineData[3]);
					//System.out.println(readvalues.getPrice());
					data.add(readvalues);
				}
				
				//System.out.println("Date= " + country[0] + " , Open=" + country[1]);
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		//System.out.println("Done");
		return data;
	  }
	
	public ArrayList<String> getFiles(){
	    //Put filenames in arraylist<string>
	    File dir = new File("C:/Users/Vasu/workspace/AlgoTrading/src/data");
	    ArrayList<String> filenames = new ArrayList<String>();
	    for(File file : dir.listFiles()){
	        filenames.add(file.getName().substring(0, file.getName().length()-4));
	    }
	    /*for (int i = 0; i < filenames.size(); i++){
            String s = filenames.get(i);
            System.out.println("File "+i+" : "+s);
        }
        System.out.println("\n");*/
	    return filenames;
	}
	
	public Double getAroonUp(String companyName, Calendar date) throws ParseException {
		Calendar fortyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		fortyDaysBefore.add(Calendar.DAY_OF_MONTH, -40);
		
		ArrayList<DateAndPrice> highValues = readHigh(companyName, fortyDaysBefore, date);
		beginDate = highValues.get(20).getDate();
		
		highValues = readHigh(companyName, beginDate, date);
		
		Double max = 0.0;
		int periodsSinceHigh = 0;
		
		for(int i=0; i<highValues.size()-1; i++) {
			if(highValues.get(i).getPrice() > max) {
				max = highValues.get(i).getPrice();
				periodsSinceHigh = i+1;
			}
		}
		
		//System.out.println(periodsSinceHigh);
		
		Double aroonUp = (((20.0 - periodsSinceHigh)/20.0)*100.0);
		
		return aroonUp;
	}
	
	public Double getAroonDown(String companyName, Calendar date) throws ParseException {
		Calendar fortyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		fortyDaysBefore.add(Calendar.DAY_OF_MONTH, -40);
		
		ArrayList<DateAndPrice> lowValues = readLow(companyName, fortyDaysBefore, date);
		beginDate = lowValues.get(20).getDate();
		
		lowValues = readHigh(companyName, beginDate, date);
		
		Double min = 20000.0;
		int periodsSinceLow = 0;
		
		for(int i=0; i<lowValues.size()-1; i++) {
			if(lowValues.get(i).getPrice() < min) {
				min = lowValues.get(i).getPrice();
				periodsSinceLow = i+1;
			}
		}
		
		//System.out.println(periodsSinceHigh);
		
		Double aroonUp = (((20.0 - periodsSinceLow)/20.0)*100.0);
		
		return aroonUp;
	}
	
	public Double getCCI(String companyName, Calendar date) throws ParseException {
		Calendar fortyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		fortyDaysBefore.add(Calendar.DAY_OF_MONTH, -80);
		
		ArrayList<DateAndPrice> tmplowValues = readLow(companyName, fortyDaysBefore, date);
		beginDate = tmplowValues.get(40).getDate();
		
		ArrayList<DateAndPrice> closeValues = read(companyName, beginDate, date);
		ArrayList<DateAndPrice> highValues = readHigh(companyName, beginDate, date);
		ArrayList<DateAndPrice> lowValues = readLow(companyName, beginDate, date);
		
		double[] typicalPrice = new double[40];
		
		for(int i=0; i<typicalPrice.length-1; i++) {
			typicalPrice[i] = (closeValues.get(i).getPrice()+highValues.get(i).getPrice()+lowValues.get(i).getPrice())/3;
			//System.out.println("typical price: " + typicalPrice[i]);
		}
		
		double SMA = calculateAverage(typicalPrice);
		
		double[] meanDev = new double[40];
		
		for(int i=0; i<typicalPrice.length-1;i++) {
			Double difference = (SMA - typicalPrice[i])/40;
			//System.out.println("difference " + SMA + " - " + typicalPrice[i]);
			meanDev[i] = Math.abs(difference);
		}
		
		double meanDeviation = calculateAverage(meanDev);
		
		//System.out.println("Mean Dev: " + meanDeviation);
		
		Double CCI = (typicalPrice[0] - SMA)/(0.15-meanDeviation); 
		
		return CCI;
	}
	
	public Double getADX(String company, Calendar date) throws ParseException {
		//Double ATR = getAverageTrueRange(company, date, 14);
		
		Calendar twentyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		twentyDaysBefore.add(Calendar.DAY_OF_MONTH, -40);
		
		ArrayList<DateAndPrice> tmplowValues = readLow(company, twentyDaysBefore, date);
		beginDate = tmplowValues.get(27).getDate();
		
		ArrayList<DateAndPrice> highValues = readHigh(company, beginDate, date);
		ArrayList<DateAndPrice> lowValues = readLow(company, beginDate, date);
		
		double[] highDiff = new double[28];
		double[] lowDiff = new double[28];
		
		for(int i=0; i<highDiff.length-1; i++) {
			highDiff[i] = highValues.get(i).getPrice() - highValues.get(i+1).getPrice();
			lowDiff[i] = lowValues.get(i).getPrice() - lowValues.get(i+1).getPrice();
		}
		
		double[] positiveDX = new double[highDiff.length];
		double[] negativeDX = new double[highDiff.length];
		
		for(int i=0; i<positiveDX.length-1; i++) {
			if(highDiff[i] > lowDiff[i] && highDiff[i]>0) {
				positiveDX[i] = highDiff[i];
			}
			else { positiveDX[i] = 0.0; }
			
			if(lowDiff[i] > highDiff[i] && lowDiff[i]>0) {
				negativeDX[i] = lowDiff[i];
			}
			else { negativeDX[i] = 0.0; }
		}
		
		double[] smoothPositiveDX = new double[14];
		double[] smoothNegativeDX = new double[14];
		
		//System.out.println(smoothNegativeDX.length);
		
		double total1 = 0.0;
		double total2 = 0.0;
		
		for(int i=0;i<13; i++) {
			total1 = positiveDX[i+14] + total1;
			total2 = negativeDX[i+14] + total2;
		}
		
		smoothPositiveDX[0] = total1/14;
		smoothNegativeDX[0] = total2/14;
		
		for (int i=0; i<smoothNegativeDX.length-1; i++) {
			smoothPositiveDX[i+1] = ((smoothPositiveDX[i]*13)+positiveDX[13-i])/14;
			smoothNegativeDX[i+1] = ((smoothNegativeDX[i]*13)+negativeDX[13-i])/14;
			
			//System.out.println("smooth DX -ve: " + smoothNegativeDX[i+1]);
		}
		
		double[] ATR = new double[14];
		
		for(int i=0;i<ATR.length;i++) {
			ATR[i] = getAverageTrueRange(company, highValues.get(13-i).getDate(), 14);
		}
		
		double[] positiveDMI = new double[ATR.length];
		double[] negativeDMI = new double[ATR.length];
		
		for(int i=0; i<positiveDMI.length; i++) {
			positiveDMI[i] = (smoothPositiveDX[i]/ATR[i])/100;
			//System.out.println("positive: " + (smoothPositiveDX[i]/ATR[i])/100);
			negativeDMI[i] = (smoothNegativeDX[i]/ATR[i])/100;
			//System.out.println("negative " + negativeDMI[i]);
		}
		
		double[] DX = new double[positiveDMI.length];
		
		for(int i=0; i<DX.length; i++) {
			DX[i] = Math.abs((positiveDMI[i]-negativeDMI[i])/(positiveDMI[i]+negativeDMI[i]))*100;
			//System.out.println(DX[i]);
		}
		
		double ADX = calculateAverage(DX);
		
		return ADX;
	}
	
	public BollingerBands getKeltner(String company, Calendar date) throws ParseException {
		BollingerBands keltner = new BollingerBands(null, null);
		Double ATR = getAverageTrueRange(company, date, 10);
		
		Calendar twentyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		twentyDaysBefore.add(Calendar.DAY_OF_MONTH, -40);
		
		ArrayList<DateAndPrice> tmplowValues = readLow(company, twentyDaysBefore, date);
		beginDate = tmplowValues.get(19).getDate();
		
		ArrayList<DateAndPrice> closeValues = read(company, beginDate, date);
		
		double[] closePrices = new double[20];
		
		for(int i=0;i<closeValues.size()-1;i++) {
			closePrices[i] = closeValues.get(i).getPrice();
		}
		
		double SMA = calculateAverage(closePrices);
		
		double multiplier = 2.0/(20.0+1.0);
		
		if(this.previousEMA10 == 0.0) {
			keltner.setLowerBollingerBand(SMA-(2*ATR));
			keltner.setUpperBollingerBand(SMA+(2*ATR));
			this.previousEMA10 = SMA;
		}
		else {
			double EMA = (closeValues.get(0).getPrice()*multiplier) + (this.previousEMA10*(1.0-multiplier));
			this.previousEMA10 = EMA;
			keltner.setLowerBollingerBand(EMA+(2*ATR));
			keltner.setUpperBollingerBand(EMA+(2*ATR));
			
		}
		
		return keltner;
	}
	
	public Double getAverageTrueRange(String company, Calendar date, int period) throws ParseException {
		Calendar twentyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		twentyDaysBefore.add(Calendar.DAY_OF_MONTH, -period*2);
		
		ArrayList<DateAndPrice> tmplowValues = readLow(company, twentyDaysBefore, date);
		beginDate = tmplowValues.get(period-1).getDate();
		
		ArrayList<DateAndPrice> highValues = readHigh(company, beginDate, date);
		ArrayList<DateAndPrice> lowValues = readLow(company, beginDate, date);
		ArrayList<DateAndPrice> closeValues = read(company, beginDate, date);
		
		double[] ATR1 = new double[period]; 
		double[] ATR2 = new double[period]; 
		double[] ATR3 = new double[period];
		
		for(int i=0; i<ATR1.length;i++) {
			ATR1[i] = highValues.get(i).getPrice()-lowValues.get(i).getPrice();
			ATR2[i] = Math.abs(highValues.get(i).getPrice()-closeValues.get(i).getPrice());
			ATR3[i] = Math.abs(lowValues.get(i).getPrice()-closeValues.get(i).getPrice());
		}
		
		double[] trueRange = new double[period];
		for(int i=0;i<trueRange.length;i++) {
			if (ATR1[i] >= ATR2[i] & ATR1[i]>=ATR3[i]) {
				trueRange[i] = ATR1[i];
			}
			if (ATR2[i] >= ATR1[i] & ATR2[i]>=ATR3[i]) {
				trueRange[i] = ATR2[i];
			}
			else trueRange[i] = ATR3[i];
		}
		
		double averageTrueRange = calculateAverage(trueRange);
		
		return averageTrueRange;
		
		
	}
	
	public Double getMassIndex(String company, Calendar date) throws ParseException {
		
		Calendar twentyDaysBefore = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		Calendar beginDate;
		twentyDaysBefore.add(Calendar.DAY_OF_MONTH, -120);
		
		ArrayList<DateAndPrice> tmplowValues = readLow(company, twentyDaysBefore, date);
		beginDate = tmplowValues.get(41).getDate();
		
		ArrayList<DateAndPrice> highValues = readHigh(company, beginDate, date);
		ArrayList<DateAndPrice> lowValues = readLow(company, beginDate, date);
		
		double[] diff = new double[42];
		
		for(int i=0;i<diff.length-1;i++) {
			diff[i] = highValues.get(i).getPrice() - lowValues.get(i).getPrice();
			//System.out.println(diff[i]);
		}
		
		double multiplier = 2.0/(9.0+1.0);
		
		double[] EMA = new double[diff.length-9];
		
		for(int i=0; i<EMA.length-1; i++) {
			if(previousEMA9 == 0.0) {
				double[] nineDiff = new double[9];
				for(int j=0; i<8; i++) {
					nineDiff[j] = diff[j];
				}
				double SMA = calculateAverage(nineDiff);
				EMA[0] = SMA;
				previousEMA9 = EMA[0];
				//System.out.println("sma " + EMA[0]);
			}
			else {
				EMA[i] = ((diff[i]-previousEMA9)*multiplier)+previousEMA9;
				previousEMA9 = EMA[i];
				//System.out.println(i + ": "+EMA[i]);
			}
		}
		
		double EMAmultiplier = 2.0/(9.0+1.0);
		
		double[] EMAEMA = new double[EMA.length-9];
		
		for(int i=0; i<EMAEMA.length-1; i++) {
			if(previousEMAEMA == 0.0) {
				double[] nineEMA = new double[9];
				for(int j=0; i<8; i++) {
					nineEMA[j] = EMA[j];
				}
				double EMASMA = calculateAverage(nineEMA);
				EMAEMA[0] = EMASMA;
				previousEMAEMA = EMAEMA[0];
				System.out.println(EMAEMA[0]);
			}
			else {
				EMAEMA[i] = ((EMA[i]-previousEMAEMA)*EMAmultiplier)+previousEMAEMA;
				previousEMAEMA = EMAEMA[i];
				System.out.println(i + ": "+EMAEMA[i]);
			}
		}
		
		double[] ratio = new double[EMAEMA.length-1];
		
		for(int i=0; i<ratio.length-1;i++) {
			ratio[i] = EMA[i+9]/EMAEMA[i+1];
			System.out.println(EMA[i+9] + "/" + EMAEMA[i+1] + "=" + ratio[i]);
		}
		
		double massIndex = 0.0;
		
		for(int i=0;i<ratio.length-1; i++) {
			massIndex = ratio[i] + massIndex;
		}
		
		return massIndex;
	}
	
	public Double getASI() {
		Random randomGenerator = new Random();
		Double aroonDown = randomGenerator.nextDouble();
		aroonDown = aroonDown*27;
		while (aroonDown < 1000 || aroonDown > 2500) {
		aroonDown = randomGenerator.nextDouble();
		aroonDown = aroonDown*2500;
		}
		return aroonDown;
	}
}

