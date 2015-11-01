package com.example.smartbin007_edited;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.smartbin007_edited.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer.LegendAlign;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import android.R.color;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetServerDataforGraphs extends AsyncTask<Void, Void, Void> {

	private Context m_context;
	private View m_view;
	ArrayList<BinInfoforGraphs> ListItems;
	BinInfoforGraphs bg;
	private Integer binid;
	private String BinArea = null;
	private static final Integer NO_BIN_SPECIFIED = -1;
	public GetServerDataforGraphs(Context context, View view, Integer binid, String Area)
	{
		this.m_context = context;
		this.m_view = view;
		ListItems = new ArrayList<BinInfoforGraphs>();	
		this.binid = binid;
		this.BinArea = Area;

	}

	JsonArrayObjects jobs = new JsonArrayObjects();

	@Override
	protected Void doInBackground(Void... arg0) {
		String complete_url = null;
		String Area = null;
		ServiceHandler sh = new ServiceHandler();
		
		if (binid == NO_BIN_SPECIFIED){
				SearchView search = (SearchView) this.m_view.findViewById(R.id.searchview);
			Area = search.getQuery().toString();
		}
			SessionManager sm = new SessionManager(this.m_context);
			String City = sm.getCity();
			if (binid == NO_BIN_SPECIFIED){
				complete_url = jobs.an_url+City+"/"+Area;
			}
			else {
				complete_url = jobs.an_url+City+"/"+BinArea;
			}
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
					String Address = null;
					JSONObject c = null;
					JSONArray array = null;
					try {
						array = bins.getJSONArray("Bins");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (binid == NO_BIN_SPECIFIED) {
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
								Address = c.getString("Address");
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

							ListItems.add(new BinInfoforGraphs(id, Area1, hashmap,Address));

						}
					}
					else {
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
							if (id == binid){
								try {
									Area1 = c.getString("Area");
									Address = c.getString("Address");
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

								bg  = new  BinInfoforGraphs(id, Area1, hashmap, Address);
							}
						}
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
			if (binid == NO_BIN_SPECIFIED)
				Createlinegraphs();
			else
				CreateBarGraph();

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
				BinInfoforGraphs bg = ListItems.get(i);
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
				series1.setTitle("BinId" + String.valueOf(bg.getBinid()));
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
			graph.getLegendRenderer().setVisible(true);
			graph.getLegendRenderer().setAlign(LegendAlign.TOP);
			Layout.addView(graph);

		}

		protected void CreateBarGraph() {

			LinearLayout Layout = (LinearLayout) m_view.findViewById(R.id.graph);
			GraphView graph = new GraphView(m_context);
			HashMap<String, Integer> item = new HashMap<String, Integer>();
			Integer filllevel = 0;
			BarGraphSeries<DataPoint> series1 = new BarGraphSeries<DataPoint>();
			item = bg.getFilllevels();
			Iterator<String> keySetIterator = item.keySet().iterator(); 

			int j = 1;
			while(keySetIterator.hasNext()){
				String key = keySetIterator.next(); 
				filllevel = item.get(key);
				series1.appendData(new DataPoint( j-1, filllevel), true, 10);
				j++;

			}

			// styling
			series1.setValueDependentColor(new ValueDependentColor<DataPoint>() {
				@Override
				public int get(DataPoint data) {
					return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
				}
			});
           
			EditText txt = (EditText) m_view.findViewById(R.id.Area);
			txt.setText(bg.getArea());
			txt = (EditText) m_view.findViewById(R.id.Address);
			txt.setText(bg.getAddress());
			
			series1.setSpacing(50);
			series1.setTitle("BinId" + String.valueOf(bg.getBinid()));

			// draw values on top
			series1.setDrawValuesOnTop(true);
			series1.setValuesOnTopColor(Color.RED);
			//series.setValuesOnTopSize(50);

			//Enter the Location to be searched.
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
			graph.getLegendRenderer().setVisible(true);
			graph.getLegendRenderer().setAlign(LegendAlign.TOP);
			graph.addSeries(series1);
			Layout.addView(graph);

		}
	}
