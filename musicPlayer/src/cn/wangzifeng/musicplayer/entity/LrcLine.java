package cn.wangzifeng.musicplayer.entity;

public class LrcLine {
	 private String time;
	 private String content;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LrcLine(String time, String content) {
		super();
		this.time = time;
		this.content = content;
	}
	public LrcLine() {
		super();
	}
	@Override
	public String toString() {
		return "time=" + time + "/n content=" + content ;
	}
	 
}
