package com.example.loadtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TestActivity extends Activity {
	
	public native static String stringFromJNI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		String jniStr = stringFromJNI();
		TextView tv = (TextView)findViewById(R.id.text);
		tv.setText(jniStr);
	}
	
	
	static {
		System.load("/mnt/sdcard/libhello-jni.so");
		System.loadLibrary("hello-jni");
	}
	
}
