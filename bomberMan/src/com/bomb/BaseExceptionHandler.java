package com.bomb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class BaseExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler

{
	public static BaseExceptionHandler defaultHandler;
	private Context context;

	private BaseExceptionHandler() {
	}

	public void init(Context context) {
		this.context = context;
		//设置该CrashHandler为程序的默认处理器  
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 */
	public static BaseExceptionHandler getInstance() {
		if (defaultHandler == null) {
			defaultHandler = new BaseExceptionHandler();
		}
		return defaultHandler;
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);

		StringBuffer sb = new StringBuffer();
		sb.append("/**************");
		SimpleDateFormat todayDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = todayDateFormatter.format(new Date());
		sb.append(time);
		sb.append("  Start **************/");
		sb.append("\n");
		sb.append(stackTrace.toString());
		sb.append("/************** End **************/");
		sb.append("\n\n\n");
		final String info = sb.toString();
		saveCrashInfo2File(info);
		sendMail("renyufeng@qq.com", null, "BomberGame for Android Phone exception report", info, context);
		Log.e("CrashExceptionHandler", info);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	private void saveCrashInfo2File(String crashinfo) {
		if (crashinfo == null)
			return;
		// 获取sd卡路径
		String sdcard_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		File dir = new File(sdcard_path + File.separator + "炸弹人游戏");
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(dir, "crashinfo.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file, true);
			fWriter.write(crashinfo);
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMail(String eMail, String ccEmail, String subject, String body, Context context) {
		if (eMail == null || context == null) {
			return;
		}

		try {
			Uri emailUri = Uri.parse("mailto:" + eMail);
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			if (ccEmail != null) {
				String[] ccs = { ccEmail };
				emailIntent.putExtra(Intent.EXTRA_CC, ccs);
			}

			if (subject != null) {
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			} else {
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
			}
			if (body != null) {
				emailIntent.putExtra(Intent.EXTRA_TEXT, body);
			} else {
				emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body text");
			}

			context.startActivity(emailIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("Customer service phone", "ActivityNotFoundException : ", e);
			Toast.makeText(context, "邮件发送失败", 0);
		}
	}

}
