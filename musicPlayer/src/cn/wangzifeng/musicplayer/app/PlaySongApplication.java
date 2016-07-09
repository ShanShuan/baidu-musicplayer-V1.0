package cn.wangzifeng.musicplayer.app;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.entity.LrcLine;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.entity.Song;

public class PlaySongApplication extends Application {
	private List<Song> songs;
	private List<Music> musics;
	// 判断是不是播放本地音乐
	private boolean isLocalPlay;
	//播放模式标识 1：列表循环  2：单曲循环 3：随机播放
	private int loopFlag=1;
	
	public int getLoopFlag() {
		return loopFlag;
	}

	public void setLoopFlag(int loopFlag) {
		this.loopFlag = loopFlag;
	}

	public boolean isLocalPlay() {
		return isLocalPlay;
	}

	public void setLocalPlay(boolean isLocalPlay) {
		this.isLocalPlay = isLocalPlay;
	}

	public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
	}

	private int position = -1;
	private List<LrcLine> lines;
	private MediaPlayer player;
	public static PlaySongApplication context;

	public static PlaySongApplication getContext() {
		return context;
	}

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

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate() {
		Log.i("123", "apponcreate");
		player = new MediaPlayer();
		context = this;
		super.onCreate();
	}

	public void next() {
		if (position == (isLocalPlay ? musics : songs).size() - 1) {
			position = 0;
		} else {
			position++;
		}

	}

	public void previous() {
		if (position == 0) {
			position = (isLocalPlay ? musics : songs).size() - 1;
		} else {
			position--;
		}

	}

	public void motifyIsLocalPlay(boolean b) {
		isLocalPlay = b;
	}

	public void playSound() throws Exception {
		player.reset();
		AssetFileDescriptor fd = this.getResources().openRawResourceFd(
				R.raw.cominsound);
		player.setDataSource(fd.getFileDescriptor());
		player.prepare();
		player.start();

	}

	public void removeSound() {
		player.pause();
		player.release();
		player = null;
	}
}
