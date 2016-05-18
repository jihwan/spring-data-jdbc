package org.springframework.data.jdbc.domain.sample;

import org.adrianwalker.multilinestring.Multiline;

public class MultilineTest {

	
	public static void main(String[] args) {
		System.out.println(Foo.hello);
	}
	
	static interface Foo {

		/**
	select * from foo where name = ?
		 */
		@Multiline
		String hello = "aa";
	}
}
