package housing;

public class HouseholdStats extends CollectorBase {
	private static final long serialVersionUID = -402486195880710795L;

	public void step() {
		BtLTotalAnnualIncome = 0.0;
    	OOTotalAnnualIncome = 0.0;
    	NonOwnerTotalAnnualIncome = 0.0;
		nRenting = 0;
    	nHomeless = 0;
    	nBtL = 0;
    	nActiveBtL = 0;
    	nHouseholds = Model.households.size();
    	rentalYield = 0.0;
    	for(Household h : Model.households) {
    		if(h.behaviour.isPropertyInvestor()) {
    			++nBtL;
    			if(h.nInvestmentProperties() > 0) ++nActiveBtL;
    			BtLTotalAnnualIncome += h.getMonthlyPreTaxIncome();
    		} else if(h.isInSocialHousing()) {
    			++nHomeless;
    	    	NonOwnerTotalAnnualIncome += h.monthlyEmploymentIncome;
    		} else if(h.isRenting()) {
    			++nRenting;
    			rentalYield += h.housePayments.get(h.home).monthlyPayment*12.0/Model.housingMarket.getAverageSalePrice(h.home.getQuality());
    	    	NonOwnerTotalAnnualIncome += h.monthlyEmploymentIncome;
    		} else {
    			OOTotalAnnualIncome += h.monthlyEmploymentIncome;
    		}
    	}
    	if(rentalYield > 0.0) rentalYield /= nRenting;
    	nNonOwner = nHomeless + nRenting;
    	nEmpty = Model.construction.housingStock + nHomeless - nHouseholds;
    	BtLTotalAnnualIncome *= 12.0; // annualise
    	OOTotalAnnualIncome *= 12.0;
    	NonOwnerTotalAnnualIncome *= 12.0;

	}

	public double [] getAgeDistribution() {
		double [] result = new double[Model.households.size()];
		int i = 0;
		for(Household h : Model.households) {
			result[i] = h.lifecycle.age;
			++i;
		}
		return(result);
	}
	public String desAgeDistribution() {
		return("Age distribution of all households");
	}
	public String nameAgeDistribution() {
		return("Age distribution of all households");
	}

	public double [] getNonOwnerAges() {
		double [] result = new double[(int)nNonOwner];
		int i = 0;
		for(Household h : Model.households) {
			if(!h.isHomeowner() && i < nNonOwner) {
				result[i++] = h.lifecycle.age;
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

	public double [] getBtLNProperties() {
		if(isActive() && nBtL > 0) {
			double [] result = new double[(int)nBtL];
			int i = 0;
			for(Household h : Model.households) {
				if(h.behaviour.isPropertyInvestor() && i<nBtL) {
					result[i] = h.nInvestmentProperties();
					++i;
				}
			}
			return(result);
		}
		return null;
	}
	public String desBtLNProperties() {
		return("Dist of Number of properties owned by BTL investors");
	}
	public String nameBtLNProperties() {
		return("Dist of Number of properties owned by BTL investors");
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
				result[i++] = h.housePayments.get(h.home).monthlyPayment*12.0/Model.housingMarket.getAverageSalePrice(h.home.getQuality());
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
	
	public double [] getLogIncomes() {
		double [] result = new double[Model.households.size()];
		int i = 0;
		for(Household h : Model.households) {
			result[i++] = Math.log(h.annualEmploymentIncome());
		}
		return(result);
	}

	public double [] getLogBankBalances() {
		double [] result = new double[Model.households.size()];
		int i = 0;
		for(Household h : Model.households) {
			result[i++] = Math.log(Math.max(0.0, h.bankBalance));
		}
		return(result);
	}

    public int getnRenting() {
		return nRenting;
	}

	public int getnHomeless() {
		return nHomeless;
	}

	public int getnNonOwner() {
		return nNonOwner;
	}

	public int getnHouseholds() {
		return nHouseholds;
	}

	public int getnEmpty() {
		return nEmpty;
	}

	public int getnBtL() {
		return nBtL;
	}
	public String desnBtL() {
		return("Number of investors with BtL gene");
	}
	public String namenBtL() {
		return("Number of BtL investors (gene)");
	}

	public int getnActiveBtL() {
		return nActiveBtL;
	}
	public String desnActiveBtL() {
		return("Number of BtL investors with one or more investment properties");
	}
	public String namenActiveBtL() {
		return("Number of BtL investors (active)");
	}

    public int 		  nRenting;
	public int 		  nHomeless;
    public int 		  nNonOwner;
    public int		  nHouseholds;
    public int 		  nBtL;
    public int 		  nActiveBtL;
    public int		  nEmpty;
    public double []  BtlNProperties; // number of properties owned by buy-to-let investors
	public double	  BtLTotalAnnualIncome;
	public double	  OOTotalAnnualIncome;
	public double	  NonOwnerTotalAnnualIncome;	
	public double	  rentalYield; // gross annual yield on occupied rental properties
}
