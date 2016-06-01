package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	
	private final int REQUEST_BT_ENABLE			= 101;
	private BluetoothAdapter m_btAdapter;
	private BroadcastReceiver m_reciver;
	private ArrayAdapter<String> m_pairedDevices;
	private BluetoothSocket m_btSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		m_pairedDevices = new ArrayAdapter(this, R.layout.bluetooth_listview);
		
		ListView lwPairedDevices = (ListView)findViewById(R.id.lwPairedDevices);
		lwPairedDevices.setAdapter(m_pairedDevices);
		
		m_btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(m_btAdapter == null)
			Toast.makeText(getApplication(), "Uredjaj nije podrzan", Toast.LENGTH_LONG).show();
		
		if(!m_btAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
		}
		
		Set<BluetoothDevice> pairedDevices = m_btAdapter.getBondedDevices();
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
	
	private class ConnectThred extends Thread {
		
		public ConnectThred(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			
			try {
				tmp = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
			} catch (IOException ex) {
				Log.println(Log.ERROR, "Bluetooth", "Kreiranje socket-a nije uspelo");
			}
			
			m_btSocket = tmp;
		}
		
		@Override
		public void run() {
			m_btAdapter.cancelDiscovery();
			
			try {
				m_btSocket.connect();
			} catch(IOException ex) {
				Log.println(Log.ERROR, "Bluetooth", "Konekcija nije uspela");
				try {
					m_btSocket.close();
				} catch (IOException ex2) {}
				return ;
			}
			
			((HardwareVUMeter)getApplication()).setBtNetwork(new BluetoothNetwork(m_btSocket));
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					finish();
				}
			});
		}
		
		public void cancel()
		{
			try {
				m_btSocket.close();
			} catch (IOException ex) {
				Log.println(Log.WARN, "Bluetooth", "Nema konekcije za zatvaranje");
			}
		}
	}
}
