package cn.wangzifeng.musicplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.wangzifeng.musicplayer.R;
import cn.wangzifeng.musicplayer.activity.SecondMainActivity;

public class MainActivity extends Activity {
	private TextView tvTime;
	private int i;
	private Button btnSlip;
	protected boolean noSlip=true;
	private Thread thread;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setViews();
		thread=new Thread() {
			public void run() {
				for (i = 5; i > 0; i--) {
					try {
						
						Thread.sleep(1000);
						runOnUiThread(new Runnable() {
							public void run() {
								tvTime.setText(i + "√Î");
							}
						});
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(noSlip){
				Intent ii = new Intent(MainActivity.this,
						SecondMainActivity.class);
				startActivity(ii);
				
				}
			};
		};
		thread.start();
		setListenners();
	}

	private void setListenners() {
		btnSlip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent ii = new Intent(MainActivity.this,
						SecondMainActivity.class);
				startActivity(ii);
				noSlip=false;
			}
		});
	}

	@Override
	protected void onPause() {
		finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		thread=null;
		super.onDestroy();
	}

	private void setViews() {
		btnSlip = (Button) findViewById(R.id.btn_slip);
		tvTime = (TextView) findViewById(R.id.tv_move_in_time);
	}
	
}
