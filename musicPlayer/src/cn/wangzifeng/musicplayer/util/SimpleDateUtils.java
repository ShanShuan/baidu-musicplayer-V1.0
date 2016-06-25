package cn.wangzifeng.musicplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateUtils {
	public static String getTime(long date){
		SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
		return sdf.format(new Date(date));
	}
}
