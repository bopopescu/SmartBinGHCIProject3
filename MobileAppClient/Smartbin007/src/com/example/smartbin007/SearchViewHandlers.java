package com.example.smartbin007;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;

public class SearchViewHandlers implements SearchView.OnQueryTextListener {

	private Context m_context;
	private View m_view;
	//ArrayList<String> listItems = null;
	public SearchViewHandlers(Context context, View view)
	{
		this.m_context = context;
		this.m_view = view;
		//listItems = new ArrayList<String>();
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {

		String location = query.toString();
		
		String urlLoc = location;

		try {
			urlLoc = URLEncoder.encode(urlLoc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		urlLoc = urlLoc.replaceAll(" ","%20");
		GetServerData gsd = new GetServerData(this.m_context, this.m_view,urlLoc);
		gsd.execute();
		return true;
	}

};
