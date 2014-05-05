package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
/**
 * 角色类
 * @author dhee
 *
 */
public class Hero {
	/**上下文，以提供资源*/
	private Context context;
	/**角色动画*/
	private Animation anim = null;
	/**动画资源*/
	private Bitmap bitmap = null;
	/**碰撞检测对象*/
	public CrashDetection check=null;
	/**备份碰撞前坐标*/
	public int back_x=32,back_y=32;
	
	
	/** 方向状态 */
	final static int moveUp = 0;
	final static int moveDown = 1;
	final static int moveLeft = 2;
	final static int moveRight = 3;
	final static int move_stop = -1;
	/**构造方法*/
	public Hero(Context context) {
		this.context = context;
		anim = new Animation(context);
		check=new CrashDetection();
	}
	/**绘制角色*/
	public void drawMe(Canvas canvas, int x, int y, int current_dir) {

		//控制Animation中tile标志变量 
		Animation.i += 1;
		if (Animation.i == 3) {
			Animation.i = 0;
		}
		//判断人物状态，初始化bitmap
		switch (current_dir) {
		case moveUp:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hero_up);
			break;
		case moveDown:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hero_down);
			break;
		case moveLeft:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hero_left);
			break;
		case moveRight:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hero_right);
			break;
		case move_stop:
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hero_stop);
			Animation.i = 0;
			break;
		}
		
		// 绘制人物行走的每一帧动画
		anim.DrawHeroAnimation(canvas, new Paint(), x, y, bitmap);	
	}
}
