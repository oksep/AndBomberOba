package com.bomb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
/**
 * 选项
 * @author dhee
 */
public class Options extends Activity {

	private String[] score;
	Result result;

	protected void onCreate(Bundle savedInstanceState) {
		// 设置风格
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.options);
		Button soundBtn=(Button) findViewById(R.id.soundBtn);
		soundBtn.setText(Music.music_flag ? "音效      (开)" : "音效      (关)");
	}
	/**
	 * 音效控制
	 * @param v
	 */
	public void soundClick(View v) {
		Music.music_flag = !Music.music_flag;
		String s = Music.music_flag ? "音效      (开)" : "音效      (关)";
		Button btn = (Button) v;
		btn.setText(s);
	}
	/**
	 * 排行榜
	 * @param v
	 */
	public void scoreClick(View v) {
		result = new Result(this);
		score = result.readData();
		score[0] = "第一名: " + score[0];
		score[1] = "第二名: " + score[1];
		score[2] = "第三名: " + score[2];
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("英雄榜");
		dialog.setIcon(R.drawable.bomb_button);
		dialog.setItems(score, new DialogInterface.OnClickListener() {		
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), score[which] + "分", 0).show();
			}
		});
		dialog.show();
	}
	/**
	 * 关于
	 * @param v
	 */
	public void aboutClick(View v) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("炸弹人1.0");
		dialog.setIcon(R.drawable.bomb_button);
		dialog.setMessage("\n第一次写游戏,有很多纰漏,请多多指教\n感谢大家的支持");
		dialog.setNegativeButton("OK", null);
		dialog.show();
	}
}
