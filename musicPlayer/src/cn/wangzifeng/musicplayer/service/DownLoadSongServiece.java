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

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

public class DownLoadSongServiece extends IntentService{

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
		}else{
			
				FileOutputStream fos=new FileOutputStream(target);
				sendNofication();
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
					sendNofication();
				}
				fos.close();
				
			
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendNofication() {
		// TODO Auto-generated method stub
		
	}

}
