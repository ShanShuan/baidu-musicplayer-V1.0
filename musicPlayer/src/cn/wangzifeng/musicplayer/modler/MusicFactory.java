package cn.wangzifeng.musicplayer.modler;

import android.content.Context;


import cn.wangzifeng.musicplayer.entity.Music;



public class MusicFactory  {
	private MusicFactory(){
		
	}
public static IDao<Music> getInstance(Context context){
	return new MediaStoreDao(context);
}

}
