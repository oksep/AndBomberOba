package com.bomb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 战绩
 * 
 * @author ryf
 */
public class Result {
	private Context context;
	private final String fileName = "score.txt";

	public Result(Context context) {
		this.context = context;
	}

	public void saveData() {
		doWrite(fileName);
	}

	public String[] readData() {
		String[] s = null;
		// try {
		s = doRead(fileName).split(";");
		// } catch (Exception e) {
		// s[0] = "0";
		// s[1] = "0";
		// s[2] = "0";
		// return s;
		// }
		return s;

	}

	/**
	 * 写入战绩
	 * 
	 * @param fileNme
	 */
	private void doWrite(String fileNme) {
		// 取得前三名
		ArrayList<Integer> score_list = new ArrayList<Integer>();
		String[] s1 = readData();

		for (String s2 : s1) {
			int num = 0;
			try {
				num = Integer.parseInt(s2);
			} catch (Exception e) {
				Log.i("paseInt:", "failure");
				num=0;
			} finally {
				score_list.add(num);
			}
		}

		for (int i = 0; i < 3; i++) {
			if (GameView.score >= score_list.get(i)) {
				score_list.add(i, GameView.score);
				break;
			}
		}
		String msg = score_list.get(0) + ";" + score_list.get(1) + ";"
				+ score_list.get(2);
		// 写数据
		try {
			FileOutputStream out = context.openFileOutput(fileNme,
					Activity.MODE_PRIVATE);
			byte[] bytes = msg.getBytes();
			out.write(bytes);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取战绩
	 * 
	 * @param fileNme
	 * @return
	 */
	private String doRead(String fileNme) {
		String s = "";
		try {
			FileInputStream in = context.openFileInput(fileNme);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			s = EncodingUtils.getString(bytes, "UTF-8");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(s.length()<5){
			s="0;0;0";
		}
		return s;
	}
}
