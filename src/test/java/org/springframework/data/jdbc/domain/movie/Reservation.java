package org.springframework.data.jdbc.domain.movie;

/**
 * Aggregate : 개념적으로 하나인 객체 군
 * 
 * 규칙.트렌젝션 범위
 * 변경의 주체가 누구냐?
 * 등을 고려하여 aggregate 선정
 * 
 * aggregate 오용방지
 * 
 * 
 * 
 * @author Jihwan Hwang
 *
 */
public class Reservation {

//	Customer customer;
//	Showing showing;
	Money fee;
	int audienceCount;
	
	public Reservation(Customer customer, Showing showing, int audienceCount) {
		
//		this.customer = customer;
//		this.showing = showing;
		this.fee = showing.calulateFee().times(audienceCount);
		this.audienceCount = audienceCount;
		
	}

}
