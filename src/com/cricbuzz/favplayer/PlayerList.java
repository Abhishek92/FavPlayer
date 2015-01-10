package com.cricbuzz.favplayer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.favplayer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PlayerList extends ListFragment implements OnItemClickListener {

	private String url = "http://m.cricbuzz.com/interview/playerlist";
	private List<PlayerRowDataItem> rowDataList;
	private ProgressDialog dialog;
	private EditText mSearchBox;
	private PlayerListAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.player_list, container,false);
		mSearchBox = (EditText) v.findViewById(R.id.searchBox);
		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				adapter.getFilter().filter(cs);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		rowDataList = new ArrayList<PlayerRowDataItem>();
		getListView().setTextFilterEnabled(true);
		dialog = Util.progressDialog(getActivity(), getActivity().getString(R.string.loading));
		JsonArrayRequest jsArrRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray arr) {
				if(dialog.isShowing())
					dialog.cancel();
				parseJsonArray(arr);
				adapter = new PlayerListAdapter(getActivity(), R.layout.player_list_item, rowDataList);
				setListAdapter(adapter);
				//mSearchBox.setAdapter(adapter);
				
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
		jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(Util.VOLLEY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));       
		// Access the RequestQueue through your VolleySingleton class.
		VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsArrRequest);
	}
	
	private void parseJsonArray(JSONArray arr) {
		for(int i=0; i<arr.length(); i++)
		{
			try {
				JSONObject jObject = arr.getJSONObject(i);
				String name = jObject.getString("name");
				String id = jObject.getString("id");
				String url = jObject.getString("url");
				PlayerRowDataItem rowData = new PlayerRowDataItem(id, name, url);
				rowDataList.add(rowData);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		PlayerRowDataItem rowData = (PlayerRowDataItem) l.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), PlayerProfile.class);
		intent.putExtra("url", rowData.url);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
		PlayerRowDataItem rowData = (PlayerRowDataItem) adapter.getItemAtPosition(pos);
		Intent intent = new Intent(getActivity(), PlayerProfile.class);
		intent.putExtra("url", rowData.url);
		startActivity(intent);
	}
}
