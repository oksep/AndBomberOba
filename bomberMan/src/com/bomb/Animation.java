package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 动画类
 * @author ryf
 *
 */
public class Animation {

	/** 角色tile的标志变量 */
	static int i = 0;
	/** 每个tile宽,高 */
	static final int tile_srcx = 32, tile_srcy = 32;
	/** 标记时间 */
	private long time = 0;
	/** 放置炸弹时间 */
	private long setBombTime = 0;
	/** 炸弹tile的标志变量 */
	private int j = 0;
	/** 炸弹爆炸的标志变量 */
	private int k = 0;

	/** 爆炸上部效果图 */
	private Bitmap bombBitmap_up = null;
	/** 爆炸下部效果图 */
	private Bitmap bombBitmap_down = null;
	/** 爆炸左部效果图 */
	private Bitmap bombBitmap_left = null;
	/** 爆炸右部效果图 */
	private Bitmap bombBitmap_right = null;
	/** 爆炸中部效果图 */
	private Bitmap bombBitmap_center = null;
	/** 爆炸标志 */
	private Boolean bombFlag = false;

	/**
	 * 构造方法
	 * @param context
	 */
	public Animation(Context context) {
		time = System.currentTimeMillis();
		setBombTime = time;
		bombBitmap_up = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb_up);
		bombBitmap_down = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb_down);
		bombBitmap_left = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb_left);
		bombBitmap_right = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb_right);
		bombBitmap_center = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.bomb_center);
		k = 0;
		bombFlag = true;
	}

	/**
	 * 绘制人物动画
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void DrawHeroAnimation(Canvas canvas, Paint paint, int x, int y,
			Bitmap bitmap) {

		canvas.save();
		// 截取画布/
		canvas.clipRect(x, y, x + 32, y + 32);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap, x - i * tile_srcx, y, new Paint());
		canvas.restore();
	}

	/**
	 * 绘制炸弹爆炸动画
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 * @param bitmap
	 */
	public void DrawBombAnimation(Canvas canvas, int x, int y, Bitmap bitmap) {
		if (bombFlag) {

			// 设置画笔
			Paint p = new Paint();
			// 炸弹放置坐标
			int bomb_x = (x / 32) * 32;
			int bomb_y = (y / 32) * 32;
			
			//碰撞检测矩阵
			Rect r1 = new Rect(bomb_x, bomb_y - 32, bomb_x + 32, bomb_y + 64);
			Rect r2 = new Rect(bomb_x - 32, bomb_y, bomb_x + 64, bomb_y + 32);

			// 当前时间
			long cur_time = System.currentTimeMillis();
			
			// 时间差判断
			if (cur_time - time >= 50) {
				j += 1;
				if (j == 3) {
					j = 0;
				}
				time = cur_time;
			}
			// 控制爆炸
			if (time - setBombTime <= 2000) {
				canvas.save();
				canvas.clipRect(bomb_x, bomb_y, bomb_x + 32, bomb_y + 32);
				canvas.drawBitmap(bitmap, bomb_x - j * tile_srcx, bomb_y, p);
				canvas.restore();
			} else {
				if (k <= 5) {
					// 绘制爆炸中间部分
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y, bomb_x + 32, bomb_y + 32);
					canvas.drawBitmap(bombBitmap_center,
							bomb_x - k * tile_srcx, bomb_y, p);
					canvas.restore();

					// 绘制爆炸上侧部分
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y - 32, bomb_x + 32, bomb_y);
					canvas.drawBitmap(bombBitmap_up, bomb_x - k * tile_srcx,
							bomb_y - 32, p);
					canvas.restore();

					// 绘制爆炸下侧部分
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y + 32, bomb_x + 32,
							bomb_y + 32 * 2);
					canvas.drawBitmap(bombBitmap_down, bomb_x - k * tile_srcx,
							bomb_y + 32, p);
					canvas.restore();
					// 绘制爆炸左侧部分
					canvas.save();
					canvas.clipRect(bomb_x - 32, bomb_y, bomb_x, bomb_y + 32);
					canvas.drawBitmap(bombBitmap_left, bomb_x - 32 - k
							* tile_srcx, bomb_y, p);
					canvas.restore();

					// 绘制爆炸右侧部分
					canvas.save();
					canvas.clipRect(bomb_x + 32, bomb_y, bomb_x + 32 * 2,
							bomb_y + 32);
					canvas.drawBitmap(bombBitmap_right, bomb_x + 32 - k
							* tile_srcx, bomb_y, p);
					canvas.restore();
					k += 1;
				} else {
					
					/** 刷新actor碰撞数组，刷新layer2绘图数组 */
					
					// 上侧刷新
					int index_up_x = bomb_x / 32;
					int index_up_y = bomb_y / 32 - 1;
					if (Map.layer1[index_up_y][index_up_x] == 0) {
						if(Map.layer2[index_up_y][index_up_x]==1){
							GameView.score+=2;
						}
						Map.layer2[index_up_y][index_up_x] = 0;
						CrashDetection.actor[index_up_y][index_up_x] = 0;
					}
					// 下侧刷新
					int index_down_x = bomb_x / 32;
					int index_down_y = bomb_y / 32 + 1;
					if (Map.layer1[index_down_y][index_down_x] == 0) {
						if(Map.layer2[index_down_y][index_down_x]==1){
							GameView.score+=2;
						}
						Map.layer2[index_down_y][index_down_x] = 0;
						CrashDetection.actor[index_down_y][index_down_x] = 0;
					}

					// 左侧刷新
					int index_left_x = bomb_x / 32 - 1;
					int index_left_y = bomb_y / 32;
					if (Map.layer1[index_left_y][index_left_x] == 0) {
						if(Map.layer2[index_left_y][index_left_x] ==1){
							GameView.score+=2;
						}
						Map.layer2[index_left_y][index_left_x] = 0;
						CrashDetection.actor[index_left_y][index_left_x] = 0;
					}
					// 右侧刷新
					int index_right_x = bomb_x / 32 + 1;
					int index_right_y = bomb_y / 32;

					if (Map.layer1[index_right_y][index_right_x] == 0) {
						if(Map.layer2[index_right_y][index_right_x] ==1){
							GameView.score+=2;
						}
						Map.layer2[index_right_y][index_right_x] = 0;
						CrashDetection.actor[index_right_y][index_right_x] = 0;
					}
					bombFlag = false;
					
					
					/**
					 * 爆炸后角色碰撞检测
					 */
					
					//hero
					if (r1.contains(GameView.hero_x + 15, GameView.hero_y + 15)
							|| r2.contains(GameView.hero_x + 15,
									GameView.hero_y + 15)) {
						GameView.heroAlive = false;
					}
					
					//enemy1
					if (r1.contains(GameView.enemy1.enemy_x + 15, GameView.enemy1.enemy_y + 15)
							|| r2.contains(GameView.enemy1.enemy_x + 15,
									GameView.enemy1.enemy_y)) {
						GameView.enemy1.alive = false;
						GameView.score+=10;
					}
					//enemy2
					if (r1.contains(GameView.enemy2.enemy_x + 15, GameView.enemy2.enemy_y + 15)
							|| r2.contains(GameView.enemy2.enemy_x + 15,
									GameView.enemy2.enemy_y)) {
						GameView.enemy2.alive = false;
						GameView.score+=10;
					}
					//enemy3
					if (r1.contains(GameView.enemy3.enemy_x + 15, GameView.enemy3.enemy_y + 15)
							|| r2.contains(GameView.enemy3.enemy_x + 15,
									GameView.enemy3.enemy_y)) {
						GameView.enemy3.alive = false;
						GameView.score+=10;
					}
					GameView.bombFlag=false;
				}
			}
		}
	}
}
