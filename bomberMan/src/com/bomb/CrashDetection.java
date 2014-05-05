package com.bomb;

import android.graphics.Rect;
import android.util.Log;
/**
 * ��ײ�����
 * @author ryf
 *
 */
public class CrashDetection {
	
	/**��ײ����*/
	static int[][] actor = new int[10][15];

	/** ������ײ���� */
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

	/** tile���,�� */
	static int tile_width = 32, tile_height = 32;
	/**
	 * ��ײ�ж�
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	boolean isCollsion(int x, int y) {

		// ��ɫ�е�����
		int center_x = x + 16;
		int center_y = y + 16;

		// ��ά������������
		int index_row = center_y / tile_height;
		// ��ά������������
		int index_col = center_x / tile_width;

		Log.i("x,y", "�� " + index_row + ",�� " + index_col);
		Log.i("���ĵ�����", "" + center_x + "," + center_y);
		// ��������ж�
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
	 * ��ײ�ж�
	 * @param x
	 * @param y
	 * @param current_dir
	 * @return
	 */
	public boolean isCollsion(int x, int y, int current_dir) {
		if(current_dir==Hero.moveUp){
			// ��ɫ�е�����
			int center_x = x + 16;
			int center_y = y + 16;

			// ��ά������������
			int index_row = center_y / tile_height;
			// ��ά������������
			int index_col = center_x / tile_width;

			Log.i("x,y", "�� " + index_row + ",�� " + index_col);
			Log.i("���ĵ�����", "" + center_x + "," + center_y);
			// ��������ж�
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
			
			// ��ɫ�е�����
			int center_x = x + 16;
			int center_y = y + 28;

			// ��ά������������
			int index_row = center_y / tile_height;
			// ��ά������������
			int index_col = center_x / tile_width;

			Log.i("x,y", "�� " + index_row + ",�� " + index_col);
			Log.i("���ĵ�����", "" + center_x + "," + center_y);
			// ��������ж�
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
			
			// ��ɫ�е�����
			int center_x = x + 9;
			int center_y = y + 25;

			// ��ά������������
			int index_row = center_y / tile_height;
			// ��ά������������
			int index_col = center_x / tile_width;

			Log.i("x,y", "�� " + index_row + ",�� " + index_col);
			Log.i("���ĵ�����", "" + center_x + "," + center_y);
			// ��������ж�
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
			
			// ��ɫ�е�����
			int center_x = x + 22;
			int center_y = y + 25;

			// ��ά������������
			int index_row = center_y / tile_height;
			// ��ά������������
			int index_col = center_x / tile_width;

			Log.i("x,y", "�� " + index_row + ",�� " + index_col);
			Log.i("���ĵ�����", "" + center_x + "," + center_y);
			// ��������ж�
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
