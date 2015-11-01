package com.example.smartbin007;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;



public class GoogleMapTab extends Fragment implements ConnectionCallbacks,
OnConnectionFailedListener, LocationListener{

	// Google client to interact with Google API
	private GoogleApiClient mGPSGoogleApiClient;
	private Location mGPSLastLocation;
	private LocationRequest mLocationRequest;
	LatLng latLng;
	MarkerOptions markerOptions;

	// Google Map
	private GoogleMap googleMap;
	private View rootView;
	SearchViewHandlers svh = null;

	// Location updates intervals in sec
	private static int UPDATE_INTERVAL = 10000; // 10 sec
	private static int FATEST_INTERVAL = 5000; // 5 sec
	private static int DISPLACEMENT = 10; // 10 meters
	private String location;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	private Context mContext;
	SessionManager Session ;


	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) 
	{
		Toast.makeText(mContext, "Connection failure!",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0)
	{
		SessionManager Session = new SessionManager(mContext);
		String loc = Session.getLocation();

		if(loc == null)
		{
			// Once connected with google api, get the location
			mGPSLastLocation = LocationServices.FusedLocationApi
					.getLastLocation(mGPSGoogleApiClient);
			double latitude = mGPSLastLocation.getLatitude();
			double longitude = mGPSLastLocation.getLongitude();
			LatLng position = new LatLng(latitude, longitude);
			// create marker
			MarkerOptions marker = new MarkerOptions().position(position).title("Current location "); 
			// adding marker 
			googleMap.addMarker(marker); 
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15);
			googleMap.animateCamera(cameraUpdate);

			//Get the location from server and plot it on graph based on fill levels
			Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
			List<Address> addresses;
			try {
				addresses = gcd.getFromLocation(latitude,longitude,1);
				if (addresses.size() > 0) 
					location= addresses.get(0).getSubLocality();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Plotlocation();
	}

	@Override
	public void onConnectionSuspended(int arg0)
	{
		mGPSGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		// Assign the new location
		mGPSLastLocation = location;

		Toast.makeText(mContext, "Location changed!",
				Toast.LENGTH_SHORT).show();

		// Displaying the new location on UI
		mGPSLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGPSGoogleApiClient);
	}

	/**
	 * Creating google api client object
	 * */
	protected synchronized void buildGoogleApiClient()
	{

		mGPSGoogleApiClient = new GoogleApiClient.Builder(mContext)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Creating location request object
	 * */
	protected void createLocationRequest()
	{
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Starting the location updates
	 * */
	public void startLocationUpdates()
	{
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGPSGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initializing          
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView == null)
		{
			rootView = inflater.inflate(R.layout.googlemap, container, false);
			setHasOptionsMenu(true);
		}
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(rootView != null)
		{
			ViewGroup group = (ViewGroup) rootView.getParent();

			if(group !=null)
			{
				group.removeView(rootView);
			}
		}
		Log.d("Tag", "FragmentA.onDestroyView() has been called.");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		ActionBar actionbar = getActivity().getActionBar();
		// Clear old menu.
		menu.clear();
		// Inflate new menu.
		inflater.inflate(R.menu.main, menu);
		// Set actionbar title and icon.
		actionbar.setTitle("Optimal Route");
		// actionbar.setIcon(R.drawable.ic_fragment);
	}

	public void onViewCreated(View v, Bundle savedInstanceState) {
		super.onViewCreated(v, savedInstanceState);

		// Building the GoogleApi client
		if(checkPlayServices()){
			buildGoogleApiClient();
			createLocationRequest();
		}

		try {
			// Loading map
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() 
	{
		super.onStart();
		if (mGPSGoogleApiClient != null) 
		{
			mGPSGoogleApiClient.connect();
		}
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (mGPSGoogleApiClient.isConnected())
		{
			mGPSGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		checkPlayServices();
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * @throws IOException 
	 * */
	private void initilizeMap() throws IOException {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(mContext,
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
				.show();
			}

			// Getting reference to btn_find of the layout activity_main
			Button btn_find = (Button) rootView.findViewById(R.id.btn_find);

			// Defining button click event listener for the find button
			OnClickListener findClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Getting reference to EditText to get the user input location
					EditText etLocation = (EditText) rootView.findViewById(R.id.et_location);

					// Getting user input location
					location = etLocation.getText().toString();
					if(location!=null && !location.equals("")){
						new GeocoderTask().execute(location);
					}
					Plotlocation() ;
				}
			};

			// Setting button click event listener for the find button
			btn_find.setOnClickListener(findClickListener);
		}
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(rootView.getContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {
			if(addresses==null || addresses.size()==0){
				Toast.makeText(rootView.getContext(), "No Location found", Toast.LENGTH_SHORT).show();
			}

			// Clears all the existing markers on the map
			googleMap.clear();

			// Adding Markers on Google Map for each matching address
			for(int i=0;i<addresses.size();i++){
				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(), address.getLongitude());

				String addressText = String.format("%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				googleMap.addMarker(markerOptions);

				// Locate the first location
				if(i==0)
				{
					CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
					googleMap.animateCamera(cameraUpdate);
				}
			}
		}
	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

		if (resultCode != ConnectionResult.SUCCESS) 
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				Toast.makeText(mContext,
						"This device error.", Toast.LENGTH_LONG).show();
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST);
			}
			else
			{
				Toast.makeText(mContext,
						"This device is not supported.", Toast.LENGTH_LONG).show();
			}
			return false;
		}
		return true;
	}

	private void Plotlocation()  {
		// TODO Auto-generated method stub
		SessionManager Session = new SessionManager(mContext);
		Session.putLocation(mContext, location);

		String urlLoc = location;

		try {
			urlLoc = URLEncoder.encode(urlLoc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		urlLoc = urlLoc.replaceAll(" ","%20");

		GetServerDataForMap gsd = new GetServerDataForMap(mContext,rootView,googleMap,urlLoc);
		gsd.execute();
	}
}


