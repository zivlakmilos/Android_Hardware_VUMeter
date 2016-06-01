package net.ddns.zivlakmilos.hardwarevumeter;

import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothNetwork extends Thread {
	private BluetoothSocket m_btSocket;
	
	public BluetoothNetwork(BluetoothSocket btSocket) {
		m_btSocket = btSocket;
	}
}
