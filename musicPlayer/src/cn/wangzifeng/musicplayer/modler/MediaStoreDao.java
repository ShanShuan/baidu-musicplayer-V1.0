package cn.wangzifeng.musicplayer.modler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import cn.wangzifeng.musicplayer.entity.Music;

public class MediaStoreDao implements IDao<Music>{

	private Context context;
	
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public MediaStoreDao(Context context) {
		super();
		this.context = context;
	}

	@Override
	public List<Music> getData() {
		List<Music> musics=new ArrayList<Music>();
		//准备ContentResolver
		ContentResolver cr=context.getContentResolver();
		//准备Uri
		Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection={"_id"//0
							,"_data"//1
							,"_size"//2
							,"title"//3
							,"duration"//4
							,"album_artist"//5
							,"album"//6
							,"artist"//7
							,"album_key"//8
				
		};
		String selection=null;
		String[] selectionArgs=null;
		String sortOrder=null;
		Cursor c=cr.query(uri, projection, selection, selectionArgs, sortOrder);
		for (c.moveToFirst();! c.isAfterLast(); c.moveToNext()) {
			Music music=new Music();
			music.setId(c.getLong(0));
			music.setPath(c.getString(1));
			music.setSize(c.getInt(2));
			music.setTitle(c.getString(3));
			music.setDruration(c.getInt(4));
			music.setAlbumArtist(c.getString(5));
			music.setAlbum(c.getString(6));
			music.setArtist(c.getString(7));
			music.setAlbumKey(c.getString(8));
			music.setAlbumArt(getAlbumArtByKey(music.getAlbumKey()));
			musics.add(music);
		}
		Log.i("musics", musics.toString());
		return musics;
	}
	public String getAlbumArtByKey(String albumKey){
		if(albumKey==null){
			return null;
		}
		String albumArt=null;
		ContentResolver cr=context.getContentResolver();
		Uri uri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		String[] projection={"album_art"};
		String selection="album_key=?";
		String[] selectionArgs={albumKey};
		String sortOrder=null;
		Cursor c=cr.query(uri, projection, selection, selectionArgs, sortOrder);
		if(c.moveToFirst()){
			albumArt=c.getString(c.getColumnIndex("album_art"));
		}
		c.close();
		return albumArt;
	}

}
