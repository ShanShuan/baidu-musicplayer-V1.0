package cn.wangzifeng.musicplayer.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import cn.wangzifeng.musicplayer.entity.GlobalConsts;
public class PlaySongService extends Service{

	private MediaPlayer player;
	private boolean isLoop=true;
	private boolean isPlayed=false;
	

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
				//TODO
				
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
