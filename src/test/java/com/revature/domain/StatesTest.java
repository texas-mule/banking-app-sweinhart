package com.revature.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StatesTest {
	@Test
	public void testStates() {
		List<String> stateAbr = new ArrayList<String>();
		stateAbr.add("AL");
        stateAbr.add("AK");
        stateAbr.add("AR");
        stateAbr.add("AZ");
        stateAbr.add("CA");
        stateAbr.add("CO");
        stateAbr.add("CT");
        stateAbr.add("DE");
        stateAbr.add("FL");
        stateAbr.add("GA");
        stateAbr.add("HI");
        stateAbr.add("IA");
        stateAbr.add("ID");
        stateAbr.add("IL");
        stateAbr.add("IN");
        stateAbr.add("KS");
        stateAbr.add("KY");
        stateAbr.add("LA");
        stateAbr.add("MA");
        stateAbr.add("ME");
        stateAbr.add("MD");
        stateAbr.add("MI");
        stateAbr.add("MN");
        stateAbr.add("MO");
        stateAbr.add("MS");
        stateAbr.add("MT");
        stateAbr.add("NC");
        stateAbr.add("ND");
        stateAbr.add("NE");
        stateAbr.add("NH");
        stateAbr.add("NJ");
        stateAbr.add("NM");
        stateAbr.add("NV");
        stateAbr.add("NY");
        stateAbr.add("OH");
        stateAbr.add("OK");
        stateAbr.add("OR");
        stateAbr.add("PA");
        stateAbr.add("RI");
        stateAbr.add("SC");
        stateAbr.add("SD");
        stateAbr.add("TN");
        stateAbr.add("TX");
        stateAbr.add("UT");
        stateAbr.add("VA");
        stateAbr.add("VT");
        stateAbr.add("WA");
        stateAbr.add("WI");
        stateAbr.add("WV");
        stateAbr.add("WY");
        States states = new States();
        for (int i = 0; i < stateAbr.size(); i++) {
        	assertEquals(stateAbr.get(i), states.getStateAbr().get(i));
        }
	}
}
