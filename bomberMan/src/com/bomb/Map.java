package com.bomb;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 地图类
 * @author ryf
 *
 */
public class Map {
	/** tile块的宽高 */
	public final static int TILE_WIDTH = 32;
	public final static int TILE_HEIGHT = 32;

	/** tile块的宽高的数量 */
	public final static int TILE_WIDTH_COUNT = 15;
	public final static int TILE_HEIGHT_COUNT = 10;

	/** 数组元素为0则什么都不画 */
	public final static int TILE_NULL = 0;
	/** 第一层游戏View地图数组 */
	public static int[][] layer1 = new int[10][15];
	/** 第二层游戏View地图数组 */
	public static int[][] layer2 =new int[10][15];
	/**第一层map*/
	Bitmap mBitmap_layer1 = null;
	/**第二层map*/
	Bitmap mBitmap_layer2 = null;
	/** 资源文件*/
	Resources mResources = null;

	/**游戏画笔*/
	Paint mPaint = null;

	/** 横向tile块的数量*/
	int mWidthTileCount = 0;
	/** 纵向tile块的数量*/
	int mHeightTileCount = 0;

	/**layer宽*/
	int mBitMapWidth = 0;
	/**layer高*/
	int mBitMapHeight = 0;

	/**
	 * 构造方法
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
	 * 执行绘图
	 * @param canvas
	 * @param paint
	 */
	public void DrawMap(Canvas canvas, Paint paint) {
		int i, j;
		for (i = 0; i < TILE_HEIGHT_COUNT; i++) {
			for (j = 0; j < TILE_WIDTH_COUNT; j++) {
				int ViewID = layer1[i][j];
				int ActorID = layer2[i][j];
				/**绘制地图第一层*/
				if (ViewID > TILE_NULL) {
					DrawMapTile(ViewID, canvas, paint, mBitmap_layer1, j
							* TILE_WIDTH, i * TILE_HEIGHT);
				}

				/**绘制地图第二层*/
				if (ActorID > TILE_NULL) {
					DrawMapTile(ActorID, canvas, paint, mBitmap_layer2, j
							* TILE_WIDTH, i * TILE_HEIGHT);
				}
			}
		}
	}

	/**
	 * 根据ID绘制一个tile块
	 * @param id
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 */
	private void DrawMapTile(int id, Canvas canvas, Paint paint, Bitmap bitmap,
			int x, int y) {
		/**根据数组中的ID算出在地图资源中的XY 坐标
		  * 因为编辑器默认0 所以第一张tile的ID不是0而是1 所以这里 -1
		  */
		id--;
		/** 行数*/
		int count = id / mWidthTileCount;
		/** 所要截取的tile横坐标*/
		int bitmapX = (id - (count * mWidthTileCount)) * TILE_WIDTH;
		/** 所要截取的tile纵坐标*/
		int bitmapY = count * TILE_HEIGHT;
		DrawClipImage(canvas, paint, bitmap, x, y, bitmapX, bitmapY,
				TILE_WIDTH, TILE_HEIGHT);
	}

	/**
	 * 读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap ReadBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 绘制图片中的一部分图片
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
		/**锁定画布*/
		canvas.save();
		/** 截取画布*/
		canvas.clipRect(x, y, x + src_xp, y + src_yp);
		/**在画布中画bitmap中被截取图*/
		canvas.drawBitmap(bitmap, x - src_x, y - src_y, paint);
		/**解锁画布*/
		canvas.restore();
	}

}
