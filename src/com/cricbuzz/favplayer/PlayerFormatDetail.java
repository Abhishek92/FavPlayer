package com.cricbuzz.favplayer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.favplayer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PlayerFormatDetail extends Activity implements OnClickListener {

	private TextView mPlayername;
	private TextView mPlayerinning;
	private TextView mPlayerbatnotOut;
	private TextView mPlayerhighScroe;
	private TextView mPlayerballTaken;
	private TextView mPlayerfours;
	private TextView mPlayersixes;
	private TextView mPlayerruns;
	private String playerName;
	private String shareText;
	private Button mTestButton;
	private Button mOdiButton;
	private Button mt20Button;
	private Button mSendSmsButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.players_format_descirption);
		mPlayername = (TextView)findViewById(R.id.playerName);
		mPlayerinning = (TextView)findViewById(R.id.innings);
		mPlayerbatnotOut = (TextView)findViewById(R.id.notouts);
		mPlayerhighScroe = (TextView)findViewById(R.id.highestScore);
		mPlayerballTaken = (TextView)findViewById(R.id.ballsTaken);
		mPlayerfours = (TextView)findViewById(R.id.fours);
		mPlayersixes = (TextView)findViewById(R.id.sixes);
		mPlayerruns = (TextView)findViewById(R.id.runs);
		mTestButton = (Button)findViewById(R.id.testButton);
		mOdiButton = (Button)findViewById(R.id.odi);
		mt20Button = (Button)findViewById(R.id.t20);
		mSendSmsButton = (Button)findViewById(R.id.send);
		
		mTestButton.setOnClickListener(this);
		mOdiButton.setOnClickListener(this);
		mt20Button.setOnClickListener(this);
		mSendSmsButton.setOnClickListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.share_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_share:
	            openShare();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String test = getIntent().getStringExtra("Test");
		String odi = getIntent().getStringExtra("ODI");
		String t20 = getIntent().getStringExtra("T20");
		playerName = getIntent().getStringExtra("name");
		
		
		if(test != null){
			mTestButton.setEnabled(false);
			setTitle(getString(R.string.app_name)+"  | "+test);
			parseJsonDataAndSetFields(Util.testStatsObject,test);
		}
		else if(odi != null){
			mOdiButton.setEnabled(false);
			setTitle(getString(R.string.app_name)+"  | "+odi);
			parseJsonDataAndSetFields(Util.odiStatsObject,odi);
		}
		else if(t20 != null){
			mt20Button.setEnabled(false);
			setTitle(getString(R.string.app_name)+"  | "+t20);
			parseJsonDataAndSetFields(Util.t20StatsObject,t20);
		}
	}

	private void parseJsonDataAndSetFields(JSONObject object,String fromatType){
		try {
			String inning = object.getString("Innings");
			String notout = object.getString("Notouts");     
			String runs = object.getString("Runs");
			String highestScore = object.getString("HighestScore");
			String ballsTaken = object.getString("BallsTaken");
			String fours = object.getString("Fours");
			String sixes = object.getString("Sixes");
			
			shareText = "Player Name: "+playerName+" /"+" "+fromatType+"\n"
					+"Innings: "+inning+"\n"
					+"Not Out: "+notout+"\n"
					+"Runs: "+runs+"\n"
					+"Highest Score: "+highestScore+"\n"
					+"Balls Taken: "+ballsTaken+"\n"
					+"Fours: "+fours+"\n"
					+"Sixes: "+sixes;
			
			mPlayername.setText(playerName+" /"+" "+fromatType);
			mPlayerinning.setText(inning);
			mPlayerbatnotOut.setText(notout);
			mPlayerruns.setText(runs);
			mPlayerhighScroe.setText(highestScore);
			mPlayerballTaken.setText(ballsTaken);
			mPlayerfours.setText(fours);
			mPlayersixes.setText(sixes);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void openShare()
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_text_to)));
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.testButton)
			startPlayerStatsActivity("Test");
		else if(v.getId() == R.id.odi)
			startPlayerStatsActivity("ODI");
		else if(v.getId() == R.id.t20)
			startPlayerStatsActivity("T20");
		else if(v.getId() == R.id.send)
			sendSms(shareText);
			
	}
	private void startPlayerStatsActivity(String formatType) {
		Intent intent = new Intent(this, PlayerFormatDetail.class);
		intent.putExtra(formatType, formatType);
		intent.putExtra("name", playerName);
		startActivity(intent);
		finish();
	}
	
	private void sendSms(String body)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(pref.getString("mobilenumber", ""),null,body,null,null);
		Crouton.makeText(this, R.string.your_sms_is_sent_, Style.CONFIRM).show();
	}
}
