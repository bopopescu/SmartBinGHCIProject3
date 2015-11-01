package com.example.smartbin007;

import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Register_Bin extends Fragment {
	Button register_bin = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_register_bin, container, false);

		register_bin = (Button) rootView.findViewById(R.id.button_registerbin);

		register_bin.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						Toast.makeText(v.getContext(), "Register Button is clicked",Toast.LENGTH_SHORT).show();

					}
				}
				);
		return rootView;
	}


}
