package com.example.smartbin007;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import com.example.smartbin007.R;

public class Register extends Activity {

	private static EditText username;
	private static EditText passwd;
	private static EditText email;
	private static EditText phone;
	private static Button register_btn;
	private final static Integer Register = 1;
	private final static Integer LOGIN_SUCCESS = 0;
	private final static Integer DATABASE_FAILURE = 1;
	private final static Integer USER_EXISTS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		OnButtonPress();
	}

	public void OnButtonPress() {
		username = (EditText) findViewById(R.id.editText_user);
		passwd = (EditText) findViewById(R.id.editText_passwd);
		email = (EditText) findViewById(R.id.editText_email);
		phone = (EditText) findViewById(R.id.editText_phone);
		register_btn = (Button) findViewById(R.id.button_register);

		register_btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if((username.getText().toString().isEmpty() ||
								passwd.getText().toString().isEmpty() ||
								email.getText().toString().isEmpty() ||
								phone.getText().toString().isEmpty()))
						{
							Toast.makeText(getApplicationContext(), "Enter all the details",Toast.LENGTH_SHORT).show();

						}

						else{
							Check_Login_data cld = new  Check_Login_data(v.getContext(), v.getRootView() , Register);
							Integer returnVal = 0;
							try {
								returnVal  = cld.execute().get();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ExecutionException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							if (returnVal == LOGIN_SUCCESS){
								Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
								SessionManager sm = new SessionManager(v.getRootView().getContext());
								sm.register(username.getText().toString(),passwd.getText().toString(), email.getText().toString(), phone.getText().toString());
								Intent intent=new Intent();  
								intent.putExtra("MESSAGE","SUCCESS");  
								setResult(2,intent);  
								finish();//finishing activity   
							}
							else if (returnVal == DATABASE_FAILURE){
								Toast.makeText(getApplicationContext(),"DataBase Error",Toast.LENGTH_SHORT).show();
							}
							else if (returnVal == USER_EXISTS){
								Toast.makeText(getApplicationContext(),"Username Exists",Toast.LENGTH_SHORT).show();

							}
							else {
								Toast.makeText(getApplicationContext(),"Unknown Failure",Toast.LENGTH_SHORT).show();
							}

						}


					}
				}
				);
	};
}

