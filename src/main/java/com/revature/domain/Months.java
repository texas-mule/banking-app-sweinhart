package com.revature.domain;

import java.util.HashMap;

public class Months {
	
	private static HashMap<String, String> months = new HashMap<String, String>();	


	public static HashMap<String, String> getMonths() {
		months.put("Jan", "01");
		months.put("Feb", "02");
		months.put("Mar", "03");
		months.put("Apr", "04");
		months.put("May", "05");
		months.put("Jun", "06");
		months.put("Jul", "07");
		months.put("Aug", "08");
		months.put("Sep", "09");
		months.put("Oct", "10");
		months.put("Nov", "11");
		months.put("Dec", "12");
		return months;
	}
	
}
