package cn.wangzifeng.musicplayer.entity;

public class Music {
	private String title;
	private String path;
	private long id;
	private int size;
	private int druration;
	private String albumArtist;
	private String album;
	private String artist;
	private String albumKey;
	private String albumArt;
	
	public Music(String title, String path, long id, int size, int druration,
			String albumArtist, String album, String artist, String albumKey,
			String albumArt) {
		super();
		this.title = title;
		this.path = path;
		this.id = id;
		this.size = size;
		this.druration = druration;
		this.albumArtist = albumArtist;
		this.album = album;
		this.artist = artist;
		this.albumKey = albumKey;
		this.albumArt = albumArt;
	}
	public String getAlbumKey() {
		return albumKey;
	}
	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}
	public String getAlbumArt() {
		return albumArt;
	}
	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}
	public Music() {
		super();
	}
	public Music(String title, String path, long id, int size, int druration,
			String albumArtist, String album, String artist) {
		super();
		this.title = title;
		this.path = path;
		this.id = id;
		this.size = size;
		this.druration = druration;
		this.albumArtist = albumArtist;
		this.album = album;
		this.artist = artist;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDruration() {
		return druration;
	}
	public void setDruration(int druration) {
		this.druration = druration;
	}
	public String getAlbumArtist() {
		return albumArtist;
	}
	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
