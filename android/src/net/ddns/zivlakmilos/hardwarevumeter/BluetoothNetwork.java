package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class BluetoothNetwork extends Thread {
	
	private BluetoothSocket m_btSocket;
	private InputStream m_inputStream = null;
	private OutputStream m_outputStream = null;
	
	public BluetoothNetwork(BluetoothSocket btSocket) {
		
		m_btSocket = btSocket;
		
		try {
			m_inputStream = m_btSocket.getInputStream();
			m_outputStream = m_btSocket.getOutputStream();
		} catch (IOException ex) {
			Log.println(Log.ERROR, "Bluetooth", "Nije moguce otvoriti input/output stream");
		}
	}
	
	public void send(byte[] bytes) {
		
		try {
			m_outputStream.write(bytes);
		} catch (IOException ex) {
			Log.println(Log.ERROR, "Bluetooth", "Greska prilikom slanja podataka");
		}
	}
	
	public void close() {
		
		try {
			m_btSocket.close();
		} catch (IOException e) {
			Log.println(Log.ERROR, "Bluetooth", "Greska prilikom zatvaranja socket-a");
		}
	}
}
