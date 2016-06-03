package org.springframework.data.jdbc.domain.movie;

import org.springframework.transaction.annotation.Transactional;

/**
 * 도메인 로직을 싷행시키기 위한 준비 작업
 * 실질적 처리는 도메인 레이어에 위임
 * 예외 후 처리
 * @author Jihwan Hwang
 *
 */
public class ReservationService {
	
	CustomerRepository customerRepository;
	ShowingRepository showingRepository;
	ReservationRepository reservationRepository;
	
	@Transactional
	public Reservation reserveShowing(int reserverId, int showingId, int audienceCount) {
		
		Customer customer = customerRepository.find(reserverId);
		Showing showing = showingRepository.find(showingId);
		
		Reservation reservation = showing.reserve(customer, audienceCount);
		
		reservationRepository.save(reservation);
		
		return reservation;
	}
}
