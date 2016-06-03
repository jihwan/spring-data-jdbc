package org.springframework.data.jdbc.domain.movie;

public class SequenceRule implements Rule {
	
	int sequence;

	@Override
	public boolean match(Showing showing) {
		return showing.isSequence(sequence);
	}

}
