package com.bomb;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

/**
 * “Ù¿÷≤•∑≈¿‡
 * 
 * @author ryf
 * 
 */
public class Music {
	/** ≤•∑≈∆˜ */
	private static MediaPlayer player = null;

	public static boolean music_flag = true;

	/**
	 * ≤•∑≈
	 * 
	 * @param context
	 * @param resource
	 */
	public static void play(Context context, int resource) {
		if (music_flag) {
			stop(context);
			player = MediaPlayer.create(context, resource);
			player.setLooping(true);
			player.start();
		}
	}

	/**
	 * Õ£÷π
	 * 
	 * @param context
	 */
	public static void stop(Context context) {
		if (music_flag) {
			if (player != null) {
				player.stop();
				player.release();
				player = null;
			}
		}
	}
}
