package com.bomb;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * ��ͼ��
 * @author ryf
 *
 */
public class Map {
	/** tile��Ŀ�� */
	public final static int TILE_WIDTH = 32;
	public final static int TILE_HEIGHT = 32;

	/** tile��Ŀ�ߵ����� */
	public final static int TILE_WIDTH_COUNT = 15;
	public final static int TILE_HEIGHT_COUNT = 10;

	/** ����Ԫ��Ϊ0��ʲô������ */
	public final static int TILE_NULL = 0;
	/** ��һ����ϷView��ͼ���� */
	public static int[][] layer1 = new int[10][15];
	/** �ڶ�����ϷView��ͼ���� */
	public static int[][] layer2 =new int[10][15];
	/**��һ��map*/
	Bitmap mBitmap_layer1 = null;
	/**�ڶ���map*/
	Bitmap mBitmap_layer2 = null;
	/** ��Դ�ļ�*/
	Resources mResources = null;

	/**��Ϸ����*/
	Paint mPaint = null;

	/** ����tile�������*/
	int mWidthTileCount = 0;
	/** ����tile�������*/
	int mHeightTileCount = 0;

	/**layer��*/
	int mBitMapWidth = 0;
	/**layer��*/
	int mBitMapHeight = 0;

	/**
	 * ���췽��
	 * 
	 * @param context
	 */
	public Map(Context context) {
		MapData mapData=new MapData();
		
		for(int i=0;i<10;i++){
			for(int j=0;j<15;j++){
				layer1[i][j]=mapData.getLayer1()[i][j];
				layer2[i][j]=mapData.getLayer2()[i][j];
			}
		}		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				if (Map.layer1[i][j] == 1) {
					CrashDetection.actor[i][j] = 1;
				}
				if (Map.layer2[i][j] == 1) {
					CrashDetection.actor[i][j] = 1;
				}
			}
		}
		
		mPaint = new Paint();
		mBitmap_layer1 = ReadBitMap(context, R.drawable.layer1);
		mBitmap_layer2 = ReadBitMap(context, R.drawable.layer2);
		mBitMapWidth = mBitmap_layer1.getWidth();
		mBitMapHeight = mBitmap_layer1.getHeight();
		mWidthTileCount = mBitMapWidth / TILE_WIDTH;
		mHeightTileCount = mBitMapHeight / TILE_HEIGHT;
	}
	/**
	 * ִ�л�ͼ
	 * @param canvas
	 * @param paint
	 */
	public void DrawMap(Canvas canvas, Paint paint) {
		int i, j;
		for (i = 0; i < TILE_HEIGHT_COUNT; i++) {
			for (j = 0; j < TILE_WIDTH_COUNT; j++) {
				int ViewID = layer1[i][j];
				int ActorID = layer2[i][j];
				/**���Ƶ�ͼ��һ��*/
				if (ViewID > TILE_NULL) {
					DrawMapTile(ViewID, canvas, paint, mBitmap_layer1, j
							* TILE_WIDTH, i * TILE_HEIGHT);
				}

				/**���Ƶ�ͼ�ڶ���*/
				if (ActorID > TILE_NULL) {
					DrawMapTile(ActorID, canvas, paint, mBitmap_layer2, j
							* TILE_WIDTH, i * TILE_HEIGHT);
				}
			}
		}
	}

	/**
	 * ����ID����һ��tile��
	 * @param id
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 */
	private void DrawMapTile(int id, Canvas canvas, Paint paint, Bitmap bitmap,
			int x, int y) {
		/**���������е�ID����ڵ�ͼ��Դ�е�XY ����
		  * ��Ϊ�༭��Ĭ��0 ���Ե�һ��tile��ID����0����1 �������� -1
		  */
		id--;
		/** ����*/
		int count = id / mWidthTileCount;
		/** ��Ҫ��ȡ��tile������*/
		int bitmapX = (id - (count * mWidthTileCount)) * TILE_WIDTH;
		/** ��Ҫ��ȡ��tile������*/
		int bitmapY = count * TILE_HEIGHT;
		DrawClipImage(canvas, paint, bitmap, x, y, bitmapX, bitmapY,
				TILE_WIDTH, TILE_HEIGHT);
	}

	/**
	 * ��ȡ������Դ��ͼƬ
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap ReadBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * ����ͼƬ�е�һ����ͼƬ
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param src_x
	 * @param src_y
	 * @param src_width
	 * @param src_Height
	 */
	private void DrawClipImage(Canvas canvas, Paint paint, Bitmap bitmap,
			int x, int y, int src_x, int src_y, int src_xp, int src_yp) {
		/**��������*/
		canvas.save();
		/** ��ȡ����*/
		canvas.clipRect(x, y, x + src_xp, y + src_yp);
		/**�ڻ����л�bitmap�б���ȡͼ*/
		canvas.drawBitmap(bitmap, x - src_x, y - src_y, paint);
		/**��������*/
		canvas.restore();
	}

}
