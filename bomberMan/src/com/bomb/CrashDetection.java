package com.bomb;

import android.graphics.Rect;
import android.util.Log;
/**
 * 碰撞检测类
 * @author ryf
 *
 */
public class CrashDetection {
	
	/**碰撞数组*/
	static int[][] actor = new int[10][15];

	/** 构建碰撞数组 */
	static {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				if (Map.layer1[i][j] == 1) {
					actor[i][j] = 1;
				}
				if (Map.layer2[i][j] == 1) {
					actor[i][j] = 1;
				}
			}
		}

		Log.i("actor", "***********************************************");
		for (int i[] : actor) {
			for (int j : i) {
				System.out.print("\t" + j);
			}
			System.out.println();
		}
		Log.i("actor", "***********************************************");
	}

	/** tile块宽,高 */
	static int tile_width = 32, tile_height = 32;
	/**
	 * 碰撞判断
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	boolean isCollsion(int x, int y) {

		// 角色中点坐标
		int center_x = x + 16;
		int center_y = y + 16;

		// 二维数组中行索引
		int index_row = center_y / tile_height;
		// 二维数组中列索引
		int index_col = center_x / tile_width;

		Log.i("x,y", "行 " + index_row + ",列 " + index_col);
		Log.i("中心点坐标", "" + center_x + "," + center_y);
		// 左侧区域判断
		try {
			if (actor[index_row][index_col] == 1) {
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
		return false;
	}
	/**
	 * 碰撞判断
	 * @param x
	 * @param y
	 * @param current_dir
	 * @return
	 */
	public boolean isCollsion(int x, int y, int current_dir) {
		if(current_dir==Hero.moveUp){
			// 角色中点坐标
			int center_x = x + 16;
			int center_y = y + 16;

			// 二维数组中行索引
			int index_row = center_y / tile_height;
			// 二维数组中列索引
			int index_col = center_x / tile_width;

			Log.i("x,y", "行 " + index_row + ",列 " + index_col);
			Log.i("中心点坐标", "" + center_x + "," + center_y);
			// 左侧区域判断
			try {
				if (actor[index_row][index_col] == 1) {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				return true;
			}
			return false;
		}
		if(current_dir==Hero.moveDown){
			
			// 角色中点坐标
			int center_x = x + 16;
			int center_y = y + 28;

			// 二维数组中行索引
			int index_row = center_y / tile_height;
			// 二维数组中列索引
			int index_col = center_x / tile_width;

			Log.i("x,y", "行 " + index_row + ",列 " + index_col);
			Log.i("中心点坐标", "" + center_x + "," + center_y);
			// 左侧区域判断
			try {
				if (actor[index_row][index_col] == 1) {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				return true;
			}
			return false;
		}
		if(current_dir==Hero.moveLeft){
			
			// 角色中点坐标
			int center_x = x + 9;
			int center_y = y + 25;

			// 二维数组中行索引
			int index_row = center_y / tile_height;
			// 二维数组中列索引
			int index_col = center_x / tile_width;

			Log.i("x,y", "行 " + index_row + ",列 " + index_col);
			Log.i("中心点坐标", "" + center_x + "," + center_y);
			// 左侧区域判断
			try {
				if (actor[index_row][index_col] == 1) {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				return true;
			}
			return false;
		}
		if(current_dir==Hero.moveRight){
			
			// 角色中点坐标
			int center_x = x + 22;
			int center_y = y + 25;

			// 二维数组中行索引
			int index_row = center_y / tile_height;
			// 二维数组中列索引
			int index_col = center_x / tile_width;

			Log.i("x,y", "行 " + index_row + ",列 " + index_col);
			Log.i("中心点坐标", "" + center_x + "," + center_y);
			// 左侧区域判断
			try {
				if (actor[index_row][index_col] == 1) {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				return true;
			}
			return false;
		}
		return false;
	}

}
