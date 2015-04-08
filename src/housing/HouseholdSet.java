package housing;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.math3.distribution.LogNormalDistribution;



@SuppressWarnings("serial")
public class HouseholdSet extends ArrayList<Household> {
	static public class Config {
		public int N = 5000; // initial number of households
		public double INCOME_LOG_MEDIAN = Math.log(29580); // Source: IFS: living standards, poverty and inequality in the UK (22,938 after taxes) //Math.log(20300); // Source: O.N.S 2011/2012
		public double INCOME_SHAPE = (Math.log(44360) - INCOME_LOG_MEDIAN)/0.6745; // Source: IFS: living standards, poverty and inequality in the UK (75th percentile is 32692 after tax)
		public double NON_OWNERS = 0.32; // proportion of population who do not own their home
		public double OWNOUTRIGHT = 0.47;  // proportion of homeowners who own outright (no mortgage)
		public double INVESTOR = 0.0257;   // proportion of homeowners who are buy-to-let investors (TODO: check this)
		
		public LogNormalDistribution INCOME_DISTRIBUTION;		// Annual household post-tax income
		public LogNormalDistribution buyToLetDistribution; 	// No. of houses owned by buy-to-let investors (ARLA review and index 2014)
		public LogNormalDistribution grossFinancialWealth;	
		public Config() {
			INCOME_DISTRIBUTION 	  = new LogNormalDistribution(INCOME_LOG_MEDIAN, INCOME_SHAPE);
			buyToLetDistribution  = new LogNormalDistribution(Math.log(3.44), 1.050); 	// Source: ARLA report Q2 2014
			grossFinancialWealth  = new LogNormalDistribution(Math.log(9500), 2.259); 	// Source: ONS Wealth in Great Britain table 5.8
		}
		
		// Probability of home ownership given income
		// sigma distribution on 
		public double P_Homeowner(double income) {
			double P50income = INCOME_DISTRIBUTION.inverseCumulativeProbability(NON_OWNERS); // income at 50% chance of ownership
			return(1.0/(1.0+Math.exp((P50income - income)/600.0)));
		}
		
		// --- Household structure in 2013. Source: ONS Families and households 2013
		public double POnePersonHousehold = 0.290; // Probability of a household being one person in 2013. Source: ONS families and households 2013
		public double PCouple = 0.562;
		public double PLoneParent = 0.105;
		public double PMultipleOccupation = 0.043;
		
		public static double PFemale = 100.0/205.1; // Probability of being male given that you were born in the UK 2007-2011 (Source: Birth ratios in the UK, 2013, Dept of health)
		public double PFemaleGivenOnePersonHousehold = 0.537;
		public double PFemaleGivenLoneParent = 0.872;
		public double PFemaleGivenMultipleOccupation =
				(PFemale 
				 - PFemaleGivenOnePersonHousehold * POnePersonHousehold 
				 - PFemaleGivenLoneParent * PLoneParent
				 - 0.5 * PCouple)/PMultipleOccupation; // N.B. Gay couples are roughly equal male/female Source: ONS Civil partnership statistics 2012
		
		public SampledFunction AgeGivenMaleOnePersonHousehold = new SampledFunction(new Double [][] {
			{16.0, 0.0},
			{25.0, 0.0296},
			{45.0, 0.2762},
			{65.0, 0.3684},
			{75.0, 0.1516},
			{100.0, 0.1742}
		});
		public SampledFunction AgeGivenFemaleOnePersonHousehold = new SampledFunction(new Double [][] {
				{16.0, 0.0},
				{25.0, 0.0214},
				{45.0, 0.1181},
				{65.0, 0.2753},
				{75.0, 0.2169},
				{100.0,0.3683}
		});
		public double [] PChildrenGivenCouple = {0.496 + 0.119, 0.163, 0.163, 0.059}; // number of dependent children given that the household is a copule. Source ONS Families and households 2013
		// N.B. the two figures for zero children are (no children) + (children but none dependent), 3 children represents 3-or-more 
		public double [] PChildrenGivenLoneParent = {0.307, 0.403, 0.213, 0.077}; // number of DEPENDENT children, starting from zero
		
		
	}

	public HouseholdSet() {
		super();
		config = new Config();
		this.ensureCapacity(config.N);
	}
	
	/**
	 * Initialises the households in this set
	 */
	public void start() {
		int i,j,n,p;
		double price;
		Household 	newHousehold;
		House 		newHouse;
		LinkedList<House> rentedHouses = new LinkedList<House>();
		// MortgageApproval mortgage;
		
		for(j = 0; j<config.N; ++j) { // create households
			newHousehold = new Household();
			newHousehold.annualEmploymentIncome = Math.max(
						config.INCOME_DISTRIBUTION.inverseCumulativeProbability((j+0.5)/config.N),
						Government.Config.INCOME_SUPPORT*12.0);
			newHousehold.bankBalance = config.grossFinancialWealth.inverseCumulativeProbability((j+0.5)/Model.households.config.N);

//			newHousehold.bankBalance = 1e8; // Interim value to ensure liquidity during initialisation of housing
			if(j >= config.N-Model.Nh) {
				newHouse = new House();
				newHouse.quality = (int)((j - (config.N - Model.Nh))*House.Config.N_QUALITY*1.0/(Model.Nh)); // equal distribution of houses
				if(Model.rand.nextDouble() < config.P_Homeowner(newHousehold.annualEmploymentIncome)) {
					// --- is a homeowner
					// ------------------
					newHouse.owner = newHousehold;
					if(Model.rand.nextDouble() < config.OWNOUTRIGHT) {
						// --- is an outright owner
						// ------------------------
						p = 0;
					} else {
						// --- has a mortgage
						// ------------------
						p = (int)(Model.bank.config.N_PAYMENTS*Model.rand.nextDouble()); // number of payments outstanding
					}
					price = HousingMarket.referencePrice(newHouse.quality);///(Math.pow(1.0+INFLATION,Math.floor((bank.config.N_PAYMENTS-p)/12.0)));
					if(price > Model.bank.getMaxMortgage(newHousehold, true)) {
						price = Model.bank.getMaxMortgage(newHousehold, true);
					}
					newHousehold.completeHousePurchase(new HouseSaleRecord(newHouse, 0));
					newHousehold.housePayments.get(newHouse).nPayments = p;
					newHousehold.bankBalance += newHousehold.housePayments.get(newHouse).downPayment;
				} else {
					// is renting
					newHouse.owner = null;
					newHouse.resident = newHousehold;
					rentedHouses.add(newHouse);
				}
			} else {
				// is homeless
				// newHousehold.home = null;
			}
			add(newHousehold);
		}
				
		while(rentedHouses.size() > 0) { // assign buyToLets
			do { // find a potential investor
				j = (int)(config.N - 1 - Model.rand.nextDouble()*Model.Nh);
			} while(!get(j).isHomeowner() || get(j).isPropertyInvestor());
			n = (int)(Math.exp(Model.rand.nextGaussian()*config.buyToLetDistribution.getShape() + config.buyToLetDistribution.getScale()) + 0.5); // number of houses owned
			while(n>0 && rentedHouses.size() > 0) {
				i = (int)(rentedHouses.size()*Model.rand.nextDouble()); // find a house
				newHouse = rentedHouses.get(i);
				newHouse.owner = get(j);
				newHousehold = newHouse.resident;
				p = (int)(Model.bank.config.N_PAYMENTS*Model.rand.nextDouble()); // number of payments outstanding
				price = Math.min(
						HousingMarket.referencePrice(newHouse.quality),
//						/(Math.pow(1.0+INFLATION,Math.floor((bank.config.N_PAYMENTS-p)/12.0))),
						Model.bank.getMaxMortgage(get(j), false)
						);
				get(j).completeHousePurchase(new HouseSaleRecord(newHouse, price));
				get(j).housePayments.get(newHouse).nPayments = p;
				get(j).bankBalance += get(j).housePayments.get(newHouse).downPayment;
				newHouse.resident = null;
				newHousehold.completeHouseRental(new HouseSaleRecord(newHouse,
						get(j).config.rentalOfferPriceEqn.price(get(j).housePayments.get(newHouse).monthlyPayment)));
				rentedHouses.remove(i);
				--n;
			}
			price = get(j).getPropertyInvestmentValuation();
			get(j).setDesiredPropertyInvestmentFraction(price/(price + get(j).bankBalance));
		}

	}
	
	Config config;
}
