package org.springframework.data.jdbc.domain.movie;

public class NonDiscountStrategy extends DiscountSupport {

	@Override
	protected Money getDiscountFee(Showing showing) {
		return Money.ZERO;
	}

}
