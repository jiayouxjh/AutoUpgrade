package com.example.dextest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String TAG = "DexTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initDir(this);
		
//		getJarVersion();
		loadAssetsPlugin(this);
		setDexText(this);
	}
	
	private void initDir(Context context) {
		String localPath = context.getFilesDir().getAbsolutePath();
		File cache = new File(localPath + "/cache");
		if (!cache.exists()) {
			cache.mkdir();
		}
		File plugin = new File(localPath + "/plugin");
		if (!plugin.exists()) {
			plugin.mkdir();
		}
	}

	private String getJarVersion() {
		String ver = "0";
		AssetManager am = this.getAssets();
		String[] assetsList = null;
		try {
			assetsList = am.list("");
			for(int i = 0; i < assetsList.length; i++) {
				Log.v(TAG, "assets i : " + i + ", file : " + assetsList[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File file = new File("file:///android_asset/jar_test.dex");
		Log.e(TAG, "jar_tes.dex is exist : " + file.exists());
		
		return ver;
	}
	
	private void setDexText(Context context) {
		String dexStr = "load dex fail";
		String localPath = context.getFilesDir().getAbsolutePath() + "/plugin/jar_test.dex";
		String extPath = "/mnt/sdcard/jar_test.dex";
		String path = localPath;
		Log.v(TAG, "read path : " + path);
		File dexFile = new File(path);
		if (dexFile.exists()) {
			DexClassLoader loader = new DexClassLoader(path, context.getFilesDir().getAbsolutePath() + "/cache", null, context.getClassLoader());
			Class<?> clazz;
			try {
				clazz = loader.loadClass("cn.jj.jar.JarImpl");
				Object obj = clazz.newInstance();
				Method dexMethod = clazz.getMethod("setJarString", String.class);
				dexMethod.invoke(obj, "hello");
				dexMethod = clazz.getMethod("getJarString", null);
				dexStr = (String) dexMethod.invoke(obj, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "dex not found in : " + path);
		}
		TextView textView = (TextView)findViewById(R.id.dex);
		textView.setText(dexStr);
	}
	
	private void loadAssetsPlugin(Context context) {
		AssetManager am = context.getAssets();
		try {
			String[] list = am.list("plugin");
			for (int i = 0; i < list.length; i++) {
				Log.v(TAG, "list[" + i + "] : " + list[i]);
				InputStream is = am.open("plugin/" + list[i]);
				String path = context.getFilesDir().getAbsolutePath() + "/plugin/" + list[i];
				Log.v(TAG, "write path : " + path);
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file, false);
				byte[] buffer = new byte[1024];
				while (true) {
					int len = is.read(buffer);
					if (len == -1) {
						break;
					}
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				Log.v(TAG, "write end");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
