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
		//��ʼ��������
		player = new MediaPlayer();
		player.setOnPreparedListener(new OnPreparedListener() {
			//prepare��ɺ�  ִ�и÷���
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				isPlayed=true;
				//�����Զ���㲥  ����Activity �����Ѿ���ʼ����
				Intent i = new Intent(GlobalConsts.ACTION_START_PLAY);
				sendBroadcast(i);
			}
		});
		//���������߳�  ÿ1���Activity��һ�ι㲥
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
							//�жϻ�ȡ���������Ƿ���null 
							if(urls == null || info==null){
								return;
							}
							//��ʼ׼������                                    ����
							s.setUrls(urls);
							s.setSongInfo(info);
							//��ȡ��ǰ��Ҫ���ŵ����ֵ�·��
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
				//�첽����������Ϣ
			
			}
		});
	}
	
	

	//ÿ1���Activity��һ�ι㲥
	class WorkThread extends Thread{
		@Override
		public void run() {
			while(isLoop){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//���͹㲥
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
	 * ���пͻ��˰󶨸�serviceʱ  ִ�� 
	 * context.bindService()
	 */
	public IBinder onBind(Intent intent) {
		return new SongBinder();
	}

	/**
	 * ���ظ��ͻ��˵�binder����
	 * �������Ÿ��ͻ��˵��õĽӿڷ���
	 */
	public class SongBinder extends Binder{
		public void stop(){
			player.pause();
		}
		
		/**
		 * ��ת��ĳ������λ��
		 * @param progress
		 */
		public void seekTo(int progress){
			player.seekTo(progress);
		}
		
		//��ͣ�򲥷�
		public void playOrPause(){
			if(player.isPlaying()){
				player.pause();
			}else{
				player.start();
			}
		}
		
		/**
		 * ��������
		 * @param url  ���ֵ�·��
		 */
		public void playMusic(String url){
			try {
				player.reset();
				player.setDataSource(url);
				//�첽����������Ϣ
				player.prepareAsync();
				//��player׼����ɺ�  ִ��start����
				//��player���ü���
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
