package cn.wangzifeng.musicplayer.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.adapter.MusicAdapter;
import cn.wangzifeng.musicplayer.app.PlaySongApplication;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.modler.LoadlLocalMusic;
import cn.wangzifeng.musicplayer.modler.MediaStoreDao;
import cn.wangzifeng.musicplayer.service.PlaySongService.SongBinder;

@SuppressLint("CommitPrefEdits")
public class LocalMusicFragment extends Fragment{
	private List<Music> musics;
	private MusicAdapter musicAdapter;
	private ListView lv;
	private PlaySongApplication app;
	private SongBinder songBinder;
//	private boolean isloaded=false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.local, null);
		lv=(ListView) view.findViewById(R.id.local);
		app=PlaySongApplication.getContext();
//		SharedPreferences sp=getActivity().getSharedPreferences("musics", 0);
//		Editor edit = sp.edit();
	
//		isloaded=sp.getBoolean("isLoaded", false);
//		if(!isloaded){
//		edit.putBoolean("isLoaded", true);	
		musics=app.getMusics();
		
//		Log.i("12345", musics.toString());
		//TODO 本地存musics的集合
//		}else{
		//TODO 从本地去musics的集合
//		}
		musicAdapter = new MusicAdapter(getActivity(), musics);
		app.setMusics(musics);
		lv.setAdapter(musicAdapter);
		setListener();
		return view;
		
	}

	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Music music=musics.get(position);
				String url=music.getPath();
				songBinder.playMusic(url);
				app.motifyIsLocalPlay(true);
				app.setPosition(position);
				
			}
			
		});
		
		
	}

	public void setSongBinder(SongBinder songBinder) {
		this.songBinder=songBinder;
	}
	
}
