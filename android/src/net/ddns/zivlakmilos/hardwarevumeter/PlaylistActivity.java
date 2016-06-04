package net.ddns.zivlakmilos.hardwarevumeter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlaylistActivity extends Activity {
	
	private Music m_musicPlayer;
	private ArrayList<HashMap<String, String>> m_songList;
	private ArrayAdapter<String> m_playList;
	private ListView m_lwPlaylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		
		m_lwPlaylist = (ListView)findViewById(R.id.lwPlaylist);
		
		m_musicPlayer = new Music(getApplication());
		m_songList = m_musicPlayer.getPlaylist();
		m_playList = new ArrayAdapter<String>(this, R.layout.playlist_listview);
		m_lwPlaylist.setAdapter(m_playList);
		
		for(HashMap<String, String> song : m_songList) {
			
			m_playList.add(song.get("title"));
		}
	}
}
