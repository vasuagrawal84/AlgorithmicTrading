import java.util.ArrayList;


public class Portfolio {
	
	private ArrayList<String> company; 
	private ArrayList<Integer> amount; 

	public Portfolio() {
		company = new ArrayList<String>();
		amount = new ArrayList<Integer>();
	}

	public void addnewCompany(int index, String element) {
		company.add(company.size(), element);
	}

	public void addnewAmount(int index, int element) {
		amount.add(index, element);
	}

	public int removeAmount(int index) {
		return amount.remove(index);
	}

	public String removeCompany(int index) {
		return company.remove(index);
	}
	
	public void subtractAmount(int index, int amounttosub) {
		Integer newAmount = new Integer(0);
		newAmount = amount.get(index) - amounttosub;
		amount.set(index, newAmount);
		if (amount.get(index) == 0) { 
			removeCompany(index);
			removeAmount(index);
		}
	}

	public int size() {
		return company.size();
	}
	
	public void addAmount(int index, int amounttoadd) {
		Integer newAmount = new Integer(0);
		newAmount = amount.get(index) + amounttoadd;
		amount.set(index, newAmount);
		//System.out.println(newAmount + "added");
	}
	
	public String getCompanyName(int index) {
		//System.out.println("porfolio size " + company.size());
		if (company.size() == 0) { return null; }
		else{ 
			//System.out.println("get company name index: " + index);
			return company.get(index);
		}
			
	}

	@Override
	public String toString() {
		return "Portfolio [company=" + company+ ", amount=" + amount + "]";
	}

	public int getAmount(int index) {
		return amount.get(index);
	}
	
	
}
