package cn.wangzifeng.musicplayer.modler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.wangzifeng.musicplayer.entity.Music;

import android.os.Environment;

public class LoadlLocalMusic implements IDao {
	List<Music> musics;
	public List<Music> getData(){
		musics=new ArrayList<Music>();
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File fileDri=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			if(fileDri.exists()){
				File[]files=fileDri.listFiles();
				if(files!=null&&files.length>0){
					for (int i = 0; i < files.length; i++) {
						if(files[i].isFile()){
							String title=files[i].getName();
							if(title.toLowerCase().endsWith(".mp3")){
								Music music=new Music();
								music.setTitle(title.substring(0, title.length()-3));
								music.setPath(files[i].getAbsolutePath());
								musics.add(music);
							}
						}
					}
				}
			}
		}
		return musics;
	}
	
}
