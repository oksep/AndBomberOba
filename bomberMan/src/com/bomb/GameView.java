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
 * ��Ϸ��ͼ��
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
	/**����*/
	public static Enemy enemy1 = null;
	public static Enemy enemy2 = null;
	public static Enemy enemy3 = null;
	/**��ɫ���*/
	public static boolean heroAlive = false;
	
	/**λͼ��Դ*/
	private Bitmap bitmap_hud = null;
	private Bitmap bitmap_hud_time = null;
	private Bitmap bitmap_numbers = null;
	private Bitmap bitmap_score = null;
	private Bitmap bitmap_speed = null;
	private Bitmap bitmap_bombBtn = null;
	
	/**����������matrix*/
	private Matrix matrix_Left;
	private Matrix matrix_Right;
	/**��Ϸʱ��*/
	private long game_time = 0;
	/**��־ʱ��*/
	private long tag_time = 0;
	/**��Ϸʱ��ʮλ��λ*/
	private int time_i = -1;
	private int time_j = -1;
	/**����ʮλ�͸�λ*/
	private int score_i = 0;
	private int score_j = 0;
	/**����*/
	public static int score = 0;
	/**�߳�״̬*/
	public static boolean isRunning = false;
	private Thread thread = null;

	/** ����״̬ */
	final static int moveUp = 0;
	final static int moveDown = 1;
	final static int moveLeft = 2;
	final static int moveRight = 3;
	final static int move_stop = -1;
	/** ��ǰ�ķ��� */
	public static int current_dir = move_stop;
	/** Ӣ��λ�� */
	public static int hero_x = 32;
	public static int hero_y = 32;
	/** �ƶ��ٶ� */
	public static int speed = 5;
	/**��������λͼƫ����*/
	public int doorLeft_tag = 0;
	public int doorRight_tag=0;

	/** ����ը����ť */
	private Rect putBombButton = new Rect(480 - 2 * 32, 320 - 32 * 2,
			480 - 2 * 32 + 32, 320 - 32 * 2 + 32);
	private Bomb bomb = null;
	/**ը�����*/
	public static boolean bombFlag = false;
	/**ҡ��СȦ*/
	private float smallCenterX = 64, smallCenterY = 320 - 56,
			smallCenterR = 20;
	/**ҡ�˴�Ȧ*/
	private float BigCenterX = 64, BigCenterY = 320 - 56, BigCenterR = 40;
	/**
	 * ���췽��
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
	 * �����¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//����ը���ж�
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

		// ����ָ̧��,�ָ�СԲ����ʼλ��
		if (event.getAction() == MotionEvent.ACTION_UP) {
			current_dir = move_stop;
			smallCenterX = BigCenterX;
			smallCenterY = BigCenterY;
		} else {
			int pointX = (int) event.getX();
			int pointY = (int) event.getY();
			// �жϵ����λ���Ƿ��ڴ�Բ��
			if (Math.sqrt(Math.pow((BigCenterX - (int) event.getX()), 2)
					+ Math.pow((BigCenterY - (int) event.getY()), 2)) <= BigCenterR) {
				// СԲ���津��λ���ƶ�
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
	 * СԲ����ڴ�Բ��Բ���˶�ʱ������СԲ���ĵ������λ��
	 * 
	 * @param centerX
	 *            Χ�Ƶ�Բ��(��Բ)���ĵ�X����
	 * @param centerY
	 *            Χ�Ƶ�Բ��(��Բ)���ĵ�Y����
	 * @param R
	 *            Χ�Ƶ�Բ��(��Բ)�뾶
	 * @param rad
	 *            ��ת�Ļ���
	 */
	public void setSmallCircleXY(float centerX, float centerY, float R,
			double rad) {
		// ��ȡԲ���˶���X����
		smallCenterX = (float) (R * Math.cos(rad)) + centerX;
		// ��ȡԲ���˶���Y����
		smallCenterY = (float) (R * Math.sin(rad)) + centerY;
	}

	/**
	 * �õ�����֮��Ļ���
	 * 
	 * @param px1
	 *            ��һ�����X����
	 * @param py1
	 *            ��һ�����Y����
	 * @param px2
	 *            �ڶ������X����
	 * @param py2
	 *            �ڶ������Y����
	 * @return
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		// �õ�����X�ľ���
		float x = px2 - px1;
		// �õ�����Y�ľ���
		float y = py1 - py2;
		// ���б�߳�
		float Hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		// �õ�����Ƕȵ�����ֵ��ͨ�����Ǻ����еĶ��� ���ڱ�/б��=�Ƕ�����ֵ��
		float cosAngle = x / Hypotenuse;
		// ͨ�������Ҷ����ȡ����ǶȵĻ���
		float rad = (float) Math.acos(cosAngle);
		// ��������λ��Y����<ҡ�˵�Y��������Ҫȡ��ֵ-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	/**
	 * ��ͼ
	 */
	public void drawView() {
		//ȡ�û���
		canvas = sfh.lockCanvas();
		//�������ͼ
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

			// ���ƴ�Բ
			paint.setColor(Color.CYAN);
			paint.setAlpha(0x77);
			canvas.drawCircle(BigCenterX, BigCenterY, BigCenterR, paint);
			// ����СԲ
			canvas.drawCircle(smallCenterX, smallCenterY, smallCenterR, paint);

			drawTip(canvas, paint);
		} else {
			//����������ͼ
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
			
			//����
			paint.setColor(Color.BLUE);
			if(doorLeft_tag>=0&&doorRight_tag<=480-348){
				paint.setTextSize(35);
				canvas.drawText("score:  "+score, 160, 150, paint);
			}

		}
		sfh.unlockCanvasAndPost(canvas);
	}
	/**
	 * �ϲ�״̬����
	 * @param canvas
	 * @param paint
	 */
	private void drawTip(Canvas canvas, Paint paint) {
		paint.setAlpha(255);
		canvas.save();
		// ��ȡ����/
		canvas.clipRect(0, 0, 480, 32);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
		canvas.drawBitmap(bitmap_hud, 0, 0, paint);
		canvas.drawBitmap(bitmap_hud_time, 32, 0, paint);
		canvas.restore();
		/** ʮλ */
		canvas.save();
		// ��ȡ����/
		canvas.clipRect(60.5f, 6f, 60.5f + 19.5f, 26f);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
		canvas.drawBitmap(bitmap_numbers, 60.5f - 19.5f * time_i, 6, paint);
		canvas.restore();

		/** ��λ */
		canvas.save();
		// ��ȡ����/
		canvas.clipRect(16 + 60.5f, 6f, 16 + 60.5f + 19.5f, 26f);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
		canvas.drawBitmap(bitmap_numbers, 16 + 60.5f - 19.5f * time_j, 6, paint);
		canvas.restore();

		// speed
		canvas.drawBitmap(bitmap_speed, 4 * 32, 0, paint);
		canvas.save();
		// ��ȡ����/
		canvas.clipRect(5 * 32, 6f, 5 * 32 + 19.5f, 26f);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
		canvas.drawBitmap(bitmap_numbers, 5 * 32 - 19.5f * speed, 6, paint);
		canvas.restore();

		// score
		canvas.drawBitmap(bitmap_score, 7 * 32, 0, paint);
		
		/** ʮλ */
		if (score_i != 0) {
			canvas.save();
			// ��ȡ����/
			canvas.clipRect(8 * 32, 6f, 8 * 32 + 19.5f, 26f);
			// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
			canvas.drawBitmap(bitmap_numbers, 8 * 32 - 19.5f * score_i, 6,
					paint);
			canvas.restore();
		}
		/** ��λ */
		canvas.save();
		// ��ȡ����/
		canvas.clipRect(8 * 32 + 16, 6f, 8 * 32 + 19.5f + 16, 26f);
		// �ڽ�ȡ�����ϻ��ƶ���Ƭ�� /
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
	 * �߼�
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
		 * �ж�hero_x,hero_y�Ƿ�����ײ�� ��ײ����ñ�������
		 */
		if (hero.check.isCollsion(hero_x, hero_y, current_dir)) {
			hero_x = hero.back_x;
			hero_y = hero.back_y;
			Log.i("��ײ", "true");
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
	 * �����r
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
	 * �N���r
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
	 * ���I���£��ƄӠ�B��׃
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
	 * ���I̧���Bֹͣ
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
