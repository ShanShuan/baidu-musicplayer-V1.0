package cn.wangzifeng.musicplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import cn.wangzifeng.musicplayer.entity.LrcLine;

public class LrcUtils {
	public static List<LrcLine> parseLrc(InputStream is,File targeFile) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String line=null;
		PrintWriter pw=new PrintWriter(targeFile);
		List<LrcLine> lrcs=new ArrayList<LrcLine>();
		while((line=br.readLine())!=null){
			if("".equals(line)){
				continue;
			}if(line.indexOf("[")<0){
				continue;
			}
			pw.println(line);
			pw.flush();
			Log.i("123", "¸è´ÊÄÚÈÝ£º"+line);
			String time=line.substring(1, line.indexOf("]"));
			String content=line.substring(line.indexOf("]")+1);
			LrcLine l=new LrcLine(time, content);
			lrcs.add(l);
		}
		pw.close();
		return lrcs;
		
	}

	public static List<LrcLine> parseLrc(File file) throws IOException {
		if(!file.exists()){
			return null;
		}
		FileInputStream is=new FileInputStream(file);
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		String line="";
		List<LrcLine> lrcs=new ArrayList<LrcLine>();
		while((line=reader.readLine())!=null){
			if("".equals(line)){
				continue;
			}if(line.indexOf("[")<0){
				continue;
			}
			String time=line.substring(1, line.lastIndexOf("]"));
			String content=line.substring(line.lastIndexOf("]")+1);
			LrcLine l=new LrcLine(time, content);
			lrcs.add(l);
		}
		return lrcs;
	}
}
