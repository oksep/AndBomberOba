package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * ������
 * @author ryf
 *
 */
public class Animation {

	/** ��ɫtile�ı�־���� */
	static int i = 0;
	/** ÿ��tile��,�� */
	static final int tile_srcx = 32, tile_srcy = 32;
	/** ���ʱ�� */
	private long time = 0;
	/** ����ը��ʱ�� */
	private long setBombTime = 0;
	/** ը��tile�ı�־���� */
	private int j = 0;
	/** ը����ը�ı�־���� */
	private int k = 0;

	/** ��ը�ϲ�Ч��ͼ */
	private Bitmap bombBitmap_up = null;
	/** ��ը�²�Ч��ͼ */
	private Bitmap bombBitmap_down = null;
	/** ��ը��Ч��ͼ */
	private Bitmap bombBitmap_left = null;
	/** ��ը�Ҳ�Ч��ͼ */
	private Bitmap bombBitmap_right = null;
	/** ��ը�в�Ч��ͼ */
	private Bitmap bombBitmap_center = null;
	/** ��ը��־ */
	private Boolean bombFlag = false;

	/**
	 * ���췽��
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
	 * �������ﶯ��
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void DrawHeroAnimation(Canvas canvas, Paint paint, int x, int y,
			Bitmap bitmap) {

		canvas.save();
		// ��ȡ����/
		canvas.clipRect(x, y, x + 32, y + 32);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
		canvas.drawBitmap(bitmap, x - i * tile_srcx, y, new Paint());
		canvas.restore();
	}

	/**
	 * ����ը����ը����
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 * @param bitmap
	 */
	public void DrawBombAnimation(Canvas canvas, int x, int y, Bitmap bitmap) {
		if (bombFlag) {

			// ���û���
			Paint p = new Paint();
			// ը����������
			int bomb_x = (x / 32) * 32;
			int bomb_y = (y / 32) * 32;
			
			//��ײ������
			Rect r1 = new Rect(bomb_x, bomb_y - 32, bomb_x + 32, bomb_y + 64);
			Rect r2 = new Rect(bomb_x - 32, bomb_y, bomb_x + 64, bomb_y + 32);

			// ��ǰʱ��
			long cur_time = System.currentTimeMillis();
			
			// ʱ����ж�
			if (cur_time - time >= 50) {
				j += 1;
				if (j == 3) {
					j = 0;
				}
				time = cur_time;
			}
			// ���Ʊ�ը
			if (time - setBombTime <= 2000) {
				canvas.save();
				canvas.clipRect(bomb_x, bomb_y, bomb_x + 32, bomb_y + 32);
				canvas.drawBitmap(bitmap, bomb_x - j * tile_srcx, bomb_y, p);
				canvas.restore();
			} else {
				if (k <= 5) {
					// ���Ʊ�ը�м䲿��
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y, bomb_x + 32, bomb_y + 32);
					canvas.drawBitmap(bombBitmap_center,
							bomb_x - k * tile_srcx, bomb_y, p);
					canvas.restore();

					// ���Ʊ�ը�ϲಿ��
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y - 32, bomb_x + 32, bomb_y);
					canvas.drawBitmap(bombBitmap_up, bomb_x - k * tile_srcx,
							bomb_y - 32, p);
					canvas.restore();

					// ���Ʊ�ը�²ಿ��
					canvas.save();
					canvas.clipRect(bomb_x, bomb_y + 32, bomb_x + 32,
							bomb_y + 32 * 2);
					canvas.drawBitmap(bombBitmap_down, bomb_x - k * tile_srcx,
							bomb_y + 32, p);
					canvas.restore();
					// ���Ʊ�ը��ಿ��
					canvas.save();
					canvas.clipRect(bomb_x - 32, bomb_y, bomb_x, bomb_y + 32);
					canvas.drawBitmap(bombBitmap_left, bomb_x - 32 - k
							* tile_srcx, bomb_y, p);
					canvas.restore();

					// ���Ʊ�ը�Ҳಿ��
					canvas.save();
					canvas.clipRect(bomb_x + 32, bomb_y, bomb_x + 32 * 2,
							bomb_y + 32);
					canvas.drawBitmap(bombBitmap_right, bomb_x + 32 - k
							* tile_srcx, bomb_y, p);
					canvas.restore();
					k += 1;
				} else {
					
					/** ˢ��actor��ײ���飬ˢ��layer2��ͼ���� */
					
					// �ϲ�ˢ��
					int index_up_x = bomb_x / 32;
					int index_up_y = bomb_y / 32 - 1;
					if (Map.layer1[index_up_y][index_up_x] == 0) {
						if(Map.layer2[index_up_y][index_up_x]==1){
							GameView.score+=2;
						}
						Map.layer2[index_up_y][index_up_x] = 0;
						CrashDetection.actor[index_up_y][index_up_x] = 0;
					}
					// �²�ˢ��
					int index_down_x = bomb_x / 32;
					int index_down_y = bomb_y / 32 + 1;
					if (Map.layer1[index_down_y][index_down_x] == 0) {
						if(Map.layer2[index_down_y][index_down_x]==1){
							GameView.score+=2;
						}
						Map.layer2[index_down_y][index_down_x] = 0;
						CrashDetection.actor[index_down_y][index_down_x] = 0;
					}

					// ���ˢ��
					int index_left_x = bomb_x / 32 - 1;
					int index_left_y = bomb_y / 32;
					if (Map.layer1[index_left_y][index_left_x] == 0) {
						if(Map.layer2[index_left_y][index_left_x] ==1){
							GameView.score+=2;
						}
						Map.layer2[index_left_y][index_left_x] = 0;
						CrashDetection.actor[index_left_y][index_left_x] = 0;
					}
					// �Ҳ�ˢ��
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
					 * ��ը���ɫ��ײ���
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
