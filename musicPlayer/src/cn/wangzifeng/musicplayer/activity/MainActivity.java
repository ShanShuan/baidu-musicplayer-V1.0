package cn.wangzifeng.musicplayer.activity;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.app.PlaySongApplication;
import cn.wangzifeng.musicplayer.entity.Music;
import cn.wangzifeng.musicplayer.modler.MediaStoreDao;

public class MainActivity extends Activity {
	private Thread thread;
	private PlaySongApplication app;
	private List<Music> musics;
	private ImageView ivMoving;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		app = PlaySongApplication.getContext();
		try {
			app.playSound();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivMoving=(ImageView) findViewById(R.id.moving);
//		Animation animation = AnimationUtils.loadAnimation(this, R.anim.bycicle_moving);
	Display display=this.getWindowManager().getDefaultDisplay();
		int x=display.getWidth();
		int y=display.getHeight();
		ivMoving.measure(0, 0);
		
		TranslateAnimation animation=new TranslateAnimation(0, x, 0, 0);
		animation.setDuration(20000);
		
		ivMoving.startAnimation(animation);
		thread = new Thread() {
			public void run() {
				
					
					MediaStoreDao dao = new MediaStoreDao(MainActivity.this);
					musics = dao.getData();
					app.setMusics(musics);
					Intent ii = new Intent(MainActivity.this,
							SecondMainActivity.class);
					startActivity(ii);

			};
		};
		thread.start();
	}

	@Override
	protected void onPause() {
		finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		thread = null;
		super.onDestroy();
	}


}
