package cn.wangzifeng.musicplayer.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;

public class BitmapUtils {

	/**
	 * @param is  数据源
	 * @param width  图片的目标宽度 
	 * @param height  图片的目标高度
	 * @return 压缩过后的图片
	 */
	public static Bitmap  loadBitmap(InputStream is, int width, int height)throws IOException{
		//通过is 读取 到一个 byte[]
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int length=0;
		while( (length=is.read(buffer)) != -1){
			bos.write(buffer, 0, length);
			bos.flush();
		}
		byte[] bytes=bos.toByteArray();
		if(width==0&&height==0){
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		//使用BitmapFactory获取图片的原始宽和高
		Options opts = new Options();
		//仅仅加载图片的边界属性 
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		//通过目标宽和高计算图片的压缩比例
		int w = opts.outWidth / width;
		int h = opts.outHeight / height;
		int scale = w > h ? h : w;
		//给Options属性设置压缩比例
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		//重新解析byte[] 获取Bitmap
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
	}
	
	public interface BitmapCallback{
		void onBitmapLoaded(Bitmap bitmap);
	}


	/**
	 * 保存图片
	 * @param bitmap
	 * @param path 图片的目标路径
	 */
	public static void save(Bitmap bitmap, String path)throws IOException{
		File file = new File(path);
		if(!file.getParentFile().exists()){ //父目录不存在
			file.getParentFile().mkdirs(); //创建父目录
		}
		FileOutputStream os = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, os);
	}
	/**
	 * 通过一个网络的路径加载一张图片
	 * @param path
	 */
	public static void loadBitmap(Context context, final String path, final BitmapCallback callback,final int width,final int height){
		//先去文件中找找  看看有没有下载过
		String filename=path.substring(path.lastIndexOf("/")+1);
		File file = new File(context.getCacheDir() , filename);
		Bitmap bitmap = loadBitmap(file.getAbsolutePath());
		if(bitmap!=null){
			callback.onBitmapLoaded(bitmap);
			return;
		}
		//文件中没有图片   则去下载
		new AsyncTask<String, String, Bitmap>() {
			protected Bitmap doInBackground(String... params) {
				try {
					InputStream is = HttpUrlUtil.getInputStream(path);
					Bitmap b = loadBitmap(is, width, height);
					return b;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(Bitmap bitmap) {
				callback.onBitmapLoaded(bitmap);
			}
		}.execute();
	}
	
	/**
	 * 从某个路径下读取一个bitmap
	 * @param path
	 * @return
	 */
	public static Bitmap loadBitmap(String path){
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		return BitmapFactory.decodeFile(path);
	}

	
}
