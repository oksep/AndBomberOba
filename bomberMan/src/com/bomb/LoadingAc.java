package com.bomb;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * 菜单类
 * @author ryf
 */
public class LoadingAc extends Activity {

	public LinearLayout menu;
	private Button enter;
	private Button options;
	private Button exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//设置风格
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.load);
		menu = (LinearLayout) findViewById(R.id.menu);
		doAnimation();
		enter = (Button) findViewById(R.id.enter);
		enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoadingAc.this, MainActivity.class);
				startActivity(intent);
			}
		});
		options=(Button) findViewById(R.id.option);
		options.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoadingAc.this, Options.class);
				startActivity(intent);
			}
		});
		exit=(Button) findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LoadingAc.this.finish();
			}
		});
		
		
	}
	/** 菜单动画*/
	public void doAnimation() {
		int animIds[] = { R.anim.menu_anim_translate, R.anim.menu_anim_rotate,
				R.anim.menu_anim_scale, R.anim.mmenu_anim_alpha };
		android.view.animation.Animation myAnimation_Translate = AnimationUtils
				.loadAnimation(this, animIds[(int) ((Math.random() * 10) % 4)]);
		menu.startAnimation(myAnimation_Translate);
	}

	@Override
	protected void onResume() {
		super.onResume();
		doAnimation();
		Music.play(this, R.raw.bg1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

}
