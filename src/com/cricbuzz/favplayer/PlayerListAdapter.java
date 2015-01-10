package com.cricbuzz.favplayer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.favplayer.R;

public class PlayerListAdapter extends ArrayAdapter<PlayerRowDataItem> {

	private LayoutInflater mInflater;
	public PlayerListAdapter(Context context, int resource, List<PlayerRowDataItem> objects) {
		super(context, resource, objects);
		mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final PlayerRowDataItem rowData = getItem(position);
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.player_list_item, null);
		
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.playerTv);
		convertView.setTag(holder);
		holder.name.setText(rowData.name);
		return convertView;
	}
	
	static class ViewHolder {
		TextView name;
	}
}
