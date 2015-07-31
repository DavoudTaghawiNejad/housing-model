package development;

public class HouseholdStats extends ModelLeaf implements ITriggerable {
//	ModelRoot root;
	Households households;
	Construction construction;
	
    public int 		  nRenting;
    public int 		  nOwnerOccupier;
    public int 		  nPrivateHousing;
    public int		  nHouseholds;
    public int		  nHousingStock;
	public double	  totalDisposableIncome;
	public double	  rentalYield; // gross annual yield on occupied rental properties
	
	@Override
	public void start(IModelNode parent) {
		super.start(parent);
//		this.root = parent.mustGet();
		households = parent.mustFind(Households.class);
		construction = parent.mustFind(Construction.class);
		Trigger.monthly().schedule(this); // TODO: does this ever need to stop?
	}
	
	public void trigger() {
		totalDisposableIncome = 0.0;
		nRenting = 0;
    	nOwnerOccupier = 0;
    	nHouseholds = households.size();
    	rentalYield = 0.0;
    	for(Household h : households) {
//			totalDisposableIncome += h.getMonthlyDisposableIncome();
    		if(h.isOwnerOccupier()) {
    			++nOwnerOccupier;
    		} else if(h.isRenting()) {
    			++nRenting;
//    			rentalYield += h.housePayments.get(h.home).monthlyPayment*12.0/Model.housingMarket.getAverageSalePrice(h.home.quality);
    		}
    	}
//    	if(rentalYield > 0.0) rentalYield /= nRenting;
    	nPrivateHousing = nOwnerOccupier + nRenting;
    	nHousingStock = construction.housingStock;
//    	totalDisposableIncome *= 12.0; // annualise
	}

	/**
	public double [] getNonOwnerAges() {
		double [] result = new double[(int)nNonOwner];
		int i = 0;
		for(Household h : Model.households) {
			if(!h.isHomeowner() && i < nNonOwner) {
				result[i] = h.lifecycle.age;
				++i;
			}
		}
		while(i < nNonOwner) {
			result[i++] = 0.0;
		}
		return(result);
	}
	public String desNonOwnerAges() {
		return("Ages of Renters and households in social housing");
	}
	public String nameNonOwnerAges() {
		return("Renter and Social-housing ages");
	}
	
	public double [] getOwnerOccupierAges() {
		double [] result = new double[(int)nNonOwner];
		int i = 0;
		for(Household h : Model.households) {
			if(!h.isHomeowner() && i < nNonOwner) {
				result[i] = h.lifecycle.age;
				++i;
			}
		}
		while(i < nNonOwner) {
			result[i++] = 0.0;
		}
		return(result);
	}
	public String desOwnerOccupierAges() {
		return("Ages of owner-occupiers");
	}
	public String nameOwnerOccupierAges() {
		return("Ages of owner-occupiers");
	}
	
	public double getBTLProportion() {
		return(((double)(nEmpty+nRenting))/Model.construction.housingStock);
	}
	public String desBTLProportion() {
		return("Proportion of stock of housing owned by buy-to-let investors");
	}
	public String nameBTLProportion() {
		return("Buy-to-let housing stock proportion");
	}
	
	public double [] getRentalYields() {
		double [] result = new double[nRenting];
		int i = 0;
		for(Household h : Model.households) {
			if(h.isRenting() && i<nRenting) {
				result[i++] = h.housePayments.get(h.home).monthlyPayment*12.0/Model.housingMarket.getAverageSalePrice(h.home.quality);
			}
		}
		return(result);
	}
	public String desRentalYields() {
		return("Gross annual rental yield on occupied rental properties");
	}
	public String nameRentalYields() {
		return("Rental Yields");
	}
	
	public double [] getIncomes() {
		double [] result = new double[Model.households.size()];
		int i = 0;
		for(Household h : Model.households) {
			result[i++] = h.annualEmploymentIncome;
		}
		return(result);
	}

	public double [] getBankBalances() {
		double [] result = new double[Model.households.size()];
		int i = 0;
		for(Household h : Model.households) {
			result[i++] = h.bankBalance;
		}
		return(result);
	}
**/
}
