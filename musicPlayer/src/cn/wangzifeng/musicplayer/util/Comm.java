package cn.wangzifeng.musicplayer.util;


import java.text.SimpleDateFormat;
import java.util.Date;



public class Comm {
	public static String formatData(int time) {
		SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
		Date data=new Date(time); 
		return sdf.format(data);

	}
}
