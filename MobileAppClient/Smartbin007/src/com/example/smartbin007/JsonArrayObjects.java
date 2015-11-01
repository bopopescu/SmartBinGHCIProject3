package com.example.smartbin007;

import org.json.JSONArray;

public class JsonArrayObjects {

	String route_url = "http://smartbin-1097.appspot.com/smartbins/routes/filllevel/";
	String list_url = "https://smartbin-1097.appspot.com/smartbins/filllevels/";
	String an_url = "http://smartbin-1097.appspot.com/smartbins/analyze/filllevels/";
	String login_url = "http://smartbin-1097.appspot.com/smartbins/user/";
	String register_url = "http://smartbin-1097.appspot.com/smartbins/user/post/";


	// JSON Node names
	String TAG_CITY = "city";
	String TAG_AREA = "Area";
	String TAG_FILLLEVEL = "fillLevel";
	String TAG_LONGITUDE = "Longitude";
	String TAG_ADDRESS = "Address";
	String TAG_LATITUDE= "Latitude";
	String TAG_HIGH = "High";
	String TAG_MEDIUM = "Medium";
	String TAG_LOW = "Low";
	String TAG_ACTIVITY = "Activity";
	String TAG_OCT = "October";
	String TAG_ID = "id";

	// contacts JSONArray
	JSONArray High, Medium,Low = null;

}
