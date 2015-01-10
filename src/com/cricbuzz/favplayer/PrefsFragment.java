package com.cricbuzz.favplayer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.favplayer.R;

public class PrefsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_data);
	}
}
