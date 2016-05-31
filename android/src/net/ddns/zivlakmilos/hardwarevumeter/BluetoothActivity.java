package net.ddns.zivlakmilos.hardwarevumeter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BluetoothActivity extends Activity {
	
	private String[] pairedDevices = { "ZI", "ZIMobile" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		ArrayAdapter<String> adapterPairedDevices =
				new ArrayAdapter(this, R.layout.bluetooth_listview, pairedDevices);
		
		ListView lwPairedDevices = (ListView)findViewById(R.id.lwPairedDevices);
		lwPairedDevices.setAdapter(adapterPairedDevices);
	}
}
