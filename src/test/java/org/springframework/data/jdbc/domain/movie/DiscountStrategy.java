package org.springframework.data.jdbc.domain.movie;

/**
 * 
 * 조조 상영인 경우
 * 10회 상영인 경우
 * 월요일 10:00 ~ 12:00 상영인 경우
 * 목요일 18:00 ~ 21:00 상영인 경우
 * 
 * 
 * @author Jihwan Hwang
 *
 */
public interface DiscountStrategy {

	Money calculateDiscountFee(Showing showing);
}
