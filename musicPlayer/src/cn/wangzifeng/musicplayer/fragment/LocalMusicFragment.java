package cn.wangzifeng.musicplayer.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.adapter.MusicAdapter;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.modler.LoadlLocalMusic;

public class LocalMusicFragment extends Fragment{
	private List<Music> musics;
	private MusicAdapter musicAdapter;
	private ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.local, null);
		lv=(ListView) view.findViewById(R.id.local);
		LoadlLocalMusic loadlLocalMusic=new LoadlLocalMusic();
		musics=loadlLocalMusic.getData();
		musicAdapter = new MusicAdapter(getActivity(), musics);
		lv.setAdapter(musicAdapter);
		setAdapter();
		return view;
		
	}

	private void setAdapter() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
			
		});
		
		
	}
	
}
