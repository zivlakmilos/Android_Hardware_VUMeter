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
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	
	public static final String BLUETOOTH_ACTIVITY_TAG = "BluetoothActivityTag";
	
	private final int REQUEST_BT_ENABLE			= 101;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
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
		
		lwPairedDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position,
					long id) {
				
				String strSelected = ((TextView)childView).getText().toString();
				String address = strSelected.split("[\n]")[1];
				BluetoothDevice device = m_btAdapter.getRemoteDevice(address);
				ConnectThred connectThred = new ConnectThred(device);
				connectThred.start();
			}
		});
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
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException ex) {
				Log.d(BLUETOOTH_ACTIVITY_TAG, "Kreiranje socket-a nije uspelo");
			}
			
			m_btSocket = tmp;
		}
		
		@Override
		public void run() {
			m_btAdapter.cancelDiscovery();
			
			try {
				m_btSocket.connect();
			} catch(IOException ex) {
				Log.d(BLUETOOTH_ACTIVITY_TAG, "Konekcija nije uspela");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						Toast.makeText(getApplication(),
								"Konekcija sa bluetooth uredjajem nije uspela", Toast.LENGTH_LONG).show();
					}
				});
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
				Log.d(BLUETOOTH_ACTIVITY_TAG, "Nema konekcije za zatvaranje");
			}
		}
	}
}
