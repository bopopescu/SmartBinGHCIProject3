package com.example.smartbin007_edited;

import java.util.ArrayList;
import java.util.HashMap;

public class BinInfoforGraphs {
       private int binid;
       private String Area;
       HashMap<String,Integer> DateFilllevel;
       private String Address;
      
                    
       public BinInfoforGraphs(int binid, String Area, HashMap<String, Integer> Filllevel, String Address) {
           this.Area = Area;
           this.binid = binid;
           this.DateFilllevel = new HashMap<String,Integer>(Filllevel);
           this.Address = Address;
           
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
    
    public void setAddress(String Address){
        this.Address= Address;
  }
    
  public String getAddress(){
         return Address;
  }
}
