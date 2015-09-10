import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;


public class RightSidePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private XYDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	
	private JPanel dateSelectorPanel;
	
	//private JLabel test;
	
	public RightSidePanel(final GUIState guiState) throws ParseException {
		this.setLayout(new GridLayout(2, 1, 20, 20));
		
		initialiseChart(guiState);
        
		this.add(chartPanel);
		
		dateSelectorPanel = new DateSelector(guiState);
		
		this.add(dateSelectorPanel);
		
		startRefreshing(guiState);
				
		this.setVisible(true);
		
	}

	private void initialiseChart(GUIState guiState) throws ParseException {
		dataset = createDataset(guiState);
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 470));
	}
	
	private void startRefreshing(final GUIState guiState) {
		int delay = 5000; //milliseconds
		ActionListener rightPerformer = new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
				  try {
					refreshChart(guiState);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		};
			
		Timer time = new Timer(delay, rightPerformer);
			
		time.start();
	}
	
	 @SuppressWarnings("deprecation")
	private JFreeChart createChart(XYDataset dataset) {
		 
		 JFreeChart chart = ChartFactory.createTimeSeriesChart(
		            "Stock Price Chart",      // chart title
		            "Date",                      // x axis label
		            "Price",                      // y axis label
		            dataset,                  // data
		            true,                     // include legend
		            true,                     // tooltips
		            false                     // urls
		        );
		 chart.setBackgroundPaint(Color.white);
		 
		 XYPlot plot = chart.getXYPlot();
		 plot.setBackgroundPaint(Color.lightGray);
		 plot.setDomainGridlinePaint(Color.white);
		 plot.setRangeGridlinePaint(Color.white);
		 
		 
		 
		 XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
         renderer.setSeriesLinesVisible(0, true);
         renderer.setSeriesShapesVisible(0, false);
         plot.setRenderer(renderer);
         
         NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
         rangeAxis.setAutoRange(true);
		 rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		 
		 DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
		 domainAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		 domainAxis.setTickUnit((new DateTickUnit(DateTickUnit.DAY, 7)));
		 
		 
		 return chart;
	}

	private XYDataset createDataset(GUIState guiState) throws ParseException {
		
		 TimeSeries series1 = new TimeSeries("Stock Price");
		 
         Indicators ind = new Indicators();
         
         ArrayList<DateAndPrice> stockPrices = ind.getStockPriceBetween(guiState.getSelectedCompany(), guiState.getStartDate(), guiState.getEndDate());
         for(int i=0; i<stockPrices.size(); i++) {
	         Date date = stockPrices.get(i).getDate().getTime();
	         series1.add(new Day(date), stockPrices.get(i).getPrice());
         }
         
         TimeSeriesCollection dataset = new TimeSeriesCollection();
		 dataset.addSeries(series1);
		 return dataset;
		 
	 }
	
	private void refreshChart(GUIState guiState) throws ParseException{
	    this.removeAll();
	    this.revalidate(); // This removes the old chart 
	    dataset = createDataset(guiState);
	    chart = createChart(dataset); 
	    chartPanel = new ChartPanel(chart); 
	    chartPanel.setPreferredSize(new java.awt.Dimension(700, 470)); 
	    dateSelectorPanel = new DateSelector(guiState);
	    this.add(chartPanel); 
		this.add(dateSelectorPanel);
	    this.repaint(); // This method makes the new chart appear
	}

}
