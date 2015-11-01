package com.example.smartbin007;

import java.util.Calendar;

import com.example.smartbin007.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private View m_view;
	private int i;
	TextView fromDate;
	public DatePickerFragment(View view, int i){
		this.m_view = view;
		this.i = i;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		if (i == 1){
			fromDate = (TextView) this.m_view.findViewById(R.id.fromdate);
			fromDate.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear).append("/").append(year));
		}
		else if (i == 2){
			fromDate = (TextView) this.m_view.findViewById(R.id.todate);
			fromDate.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear).append("/").append(year));
		}
	}

}
