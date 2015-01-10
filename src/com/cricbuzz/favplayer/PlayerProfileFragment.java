package com.cricbuzz.favplayer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.favplayer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PlayerProfileFragment extends Fragment implements OnClickListener {

	private String url;
	private TextView mPlayername;
	private TextView mPlayerbowlStyle;
	private TextView mPlayerbatStyle;
	private TextView mPlayerrole;
	private TextView mPlayerteam;
	private ProgressDialog dialog;
	private Button mTestButton;
	private Button mOdiButton;
	private Button mt20Button;
	private String name;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		url = getArguments().getString("url");
		View v = inflater.inflate(R.layout.player_profile, container,false);
		mPlayername = (TextView)v.findViewById(R.id.playerName);
		mPlayerteam = (TextView)v.findViewById(R.id.team);
		mPlayerrole = (TextView)v.findViewById(R.id.role);
		mPlayerbatStyle = (TextView)v.findViewById(R.id.batStyle);
		mPlayerbowlStyle = (TextView)v.findViewById(R.id.bowlStyle);
		mTestButton = (Button)v.findViewById(R.id.testButton);
		mOdiButton = (Button)v.findViewById(R.id.odi);
		mt20Button = (Button)v.findViewById(R.id.t20);
		
		mTestButton.setOnClickListener(this);
		mOdiButton.setOnClickListener(this);
		mt20Button.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		dialog = Util.progressDialog(getActivity(), getActivity().getString(R.string.loading));
		JsonObjectRequest jObRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				if(dialog.isShowing())
					dialog.cancel();
				parseResponseAndSetData(object);
			}

			
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(dialog.isShowing())
					dialog.cancel();
				if(error != null)
					Crouton.makeText(getActivity(), R.string.oops_something_wrong_please_try_again, Style.ALERT).show();
			}
		});
		//Set Retry policy of Volley Request.
		jObRequest.setRetryPolicy(new DefaultRetryPolicy(Util.VOLLEY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));       
		// Access the RequestQueue through your VolleySingleton class.
		VolleySingleton.getInstance(getActivity()).addToRequestQueue(jObRequest);
	}
	
	private void parseResponseAndSetData(JSONObject object) {
		try {
			JSONObject profileObject = object.getJSONObject("playerInfo");
			Util.odiStatsObject = object.getJSONObject("odiStats");
			Util.testStatsObject = object.getJSONObject("testStats");
			Util.t20StatsObject = object.getJSONObject("t20iStats");
			
			name = profileObject.getString("fullName");
			String team = profileObject.getString("team");
			String role = profileObject.getString("player_role");
			String batStyle = profileObject.getString("playerBatStyle");
			String bowlStyle = profileObject.getString("playerBowlStyle");
			
			mPlayername.setText(name);
			mPlayerteam.setText(team);
			mPlayerrole.setText(role);
			mPlayerbatStyle.setText(batStyle);
			mPlayerbowlStyle.setText(bowlStyle);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.testButton)
			startPlayerStatsActivity("Test");
		else if(v.getId() == R.id.odi)
			startPlayerStatsActivity("ODI");
		else if(v.getId() == R.id.t20)
			startPlayerStatsActivity("T20");
		
	}

	private void startPlayerStatsActivity(String formatType) {
		Intent intent = new Intent(getActivity(), PlayerFormatDetail.class);
		intent.putExtra(formatType, formatType);
		intent.putExtra("name", name);
		startActivity(intent);
	}
	
	
}
