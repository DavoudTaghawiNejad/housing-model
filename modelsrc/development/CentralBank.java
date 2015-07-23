package development;

import housing.CoreIndicators;

public class CentralBank {
	
	public CentralBank() {
		// Setup initial values
		firstTimeBuyerLTVLimit = 0.95;
		ownerOccupierLTVLimit= 0.9;
		buyToLetLTVLimit = 0.6;
		
		firstTimeBuyerLTILimit = 6.5;
		ownerOccupierLTILimit = 6.5;
		buyToLetLTILimit = 1000.0; // unregulated

		proportionOverLTILimit= 0.15;
		proportionOverLTVLimit= 0.0;		
	}
	
	
	public void step(CoreIndicators coreIndicators) {
//  Example policy: if house price growth is greater than 0.001 then FTB LTV limit is 0.75
//                  otherwise (if house price growth is less than or equal to  0.001)
//					FTB LTV limit is 0.95
//
//		if(coreIndicators.getHousePriceGrowth() > 0.001) {
//			firstTimeBuyerLTVLimit = 0.75;
//		} else {
//			firstTimeBuyerLTVLimit = 0.95;
//		}
	}
	
	public double loanToIncomeRegulation(Mortgage.Borrower h, boolean isHome) {
		if(isHome) {
			if(h.isFirstTimeBuyer()) {
				return(firstTimeBuyerLTILimit);
			}
			return(ownerOccupierLTILimit);
		}
		return(buyToLetLTILimit);
	}

	public double loanToValueRegulation(Mortgage.Borrower h, boolean isHome) {
		if(isHome) {
			if(h.isFirstTimeBuyer()) {
				return(firstTimeBuyerLTVLimit);
			}
			return(ownerOccupierLTVLimit);
		}
		return(buyToLetLTVLimit);
	}

	public double ownerOccupierLTILimit;	// LTI upper limit for owner-occupiers
	public double ownerOccupierLTVLimit;	// LTV upper limit for owner-occupiers
	public double buyToLetLTILimit;			// LTI upper limit for Buy-to-let investors
	public double buyToLetLTVLimit;			// LTV upper limit for Buy-to-let investors
	public double firstTimeBuyerLTILimit;	// LTI upper limit for first-time buyers
	public double firstTimeBuyerLTVLimit;	// LTV upper limit for first-time buyers
	public double proportionOverLTILimit;	// proportion of mortgages that are allowed to be above the respective LTI limit
	public double proportionOverLTVLimit;	// proportion of mortgages that are allowed to be above the respective LTV limit
}
