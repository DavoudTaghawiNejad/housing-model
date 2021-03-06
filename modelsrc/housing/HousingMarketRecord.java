package housing;

import java.io.Serializable;

import utilities.PriorityQueue2D;

public abstract class HousingMarketRecord implements Serializable {
	private static final long serialVersionUID = 942379254469390885L;
	private double	price;
	private int		id;	// in order to get a unique, repeatable ordering
	static int 		id_pool = 0;
	
	public HousingMarketRecord(double price) {
		this.price = price;
		id = id_pool++;
	}
	
	public abstract int getQuality();
	//{
	//	return 0;
	//}

	public double getYield() {
		return 0.0;
	}
	
	public int getId() {
		return id;
	}
	
	public double getPrice() {
		return price;
	}
	
	/*** only the housing market has the authority to change the price of a market record */
	public void setPrice(double newPrice, HousingMarket.Authority auth) {
		price = newPrice;
	}

	public static class PQComparator implements PriorityQueue2D.XYComparator<HousingMarketRecord>, Serializable {
		private static final long serialVersionUID = 6225466622291609603L;

		@Override
		public int XCompare(HousingMarketRecord arg0, HousingMarketRecord arg1) {
			double diff = arg0.price - arg1.price;
			if(diff == 0.0) {
				diff = arg0.getId() - arg1.getId();
			}
			return (int)Math.signum(diff);
		}

		@Override
		public int YCompare(HousingMarketRecord arg0, HousingMarketRecord arg1) {
			int diff = arg0.getQuality() - arg1.getQuality();
			if(diff == 0) {
				diff = arg0.getId() - arg1.getId();
			}
//			System.out.println(arg0.getQuality()+" "+arg1.getQuality());
//			System.out.println(arg0.getId()+" "+arg1.getId());

			return Integer.signum(diff);
		}
	}
	
	public static class PYComparator implements PriorityQueue2D.XYComparator<HousingMarketRecord>, Serializable {
		private static final long serialVersionUID = -193994969560422524L;

		@Override
		public int XCompare(HousingMarketRecord arg0, HousingMarketRecord arg1) {
			double diff = arg0.price - arg1.price;
			if(Double.isNaN(diff)) {
				System.out.println("Got price NaN in PYComparator");
			}
			if(diff == 0.0) {
				diff = arg0.getId() - arg1.getId();
			}
			return (int)Math.signum(diff);
		}

		@Override
		public int YCompare(HousingMarketRecord arg0, HousingMarketRecord arg1) {
			double diff = arg0.getYield() - arg1.getYield();
			if(Double.isNaN(diff)) {
				System.out.println("Got yield NaN in PYComparator");
			}
			if(diff == 0.0) {
				diff = arg0.getId() - arg1.getId();
			}
			return (int)Math.signum(diff);
		}
	}
}
