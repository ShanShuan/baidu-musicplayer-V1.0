package cn.wangzifeng.musicplayer.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

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
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.entity.Song;
import cn.wangzifeng.musicplayer.entity.SongInfo;
import cn.wangzifeng.musicplayer.entity.SongUrl;
import cn.wangzifeng.musicplayer.modler.SongMolde;

public class PlaySongService extends Service {

	private MediaPlayer player;
	private boolean isLoop = true;
	private boolean isPlayed = false;
	protected PlaySongApplication app;
	private String musicpath;
	private SongMolde molde;
	private int loopFlag;
	Random random1;
	Random random2;

	@Override
	public void onCreate() {
		app = PlaySongApplication.getContext();
		random1=new Random();
		random2=new Random();
		
		molde = new SongMolde();
		// 初始化播放器
		player = new MediaPlayer();
		player.setOnPreparedListener(new OnPreparedListener() {
			// prepare完成后 执行该方法
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				isPlayed = true;
				// 发送自定义广播 告诉Activity 音乐已经开始播放
				Intent i = new Intent(GlobalConsts.ACTION_START_PLAY);
				sendBroadcast(i);
			}
		});
		// 启动工作线程 每1秒给Activity发一次广播
		new WorkThread().start();
		complete();
	}

	private void complete() {
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				loopFlag=app.getLoopFlag();
				if(loopFlag==1){
					
				app.next();
				if(!app.isLocalPlay()){
				final Song s = app.getSongs().get(app.getPosition());
				String song_id = s.getSong_id();
				
				molde.getSongInfoBySongId(song_id,
						new SongMolde.SongInfoCallback() {

							public void onSongInfoLoaded(List<SongUrl> urls,
									SongInfo info) {
								// 判断获取到的数据是否是null
								if (urls == null || info == null) {
									return;
								}
								// 开始准备播放 音乐
								s.setUrls(urls);
								s.setSongInfo(info);
								// 获取当前需要播放的音乐的路径
								SongUrl url = urls.get(0);
								musicpath = url.getShow_link();
								Log.i("musicPath", musicpath);
								try {
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
							}
						});
				}else{
					try {
						Log.i("123", "自动播放下一首");
						Music music=app.getMusics().get(app.getPosition());
						
						String url=music.getPath();
						Log.i("url", ""+url);
						player.reset();
						player.setDataSource(url);
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
					
				}

			}else if(loopFlag==2){
				
				// 单曲循环
				
				if(!app.isLocalPlay()){
					//不是播放本地音乐
					 Song s = app.getSongs().get(app.getPosition());
					
					try {
						player.reset();
						player.setDataSource(s.getUrls().get(0).getShow_link());
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
					
				}else{
					//播放本地音乐
					
					try {
						Music music=app.getMusics().get(app.getPosition());
						
						String url=music.getPath();
						player.reset();
						player.setDataSource(url);
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
					
				}
			}else if(loopFlag==3){
				// 随机播放
				if(!app.isLocalPlay()){
					//不是播放本地音乐
					
					int p=random1.nextInt(app.getSongs().size());
					app.setPosition(p);
					Log.i("p", "位置"+p);
					final Song s=app.getSongs().get(p);
					
					molde.getSongInfoBySongId(s.getSong_id(),
							new SongMolde.SongInfoCallback() {

								public void onSongInfoLoaded(List<SongUrl> urls,
										SongInfo info) {
									// 判断获取到的数据是否是null
									if (urls == null || info == null) {
										return;
									}
									// 开始准备播放 音乐
									s.setUrls(urls);
									s.setSongInfo(info);
									// 获取当前需要播放的音乐的路径
									SongUrl url = urls.get(0);
									musicpath = url.getShow_link();
									Log.i("musicPath", musicpath);
									try {
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
								}
							});
				}else{
					
					try {
						int p2=random2.nextInt(app.getMusics().size());
						app.setPosition(p2);
						Music music=app.getMusics().get(p2);
						String url=music.getPath();
						player.reset();
						player.setDataSource(url);
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
				}
			}
				
			}
		});
	}

	// 每1秒给Activity发一次广播
	class WorkThread extends Thread {
		@Override
		public void run() {
			while (isLoop) {
				try {
					Thread.sleep(1000);
					// 发送广播
					if (player!=null&&player.isPlaying()) {
						Intent i = new Intent(GlobalConsts.ACTION_UPDATE_PROGRESS);
						i.putExtra("current", player.getCurrentPosition());
						i.putExtra("total", player.getDuration());
						sendBroadcast(i);
					} else {
						Intent i = new Intent(GlobalConsts.ACTION_STOP_PLAY);
						sendBroadcast(i);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 当有客户端绑定该service时 执行 context.bindService()
	 */
	public IBinder onBind(Intent intent) {
		return new SongBinder();
	}

	/**
	 * 返回给客户端的binder对象 声明开放给客户端调用的接口方法
	 */
	public class SongBinder extends Binder {
		public void stop() {
			player.pause();
		}

		/**
		 * 跳转到某个播放位置
		 * 
		 * @param progress
		 */
		public void seekTo(int progress) {
			player.seekTo(progress);
		}

		// 暂停或播放
		public void playOrPause() {
			if (player.isPlaying()) {
				player.pause();
			} else {
				player.start();
			}
		}

		/**
		 * 播放音乐
		 * 
		 * @param url
		 *            音乐的路径
		 */
		public void playMusic(String url) {
			try {
				if(!app.isLocalPlay()){
				player.reset();
				player.setDataSource(url);
				// 异步加载音乐信息
				player.prepareAsync();
				// 在player准备完成后 执行start播放
				// 给player设置监听
				}else{
					player.reset();
					player.setDataSource(url);
					player.prepare();
					player.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public boolean isPlaying() {
			if (player.isPlaying()) {
				return true;
			} else {
				return false;
			}
		}

		public void play() {
			player.start();
		}

		public boolean isPlayed() {
			if (isPlayed) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void onDestroy() {
		if (player.isPlaying()) {
			player.stop();
		}
		player = null;
		super.onDestroy();
	}
}
