package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Music {
	
	public final String MUSIC_TAG = "MusicTag";
	
	private MediaPlayer m_mediaPlayer;
	private Context m_context;
	private ArrayList<HashMap<String, String>> m_songList = new ArrayList<HashMap<String,String>>();
	private int m_songIndex;
	private Visualizer m_visualizer;
	private float m_amplitude;
	
	public Music(Context context) {
		
		m_context = context;
		
		createPlaylist(Environment.getExternalStorageDirectory());
		m_mediaPlayer = new MediaPlayer();
		setSong(0);
		
		creeateVisualizer();
	}
	
	private void createPlaylist(File home) {
		
		FilenameFilter filterMp3 = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String fileName) {
				
				return fileName.endsWith(".mp3") | fileName.endsWith(".MP3");
			}
		};
		FilenameFilter filterDir = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String fileName) {
				
				File file = new File(dir.getPath() + "/" + fileName);
				return file.isDirectory();
			}
		};
		
		if(home.listFiles(filterMp3).length > 0) {
			
			for(File file : home.listFiles(filterMp3)) {
				
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("title", file.getName().substring(0, file.getName().length() - 4));
				song.put("path", file.getPath());
				m_songList.add(song);
			}
		}
		if(home.listFiles(filterDir).length > 0) {
			
			for(File file : home.listFiles(filterDir)) {
				
				createPlaylist(file);
			}
		}
	}
	
	public void play() {
		
		try {
			m_mediaPlayer.start();
		} catch(Exception ex) {
			Log.d(MUSIC_TAG, "Greka prilikom pokusaja pustanja reprodukcije");
		}
	}
	
	public void pause() {
		
		try {
			m_mediaPlayer.pause();
		} catch(Exception ex) {
			Log.d(MUSIC_TAG, "Greska prilikom pokusaja pauziranja reprodukcije");
		}
	}
	
	public void stop() {
		
		try {
			m_mediaPlayer.stop();
		} catch(Exception ex) {
			Log.d(MUSIC_TAG, "Greska prilikom pokusaja zaustavljanja reprodukcije");
		}
	}
	
	public boolean isPlaying() {
		
		return m_mediaPlayer.isPlaying();
	}
	
	public ArrayList<HashMap<String, String>> getPlaylist() {
		return m_songList;
	}
	
	public int getDuration() {
		return m_mediaPlayer.getDuration();
	}
	
	public int getPosition() {
		return m_mediaPlayer.getCurrentPosition();
	}
	
	public void setSong(int songIndex) {
		
		boolean isPlaying = m_mediaPlayer.isPlaying();
		
		m_songIndex = songIndex;
		String filePath = m_songList.get(songIndex).get("path");
		try {
			if(m_mediaPlayer.isPlaying())
				m_mediaPlayer.stop();
			m_mediaPlayer.reset();
			m_mediaPlayer.setDataSource(filePath);
			m_mediaPlayer.prepare();
			if(isPlaying)
				m_mediaPlayer.start();
		} catch (Exception e) {
			Log.d(MUSIC_TAG, "Greska prilikom postavljanja pesme");
		}
	}
	
	public void setPosition(int position) {
		
		m_mediaPlayer.seekTo(position);
	}
	
	public void next() {
		
		int index = m_songIndex + 1;
		if(index > m_songList.size() - 1)
			index = 0;
		setSong(m_songIndex + 1);
	}
	
	public void previous() {
		
		int index = m_songIndex;
		if(m_mediaPlayer.getCurrentPosition() < 1000) {
			
			index = m_songIndex - 1;
			if(index < 0)
				index = 0;
		}
		
		setSong(index);
	}
	
	private void creeateVisualizer() {
		
		int rate = Visualizer.getMaxCaptureRate();
		
		m_visualizer = new Visualizer(0);
		
		m_visualizer.setDataCaptureListener(new OnDataCaptureListener() {
			
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
				
				m_amplitude = waveform[0] + 128.0f;
				Log.d(MUSIC_TAG, String.valueOf(m_amplitude));
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
				
			}
		}, rate, true, false);
		
		m_visualizer.setEnabled(true);
	}
}
