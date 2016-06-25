package cn.wangzifeng.musicplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseAdapter<T> extends android.widget.BaseAdapter{
	private Context context;
	private List<T> data;
	private LayoutInflater inflater;
	
	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public List<T> getData() {
		return data;
	}


	public void setData(List<T> data) {
		this.data = data;
	}


	public LayoutInflater getInflater() {
		return inflater;
	}


	public void setInflater() {
		inflater=LayoutInflater.from(context);
	}


	public BaseAdapter(Context context, List<T> data) {
		super();
		this.context = context;
		this.data = data;
		setInflater();
	}
	
	
	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	

	

}
