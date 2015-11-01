package com.example.smartbin007;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.smartbin007.R;
import com.jjoe64.graphview.series.DataPoint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetServerData extends AsyncTask<Void, Void, Void> {

	private Context m_context;
	private View m_view;
	private String Location;
	ArrayList<BinInformation> ListItems;
	public GetServerData(Context context, View view,String loc)
	{
		this.m_context = context;
		this.m_view = view;
		this.Location = loc;
		ListItems = new ArrayList<BinInformation>();

	}

	JsonArrayObjects jobs = new JsonArrayObjects();

	@Override
	protected Void doInBackground(Void... arg0) {

		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();
		
		String Area = Location;
		
		SessionManager sm = new SessionManager(this.m_context);
		String City = sm.getCity();
		String complete_url = jobs.list_url+City+"/"+Area;
		String jsonarray = null;
		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(complete_url, ServiceHandler.GET);

		//Log.d("Response: ", "> " + jsonStr);
		ListItems.clear();

		if (jsonStr != null) {         
			for (int j =0; j<3;j++){
				try {

					JSONObject jsonObj = new JSONObject(jsonStr);
					jsonarray = null;
					// Getting JSON Array node
					if (j==0){
						jsonarray = jobs.TAG_HIGH;
					}
					else if (j==1){
						jsonarray = jobs.TAG_MEDIUM;
					}
					else if(j==2){
						jsonarray = jobs.TAG_LOW;
					}


					JSONArray high = jsonObj.getJSONArray(jsonarray);

					// looping through All Contacts
					for (int i = 0; i < high.length(); i++) {
						try {
							JSONObject c = high.getJSONObject(i);

							String FillLevel = c.getString(jobs.TAG_FILLLEVEL);
							String area = c.getString(jobs.TAG_AREA);
							String binid = c.getString(jobs.TAG_ID);

							ListItems.add(new BinInformation(jsonarray,area, FillLevel+"%",binid));

						}
						catch(Exception e)
						{
							Log.d("Failed to parse JSON", e.toString());
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
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

		/**
		 * Updating parsed JSON data into ListView
		 * */
		SpecialListAdapter adapter = new SpecialListAdapter(this.m_context,ListItems);
		ListView listView = (ListView)(this.m_view.findViewById(R.id.listView));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Fragment an = new GraphFragment(new DataPoint[] {new DataPoint(1, 5)});
				FragmentTransaction transaction = ((Activity) view.getContext()).getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container, an);
				transaction.addToBackStack(null);
				transaction.commit(); 	

			}});
	}

}
