package cn.wangzifeng.musicplayer.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.app.PlaySongApplication;
import cn.wangzifeng.musicplayer.entity.GlobalConsts;
import cn.wangzifeng.musicplayer.entity.LrcLine;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.entity.Song;
import cn.wangzifeng.musicplayer.entity.SongInfo;
import cn.wangzifeng.musicplayer.entity.SongUrl;
import cn.wangzifeng.musicplayer.modler.SongMolde;
import cn.wangzifeng.musicplayer.modler.SongMolde.LrcCallBack;
import cn.wangzifeng.musicplayer.modler.SongMolde.SongInfoCallback;
import cn.wangzifeng.musicplayer.service.DownLoadSongServiece;
import cn.wangzifeng.musicplayer.service.PlaySongService;
import cn.wangzifeng.musicplayer.service.PlaySongService.SongBinder;
import cn.wangzifeng.musicplayer.util.BitmapUtils;
import cn.wangzifeng.musicplayer.util.BitmapUtils.BitmapCallback;
import cn.wangzifeng.musicplayer.util.SimpleDateUtils;

public class PlayOneSongActivity extends Activity implements OnClickListener {
	private PlaySongApplication app;
	private SongMolde molde;
	private int position;
	private ImageView ivBackground;
	private ImageView ivSinger;
	private SeekBar sb;
	private TextView tvDruration;
	private TextView tvCurrentTime;
	private TextView tvScl;
	private TextView tvSinger;
	private TextView tvSongname;
	protected SongBinder binder;
	private ServiceConnection conn;
	protected RotateAnimation animation = null;
	private MyUpdateBoradcast receiver;
	private ImageView ivBack;
	private ImageView ivPauseOrPlay;
	private ImageView ivPrevious;
	private ImageView ivNext;
	private boolean ivSingerIsChecked = false;
	private float x;
	private ImageView ivDownload;
	protected List<Song> songs;
	private int loopFlag=1;
	private ImageView ivLoop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_one_song);
		setViews();
		app = (PlaySongApplication) getApplication();
		loopFlag=app.getLoopFlag();
		if(loopFlag==1){
			
		}else if(loopFlag==2){
			ivLoop.setImageResource(R.drawable.oneloop);
		}else if(loopFlag==3){
			ivLoop.setImageResource(R.drawable.random);
		}
		molde = new SongMolde();
		show();
		bindService();
		receiver = new MyUpdateBoradcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_UPDATE_PROGRESS);
		filter.addAction(GlobalConsts.ACTION_STOP_PLAY);
		filter.addAction(GlobalConsts.ACTION_START_PLAY);
		registerReceiver(receiver, filter);
		setListenners();

	}

	private void setListenners() {
		//循环模式的点击事件
		ivLoop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(loopFlag==1){
					
					loopFlag=2;
					Log.i("123", "loopFlag=2");
					app.setLoopFlag(loopFlag);
					ivLoop.setImageResource(R.drawable.oneloop);
				}else if(loopFlag==2){
				
					loopFlag=3;
					Log.i("123", "loopFlag=3");
					app.setLoopFlag(loopFlag);
					ivLoop.setImageResource(R.drawable.random);
				}else if(loopFlag==3){
					loopFlag=1;
					Log.i("123", "loopFlag=1");
					app.setLoopFlag(loopFlag);
					ivLoop.setImageResource(R.drawable.loop);
				}
				
			}
		});
		// 点击下载按钮的监听事件
		ivDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!app.isLocalPlay()) {//不是播放本地音乐
					songs = app.getSongs();
					// position=app.getPosition();
					final Song s = songs.get(position);
					AlertDialog.Builder builder = new Builder(
							PlayOneSongActivity.this);
					builder.setMessage("下载音乐")
							.setTitle("请确认接入wifi")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String fileLink = s.getUrls()
													.get(0).getShow_link();
											Intent intent = new Intent(
													PlayOneSongActivity.this,
													DownLoadSongServiece.class);
											intent.putExtra("fileLink",
													fileLink);
											intent.putExtra("title",
													s.getTitle());
											intent.putExtra("bit", s.getUrls()
													.get(0).getFile_bitrate());
											startService(intent);
											dialog.cancel();
										}
									}).setNegativeButton("取消", null).create()
							.show();
				} else {
					Toast.makeText(PlayOneSongActivity.this, "大哥o.o本地音乐不需要下载", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// seekpar 监听 实现拖拽 跳到播放音乐
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					binder.seekTo(progress);
				}

			}
		});
		ivBack.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		ivPauseOrPlay.setOnClickListener(this);
		ivPrevious.setOnClickListener(this);
		ivSinger.setOnClickListener(this);
		// tvBackgrund.setOnClickListener(this);

	}

	class MyUpdateBoradcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			app = PlaySongApplication.getContext();
			String action = intent.getAction();
			if (GlobalConsts.ACTION_UPDATE_PROGRESS.equals(action)) {
				int current = intent.getIntExtra("current", 0);
				int duration = intent.getIntExtra("total", 0);
				sb.setProgress(current);
				sb.setMax(duration);
				tvCurrentTime.setText(SimpleDateUtils.getTime(current));
				tvDruration.setText(SimpleDateUtils.getTime(duration));
				if (animation == null) {
					animation = new RotateAnimation(0, 359,
							ivSinger.getWidth() / 2, ivSinger.getHeight() / 2);
					animation.setDuration(25000);
					animation.setRepeatCount(-1);
					animation.setInterpolator(new LinearInterpolator());
					ivSinger.startAnimation(animation);
				}
				List<LrcLine> lines = app.getLines();

				if (lines == null) {
					return;
				}
				for (int i = 0; i < lines.size(); i++) {
					LrcLine l = lines.get(i);
					// boolean
					// bl=l.getTime().startsWith(SimpleDateUtils.getTime(current));
					if (l.getTime()
							.startsWith(SimpleDateUtils.getTime(current))) {
						tvScl.setText(l.getContent());
						return;
					}
				}
			} else if (GlobalConsts.ACTION_STOP_PLAY.equals(action)) {
				ivSinger.clearAnimation();
				animation = null;
			} else if (GlobalConsts.ACTION_START_PLAY.equals(action)) {
				if (!app.isLocalPlay()) {// 不是播放本地音乐
					show();
					// 加载歌词
					Song s = app.getSongs().get(app.getPosition());
					String lrc = s.getSongInfo().getLrclink();
					// Log.i("123", "开始加载歌词");
					molde.downLoadLrc(PlayOneSongActivity.this, lrc,
							new LrcCallBack() {

								@Override
								public void onLrcLoaded(List<LrcLine> lrcs) {
									app.setLines(lrcs);
								}
							});
				} else {// 播放本地音乐
					show();
				}
			}

		}

	}

	private void bindService() {
		Intent service = new Intent(this, PlaySongService.class);
		conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				binder = (SongBinder) service;
			}
		};
		bindService(service, conn, Service.BIND_AUTO_CREATE);

	}

	private void show() {
		position = app.getPosition();
		if (!app.isLocalPlay()) {// 不是播放本地音乐
			Song s = app.getSongs().get(position);
			String ss = s.getAuthor() == null ? s.getArtist_name() : s
					.getAuthor();
			tvSinger.setText(ss);
			tvSongname.setText(s.getTitle());
			if (s.getSongInfo() == null) {
				molde.getSongInfoBySongId(s.getSong_id(),
						new SongInfoCallback() {

							@Override
							public void onSongInfoLoaded(List<SongUrl> url,
									SongInfo info) {
								BitmapUtils.loadBitmap(
										PlayOneSongActivity.this,
										info.getArtist_1000_1000(),
										new BitmapCallback() {

											@Override
											public void onBitmapLoaded(
													Bitmap bitmap) {
												ivBackground
														.setImageBitmap(bitmap);
											}
										}, 0, 0);

							}
						});
			} else {
				String pa = s.getSongInfo().getArtist_1000_1000();
				String pa1 = s.getSongInfo().getArtist_480_800();
				String pa2 = s.getSongInfo().getArtist_500_500();

				BitmapUtils.loadBitmap(PlayOneSongActivity.this,
						(pa == null ? pa1 : pa) == null ? pa2
								: (pa == null ? pa1 : pa),
						new BitmapCallback() {

							@Override
							public void onBitmapLoaded(Bitmap bitmap) {
								if (bitmap != null) {
									ivBackground.setImageBitmap(bitmap);
								} else {
									ivBackground
											.setImageResource(R.drawable.movein);
								}
							}
						}, 0, 0);
				String p = s.getSongInfo().getAlbum_500_500();
				String p1 = s.getPic_small();
				String p2 = s.getPic_big();
				BitmapUtils.loadBitmap(this, (p == null ? p1 : p) == null ? p2
						: (p == null ? p1 : p), new BitmapCallback() {

					@Override
					public void onBitmapLoaded(Bitmap bitmap) {
						if (bitmap != null) {
							ivSinger.setImageBitmap(bitmap);
						} else {
							ivSinger.setImageResource(R.drawable.d);
						}

					}
				}, 210, 210);
			}
		} else {// 播放本地音乐
			Music music = app.getMusics().get(app.getPosition());
			tvSinger.setText(music.getArtist());
			tvSongname.setText(music.getTitle());
			ivSinger.setImageBitmap(BitmapFactory.decodeFile(music.getAlbumArt()));
		}

	}

	private void setViews() {
		ivPauseOrPlay = (ImageView) findViewById(R.id.iv_one_song_pause_or_play);
		ivPrevious = (ImageView) findViewById(R.id.iv_one_song_previous);
		ivNext = (ImageView) findViewById(R.id.iv_one_song_next);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvSinger = (TextView) findViewById(R.id.tv_one_song_artistor);
		tvSongname = (TextView) findViewById(R.id.tv_one_song_name);
		tvCurrentTime = (TextView) findViewById(R.id.tv_one_song_current_time);
		tvDruration = (TextView) findViewById(R.id.tv_one_song_duration);
		sb = (SeekBar) findViewById(R.id.seekBar1);
		tvScl = (TextView) findViewById(R.id.tv_one_song_srl);
		ivSinger = (ImageView) findViewById(R.id.iv_one_song_singer);
		ivBackground = (ImageView) findViewById(R.id.iv_one_song_background);
		ivDownload = (ImageView) findViewById(R.id.ivDownload);
		ivLoop=(ImageView) findViewById(R.id.loopmode);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_one_song, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		unbindService(conn);
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			Log.i("123", "x" + x);
			break;
		case MotionEvent.ACTION_UP:
			float y = event.getX();
			Log.i("123", "y" + y);
			if ((y - x) > 80) {
				// 上一首
				app.previous();
				if (!app.isLocalPlay()) {// 不是播放本地音乐
					final Song s1 = app.getSongs().get(app.getPosition());
					molde.getSongInfoBySongId(s1.getSong_id(),
							new SongInfoCallback() {

								@Override
								public void onSongInfoLoaded(List<SongUrl> url,
										SongInfo info) {
									s1.setUrls(url);
									s1.setSongInfo(info);
									String path = s1.getUrls().get(0)
											.getShow_link();
									if ("".equals(path) || path == null) {

										Toast.makeText(
												PlayOneSongActivity.this,
												"上一首 《" + s1.getTitle()
														+ "》需会员才能听！请选择其他歌曲",
												Toast.LENGTH_SHORT).show();
										return;
									}
									binder.playMusic(path);
								}
							});
				} else {
					// 播放本地音乐
					// 上一首
					Music music = app.getMusics().get(app.getPosition());
					binder.playMusic(music.getPath());
					ivSinger.setImageBitmap(BitmapFactory.decodeFile(music.getAlbumArt()));
				}
			} else if ((x - y) > 80) {
				// 下一首
				app.next();
				if (!app.isLocalPlay()) {// 不是播放本地音乐
					final Song s = app.getSongs().get(app.getPosition());
					molde.getSongInfoBySongId(s.getSong_id(),
							new SongInfoCallback() {

								@Override
								public void onSongInfoLoaded(List<SongUrl> url,
										SongInfo info) {
									s.setUrls(url);
									s.setSongInfo(info);
									String path = s.getUrls().get(0)
											.getShow_link();
									if ("".equals(path) || path == null) {

										Toast.makeText(
												PlayOneSongActivity.this,
												"下一首 《" + s.getTitle()
														+ "》需会员才能听！请选择其他歌曲",
												Toast.LENGTH_SHORT).show();
										return;
									}
									binder.playMusic(path);
								}
							});
				} else {
					// 　播放本地音乐
					// 播放下一首
					Music music = app.getMusics().get(app.getPosition());
					binder.playMusic(music.getPath());
					ivSinger.setImageBitmap(BitmapFactory.decodeFile(music.getAlbumArt()));
				}
			}
			break;

		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_one_song_singer:

			ivSinger.clearAnimation();
			ivSinger.setVisibility(View.INVISIBLE);

			break;
		case R.id.iv_back:
			onBackPressed();
			break;
		case R.id.iv_one_song_pause_or_play:
			binder.playOrPause();
			break;
		case R.id.iv_one_song_next:
			app.next();
			if (!app.isLocalPlay()) {// 不是播放本地音乐
				final Song s = app.getSongs().get(app.getPosition());
				molde.getSongInfoBySongId(s.getSong_id(),
						new SongInfoCallback() {

							@Override
							public void onSongInfoLoaded(List<SongUrl> url,
									SongInfo info) {
								s.setUrls(url);
								s.setSongInfo(info);
								String path = s.getUrls().get(0).getShow_link();

								if ("".equals(path) || path == null) {

									Toast.makeText(
											PlayOneSongActivity.this,
											" 下一首  《" + s.getTitle()
													+ "》需会员才能听！请选择其他歌曲",
											Toast.LENGTH_SHORT).show();
									return;
								}
								binder.playMusic(path);
							}
						});
			} else {
				// 播放本地音乐
				Music music = app.getMusics().get(app.getPosition());
				binder.playMusic(music.getPath());
			}
			break;
		case R.id.iv_one_song_previous:
			app.previous();
			if (!app.isLocalPlay()) {// 不是播放本地音乐
				final Song s1 = app.getSongs().get(app.getPosition());
				molde.getSongInfoBySongId(s1.getSong_id(),
						new SongInfoCallback() {

							@Override
							public void onSongInfoLoaded(List<SongUrl> url,
									SongInfo info) {
								s1.setUrls(url);
								s1.setSongInfo(info);
								String path = s1.getUrls().get(0)
										.getShow_link();
								if ("".equals(path) || path == null) {

									Toast.makeText(
											PlayOneSongActivity.this,
											"上一首 《" + s1.getTitle()
													+ "》需会员才能听！请选择其他歌曲",
											Toast.LENGTH_SHORT).show();
									return;
								}
								binder.playMusic(path);
							}
						});
			} else {
				// 播放本地音乐
				Music music = app.getMusics().get(app.getPosition());
				binder.playMusic(music.getPath());
			}
			break;

		}

	}

}
