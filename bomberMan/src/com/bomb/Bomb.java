package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/**
 * ը����
 * @author ryf
 *
 */
public class Bomb {

	private Context context;
	/** ը������ */
	private Animation anim = null;
	/**ը���붯����Դ*/
	private Bitmap bitmap = null;
	
	/**ը���ͷ�����*/
	int bomb_x;
	int bomb_y;
	/**
	 * ���췽��
	 * @param context
	 * @param bomb_x
	 * @param bomb_y
	 */
	public Bomb(Context context, int bomb_x, int bomb_y) {
		this.context = context;
		anim = new Animation(context);
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb2);
		this.bomb_x = bomb_x;
		this.bomb_y = bomb_y;
	}
	/**����ը��*/
	public void DrawMe(Canvas canvas) {
		anim.DrawBombAnimation(canvas, bomb_x, bomb_y, bitmap);
	}

}
