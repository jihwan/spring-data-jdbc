package org.springframework.data.jdbc.domain.movie;

public class Interval {

	public static Interval closed(int startTime, int endTime) {
		return new Interval();
	}

	public boolean includes(Interval playingInterval) {
		return false;
	}

}
