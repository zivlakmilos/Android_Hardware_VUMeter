package net.ddns.zivlakmilos.hardwarevumeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	
	public static final String MAIN_ACTIVITY_TAG = "MainActivityTag";
	public static final int PLAYLIST_ACTIVITY_REQUEST = 1001;
	
	private Music m_musicPlayer;
	private SeekBar m_seekBar;

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
					if(m_musicPlayer.isPlaying())
						sender.setText("||");
					setupPlayer();
				} else {
					m_musicPlayer.pause();
					if(!m_musicPlayer.isPlaying());
						sender.setText(">");
				}
			}
		});
		
		m_seekBar = (SeekBar)findViewById(R.id.seekBar);
		m_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				
				m_musicPlayer.setPosition(m_seekBar.getProgress());
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				
			}
		});
		
		Button btnPrev = (Button)findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				m_musicPlayer.previous();
			}
		});
		
		Button btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				m_musicPlayer.next();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == PLAYLIST_ACTIVITY_REQUEST) {
			
			if(resultCode == RESULT_OK) {
				
				Bundle bundle = data.getExtras();
				int songIndex = bundle.getInt("songIndex");
				m_musicPlayer.setSong(songIndex);
				setupPlayer();
			}
		}
	}
	
	private void actionBluetooth_Click() {
		
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
	
	private void actionPlaylist_Click() {
		
		Intent intent = new Intent(this, PlaylistActivity.class);
		startActivityForResult(intent, PLAYLIST_ACTIVITY_REQUEST);
	}
	
	private void setupPlayer() {
		
		m_seekBar.setMax(m_musicPlayer.getDuration());
	}
}
