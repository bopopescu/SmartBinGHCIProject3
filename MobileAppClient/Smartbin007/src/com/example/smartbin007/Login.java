package com.example.smartbin007;

import com.example.smartbin007.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity {

	private static EditText username;
	private static EditText passwd;
	private static int Attempts;
	private static Button   login_btn;
	private static Button   register_btn;
	int attempt_cnt = 5;
	SessionManager sm = new SessionManager(getApplicationContext());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		Login();
	}


	public void Login(){
		username = (EditText)findViewById(R.id.editText_user);
		passwd = (EditText)findViewById(R.id.editText_passwd);
		login_btn = (Button)findViewById(R.id.button_login);
		register_btn = (Button)findViewById(R.id.button_register);
		Attempts  = 0;
		login_btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if((username.getText().toString().equals("user")) &&
								(passwd.getText().toString().equals("pass"))) {
							Toast.makeText(getApplicationContext(),"User Password is Correct",Toast.LENGTH_SHORT).show();
							sm.createLoginSession(username.toString(), passwd.toString());
							Intent intent = new Intent(v.getContext(), TabbedActivity.class);
							startActivity(intent);
						}							
						else {
							Toast.makeText(getApplicationContext(),"User Password is  Not Correct",Toast.LENGTH_SHORT).show();
							attempt_cnt--;
							if(attempt_cnt == 0) {
								login_btn.setEnabled(false);
							}
						}
					}
				}
				);

		register_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Register.class);
				startActivity(intent);

			}
		}
				);

	}

}

