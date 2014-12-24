package plugin.jj.component;

import android.content.Intent;

public class JJPluginIntent extends Intent {

    private String mPluginPackage;
    private String mPluginClass;

    public JJPluginIntent() {
        super();
    }

    public JJPluginIntent(String pluginPackage) {
        super();
        this.mPluginPackage = pluginPackage;
    }

    public JJPluginIntent(String pluginPackage, String pluginClass) {
        super();
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = pluginClass;
    }

    public JJPluginIntent(String pluginPackage, Class<?> clazz) {
        super();
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = clazz.getName();
    }

    public String getPluginPackage() {
        return mPluginPackage;
    }

    public void setPluginPackage(String pluginPackage) {
        this.mPluginPackage = pluginPackage;
    }

    public String getPluginClass() {
        return mPluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.mPluginClass = pluginClass;
    }

    public void setPluginClass(Class<?> clazz) {
        this.mPluginClass = clazz.getName();
    }
    
}
