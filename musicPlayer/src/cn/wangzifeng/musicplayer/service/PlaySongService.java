package cn.wangzifeng.musicplayer.service;

import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import cn.wangzifeng.musicplayer.app.PlaySongApplication;
import cn.wangzifeng.musicplayer.entity.GlobalConsts;
import cn.wangzifeng.musicplayer.entity.Song;
import cn.wangzifeng.musicplayer.entity.SongInfo;
import cn.wangzifeng.musicplayer.entity.SongUrl;
import cn.wangzifeng.musicplayer.modler.SongMolde;
public class PlaySongService extends Service{

	private MediaPlayer player;
	private boolean isLoop=true;
	private boolean isPlayed=false;
	protected PlaySongApplication app;
	private String musicpath;

	@Override
	public void onCreate() {
		//初始化播放器
		player = new MediaPlayer();
		player.setOnPreparedListener(new OnPreparedListener() {
			//prepare完成后  执行该方法
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				isPlayed=true;
				//发送自定义广播  告诉Activity 音乐已经开始播放
				Intent i = new Intent(GlobalConsts.ACTION_START_PLAY);
				sendBroadcast(i);
			}
		});
		//启动工作线程  每1秒给Activity发一次广播
		new WorkThread().start();
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				
				try {
					app=PlaySongApplication.getContext();
					app.next();
					final Song s=app.getSongs().get(app.getPosition());
					String song_id=s.getSong_id();
					SongMolde molde=new SongMolde();
					molde.getSongInfoBySongId(song_id, new SongMolde.SongInfoCallback() {
						

						public void onSongInfoLoaded(List<SongUrl> urls,SongInfo info) {
							//判断获取到的数据是否是null 
							if(urls == null || info==null){
								return;
							}
							//开始准备播放                                    音乐
							s.setUrls(urls);
							s.setSongInfo(info);
							//获取当前需要播放的音乐的路径
							SongUrl url =urls.get(0);
							 musicpath=url.getShow_link();
							 Log.i("musicPath", musicpath);
						}
					});
					
			
					player.reset();
					player.setDataSource(musicpath);
					player.prepareAsync();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//异步加载音乐信息
			
			}
		});
	}
	
	

	//每1秒给Activity发一次广播
	class WorkThread extends Thread{
		@Override
		public void run() {
			while(isLoop){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//发送广播
				if(player.isPlaying()){
					Intent i = new Intent(GlobalConsts.ACTION_UPDATE_PROGRESS);
					i.putExtra("current", player.getCurrentPosition());
					i.putExtra("total", player.getDuration());
					sendBroadcast(i);
				}else{
					Intent i = new Intent(GlobalConsts.ACTION_STOP_PLAY);
					sendBroadcast(i);
				}
			}
		}
	}
	
	/**
	 * 当有客户端绑定该service时  执行 
	 * context.bindService()
	 */
	public IBinder onBind(Intent intent) {
		return new SongBinder();
	}

	/**
	 * 返回给客户端的binder对象
	 * 声明开放给客户端调用的接口方法
	 */
	public class SongBinder extends Binder{
		public void stop(){
			player.pause();
		}
		
		/**
		 * 跳转到某个播放位置
		 * @param progress
		 */
		public void seekTo(int progress){
			player.seekTo(progress);
		}
		
		//暂停或播放
		public void playOrPause(){
			if(player.isPlaying()){
				player.pause();
			}else{
				player.start();
			}
		}
		
		/**
		 * 播放音乐
		 * @param url  音乐的路径
		 */
		public void playMusic(String url){
			try {
				player.reset();
				player.setDataSource(url);
				//异步加载音乐信息
				player.prepareAsync();
				//在player准备完成后  执行start播放
				//给player设置监听
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public boolean isPlaying (){
			if(player.isPlaying()){
				return true;
			}else{
				return false;
			}
		}
		public void play(){
			player.start();
		}
		public boolean isPlayed(){
			if(isPlayed){
				return true;
			}else{
				return false;
			}
		}
	}
	@Override
	public void onDestroy() {
		if(player.isPlaying()){
		player.stop();
		}
		player=null;
		super.onDestroy();
	}
}
