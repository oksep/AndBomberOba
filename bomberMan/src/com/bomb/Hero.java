package com.bomb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
/**
 * ��ɫ��
 * @author dhee
 *
 */
public class Hero {
	/**�����ģ����ṩ��Դ*/
	private Context context;
	/**��ɫ����*/
	private Animation anim = null;
	/**������Դ*/
	private Bitmap bitmap = null;
	/**��ײ������*/
	public CrashDetection check=null;
	/**������ײǰ����*/
	public int back_x=32,back_y=32;
	
	
	/** ����״̬ */
	final static int moveUp = 0;
	final static int moveDown = 1;
	final static int moveLeft = 2;
	final static int moveRight = 3;
	final static int move_stop = -1;
	/**���췽��*/
	public Hero(Context context) {
		this.context = context;
		anim = new Animation(context);
		check=new CrashDetection();
	}
	/**���ƽ�ɫ*/
	public void drawMe(Canvas canvas, int x, int y, int current_dir) {

		//����Animation��tile��־���� 
		Animation.i += 1;
		if (Animation.i == 3) {
			Animation.i = 0;
		}
		//�ж�����״̬����ʼ��bitmap
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
		
		// �����������ߵ�ÿһ֡����
		anim.DrawHeroAnimation(canvas, new Paint(), x, y, bitmap);	
	}
}
