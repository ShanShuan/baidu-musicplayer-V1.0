package cn.wangzifeng.musicplayer.adapter;

import java.util.List;

import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.util.Comm;






import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter<Music>{


	public MusicAdapter(Context context, List<Music> data) {
		super(context, data);
		               
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView=getInflater().inflate(R.layout.item_music, null);
			holder=new ViewHolder();
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
			holder.tvDuration=(TextView) convertView.findViewById(R.id.tv_duration);
			holder.tvAlbum=(TextView) convertView.findViewById(R.id.tv_album);
			holder.tvArtist=(TextView) convertView.findViewById(R.id.tv_artist);
			holder.imageView=(ImageView) convertView.findViewById(R.id.iv_title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Music music=getData().get(position);
		holder.tvTitle.setText(music.getTitle());
		holder.tvDuration.setText(Comm.formatData(music.getDruration()));
		holder.tvAlbum.setText("专辑："+music.getAlbum());
		holder.tvArtist.setText("艺术家："+music.getArtist());
//		if(music.getAlbumArt()==null){
//			
//		}else{
//			Bitmap bm=BitmapFactory.decodeFile(music.getAlbumArt());
//			holder.imageView.setImageBitmap(bm);
//		}
		return convertView;
	}
	class ViewHolder{
		ImageView imageView;
		TextView tvTitle;
		TextView tvDuration;
		TextView tvAlbum;
		TextView tvArtist;
	}
}
