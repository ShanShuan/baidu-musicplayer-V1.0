package cn.wangzifeng.musicplayer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cn.wangzifeng.musicplayer.entity.Song;

import android.util.Xml;

/**
 * 解析xml的工具类
 */
public class XmlParser {
	/**
	 * 解析输入流  获取List<Music>
	 * @param is
	 * @return
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	public static List<Song> parseSongList(InputStream is) throws IOException, XmlPullParserException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int eventType=parser.getEventType();
		List<Song> songs = new ArrayList<Song>();
		Song song = null;
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String name=parser.getName();
				if("song".equals(name)){
					song = new Song();
					songs.add(song);
				}else if("artist_id".equals(name)){
					song.setArtist_id(parser.nextText());
				}else if("language".equals(name)){
					song.setLanguage(parser.nextText());
				}else if("pic_big".equals(name)){
					song.setPic_big(parser.nextText());
				}else if("pic_small".equals(name)){
					song.setPic_small(parser.nextText());
				}else if("publishtime".equals(name)){
					song.setPublishtime(parser.nextText());
				}else if("lrclink".equals(name)){
					song.setLrclink(parser.nextText());
				}else if("all_artist_ting_uid".equals(name)){
					song.setAll_artist_ting_uid(parser.nextText());
				}else if("all_artist_id".equals(name)){
					song.setAll_artist_id(parser.nextText());
				}else if("style".equals(name)){
					song.setStyle(parser.nextText());
				}else if("song_id".equals(name)){
					song.setSong_id(parser.nextText());
				}else if("title".equals(name)){
					song.setTitle(parser.nextText());
				}else if("author".equals(name)){
					song.setAuthor(parser.nextText());
				}else if("album_id".equals(name)){
					song.setAlbum_id(parser.nextText());
				}else if("album_title".equals(name)){
					song.setAlbum_title(parser.nextText());
				}else if("artist_name".equals(name)){
					song.setArtist_name(parser.nextText());
				}
				break;
			}
			//向后驱动事件
			eventType=parser.next();
		}
		return songs;
	}
	
}


