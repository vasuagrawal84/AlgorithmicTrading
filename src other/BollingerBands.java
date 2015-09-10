
public class BollingerBands {
	
	private Double upperBollingerBand;
	private Double lowerBollingerBand;
	
	public BollingerBands(Double upperBollingerBand, Double lowerBollingerBand) {
		this.upperBollingerBand = upperBollingerBand;
		this.lowerBollingerBand = lowerBollingerBand;
	}

	public Double getUpperBollingerBand() {
		return upperBollingerBand;
	}

	public void setUpperBollingerBand(Double upperBollingerBand) {
		this.upperBollingerBand = upperBollingerBand;
	}

	public Double getLowerBollingerBand() {
		return lowerBollingerBand;
	}

	public void setLowerBollingerBand(Double lowerBollingerBand) {
		this.lowerBollingerBand = lowerBollingerBand;
	}
}
