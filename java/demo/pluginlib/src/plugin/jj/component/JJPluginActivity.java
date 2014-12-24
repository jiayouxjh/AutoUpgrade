package plugin.jj.component;

import plugin.jj.JJPlugin;
import plugin.jj.JJPluginEntity;
import plugin.jj.JJPluginManager;
import plugin.jj.utils.JJPluginConstants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class JJPluginActivity extends Activity implements JJPlugin {

    private static final String TAG = "JJPluginActivity";

    /**
     * Proxy activity, could used to be context, whether point to this depends on requirement.
     */
    protected Activity mProxyActivity;

    /**
     * Same as mProxyActivity, counld used to be this.
     */
    protected Activity that;
    protected JJPluginManager mPluginManager;
    protected JJPluginEntity mPluginEntity;

    protected int mFrom = JJPluginConstants.FROM_INTERNAL;

    @Override
    public void attach(Activity proxyActivity, JJPluginEntity entity) {
        Log.d(TAG, "attach: proxyActivity= " + proxyActivity);
        mProxyActivity = (Activity)proxyActivity;
        that = mProxyActivity;
        mPluginEntity = entity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(JJPluginConstants.FROM, JJPluginConstants.FROM_INTERNAL);
        }
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
            that = mProxyActivity;
        }

        mPluginManager = JJPluginManager.getInstance(that);
        Log.d(TAG, "onCreate: from= " + 
        		(mFrom == JJPluginConstants.FROM_INTERNAL ? "JJPluginConstants.FROM_INTERNAL" : "JJPluginConstants.FROM_EXTERNAL"));
    }

    @Override
    public void setContentView(View view) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Intent getIntent() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getIntent();
        } else {
            return mProxyActivity.getIntent();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getClassLoader();
        } else {
            return mProxyActivity.getClassLoader();
        }
    }

    @Override
    public Resources getResources() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getResources();
        } else {
            return mProxyActivity.getResources();
        }
    }

    @Override
    public String getPackageName() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getPackageName();
        } else {
            return mPluginEntity.getPackageName();
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getLayoutInflater();
        } else {
            return mProxyActivity.getLayoutInflater();
        }
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getMenuInflater();
        } else {
            return mProxyActivity.getMenuInflater();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mProxyActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getApplicationContext();
        } else {
            return mProxyActivity.getApplicationContext();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getWindowManager();
        } else {
            return mProxyActivity.getWindowManager();
        }
    }

    @Override
    public Window getWindow() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getWindow();
        } else {
            return mProxyActivity.getWindow();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.getSystemService(name);
        } else {
            return mProxyActivity.getSystemService(name);
        }
    }

    @Override
    public void finish() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.finish();
        } else {
            mProxyActivity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onRestart();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onSaveInstanceState(outState);
        }
    }

    public void onNewIntent(Intent intent) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
    	Log.d(TAG, "JJPluginActivity onResume mFrom : " + mFrom);
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onWindowAttributesChanged(params);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFrom == JJPluginConstants.FROM_INTERNAL) {
            return onOptionsItemSelected(item);
        }
        return false;
    }

    /**
     * @param intent
     * @return may be {@link #START_RESULT_SUCCESS}, {@link #START_RESULT_NO_PKG},
     *    {@link #START_RESULT_NO_CLASS}, {@link #START_RESULT_TYPE_ERROR}
     */
    public int startPluginActivity(JJPluginIntent intent) {
        return startPluginActivityForResult(intent, -1);
    }

    public int startPluginActivityForResult(JJPluginIntent intent, int requestCode) {
        if (mFrom == JJPluginConstants.FROM_EXTERNAL) {
            if (intent.getPluginPackage() == null) {
            	intent.setPluginPackage(mPluginEntity.getPackageName());
            }
        }
        return mPluginManager.startPluginActivityForResult(that, intent, requestCode);
    }

}
