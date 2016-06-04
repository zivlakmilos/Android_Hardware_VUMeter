package net.ddns.zivlakmilos.hardwarevumeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	public static final String MAIN_ACTIVITY_TAG = "MainActivityTag";
	
	private Music m_musicPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_musicPlayer = new Music(getApplication());
		
		Button btnPlayPause = (Button)findViewById(R.id.btnPlayPause);
		btnPlayPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Button sender = (Button)arg0;
				
				if(sender.getText().equals(">")) {
					m_musicPlayer.play();
					sender.setText("||");
				} else {
					m_musicPlayer.pause();
					sender.setText(">");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_bluetooth) {
			actionBluetooth_Click();
			return true;
		} else if(id == R.id.action_playlist) {
			actionPlaylist_Click();
		}
		return super.onOptionsItemSelected(item);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		m_musicPlayer.stop();
	}
	
	private void actionBluetooth_Click() {
		
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
	
	private void actionPlaylist_Click() {
		
		Intent intent = new Intent(this, PlaylistActivity.class);
		startActivity(intent);
	}
}
