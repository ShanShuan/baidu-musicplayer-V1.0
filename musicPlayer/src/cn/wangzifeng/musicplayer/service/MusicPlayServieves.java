package cn.wangzifeng.musicplayer.service;

import java.io.IOException;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.util.Itans;


public class MusicPlayServieves extends BaseService implements Itans {
	private List<Music> musics;
	private MediaPlayer player;
	private int currentMusciIndex;
	private int pausePosition;
	private InnerBroadCast recevier;
	private InnerThread thread;
	private boolean isPlayed;
	private boolean isunTRruching;
	private boolean isOnBackgruond;

	
	
	
	
	//service 创建时的时间
	@Override
	public void onCreate() {
//		app = (MusicPlayApplication) getApplication();
//		musics = app.getMusics();
		player = new MediaPlayer();
		Log.v("123", musics.get(currentMusciIndex).getTitle());
		recevier = new InnerBroadCast();
		IntentFilter filter = new IntentFilter();
		isunTRruching=true;
		filter.addAction(ACTION_PALY_OR_PAUSE);
		filter.addAction(ACTION_PREVIOUS);
		filter.addAction(ACTION_NEXT);
		filter.addAction(ACTION_SEEKTO);
		filter.addAction(ACRION_ITEM);
		filter.addAction(ACTION_STOP_THREAD_SEND_PERCENT);
		filter.addAction(ACTION_LET_THREAD_STOPN);
		filter.addAction(ACTION_LET_THREAD_RESTAR);
		registerReceiver(recevier, filter);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
				
			}
		});
		super.onCreate();

	}

	
	
	
	
	//service   onStartCommand的事件
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_NOT_STICKY;
	}
	
	//销毁是的事件
	@Override
	public void onDestroy() {
		unregisterReceiver(recevier);
		player.release();
		player = null;

	}
	
	
	
	
	
	//播放 方法
	private void play() {
		try {
			stopThread();
			player.reset();
			player.setDataSource(musics.get(currentMusciIndex).getPath());
			player.prepare();
			player.seekTo(pausePosition);
			player.start();
			isPlayed=true;
			startThread();
			Intent intent = new Intent();
			intent.setAction(ACTION_SET_PLAYING);
			intent.putExtra(EXTRA_DRURATION, player.getDuration());
			intent.putExtra(EXTRA_INDEX, currentMusciIndex);
			sendBroadcast(intent);	
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

	//播放指定位置的方法
	private void play(int position) {
		currentMusciIndex = position;
		pausePosition = 0;
		play();

	}
	
	//暂停方法
	private void pasuse() {
		player.pause();
		Intent intent = new Intent();
		intent.putExtra("superman", player.getCurrentPosition());
		intent.setAction(ACTION_SET_PAUSEING);
		pausePosition = player.getCurrentPosition();
		sendBroadcast(intent);
		stopThread();
	}
	
	//播放下一首方法
	private void next() {
		stopThread();
		currentMusciIndex++;
		if (currentMusciIndex >= musics.size()) {
			currentMusciIndex = 0;
		}
		pausePosition = 0;
		play();

	}

	//播放上一首方法
	private void previous() {
		stopThread();
		currentMusciIndex--;
		if (currentMusciIndex < 0) {
			currentMusciIndex = musics.size() - 1;
		}
		pausePosition = 0;
		play();

	}

	
	
	//广播类
	class InnerBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_PALY_OR_PAUSE.equals(action)) {
				if (player.isPlaying()) {
					pasuse();
				} else {
					play();
				}
			} else if (ACTION_NEXT.equals(action)) {
				next();

			} else if (ACTION_PREVIOUS.equals(action)) {
				previous();
			}else if(ACTION_SEEKTO.equals(action)){
				int seektoPosition=intent.getIntExtra(EXTRA_SEEKTO_POSITION, 0);
				isunTRruching=true;
				stopThread();
				if(isPlayed){
				pausePosition=seektoPosition*player.getDuration()/100;
				play();
				}
			}else if(ACRION_ITEM.equals(action)){
				play(intent.getIntExtra(EXTRA_ITEM_POSITION, 0));
			}else if(ACTION_STOP_THREAD_SEND_PERCENT.equals(action)){
				int progress=intent.getIntExtra(EXTRA_SEEKBARPROGRESS, 0);
				Log.v("123", progress+"progress");
				Intent intent2=new Intent();
				intent2.setAction(ACTION_SPECIAL_SEEKBAR_PROGRESS);
				intent2.putExtra(EXTRA_SPECAIL_SEEKPAR_PROGRESS, progress);
				intent2.putExtra(EXTRA_SPECAIL_DURATION, player.getDuration());
				sendBroadcast(intent2);
			}else if(ACTION_LET_THREAD_STOPN.equals(action)){
				stopThread();
			}else if(ACTION_LET_THREAD_RESTAR.equals(action)){
				startThread();
		}

	}

}
	
	
	//发送intent 线程
	class InnerThread extends Thread {
		private boolean isRunning;

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		@Override
		public void run() {
			Intent intent = new Intent();
			if(!isOnBackgruond){
			while (isRunning) {
				try {
					
					
						intent.setAction(ACTION_UPDATE);
					intent.putExtra(EXTRA_CURRENTPOSITION,
							player.getCurrentPosition());
					
					intent.putExtra(EXTRA_PERCENT, player.getCurrentPosition()
							* 100 / player.getDuration());
					sendBroadcast(intent);
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}

		}
	}

	//开始线程方法
	private void startThread() {
		if (thread == null) {
			thread = new InnerThread();
			thread.setRunning(true);
			thread.start();
		}
	}
	//停止线程方法
	private void stopThread() {
		if (thread != null) {
			thread.setRunning(false);
			thread = null;
		}
	}
	
	
	
}
