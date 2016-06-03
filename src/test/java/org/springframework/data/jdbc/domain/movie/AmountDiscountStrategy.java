package org.springframework.data.jdbc.domain.movie;

public class AmountDiscountStrategy extends DiscountSupport {

	Money discountAmount;
	
	@Override
	protected Money getDiscountFee(Showing showing) {
		return discountAmount;
	}

}
