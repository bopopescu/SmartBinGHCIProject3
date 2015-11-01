package com.example.smartbin007;

import java.util.HashMap;

public class BinInfoForGraphs {
	private int binid;
	private String Area;
	HashMap<String,Integer> DateFilllevel;

	public BinInfoForGraphs(int binid, String Area, HashMap<String, Integer> Filllevel) {
		this.Area = Area;
		this.binid = binid;
		this.DateFilllevel = new HashMap<String,Integer>(Filllevel);
	}

	public int getBinid() {
		return binid;
	}

	public void setBinid(int binid) {
		this.binid = binid;
	}

	public HashMap<String,Integer> getFilllevels() {
		return DateFilllevel;
	}

	public void setFilllevels(HashMap<String,Integer> filllevels) {
		this.DateFilllevel = filllevels;
	}


	public void setArea(String Area){
		this.Area= Area;
	}

	public String getArea(){
		return Area;
	}


}



