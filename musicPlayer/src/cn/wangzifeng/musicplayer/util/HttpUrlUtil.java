package cn.wangzifeng.musicplayer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrlUtil {
	public static InputStream getInputStream(String path) throws IOException{
		URL url=new URL(path);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		return conn.getInputStream();
	}
	public static String toString(InputStream is) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		StringBuilder sb=new StringBuilder();
		String line=null;
		while((line=reader.readLine())!=null){
			sb.append(line);
		}
		return sb.toString();	
		
	}
	/**
	 * 发送get请求 获取服务端返回的输入流
	 * @param url
	 * @return
	 * @throws IOExceptionException 
	 */
	public static InputStream get(String path) throws IOException{
		URL url = new URL(path);
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return is;
	}
	/**
	 * 把输入流 按照utf-8编码解析为字符串
	 * @param is
	 * @return 解析成功的字符串
	 */
	public static String isToString(InputStream is)throws IOException{
		StringBuilder sb = new StringBuilder();
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while((line = reader.readLine()) != null){
			sb.append(line);
		}
		return sb.toString();
	}
}
