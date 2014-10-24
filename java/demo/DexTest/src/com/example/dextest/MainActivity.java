package com.example.dextest;

import java.io.File;
import java.lang.reflect.Method;

//import cn.jj.jar.JarImpl;
import dalvik.system.DexClassLoader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String TAG = "DexTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setDexText(this);
//		setJarText(this);
	}
	
//	private void setJarText(Context context) {
//		JarImpl impl = new JarImpl();
//		String jarStr = impl.jarString();
//		TextView textView = (TextView)findViewById(R.id.jar);
//		textView.setText(jarStr);
//	}

	private void setDexText(Context context) {
		String dexStr = "no success";
		String path = "/mnt/sdcard/test.dex";
		File dexFile = new File(path);
		if (dexFile.exists()) {
			DexClassLoader loader = new DexClassLoader(path, context.getFilesDir().getAbsolutePath(), null, context.getClassLoader());
			Class<?> libProviderClazz;
			try {
				libProviderClazz = loader.loadClass("cn.jj.jar.JarImpl");
				Method[] methods = libProviderClazz.getDeclaredMethods();
				for(int i = 0; i < methods.length; i++) {
					Log.v(TAG, "method " + i + " is : " + methods[i]);
				}
				Method dexMethod = libProviderClazz.getDeclaredMethod("jarString", null);
				dexMethod.setAccessible(true);
				dexStr = (String) dexMethod.invoke(libProviderClazz.newInstance(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "dex not found in : " + path);
		}
		TextView textView = (TextView)findViewById(R.id.dex);
		textView.setText(dexStr);
	}
	
}
