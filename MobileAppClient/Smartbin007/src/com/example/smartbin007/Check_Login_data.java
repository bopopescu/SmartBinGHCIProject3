package com.example.smartbin007;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class Check_Login_data extends AsyncTask<Void, Void, Integer> {

	private Context m_context;
	private View m_view;
	int login_register = 0;
	JsonArrayObjects jobs = new JsonArrayObjects();
	int returnval_fromserver = 0;
	private final static Integer Login = 0;
	private final static Integer Register = 1;
	public Check_Login_data(Context context, View view, int login_register)
	{
		this.m_context = context;
		this.m_view = view;
		this.login_register = login_register;
	}	

	@Override
	protected Integer doInBackground(Void... params) {

		if (this.login_register == Login) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			EditText username = (EditText) this.m_view.findViewById(R.id.editText_user);
			EditText password = (EditText) this.m_view.findViewById(R.id.editText_passwd);
			String user =  username.getText().toString();
			String pass = password.getText().toString();

			String complete_url = jobs.login_url+user+"/"+pass;
			String jsonStr = sh.makeServiceCall(complete_url, ServiceHandler.GET);

			//Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {         

				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(jsonStr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					returnval_fromserver = jsonObj.getInt("error");
					String email = jsonObj.getString("email");
					SessionManager sm = new SessionManager(this.m_context);
					sm.setemail(email);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");

			}

		}

		else if (this.login_register == Register){
			ServiceHandler sh = new ServiceHandler();
			EditText username = (EditText) this.m_view.findViewById(R.id.editText_user);
			EditText password = (EditText) this.m_view.findViewById(R.id.editText_passwd);
			EditText email_id = (EditText) this.m_view.findViewById(R.id.editText_email);
			EditText phone =  (EditText) this.m_view.findViewById(R.id.editText_phone);
			String user =  username.getText().toString();
			String pass = password.getText().toString();
			String mail_id = email_id.getText().toString();
			String phone_num = phone.getText().toString();

			String complete_url = jobs.register_url+user+"/"+pass+"/"+phone_num+"/"+mail_id;
			String jsonStr = sh.makeServiceCall(complete_url, ServiceHandler.GET);

			//Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {         

				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(jsonStr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					returnval_fromserver = jsonObj.getInt("error");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");

			}


		}
		return returnval_fromserver;

	}



	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

	}
}