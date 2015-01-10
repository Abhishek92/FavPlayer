package com.cricbuzz.favplayer;

import com.example.favplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class PlayerProfile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_profile);
		String url = getIntent().getStringExtra("url");
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		PlayerProfileFragment profileFragment = new PlayerProfileFragment();
		profileFragment.setArguments(bundle);
		getFragmentManager().beginTransaction().add(R.id.container, profileFragment).commit();
	}
}
