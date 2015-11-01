package com.example.smartbin007;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Fragment {
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
						if((city.getText().toString().isEmpty() ||
								email.getText().toString().isEmpty() 
								))
						{
							Toast.makeText(rootView.getContext(), "Enter all the details",Toast.LENGTH_SHORT).show();

						}

					}
				}
				);
		return rootView;
	}


}

