package com.cricbuzz.favplayer;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

public class Util {

	public static JSONObject odiStatsObject = null;
	public static JSONObject testStatsObject = null;
	public static JSONObject t20StatsObject = null;
	//Volley Retry timeout of one and half minutes.
	public static int VOLLEY_SOCKET_TIMEOUT_MS = 90000;
	
	public static ProgressDialog progressDialog(Context context,String message)
	{
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(message);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
		
		return mProgressDialog;
	}
}
