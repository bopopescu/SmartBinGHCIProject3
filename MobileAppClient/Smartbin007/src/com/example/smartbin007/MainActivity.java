package com.example.smartbin007;


import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.content.Intent;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;



public class MainActivity extends Activity {
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment fragmentTab1;
	Fragment fragmentTab2;
	Fragment fragmentTab3;
	private static EditText username;
	private static EditText passwd;
	private static Button   login_btn;
	private static Button   register_btn;
	int attempt_cnt = 5;
	SessionManager sm = null;
	private final static Integer Login = 0;
	private final static Integer Register = 1;
	private final static Integer LOGIN_SUCCESS = 0;
	private final static Integer LOGIN_FAILURE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sm = new SessionManager(getApplicationContext());
		sm.putLocation(getApplicationContext(), null);
		//sm.logoutUser();
		if (sm.isLoggedIn() == false){
			Login();
		}
		else {
			CreateTabView();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void CreateTabView(){
		if ( null == fragmentTab1)
			fragmentTab1 = new LocateBin();
		if ( null == fragmentTab2)
			fragmentTab2 = new GoogleMapTab();
		if ( null == fragmentTab3)
			fragmentTab3 = new AnalyzeTab();
		setContentView(R.layout.activity_main); 

		ActionBar actionBar = getActionBar();

		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(false);

		// Hide Actionbar Title
		actionBar.setDisplayShowTitleEnabled(true);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
		//Tab1 = actionBar.newTab().setIcon(R.drawable.tab1);
		//Tab1 = actionBar.newTab().setText("GMAP");
		Tab1 = actionBar.newTab().setIcon(R.drawable.ic_action);
		Tab2 = actionBar.newTab().setIcon(R.drawable.ic_bin);
		//Tab3 = actionBar.newTab().setText("ANALYZE");
		Tab3 = actionBar.newTab().setIcon(R.drawable.ic_analyze);

		// Set Tab Listeners
		Tab2.setTabListener(new TabListenerFragment(fragmentTab1));
		Tab1.setTabListener(new TabListenerFragment(fragmentTab2));
		Tab3.setTabListener(new TabListenerFragment(fragmentTab3));																		

		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
		actionBar.addTab(Tab3);
	}

	public void Login(){
		username = (EditText)findViewById(R.id.editText_user);
		passwd = (EditText)findViewById(R.id.editText_passwd);
		login_btn = (Button)findViewById(R.id.button_login);
		register_btn = (Button)findViewById(R.id.button_register);
		login_btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						int returnVal = LOGIN_FAILURE;
						if((username.getText().toString().isEmpty() ||
								passwd.getText().toString().isEmpty() ))
						{
							Toast.makeText(getApplicationContext(), "Enter all the details",Toast.LENGTH_SHORT).show();

						}
						else{ 
							Check_Login_data cld = new  Check_Login_data(v.getContext(), v.getRootView() , Login);
							try {
								returnVal = cld.execute().get();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ExecutionException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							if (returnVal == LOGIN_SUCCESS){
								Toast.makeText(getApplicationContext(),"User Password is Correct",Toast.LENGTH_SHORT).show();
								sm.createLoginSession(username.toString(), passwd.toString());
								CreateTabView();
							}
							else if (returnVal == LOGIN_FAILURE){
								Toast.makeText(getApplicationContext(),"Username or Password is Invalid",Toast.LENGTH_SHORT).show();
								attempt_cnt--;
								if(attempt_cnt == 0) {
									login_btn.setEnabled(false);
								}
							}
							else {
								Toast.makeText(getApplicationContext(),"Unknown Failure",Toast.LENGTH_SHORT).show();
							}

						}
					}
				}
				);

		register_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Register.class);
				startActivityForResult(intent, 2);// Activity is started with requestCode 2 

			}
		}
				);

	}

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  
		// check if the request code is same as what is passed  here it is 2  
		if(requestCode==2)  
		{  
			String message=data.getStringExtra("SUCCESS");   
			CreateTabView();
		}  

	}
}