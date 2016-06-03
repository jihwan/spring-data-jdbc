package org.springframework.data.jdbc.domain.movie;

import java.util.List;

public abstract class DiscountSupport implements DiscountStrategy {

	List<Rule> rules;
	
	@Override
	public Money calculateDiscountFee(Showing showing) {
		
		for (Rule rule : rules) {
			if (rule.match(showing)) {
				return getDiscountFee(showing);
			}
		}
		
		return Money.ZERO;
	}

	protected abstract Money getDiscountFee(Showing showing);
}
