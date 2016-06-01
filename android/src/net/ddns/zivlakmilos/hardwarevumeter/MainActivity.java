package net.ddns.zivlakmilos.hardwarevumeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private BluetoothNetwork m_btNetwork = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		m_btNetwork = ((HardwareVUMeter)getApplication()).getBtNetwork();
	}
}
