package com.revature.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MonthsTest {
	@Test
	public void testMonths() {
		Integer month;
		String [] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		for (int i = 0; i < 12; i++) {
			month = Integer.valueOf(Months.getMonths().get(months[i]));
			assertEquals((Integer) (i + 1), month);
		}
	}
}
