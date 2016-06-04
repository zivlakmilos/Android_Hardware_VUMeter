package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Music {
	
	public final String MUSIC_TAG = "MusicTag";
	
	private MediaPlayer m_mediaPlayer;
	private Context m_context;
	private boolean m_isPlaying;
	private ArrayList<HashMap<String, String>> m_songList = new ArrayList<HashMap<String,String>>();
	private int m_songIndex = 0;
	
	public Music(Context context) {
		
		m_context = context;
		
		createPlaylist(Environment.getExternalStorageDirectory());
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
	
	public ArrayList<HashMap<String, String>> getPlaylist() {
		return m_songList;
	}
	
	public void setSong(int songIndex) {
		
		m_songIndex = songIndex;
		String filePath = m_songList.get(songIndex).get("path");
		try {
			if(m_mediaPlayer != null)
				m_mediaPlayer.setDataSource(m_songList.get(songIndex).get(filePath));
			else
				m_mediaPlayer = MediaPlayer.create(m_context, Uri.parse(filePath));
			m_mediaPlayer.prepare();
		} catch (Exception e) {
			Log.d(MUSIC_TAG, "Greska prilikom postavljanja pesme");
		}
	}
}
