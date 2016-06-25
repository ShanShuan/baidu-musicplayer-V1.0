package cn.wangzifeng.musicplayer.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.adapter.SongAdapter.ViewHolder;
import cn.wangzifeng.musicplayer.entity.Song;
import cn.wangzifeng.musicplayer.util.ImageLoader;

public class SearchSongAdapter extends BaseAdapter {
	private Context context;
	private List<Song> songs;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public SearchSongAdapter(Context context, List<Song> songs, ListView listView) {
		this.context = context;
		this.songs = songs;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(context, listView);
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	@Override
	public Song getItem(int position) {
		return songs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_lv_search_song, null);
			holder = new ViewHolder();
			holder.ivSearchPic = (ImageView) convertView.findViewById(R.id.ivSearchPic);
			holder.tvSearchName = (TextView) convertView.findViewById(R.id.tvSearchName);
			holder.tvSearchSinger = (TextView) convertView
					.findViewById(R.id.tvSearchSinger);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		// 给holder中控件赋值
		Song s = getItem(position);
		Log.i("s", s.toString());
		holder.tvSearchName.setText(s.getTitle());
		holder.tvSearchSinger.setText(s.getAuthor());
		// 显示图片
		imageLoader.displayImage(holder.ivSearchPic, s.getPic_small());
		return convertView;
	}

	class ViewHolder {
		ImageView ivSearchPic;
		TextView tvSearchName;
		TextView tvSearchSinger;
	}

	/**
	 * 停止线程
	 */
	public void stopThread() {
		imageLoader.stopThread();
	}
}
