package plugin.jj;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import plugin.jj.component.JJPluginIntent;
import plugin.jj.component.JJPluginProxyActivity;
import plugin.jj.utils.JJPluginConstants;
import dalvik.system.DexClassLoader;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * A singleton intance to manage load plugin and start plugin.
 * <p>
 * 
 * @author xujh
 */
public class JJPluginManager {

	/**
	 * return value of {@link #startPluginActivity(Activity, JJPluginIntent)}
	 * start success
	 */
	public static final int START_RESULT_SUCCESS = 0;

	/**
	 * return value of {@link #startPluginActivity(Activity, JJPluginIntent)}
	 * package not found
	 */
	public static final int START_RESULT_NO_PKG = 1;

	/**
	 * return value of {@link #startPluginActivity(Activity, JJPluginIntent)}
	 * class not found
	 */
	public static final int START_RESULT_NO_CLASS = 2;

	/**
	 * return value of {@link #startPluginActivity(Activity, JJPluginIntent)}
	 * class type error
	 */
	public static final int START_RESULT_TYPE_ERROR = 3;

	private static final String TAG = "JJPluginManager";
	private static JJPluginManager mInstance = null;
	private Context mContext = null;
	private HashMap<String, JJPluginEntity> mPluginHolder = new HashMap<String, JJPluginEntity>();
	private int mFrom = JJPluginConstants.FROM_INTERNAL;

	public JJPluginManager(Context context) {
		this.mContext = context;
	}

	public static JJPluginManager getInstance(Context context) {
		if (mInstance == null) {
			synchronized (JJPluginManager.class) {
				if (mInstance == null) {
					mInstance = new JJPluginManager(context);
				}
			}
		}
		return mInstance;
	}

	public JJPluginEntity loadPlugin(String pluginPath) {
		mFrom = JJPluginConstants.FROM_EXTERNAL;
		PackageManager pm = mContext.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(pluginPath,
				PackageManager.GET_ACTIVITIES);
		if (packageInfo == null) {
			Log.e(TAG, "loadPlugin packageInfo is null");
			return null;
		}
		String packageName = packageInfo.packageName;
		JJPluginEntity entity = mPluginHolder.get(packageName);
		Log.e(TAG, "loadPlugin packageName : " + packageName);
		if (entity == null) {
			DexClassLoader dexClassLoader = createDexClassLoader(pluginPath);
			AssetManager assetManager = createAssetManager(pluginPath);
			Resources resources = createResources(assetManager);
			entity = new JJPluginEntity(packageName, dexClassLoader,
					assetManager, resources, packageInfo);
			mPluginHolder.put(packageName, entity);
		}
		return entity;
	}

	private DexClassLoader createDexClassLoader(String dexPath) {
		File cacheDir = mContext.getDir("cache", Context.MODE_PRIVATE);
		String cachePath = cacheDir.getAbsolutePath();
		DexClassLoader loader = new DexClassLoader(dexPath, cachePath, null,
				mContext.getClassLoader());
		return loader;
	}

	private AssetManager createAssetManager(String dexPath) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager, dexPath);
			return assetManager;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Resources createResources(AssetManager assetManager) {
		Resources superRes = mContext.getResources();
		Resources resources = new Resources(assetManager,
				superRes.getDisplayMetrics(), superRes.getConfiguration());
		return resources;
	}

	public void startActivity() {

	}

	public int startPluginActivity(Context context, JJPluginIntent intent) {
		return startPluginActivityForResult(context, intent, -1);
	}

	public int startPluginActivityForResult(Context context, JJPluginIntent intent, int requestCode) {
		if (mFrom == JJPluginConstants.FROM_INTERNAL) {
			intent.setClassName(context, intent.getPluginClass());
			performStartActivityForResult(context, intent, requestCode);
			return JJPluginManager.START_RESULT_SUCCESS;
		}

		String packageName = intent.getPluginPackage();
		if (packageName == null) {
			throw new NullPointerException("disallow null packageName.");
		}
		JJPluginEntity entity = getPluginEntity(packageName);
		if (entity == null) {
			return START_RESULT_NO_PKG;
		}

		DexClassLoader classLoader = entity.getDexLoader();
		String className = intent.getPluginClass();
		className = (className == null ? entity.getDefaultActivity() : className);
		if (className.startsWith(".")) {
			className = packageName + className;
		}
		Log.d(TAG, "startPluginActivityForResult className : " + className);
		Class<?> clazz = null;
		try {
			clazz = classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return START_RESULT_NO_CLASS;
		}

		Class<? extends Activity> activityClass = JJPluginProxyActivity.class;
		// if (DLBasePluginActivity.class.isAssignableFrom(clazz)) {
		// activityClass = DLProxyActivity.class;
		// } else if
		// (DLBasePluginFragmentActivity.class.isAssignableFrom(clazz)) {
		// activityClass = DLProxyFragmentActivity.class;
		// } else {
		// return START_RESULT_TYPE_ERROR;
		// }

		intent.putExtra(JJPluginConstants.EXTRA_CLASS, className);
		intent.putExtra(JJPluginConstants.EXTRA_PACKAGE, packageName);
		intent.setClass(mContext, activityClass);
		performStartActivityForResult(context, intent, requestCode);
		return START_RESULT_SUCCESS;
	}

	private void performStartActivityForResult(Context context,
			JJPluginIntent intent, int requestCode) {
		if (context instanceof Activity) {
			((Activity) context).startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

	public JJPluginEntity getPluginEntity(String packageName) {
		return mPluginHolder.get(packageName);
	}

}
