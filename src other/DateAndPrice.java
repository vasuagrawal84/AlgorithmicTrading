import java.util.Calendar;


public class DateAndPrice {
	
	private Calendar date;
	private Double price;
	
	public DateAndPrice(Calendar tmp, String price) {
		this.date = tmp;
		this.price = Double.parseDouble(price);
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
