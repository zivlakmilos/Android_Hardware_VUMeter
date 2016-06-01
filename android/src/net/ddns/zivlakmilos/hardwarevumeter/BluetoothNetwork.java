package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.IOException;

import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class BluetoothNetwork extends Thread {
	
	private BluetoothSocket m_btSocket;
	
	public BluetoothNetwork(BluetoothSocket btSocket) {
		
		m_btSocket = btSocket;
	}
	
	public void close() {
		
		try {
			m_btSocket.close();
		} catch (IOException e) {
			Log.println(Log.ERROR, "Bluetooth", "Greska prilikom zatvaranja socket-a");
		}
	}
}
