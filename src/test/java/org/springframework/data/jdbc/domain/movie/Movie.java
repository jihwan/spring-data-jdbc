package org.springframework.data.jdbc.domain.movie;

public class Movie {
	
	DiscountStrategy discountStrategy;
	
	Money fee;
	
	public Money calulateFee(Showing showing) {
		return fee.minus(discountStrategy.calculateDiscountFee(showing));
	}
}
