package com.bomb;

import android.app.Application;

public class MyApplication extends Application
{
	@Override
	public void onCreate() {
		super.onCreate();
		//����ȫ���쳣������
		BaseExceptionHandler defaultHandler = BaseExceptionHandler.getInstance();
		defaultHandler.init(getApplicationContext());
	}
}
