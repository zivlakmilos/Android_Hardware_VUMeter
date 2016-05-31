package net.ddns.zivlakmilos.hardwarevumeter;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	
	private final int REQUEST_BT_ENABLE			= 101;
	private BroadcastReceiver m_reciver;
	private ArrayAdapter<String> m_pairedDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		m_pairedDevices = new ArrayAdapter(this, R.layout.bluetooth_listview);
		
		ListView lwPairedDevices = (ListView)findViewById(R.id.lwPairedDevices);
		lwPairedDevices.setAdapter(m_pairedDevices);
		
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter == null)
			Toast.makeText(getApplication(), "Uredjaj nije podrzan", Toast.LENGTH_LONG).show();
		
		if(!btAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
		}
		
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if(pairedDevices.size() > 0)
			for(BluetoothDevice device : pairedDevices) {
				m_pairedDevices.add(device.getName() + "\n" + device.getAddress());
			}
		
		m_reciver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				if(BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					m_pairedDevices.add(device.getName() + "\n" + device.getAddress());
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(m_reciver, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(m_reciver);
	}
}
