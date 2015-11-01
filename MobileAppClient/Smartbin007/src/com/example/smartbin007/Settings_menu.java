package com.example.smartbin007;

import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings_menu extends Fragment {
	Button submit = null;
	View rootView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.settings, container, false);

		submit = (Button) rootView.findViewById(R.id.submit);

		submit.setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						EditText city = (EditText) rootView.findViewById((Integer) R.id.city);
						EditText email = (EditText) rootView.findViewById((Integer) R.id.email);
						String Current_city = city.getText().toString();
						String mail_id = email.getText().toString();
						SessionManager sm = new SessionManager(v.getRootView().getContext());
						if(Current_city.isEmpty() == false )
						{

							sm.setCity(Current_city);
							Toast.makeText(rootView.getContext(), "Current City Saved",Toast.LENGTH_SHORT).show();

						}
						else if (mail_id.isEmpty() == false){
							sm.setemail(mail_id);
							Toast.makeText(rootView.getContext(), "Email-id is saved",Toast.LENGTH_SHORT).show();
						}
						else if (mail_id.isEmpty() == false && mail_id.isEmpty() == false){
							sm.setCity(Current_city);
							sm.setemail(mail_id);
							Toast.makeText(rootView.getContext(), "Email-id and City Saved",Toast.LENGTH_SHORT).show();
						}
						else if (mail_id.isEmpty() && mail_id.isEmpty())
							Toast.makeText(rootView.getContext(), "Enter City or Email",Toast.LENGTH_SHORT).show();         
					}

				}
				);	
		return rootView;
	}

}

