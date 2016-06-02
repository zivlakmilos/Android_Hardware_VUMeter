package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnPlayPause = (Button)findViewById(R.id.btnPlayPause);
		btnPlayPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				BluetoothNetwork btNetwork = ((HardwareVUMeter)getApplication()).getBtNetwork();
				if(btNetwork != null) {
					btNetwork.send("U".getBytes());
					Log.println(Log.DEBUG, "Bluetooth", "U".getBytes().toString());
				} else
					Log.println(Log.ERROR, "Bluetooth", "Objekat m_btNetwork nije instanciran");
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
	
	private void actionBluetooth_Click()
	{
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
}
