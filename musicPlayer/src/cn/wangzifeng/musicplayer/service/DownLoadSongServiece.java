package cn.wangzifeng.musicplayer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.wangzifeng.musicplayer.R;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

@SuppressLint("NewApi")
public class DownLoadSongServiece extends IntentService{

	private static final int id=1886714;
	public DownLoadSongServiece(){
		super("download");
	}

	public DownLoadSongServiece(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
		String fileLink=intent.getStringExtra("fileLink");
		String title=intent.getStringExtra("title");
		String bit=intent.getStringExtra("bit");
		String fileName="_"+bit+"/"+title+".mp3";
		File target=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),fileName);
		if(target.exists()){
			return;
		}
			
		if(!target.getParentFile().exists()){
			target.getParentFile().mkdirs();
		}
		sendNofication("音乐开始下载",0,0,true);
				FileOutputStream fos=new FileOutputStream(target);
			
				URL url=new URL(fileLink);
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				InputStream is = conn.getInputStream();
				byte[] buffer=new byte[1024];
				int length=0;
				int current=0;
				int total=Integer.parseInt(conn.getHeaderField("Content-Length"));
				while((length=(is.read(buffer)))!=-1){
					fos.write(buffer, 0, length);
					fos.flush();
					current+=length;
					sendNofication("音乐下载中",total,current,false);
				}
				fos.close();
				removeNotifycation("音乐下载完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void removeNotifycation(String text) {
		NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Builder builder=new Builder(this);
		manager.cancel(id);
		builder.setTicker(text).setContentInfo(text).setSmallIcon(R.drawable.download1)
		.setProgress(0, 0, false);
		manager.notify(id, builder.build());
	}

	private void sendNofication(String info,int max,int progress,boolean isUnsure) {
		NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Builder builder=new Builder(this);
		builder.setTicker(info).setContentInfo(info).setSmallIcon(R.drawable.download1)
		.setProgress(max, progress, isUnsure);
		manager.notify(id, builder.build());
	}

}
