import java.util.Comparator;


public class StringAndDouble implements Comparable<StringAndDouble> {
	
	private String company;
	private Double value;
	
	public StringAndDouble(String company, Double value) {
		this.company = company;
		this.value = value;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public int compareTo(StringAndDouble compareObj) {
		Double compareDouble = ((StringAndDouble) compareObj).getValue();
		return compareDouble.compareTo(this.value);
	}
	
	public static Comparator<StringAndDouble> ObjectValueComparator = new Comparator<StringAndDouble>() {

		public int compare(StringAndDouble obj1, StringAndDouble obj2) {
			
			//String str1 = obj1.getCompany().toUpperCase();
			//String str2 = obj2.getCompany().toUpperCase();
			
			//ascending order
			return obj1.compareTo(obj2);
		}
		
	};
}
