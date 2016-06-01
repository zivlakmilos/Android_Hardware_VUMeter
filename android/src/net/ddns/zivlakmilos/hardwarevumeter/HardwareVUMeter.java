package net.ddns.zivlakmilos.hardwarevumeter;

import android.app.Application;

public class HardwareVUMeter extends Application {
	
	private BluetoothNetwork m_btNetwork = null;

	public BluetoothNetwork getBtNetwork() {
		return m_btNetwork;
	}

	public void setBtNetwork(BluetoothNetwork btNetwork) {
		m_btNetwork = btNetwork;
	}
}
