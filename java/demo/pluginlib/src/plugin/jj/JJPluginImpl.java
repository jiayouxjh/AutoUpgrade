package plugin.jj;

import java.lang.reflect.Constructor;

import plugin.jj.utils.JJPluginConstants;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;

/**
 * The implemetion for proxy activity to attach remote activity.
 * <p>
 * 
 * @author xujh
 */
public class JJPluginImpl {

	private static final String TAG = "JJPluginImpl";

	private String mClass = null;
	private String mPackageName = null;

	private Activity mActivity = null;
	private ActivityInfo mActivityInfo = null;
	private JJPluginManager mPluginManager = null;
	private JJPluginEntity mPluginEntity = null;
	private AssetManager mAssetManager = null;
	private Resources mResources = null;
	private Theme mTheme = null;
	protected JJPlugin mRemoteActivity = null;

	public JJPluginImpl(Activity activity) {
		this.mActivity = activity;
	}

	public void onCreate(Intent intent) {
		mPackageName = intent.getStringExtra(JJPluginConstants.EXTRA_PACKAGE);
		mClass = intent.getStringExtra(JJPluginConstants.EXTRA_CLASS);
		mPluginManager = JJPluginManager.getInstance(mActivity);
		mPluginEntity = mPluginManager.getPluginEntity(mPackageName);
		mAssetManager = mPluginEntity.getAssetManager();
		mResources = mPluginEntity.getResources();

		initActivityInfo();
		handleActivityInfo();
		launchTargetActivity();
	}

	private void handleActivityInfo() {
		if (mActivityInfo.theme > 0) {
			mActivity.setTheme(mActivityInfo.theme);
		}
		Theme superTheme = mActivity.getTheme();
		mTheme = mResources.newTheme();
		mTheme.setTo(superTheme);
	}

	private void initActivityInfo() {
		PackageInfo packageInfo = mPluginEntity.getPackageInfo();
		if ((packageInfo.activities != null)
				&& (packageInfo.activities.length > 0)) {
			if (mClass == null) {
				mClass = packageInfo.activities[0].name;
			}
			for (ActivityInfo a : packageInfo.activities) {
				if (a.name.equals(mClass)) {
					mActivityInfo = a;
				}
			}
		}
	}

	protected void launchTargetActivity() {
		try {
			Class<?> localClass = getClassLoader().loadClass(mClass);
			Constructor<?> localConstructor = localClass
					.getConstructor(new Class[] {});
			Object instance = localConstructor.newInstance(new Object[] {});
			mRemoteActivity = (JJPlugin) instance;
			((JJProxy) mActivity).attach(mRemoteActivity, mPluginManager);

			mRemoteActivity.attach(mActivity, mPluginEntity);

			Bundle bundle = new Bundle();
			bundle.putInt(JJPluginConstants.FROM,
					JJPluginConstants.FROM_EXTERNAL);
			mRemoteActivity.onCreate(bundle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ClassLoader getClassLoader() {
		return mPluginEntity.getDexLoader();
	}

	public AssetManager getAssets() {
		return mAssetManager;
	}

	public Resources getResources() {
		return mResources;
	}

	public Theme getTheme() {
		return mTheme;
	}

	public JJPlugin getRemoteActivity() {
		return mRemoteActivity;
	}

	public interface JJProxy {
		void attach(JJPlugin remoteActivity, JJPluginManager pluginManager);
	}

}
