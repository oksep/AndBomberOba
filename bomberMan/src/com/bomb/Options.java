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
 * ѡ��
 * @author dhee
 */
public class Options extends Activity {

	private String[] score;
	Result result;

	protected void onCreate(Bundle savedInstanceState) {
		// ���÷��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.options);
		Button soundBtn=(Button) findViewById(R.id.soundBtn);
		soundBtn.setText(Music.music_flag ? "��Ч      (��)" : "��Ч      (��)");
	}
	/**
	 * ��Ч����
	 * @param v
	 */
	public void soundClick(View v) {
		Music.music_flag = !Music.music_flag;
		String s = Music.music_flag ? "��Ч      (��)" : "��Ч      (��)";
		Button btn = (Button) v;
		btn.setText(s);
	}
	/**
	 * ���а�
	 * @param v
	 */
	public void scoreClick(View v) {
		result = new Result(this);
		score = result.readData();
		score[0] = "��һ��: " + score[0];
		score[1] = "�ڶ���: " + score[1];
		score[2] = "������: " + score[2];
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Ӣ�۰�");
		dialog.setIcon(R.drawable.bomb_button);
		dialog.setItems(score, new DialogInterface.OnClickListener() {		
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), score[which] + "��", 0).show();
			}
		});
		dialog.show();
	}
	/**
	 * ����
	 * @param v
	 */
	public void aboutClick(View v) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("ը����1.0");
		dialog.setIcon(R.drawable.bomb_button);
		dialog.setMessage("\n��һ��д��Ϸ,�кܶ��©,����ָ��\n��л��ҵ�֧��");
		dialog.setNegativeButton("OK", null);
		dialog.show();
	}
}
