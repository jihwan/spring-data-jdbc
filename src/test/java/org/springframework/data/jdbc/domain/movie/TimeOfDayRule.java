package org.springframework.data.jdbc.domain.movie;

public class TimeOfDayRule implements Rule {
	
	int dayOfWeek;
	int startTime;
	int endTime;
	
	@Override
	public boolean match(Showing showing) {
		
		return showing.isPlayingOn(dayOfWeek) &&
				Interval.closed(startTime, endTime)
					.includes(showing.getPlayingInterval());
	}

}
