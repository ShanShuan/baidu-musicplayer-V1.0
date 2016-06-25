package cn.wangzifeng.musicplayer.app;

import java.util.List;

import cn.wangzifeng.musicplayer.entity.LrcLine;
import cn.wangzifeng.musicplayer.entity.Song;

import android.app.Application;

public class PlaySongApplication extends Application{
	private List<Song> songs;
	private int position=-1;
	private List<LrcLine> lines;
	
	public List<LrcLine> getLines() {
		return lines;
	}
	public void setLines(List<LrcLine> lines) {
		this.lines = lines;
	}
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	public void next() {
		if(position==songs.size()-1){
			position=0;
		}else{
			position++;
		}
		
	}
	public void previous() {
		if(position==0){
			position=songs.size()-1;
		}else{
			position--;
		}
		
	}
}
