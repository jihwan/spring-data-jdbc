package org.springframework.data.jdbc.domain.movie;

public class Showing {
	
	Movie movie;

	public Reservation reserve(Customer customer, int audienceCount) {
		
		return new Reservation(customer, this, audienceCount);
	}
	
	public Money calulateFee() {
		return movie.calulateFee(this);
	}

	public boolean isSequence(int sequence) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPlayingOn(int dayOfWeek) {
		// TODO Auto-generated method stub
		return false;
	}

	public Interval getPlayingInterval() {
		// TODO Auto-generated method stub
		return null;
	}

	public Money getFixedFee() {
		// TODO Auto-generated method stub
		return null;
	}
}
