package com.example.smartbin007;

import java.util.ArrayList;
import com.example.smartbin007.R;
import android.R.color;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpecialListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<BinInformation> mList;
	LayoutInflater lInflater;
	public SpecialListAdapter(Context m_context,ArrayList<BinInformation> ListItems)	
	{
		this.m_context = m_context;
		this.mList= ListItems;

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		TextView txt;
		String category, pos;
		if (view == null) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemlayout, null);
		}

		txt = (TextView) view.findViewById(R.id.textView1);
		//txt.setText(String.valueOf(position));
		txt.setText(mList.get(position).getBinId());
		txt.setBackgroundResource(color.holo_blue_light);
		// txt.setBackgroundColor(color.holo_blue_light);

		txt = (TextView) view.findViewById(R.id.textView2);
		txt.setText(mList.get(position).getArea());
		txt.setBackgroundResource(color.holo_blue_light);
		// txt.setBackgroundColor(color.holo_blue_light);

		txt = (TextView) view.findViewById(R.id.textView3);
		pos = mList.get(position).getFilllevel();
		// DecimalFormat decimalFormat = new DecimalFormat("0.#####");
		//String result = decimalFormat.format(Double.valueOf(pos));
		txt.setText(pos);

		category = mList.get(position).getCategory();
		if (category.equals("High")){
			txt.setBackgroundResource(color.holo_red_light);
			//txt.setBackgroundColor(color.holo_red_light);
		}
		else if (category.equals("Medium")){
			txt.setBackgroundResource(color.holo_orange_light);
			//txt.setBackgroundColor(color.holo_orange_light);
		}

		else if (category.equals("Low")){
			txt.setBackgroundResource(color.holo_green_light);
			//txt.setBackgroundColor(color.holo_green_light);
		}

		return view;
	}

}


