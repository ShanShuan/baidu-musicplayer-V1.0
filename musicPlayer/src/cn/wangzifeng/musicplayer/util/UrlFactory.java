package cn.wangzifeng.musicplayer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlFactory {
	public static String getNewMusicUrl(int offsize,int size){
		return "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0" +
				"&method=baidu.ting.billboard.billList&format=xml&type=1&offset="+offsize+"&size="+size; 
	}
	/**
	 * 查询歌曲详细信息的url地址
	 * @param songId
	 * @return
	 */
	public static String getSongInfoUrl(String songId) {
		String path = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid="+songId+"&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
		return path;
	}
	/**
	 * 根据关键字查询音乐列表的接口地址
	 * @param key
	 * @param pageno  页码
	 * @param pagesize    每页多少条
	 * @return
	 */
	public static String getSearchSongUrl(String key, int pageno, int pagesize){
		try {
			key = URLEncoder.encode(key, "utf-8");
			String url ="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query="+key+"&page_no="+pageno+"&page_size="+pagesize;
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getHotMusicUrl(int offset, int size) {
		return "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=xml&type=2&offset="+offset+"&size="+size; 
	}
}
