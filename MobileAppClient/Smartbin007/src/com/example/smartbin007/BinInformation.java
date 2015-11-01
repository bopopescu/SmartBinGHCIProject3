package com.example.smartbin007;

public class BinInformation {
	private String category;
	private String Area;
	private String Filllevel;
	private String BinId;

	public BinInformation(String category, String Area, String Filllevel, String BinId) {
		this.category = category;
		this.Area = Area;
		this.Filllevel = Filllevel;
		this.BinId = BinId;
	}

	public void setCategory(String category){
		this.category= category;
	}

	public String getCategory(){
		return category;
	}

	public void setArea(String Area){
		this.Area= Area;
	}

	public String getArea(){
		return Area;
	}

	public String getBinId(){
		return BinId;
	}

	public void setBinId(String BinId)
	{
		this.BinId = BinId;
	}

	public void setFilllevel(String Filllevel){
		this.Filllevel= Filllevel;
	}

	public String getFilllevel(){
		return Filllevel;
	}
}


