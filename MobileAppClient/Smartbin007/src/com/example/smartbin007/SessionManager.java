package com.example.smartbin007;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "AndroidPref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String KEY_PASSWD = "passwd";

	public static final String KEY_EMAIL = "email";

	public static final String KEY_PHONE = "phone";

	public static final String KEY_CITY = "city";

	//Location details
	public static final String KEY_Location = "location";

	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String passwd){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing passwd in pref
		editor.putString(KEY_PASSWD, passwd);

		// commit changes
		editor.commit();
	}   

	public void register(String name, String passwd, String email, String phone){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing passwd in pref
		editor.putString(KEY_PASSWD, passwd);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		// Storing phone in pref
		editor.putString(KEY_PHONE, phone);

		// commit changes
		editor.commit();
	}   

	public void setemail(String email){
		// Storing login value as TRUE
		// Storing email in pref
		editor.putString(KEY_EMAIL, email);
		editor.commit();
	}   

	public void setCity(String city){
		// Storing login value as TRUE
		// Storing email in pref
		editor.putString(KEY_CITY, city);
		editor.commit();
	} 

	public String getCity(){
		// Storing login value as TRUE
		// Storing email in pref
		return pref.getString(KEY_CITY, "Bengaluru");
	}   
	/**
	 * Check login method will check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		/*  if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }*/

	}



	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));

		// user email id
		user.put(KEY_PASSWD, pref.getString(KEY_PASSWD, null));

		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		/*Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);*/
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}

	//JSOON Object
	public void putLocation(Context context, String location ){
		// Storing email in pref
		editor.putString(KEY_Location, location);
		editor.commit();
	}

	public String getLocation(){
		// Storing email in pref
		return pref.getString(KEY_Location, null);
	}
}
