package plugin.jj;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

/**
 * A entity for plugin which contain DexClassLoader, AssetManager, Resources and
 * PackageInfo.
 * <p>
 * 
 * @author xujh
 */
public class JJPluginEntity {

	private String mPackageName = null;
	private String mDefaultActivity = null;
	private DexClassLoader mDexLoader = null;
	private AssetManager mAssetManager = null;
	private Resources mResources = null;
	private PackageInfo mPackageInfo = null;

	public JJPluginEntity(String packageName, DexClassLoader loader,
			AssetManager assetManager, Resources resources,
			PackageInfo packageInfo) {
		this.mPackageName = packageName;
		this.mDexLoader = loader;
		this.mAssetManager = assetManager;
		this.mResources = resources;
		this.mPackageInfo = packageInfo;
	}
	
	public String getDefaultActivity() {
        if (mPackageInfo.activities != null && mPackageInfo.activities.length > 0) {
            mDefaultActivity = mPackageInfo.activities[0].name;
        }
        return mDefaultActivity;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public void setPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

	public DexClassLoader getDexLoader() {
		return mDexLoader;
	}

	public void setDexLoader(DexClassLoader mDexLoader) {
		this.mDexLoader = mDexLoader;
	}

	public AssetManager getAssetManager() {
		return mAssetManager;
	}

	public void setAssetManager(AssetManager mAssetManager) {
		this.mAssetManager = mAssetManager;
	}

	public Resources getResources() {
		return mResources;
	}

	public void setResources(Resources mResources) {
		this.mResources = mResources;
	}

	public PackageInfo getPackageInfo() {
		return mPackageInfo;
	}

	public void setPackageInfo(PackageInfo mPackageInfo) {
		this.mPackageInfo = mPackageInfo;
	}

}
