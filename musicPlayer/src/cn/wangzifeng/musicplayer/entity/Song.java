package cn.wangzifeng.musicplayer.entity;

import java.util.List;

public class Song {
	private String artist_id;
	private String language;
	private String pic_big;
	private String pic_small;
	private String publishtime;
	private String lrclink;
	private String all_artist_ting_uid;
	private String all_artist_id;
	private String style;
	private String song_id;
	private String title;
	private String author;
	private String album_id;
	private String album_title;
	private String artist_name;
	private List<SongUrl> urls;
	private SongInfo songInfo;

	public Song() {
		// TODO Auto-generated constructor stub
	}

	

	public Song(String artist_id, String language, String pic_big,
			String pic_small, String publishtime, String lrclink,
			String all_artist_ting_uid, String all_artist_id, String style,
			String song_id, String title, String author, String album_id,
			String album_title, String artist_name, List<SongUrl> urls,
			SongInfo songInfo) {
		super();
		this.artist_id = artist_id;
		this.language = language;
		this.pic_big = pic_big;
		this.pic_small = pic_small;
		this.publishtime = publishtime;
		this.lrclink = lrclink;
		this.all_artist_ting_uid = all_artist_ting_uid;
		this.all_artist_id = all_artist_id;
		this.style = style;
		this.song_id = song_id;
		this.title = title;
		this.author = author;
		this.album_id = album_id;
		this.album_title = album_title;
		this.artist_name = artist_name;
		this.urls = urls;
		this.songInfo = songInfo;
	}



	public List<SongUrl> getUrls() {
		return urls;
	}

	public void setUrls(List<SongUrl> urls) {
		this.urls = urls;
	}

	public SongInfo getSongInfo() {
		return songInfo;
	}

	public void setSongInfo(SongInfo songInfo) {
		this.songInfo = songInfo;
	}

	public String getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPic_big() {
		return pic_big;
	}

	public void setPic_big(String pic_big) {
		this.pic_big = pic_big;
	}

	public String getPic_small() {
		return pic_small;
	}

	public void setPic_small(String pic_small) {
		this.pic_small = pic_small;
	}

	public String getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}

	public String getLrclink() {
		return lrclink;
	}

	public void setLrclink(String lrclink) {
		this.lrclink = lrclink;
	}

	public String getAll_artist_ting_uid() {
		return all_artist_ting_uid;
	}

	public void setAll_artist_ting_uid(String all_artist_ting_uid) {
		this.all_artist_ting_uid = all_artist_ting_uid;
	}

	public String getAll_artist_id() {
		return all_artist_id;
	}

	public void setAll_artist_id(String all_artist_id) {
		this.all_artist_id = all_artist_id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSong_id() {
		return song_id;
	}

	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public String getAlbum_title() {
		return album_title;
	}

	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}



	@Override
	public String toString() {
		return "Song [artist_id=" + artist_id + ", language=" + language
				+ ", pic_big=" + pic_big + ", pic_small=" + pic_small
				+ ", publishtime=" + publishtime + ", lrclink=" + lrclink
				+ ", all_artist_ting_uid=" + all_artist_ting_uid
				+ ", all_artist_id=" + all_artist_id + ", style=" + style
				+ ", song_id=" + song_id + ", title=" + title + ", author="
				+ author + ", album_id=" + album_id + ", album_title="
				+ album_title + ", artist_name=" + artist_name + ", urls="
				+ urls + ", songInfo=" + songInfo + "]";
	}

	

}
