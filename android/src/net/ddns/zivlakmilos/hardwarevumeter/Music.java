package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class Music {
	
	public final String MUSIC_TAG = "MusicTag";
	
	private MediaPlayer m_mediaPlayer;
	private File m_song;
	private Context m_context;
	private boolean m_isPlaying;
	
	public Music(Context context, File song) {
		
		m_context = context;
		m_song = song;
		m_mediaPlayer = MediaPlayer.create(context, Uri.parse(song.getAbsolutePath()));
		Log.d(MUSIC_TAG, song.getAbsolutePath());
		Log.d(MUSIC_TAG, song.getPath());
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
	
	public void setSong(File song) {
	}
}
