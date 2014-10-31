基于Android平台动态加在使用dex字节码的研究总结
======

## DexClassLoader的基本介绍

### 1. 构造函数

    DexClassLoader(
        String dexPath,           加在所需dex字节码文件的绝对路径
        String optimizedDirectory 字节码使用缓存目录
        String libraryPath        本地库目录列表，可以置成null
        ClassLoader parent        父ClassLoader，加载类时优先从改loader中查找
    )

使用时需注意，dexPath和optimizedDirectory不要使用同一路径，否则会造成load dex失败的错误。

### 2. 使用示例(Java)

    DexClassLoader loader = new DexClassLoader(path, cachePath, null, context.getClassLoader());
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

上述代码首先构造了一个DexClassLoader实例，通过上下文context获得的ClassLoader作为其父loader。然后利用反射装在"cn.jj.jar.JarImpl"类，调用newInstance方法生成相应实例obj，再通过反射来调用接口，先调用setJarString传递设置一个字符串，再调用getJarString来获取该字符串。

## Android下的ClassLoader机制

Android平台使用Dalvik虚拟机来解析dex字节码（Android 5.0已添加新的ART机制），它与普通的JVM类似，使用双亲代理模式，即装载一个类时，先由其parent来查找load，不存在再由自己load加载。
