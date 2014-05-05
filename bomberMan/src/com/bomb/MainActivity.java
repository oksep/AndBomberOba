package com.bomb;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 主界面类
 * 
 * @author ryf
 * 
 */
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 去标题 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** 设置全屏 */
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/** 载入 */
		setContentView(R.layout.game);

		Button btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.bg3);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

	@Override
	protected void onDestroy() {
		Result re = new Result(MainActivity.this);
		re.saveData();
		super.onDestroy();
	}

}