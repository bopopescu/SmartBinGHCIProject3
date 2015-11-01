package com.example.smartbin007;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

/**
 * Async task class to get json by making HTTP call
 */
public class GetServerDataForMap extends AsyncTask<Void, Void, Void> {

	private Context m_context;
	private View m_view;
	private GoogleMap googleMap;
	private String Location;
	private LatLng Origin;
	private LatLng Dest;
	ArrayList<LatLng> binlocationhigh = null;
	ArrayList<LatLng> binlocationmed = null;
	ArrayList<LatLng> binlocationlow = null;
	ArrayList<LatLng> markerPoints = null;

	public GetServerDataForMap(Context mContext, View rootView, GoogleMap googleMap, String Loc) {
		// TODO Auto-generated constructor stub
		this.m_context = mContext;
		this.Location = Loc;
		this.m_view = rootView;
		this.googleMap = googleMap;
		binlocationhigh = new ArrayList<LatLng>();
		binlocationmed = new ArrayList<LatLng>();
		binlocationlow = new ArrayList<LatLng>();
		markerPoints = new ArrayList<LatLng>();
	}

	JsonArrayObjects jobs = new JsonArrayObjects();


	@Override
	protected Void doInBackground(Void... arg0) {

		//String complete_url = jobs.route_url + Location + "/10/12";
		SessionManager sm = new SessionManager(this.m_context);
		String City = sm.getCity();


		String complete_url = jobs.route_url+City+"/"+Location+"/10/12";
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(complete_url, ServiceHandler.GET);

		// Log.d("Response: ", "> " + jsonStr);



		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				// Fill level high

				JSONArray high = jsonObj.getJSONArray("High");

				// looping through All Contacts
				int j = 0;
				for (int i = 0; i < high.length(); i++) {
					try {
						JSONObject c = high.getJSONObject(i);

						double lat = c.getDouble("Latitude");
						double lng = c.getDouble("Longitude");
						String Type = c.getString("Type");
						LatLng position = new LatLng(lat, lng);


						if(Type.equals("Origin") )
							Origin = position;
						else if(Type.equals("Destination"))
							Dest = position;
						else if(Type.equals("Waypoints"))
						{
							int wayPtIndex = 0;
							markerPoints.add(wayPtIndex++, position);
						}

						j = i;
						binlocationhigh.add(j, position);
					} catch (Exception e) {
						Log.d("Failed to parse JSON", e.toString());
					}
				}

				// Fill level med
				JSONArray med = jsonObj.getJSONArray("Medium");

				// looping through All Contacts
				j = 0;
				for (int i = 0; i < med.length(); i++) {
					try {
						JSONObject d = med.getJSONObject(i);

						double lat = d.getDouble("Latitude");
						double lng = d.getDouble("Longitude");
						LatLng position = new LatLng(lat, lng);
						j = i;
						binlocationmed.add(j, position);
					} catch (Exception e) {
						Log.d("Failed to parse JSON", e.toString());
					}
				}

				// Fill level low
				JSONArray low = jsonObj.getJSONArray("Low");

				// looping through All Contacts
				j = 0;
				for (int i = 0; i < low.length(); i++) {
					try {
						JSONObject e = low.getJSONObject(i);

						double lat = e.getDouble("Latitude");
						double lng = e.getDouble("Longitude");
						LatLng position = new LatLng(lat, lng);
						j = i;
						binlocationlow.add(j, position);
					} catch (Exception e) {
						Log.d("Failed to parse JSON", e.toString());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if((binlocationhigh.size() == 0) && (binlocationmed.size() == 0) && (binlocationlow.size() == 0))
			{
				Log.d("ServiceHandler","No bins in this location");
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Dismiss the progress dialog
		googleMap.clear();
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.setMyLocationEnabled(true);

		/**
		 * High Fill level
		 */
		for (int i = 0; i < binlocationhigh.size(); i++) {
			// create marker
			MarkerOptions marker = new MarkerOptions().position(binlocationhigh.get(i)).title("Current location ");
			// adding marker
			googleMap.addMarker(marker);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(binlocationhigh.get(i), 15);
			googleMap.animateCamera(cameraUpdate);
		}

		if(binlocationhigh.size() != 0)
		{
			String url = getDirectionsUrl(Origin, Dest);
			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}

		/**
		 * Mid fill level
		 */
		for (int i = 0; i < binlocationmed.size(); i++) {
			// create marker
			MarkerOptions marker = new MarkerOptions().position(binlocationmed.get(i))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
			// adding marker
			googleMap.addMarker(marker);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(binlocationmed.get(i), 15);
			googleMap.animateCamera(cameraUpdate);
		}

		/**
		 * low fill level
		 */
		for (int i = 0; i < binlocationlow.size(); i++) {
			// create marker
			MarkerOptions marker = new MarkerOptions().position(binlocationlow.get(i))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			// adding marker
			googleMap.addMarker(marker);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(binlocationlow.get(i), 15);
			googleMap.animateCamera(cameraUpdate);
		}

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "";
		for (int i = 0; i < markerPoints.size(); i++) {
			LatLng point = (LatLng) markerPoints.get(i);

			waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service

			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionParser parser = new DirectionParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(10);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);
		}
	}
}
