package contracts;

import utilities.ModelTime;
import development.IMessage;
import development.ITriggerable;
import development.Trigger;
import development.IMessage.IReceiver;

public class MarketBid extends Contract {
	long price;
	IMessage.IReceiver market;
	
	public MarketBid(IIssuer issuer, long price, IMessage.IReceiver market) {
		super(issuer);
		this.price = price;
		this.market = market;
	}
	
	public long getPrice() {
		return(price);
	}
	
	public IIssuer getIssuer() {
		return((IIssuer)issuer);
	}
	
	public interface IIssuer extends Contract.IIssuer, IMessage.IReceiver {
	}
	
	public static class Issuer extends Contract.Issuer<MarketBid> implements IIssuer {
		public Issuer() {
			super(MarketBid.class);
		}
		
		public boolean issue(MarketBid bid) {
			if(super.issue(bid, bid.market)) {
				return(true);
			}
			reIssue(bid, ModelTime.week());
			return(false);
		}

		@Override
		public boolean terminate(Contract contract) {
			if(super.terminate(contract)) {
				reIssue(((MarketBid)contract), ModelTime.week());				
			}
			return(false);
		}
		
		void reIssue(final MarketBid bid, ModelTime delay) {
			Trigger.after(delay).schedule(new ITriggerable() {
				public void trigger() {issue(bid);}
			});			
		}

	}
	
	
}