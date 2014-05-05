package com.bomb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;
/**
 * 游戏视图类
 * @author ryf
 *
 */
public class GameView extends SurfaceView implements Callback, Runnable {
	
	private Context context = null;
	private Canvas canvas = null;
	private Paint paint = null;
	private SurfaceHolder sfh = null;
	private Map map = null;
	private Hero hero = null;
	/**敌人*/
	public static Enemy enemy1 = null;
	public static Enemy enemy2 = null;
	public static Enemy enemy3 = null;
	/**角色存活*/
	public static boolean heroAlive = false;
	
	/**位图资源*/
	private Bitmap bitmap_hud = null;
	private Bitmap bitmap_hud_time = null;
	private Bitmap bitmap_numbers = null;
	private Bitmap bitmap_score = null;
	private Bitmap bitmap_speed = null;
	private Bitmap bitmap_bombBtn = null;
	
	/**结束动画的matrix*/
	private Matrix matrix_Left;
	private Matrix matrix_Right;
	/**游戏时间*/
	private long game_time = 0;
	/**标志时间*/
	private long tag_time = 0;
	/**游戏时间十位个位*/
	private int time_i = -1;
	private int time_j = -1;
	/**分数十位和个位*/
	private int score_i = 0;
	private int score_j = 0;
	/**分数*/
	public static int score = 0;
	/**线程状态*/
	public static boolean isRunning = false;
	private Thread thread = null;

	/** 方向状态 */
	final static int moveUp = 0;
	final static int moveDown = 1;
	final static int moveLeft = 2;
	final static int moveRight = 3;
	final static int move_stop = -1;
	/** 当前的方向 */
	public static int current_dir = move_stop;
	/** 英雄位置 */
	public static int hero_x = 32;
	public static int hero_y = 32;
	/** 移动速度 */
	public static int speed = 5;
	/**结束动画位图偏移量*/
	public int doorLeft_tag = 0;
	public int doorRight_tag=0;

	/** 放置炸弹按钮 */
	private Rect putBombButton = new Rect(480 - 2 * 32, 320 - 32 * 2,
			480 - 2 * 32 + 32, 320 - 32 * 2 + 32);
	private Bomb bomb = null;
	/**炸弹存活*/
	public static boolean bombFlag = false;
	/**摇杆小圈*/
	private float smallCenterX = 64, smallCenterY = 320 - 56,
			smallCenterR = 20;
	/**摇杆大圈*/
	private float BigCenterX = 64, BigCenterY = 320 - 56, BigCenterR = 40;
	/**
	 * 构造方法
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context);
		this.context = context;
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		map = new Map(context);
		Log.i("map", "---------------------------------------");
		enemy1 = new Enemy(context, 5 * 32, 4 * 32);
		enemy2 = new Enemy(context, 5 * 32, 8 * 32);
		enemy3 = new Enemy(context, 11 * 32, 6 * 32);
		hero = new Hero(context);
		setFocusable(true);
	}

	/**
	 * 触屏事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//放置炸弹判断
		if (putBombButton.contains((int) event.getX(), (int) event.getY())) {
			if (bombFlag == true) {
				return false;
			}
			bombFlag = true;
			int bomb_x = hero_x + 15;
			int bomb_y = hero_y + 15;
			bomb = new Bomb(context, bomb_x, bomb_y);
			return true;
		}

		// 当手指抬起,恢复小圆到初始位置
		if (event.getAction() == MotionEvent.ACTION_UP) {
			current_dir = move_stop;
			smallCenterX = BigCenterX;
			smallCenterY = BigCenterY;
		} else {
			int pointX = (int) event.getX();
			int pointY = (int) event.getY();
			// 判断点击的位置是否在大圆内
			if (Math.sqrt(Math.pow((BigCenterX - (int) event.getX()), 2)
					+ Math.pow((BigCenterY - (int) event.getY()), 2)) <= BigCenterR) {
				// 小圆跟随触点位置移动
				smallCenterX = pointX;
				smallCenterY = pointY;
			} else {
				setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR,
						getRad(BigCenterX, BigCenterY, pointX, pointY));
			}
			// down
			if (Math.abs(pointX - BigCenterX) <= (pointY - BigCenterY)
					&& (pointY - BigCenterY) > 0) {
				current_dir = moveDown;
			}
			// up
			if (Math.abs(pointX - BigCenterX) <= Math.abs(pointY - BigCenterY)
					&& (pointY - BigCenterY) < 0) {
				current_dir = moveUp;
			}
			// left
			if (Math.abs(pointX - BigCenterX) > Math.abs(pointY - BigCenterY)
					&& (pointX - BigCenterX) < 0) {
				current_dir = moveLeft;
			}
			// right
			if (Math.abs(pointX - BigCenterX) > Math.abs(pointY - BigCenterY)
					&& (pointX - BigCenterX) > 0) {
				current_dir = moveRight;
			}
		}
		return true;
	}

	/**
	 * 小圆针对于大圆做圆周运动时，设置小圆中心点的坐标位置
	 * 
	 * @param centerX
	 *            围绕的圆形(大圆)中心点X坐标
	 * @param centerY
	 *            围绕的圆形(大圆)中心点Y坐标
	 * @param R
	 *            围绕的圆形(大圆)半径
	 * @param rad
	 *            旋转的弧度
	 */
	public void setSmallCircleXY(float centerX, float centerY, float R,
			double rad) {
		// 获取圆周运动的X坐标
		smallCenterX = (float) (R * Math.cos(rad)) + centerX;
		// 获取圆周运动的Y坐标
		smallCenterY = (float) (R * Math.sin(rad)) + centerY;
	}

	/**
	 * 得到两点之间的弧度
	 * 
	 * @param px1
	 *            第一个点的X坐标
	 * @param py1
	 *            第一个点的Y坐标
	 * @param px2
	 *            第二个点的X坐标
	 * @param py2
	 *            第二个点的Y坐标
	 * @return
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		// 得到两点X的距离
		float x = px2 - px1;
		// 得到两点Y的距离
		float y = py1 - py2;
		// 算出斜边长
		float Hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		// 得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
		float cosAngle = x / Hypotenuse;
		// 通过反余弦定理获取到其角度的弧度
		float rad = (float) Math.acos(cosAngle);
		// 当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	/**
	 * 绘图
	 */
	public void drawView() {
		//取得画布
		canvas = sfh.lockCanvas();
		//人物存活绘图
		if (heroAlive == true) {

			paint.setAlpha(250);
			canvas.drawBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.mapbg), 0, 0, paint);
			map.DrawMap(canvas, paint);

			if (bomb != null) {
				bomb.DrawMe(canvas);
			}
			hero.drawMe(canvas, hero_x, hero_y, current_dir);
			if (enemy1.alive) {
				enemy1.drawMe(canvas, hero);
			}
			if (enemy2.alive) {
				enemy2.drawMe(canvas, hero);
			}

			if (enemy3.alive) {
				enemy3.drawMe(canvas, hero);
			}

			paint.setAlpha(0);
			canvas.drawRect(putBombButton, paint);
			paint.setTextSize(35);
			canvas.drawText("time:" + System.currentTimeMillis() / 1000, 160,
					32, paint);

			// 绘制大圆
			paint.setColor(Color.CYAN);
			paint.setAlpha(0x77);
			canvas.drawCircle(BigCenterX, BigCenterY, BigCenterR, paint);
			// 绘制小圆
			canvas.drawCircle(smallCenterX, smallCenterY, smallCenterR, paint);

			drawTip(canvas, paint);
		} else {
			//人物消亡绘图
			paint.setAlpha(250);
			 canvas.drawBitmap(BitmapFactory.decodeResource(
			 context.getResources(), R.drawable.mapbg), 0, 0, paint);
			 map.DrawMap(canvas, paint);		

			//doorLeft
			if(doorLeft_tag>=0){
				doorLeft_tag=0;
			}
			
			matrix_Left.reset();
			matrix_Left.postTranslate(doorLeft_tag, 0);
			doorLeft_tag += 15;		
			canvas.drawBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.wl), matrix_Left,
					paint);

			

			//doorRight
			if(doorRight_tag<=480-348){
				doorRight_tag=480-348;
			}
			matrix_Right.reset();
			matrix_Right.postTranslate(doorRight_tag, 0);
			doorRight_tag -= 15;		
			canvas.drawBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.wr), matrix_Right,
					paint);
			
			//分数
			paint.setColor(Color.BLUE);
			if(doorLeft_tag>=0&&doorRight_tag<=480-348){
				paint.setTextSize(35);
				canvas.drawText("score:  "+score, 160, 150, paint);
			}

		}
		sfh.unlockCanvasAndPost(canvas);
	}
	/**
	 * 上部状态绘制
	 * @param canvas
	 * @param paint
	 */
	private void drawTip(Canvas canvas, Paint paint) {
		paint.setAlpha(255);
		canvas.save();
		// 截取画布/
		canvas.clipRect(0, 0, 480, 32);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap_hud, 0, 0, paint);
		canvas.drawBitmap(bitmap_hud_time, 32, 0, paint);
		canvas.restore();
		/** 十位 */
		canvas.save();
		// 截取画布/
		canvas.clipRect(60.5f, 6f, 60.5f + 19.5f, 26f);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap_numbers, 60.5f - 19.5f * time_i, 6, paint);
		canvas.restore();

		/** 个位 */
		canvas.save();
		// 截取画布/
		canvas.clipRect(16 + 60.5f, 6f, 16 + 60.5f + 19.5f, 26f);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap_numbers, 16 + 60.5f - 19.5f * time_j, 6, paint);
		canvas.restore();

		// speed
		canvas.drawBitmap(bitmap_speed, 4 * 32, 0, paint);
		canvas.save();
		// 截取画布/
		canvas.clipRect(5 * 32, 6f, 5 * 32 + 19.5f, 26f);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap_numbers, 5 * 32 - 19.5f * speed, 6, paint);
		canvas.restore();

		// score
		canvas.drawBitmap(bitmap_score, 7 * 32, 0, paint);
		
		/** 十位 */
		if (score_i != 0) {
			canvas.save();
			// 截取画布/
			canvas.clipRect(8 * 32, 6f, 8 * 32 + 19.5f, 26f);
			// 在截取画布上绘制动画片段 /
			canvas.drawBitmap(bitmap_numbers, 8 * 32 - 19.5f * score_i, 6,
					paint);
			canvas.restore();
		}
		/** 个位 */
		canvas.save();
		// 截取画布/
		canvas.clipRect(8 * 32 + 16, 6f, 8 * 32 + 19.5f + 16, 26f);
		// 在截取画布上绘制动画片段 /
		canvas.drawBitmap(bitmap_numbers, 8 * 32 + 16 - 19.5f * score_j, 6,
				paint);
		canvas.restore();

		canvas.drawBitmap(bitmap_bombBtn, 12 * 32 + 25, 7 * 32 + 25, paint);

	}


	public void run() {
		while (isRunning) {
			long start = System.currentTimeMillis();
			logic();
			drawView();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					// Thread.sleep(50 - (end - start));
					Thread.sleep(3);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 逻辑
	 */
	private void logic() {
		switch (current_dir) {
		case moveUp:
			hero_y -= speed;
			break;
		case moveDown:
			hero_y += speed;
			break;
		case moveLeft:
			hero_x -= speed;
			break;
		case moveRight:
			hero_x += speed;
			break;
		case move_stop:
			break;
		}
		/**
		 * 判断hero_x,hero_y是否在碰撞区 碰撞则调用备份坐标
		 */
		if (hero.check.isCollsion(hero_x, hero_y, current_dir)) {
			hero_x = hero.back_x;
			hero_y = hero.back_y;
			Log.i("碰撞", "true");
		} else {
			hero.back_x = hero_x;
			hero.back_y = hero_y;
		}
		game_time = 99 - (System.currentTimeMillis() - tag_time) / 1000;
		time_i = (int) (game_time / 10);
		time_j = (int) (game_time % 10);
		if (time_i == 0 && time_j == -1) {
			heroAlive = false;
		}
		score_i = (int) (score / 10);
		score_j = (int) (score % 10);
	}
	/**
	 * 創建時
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		score = 0;
		matrix_Left = new Matrix();
		matrix_Right = new Matrix();
		isRunning = true;
		heroAlive = true;
		doorLeft_tag=-346;
		doorRight_tag=480;
		thread = new Thread(this);
		thread.start();
		tag_time = System.currentTimeMillis();
		bitmap_hud = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.hud);
		bitmap_hud_time = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.hud_timer);
		bitmap_numbers = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.numbers);
		bitmap_score = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.star);
		bitmap_speed = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.speed);
		bitmap_bombBtn = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bomb_button);
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}
	/***
	 * 銷毀時
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRunning = false;
		map = new Map(context);
		// thread=new Thread(this);
		hero_x = 32;
		hero_y = 32;
		speed = 5;
		Log.i("info", "************************************************");
	}
	/***
	 * 按鍵按下，移動狀態改變
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			current_dir = moveUp;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			current_dir = moveDown;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			current_dir = moveLeft;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			current_dir = moveRight;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 按鍵抬起狀態停止
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			current_dir = move_stop;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			current_dir = move_stop;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			current_dir = move_stop;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			current_dir = move_stop;
		}
		return super.onKeyUp(keyCode, event);
	}

}
