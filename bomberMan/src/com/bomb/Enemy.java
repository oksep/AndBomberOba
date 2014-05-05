package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
/**
 * 敌人类
 * @author ryf
 *
 */
public class Enemy {
	/** 上下文,提供资源 */
	private Context context;
	/** 动画资源 */
	private Bitmap[] bitmaps = new Bitmap[4];
	/** 碰撞检测对象 */
	private CrashDetection check = null;
	/** 备份碰撞前坐标 */
	private int back_x = 0, back_y = 0;
	/** 方向状态 */
	final static int moveUp = 0;
	final static int moveDown = 1;
	final static int moveLeft = 2;
	final static int moveRight = 3;
	final static int move_stop = -1;
	/**挡墙方向*/
	private int dir = 0;
	/**敌人生存标志*/
	public boolean alive=false;
	/**敌人碰撞矩阵*/
	private Rect rect = null;
	/**敌人坐标*/
	public int enemy_x;
	public int enemy_y;
	/**移动速度*/
	private float speed = 2.5f;
	/**敌人动画标志*/
	private int index = 0;
	

	/** 
	 * 构造方法 
	 * @param context
	 * @param x
	 * @param y
	 */
	public Enemy(Context context, int x, int y) {
		this.context = context;
		check = new CrashDetection();
		bitmaps[0] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.enemy1);
		bitmaps[1] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.enemy2);
		bitmaps[2] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.enemy3);
		bitmaps[3] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.enemy4);
		this.enemy_x = x;
		this.enemy_y = y;
		back_x = enemy_x;
		back_y = enemy_y;
		rect = new Rect(enemy_x, enemy_y, enemy_x + 32, enemy_y + 32);
		alive=true;
	}

	/**
	 * 绘制敌人
	 */
	public void drawMe(Canvas canvas, Hero hero) {
		//方向判断
		switch (dir) {
		case moveUp:
			enemy_y -= speed;
			break;
		case moveDown:
			enemy_y += speed;
			break;
		case moveLeft:
			enemy_x -= speed;
			break;
		case moveRight:
			enemy_x += speed;
			break;
		}
		//备份坐标
		if (check.isCollsion(enemy_x, enemy_y, dir)) {
			enemy_x = back_x;
			enemy_y = back_y;
			dir = (int) (Math.random() * 10) % 4;
		} else {
			back_x = enemy_x;
			back_y = enemy_y;
		}
		//绘制
		canvas.drawBitmap(bitmaps[index], enemy_x, enemy_y, new Paint());
		
		rect.left = enemy_x;
		rect.top = enemy_y;
		rect.right = enemy_x + 32;
		rect.bottom = enemy_y + 32;
		//检测
		if (rect.contains(GameView.hero_x + 8, GameView.hero_y + 8)
				|| rect.contains(GameView.hero_x + 32 - 8, GameView.hero_y + 8)
				|| rect.contains(GameView.hero_x + 8, GameView.hero_y + 32 - 2)
				|| rect.contains(GameView.hero_x + 32 - 8,
						GameView.hero_y + 32 - 2)) {
			GameView.heroAlive = false;
		}
		index += 1;
		if (index >= 4) {
			index = 0;
		}
	}
}
