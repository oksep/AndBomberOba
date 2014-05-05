package com.bomb;

import android.app.Application;

public class MyApplication extends Application
{
	@Override
	public void onCreate() {
		super.onCreate();
		//构建全局异常捕获器
		BaseExceptionHandler defaultHandler = BaseExceptionHandler.getInstance();
		defaultHandler.init(getApplicationContext());
	}
}
