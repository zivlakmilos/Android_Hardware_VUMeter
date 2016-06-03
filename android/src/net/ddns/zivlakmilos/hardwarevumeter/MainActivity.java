package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String MAIN_ACTIVITY_TAG = "MainActivityTag";
	
	private Music m_musicPlayer = null;
	private File m_selectedSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnPlayPause = (Button)findViewById(R.id.btnPlayPause);
		btnPlayPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Button sender = (Button)arg0;
				
				if(sender.getText().equals(">")) {
					fileChoser();
					sender.setText("||");
				} else {
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
		}
		return super.onOptionsItemSelected(item);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(m_musicPlayer != null) {
			m_musicPlayer.stop();
		}
	}
	
	private void actionBluetooth_Click()
	{
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
	
	private void fileChoser() {
		
		MusicOpenDialog openDialog = new MusicOpenDialog(this, Environment.getExternalStorageDirectory());
		openDialog.show();
		openDialog.setOnSelectSongListener(new MusicOpenDialog.OnSelectSongListener() {
			
			@Override
			public void onSongSelected(File selectedSong) {
				
				m_selectedSong = new File(Environment.getExternalStorageDirectory() + selectedSong.getAbsolutePath());
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						playSelectedSong();
					}
				});
			}
		});
	}
	
	private void playSelectedSong() {
		
		if(m_musicPlayer == null) {
			m_musicPlayer = new Music(getApplication(), m_selectedSong);
		} else {
			m_musicPlayer.setSong(m_selectedSong);
		}
		m_musicPlayer.play();
	}
}
