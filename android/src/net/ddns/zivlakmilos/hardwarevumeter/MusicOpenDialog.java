package net.ddns.zivlakmilos.hardwarevumeter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.style.BulletSpan;

public class MusicOpenDialog {
	
	private static String FILE_TYPE = ".mp3";
	private File m_path;
	private String[] m_fileList;
	private Context m_context;
	
	private List<OnSelectSongListener> listeners = new ArrayList<OnSelectSongListener>();
	
	public MusicOpenDialog(Context context, File basePath) {
		
		m_path = basePath;
		m_context = context;
		
		if(m_path.exists()) {
			
			FilenameFilter filter = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String fileName) {
					
					File file = new File(dir, fileName);
					return fileName.contains(FILE_TYPE) | file.isDirectory();
				}
			};
			m_fileList = m_path.list(filter);
		} else
			m_fileList = new String[0];
	}
	
	public void setOnSelectSongListener(OnSelectSongListener onSelectSongListener) {
		listeners.add(onSelectSongListener);
	}
	
	public void show() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		builder.setTitle("Izbor pesme");
		builder.setItems(m_fileList, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				File selectedSong = new File(m_fileList[which]);
				for(OnSelectSongListener listener : listeners) {
					listener.onSongSelected(selectedSong);
				}
			}
		});
		builder.create().show();
	}
	
	public interface OnSelectSongListener {
		public void onSongSelected(File selectedFile);
	}
}
