package com.example.smartbin007;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.smartbin007.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import android.R.color;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetServerDataForGraphs extends AsyncTask<Void, Void, Void> {

	private Context m_context;
	private View m_view;
	ArrayList<BinInfoForGraphs> ListItems;
	public GetServerDataForGraphs(Context context, View view)
	{
		this.m_context = context;
		this.m_view = view;
		ListItems = new ArrayList<BinInfoForGraphs>();

	}

	JsonArrayObjects jobs = new JsonArrayObjects();

	@Override
	protected Void doInBackground(Void... arg0) {

		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();
		SearchView search = (SearchView) this.m_view.findViewById(R.id.searchview);
		String Area = search.getQuery().toString();
		SessionManager sm = new SessionManager(this.m_context);
		String City = sm.getCity();
		String complete_url = jobs.an_url+City+"/"+Area;
		JSONObject bins = null;
		String jsonStr = sh.makeServiceCall(complete_url, ServiceHandler.GET);
		ListItems.clear();
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();   

		if (jsonStr != null) {         
			try {
				bins = new JSONObject(jsonStr);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for(int j=0; j<bins.length(); j++)
			{    
				int id = 0;	
				String Area1 = null;
				JSONObject c = null;
				JSONArray array = null;
				try {
					array = bins.getJSONArray("Bins");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int k= 0; k <array.length(); k++){
					try {
						c = array.getJSONObject(k);
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					try {
						id = c.getInt("id");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Area1 = c.getString("Area");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONObject month = null;
					try {
						month = c.getJSONObject("October");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//hashmap.clear();
					for (int i= 0; i< month.length(); i++){
						int Filllevel = 0;

						try {
							Filllevel = month.getInt("week"+(i+1));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						hashmap.put("week"+(i+1), Filllevel);
					}
					ListItems.add(new BinInfoForGraphs(id, Area1, hashmap));
				}

			}	

		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Createlinegraphs();

	}

	protected void Createlinegraphs() {
		// TODO Auto-generated method stub
		LinearLayout Layout = (LinearLayout) this.m_view.findViewById(R.id.graph2);
		GraphView graph = new GraphView(this.m_context);
		HashMap<String, Integer> item = new HashMap<String, Integer>();
		int filllevel = 0;
		int[] intArray = new int[] {Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.YELLOW, Color.GRAY, Color.LTGRAY, color.holo_orange_dark, color.holo_purple, color.white, color.holo_red_light};
		ArrayList<Integer> intList = new ArrayList<Integer>();

		for(int intValue : intArray) {
			intList.add(intValue);
		}    
		for (int i = 0; i< ListItems.size(); i++){

			LineGraphSeries<DataPoint> series1= new LineGraphSeries<DataPoint>();
			BinInfoForGraphs bg = ListItems.get(i);
			item = bg.getFilllevels();
			Iterator<String> keySetIterator = item.keySet().iterator(); 

			int j = 1;
			while(keySetIterator.hasNext()){
				String key = keySetIterator.next(); 
				filllevel = item.get(key);
				series1.appendData(new DataPoint( j-1, filllevel), true, 10);
				j++;

			}
			series1.setDrawDataPoints(true);
			int color = intList.get(i);
			series1.setColor(color); 
			graph.addSeries(series1);
		}

		graph.setTitleColor(Color.BLUE);
		graph.setTitle("Trends for the month of October 2015");
		graph.getGridLabelRenderer().setGridColor(Color.BLACK);
		graph.getGridLabelRenderer().setHorizontalAxisTitle("Week");
		graph.getGridLabelRenderer().setVerticalAxisTitle("Filllevels");
		graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.MAGENTA);
		graph.getGridLabelRenderer().setVerticalLabelsColor(Color.GREEN);
		StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
		staticLabelsFormatter.setHorizontalLabels(new String[] {"week1", "week2", "week3", "week4"});
		graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
		Layout.addView(graph);
	}
}



