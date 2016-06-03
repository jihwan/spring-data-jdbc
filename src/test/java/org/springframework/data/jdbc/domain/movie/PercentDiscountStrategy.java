package org.springframework.data.jdbc.domain.movie;

public class PercentDiscountStrategy extends DiscountSupport {

	int percent;
	
	@Override
	protected Money getDiscountFee(Showing showing) {
		return showing.getFixedFee().times(percent);
	}

}
