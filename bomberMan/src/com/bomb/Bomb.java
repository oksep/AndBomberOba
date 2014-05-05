package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/**
 * 炸弹类
 * @author ryf
 *
 */
public class Bomb {

	private Context context;
	/** 炸弹动画 */
	private Animation anim = null;
	/**炸弹针动画资源*/
	private Bitmap bitmap = null;
	
	/**炸弹释放坐标*/
	int bomb_x;
	int bomb_y;
	/**
	 * 构造方法
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
	/**绘制炸弹*/
	public void DrawMe(Canvas canvas) {
		anim.DrawBombAnimation(canvas, bomb_x, bomb_y, bitmap);
	}

}
