package com.liuqi.springbootsecurityredisnginx.utils;

import com.liuqi.springbootsecurityredisnginx.SpringbootSecurityRedisNginxApplication;
import com.liuqi.springbootsecurityredisnginx.core.JarResourceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

/**
 * 类说明 <br>
 *     类操作工具类
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:30 AM 2020/3/6
 */
public class ClassUtil {

    private static final Logger log = LogManager.getLogger(ClassUtil.class);

    /**
     * java 类后缀名
     */
    private String CLASS_FILE_SUFFIX = ".class";

    /**
     * 包 类路径分隔符
     */
    private String PACKAGE_SEPARATOR = ".";

    /**
     * 包 路径分隔符
     */
    private String PACKAGE_PATH_SEPARATOR = "/";

    /**
     * 默认查找 jar class 的路径
     */
    private String JAR_FILE_FIND_PRE = "classpath*:";

    /**
     * 默认查找 jar class 的规则
     */
    private String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private ResourceLoader resourceLoader;

    /**
     * spring 获取路径的 resource 处理器
     */
    PathMatchingResourcePatternResolver resourcePatternResolver;

    /**
     * classLoder 默认为当前类的 classLoader
     */
    private ClassLoader classLoader;

    /**
     * 文件协议 file
     */
    private static final String FILE_PROTOCOL = "file";

    /**
     * 文件协议 jar
     */
    private static final String JAR_PROTOCOL = "jar";

    private static final boolean logDebugEnabled = log.isDebugEnabled();

    private static final boolean logTraceEnabled = log.isTraceEnabled();

    private ClassUtil(){}

    public static ClassUtil getInstance(){
        return ClassUtilHook.instance;
    }

    private static class ClassUtilHook {
        private static ClassUtil instance = new ClassUtil();
    }

    public void setClassLoader(ClassLoader classLoader){

        log.info("设置VanasUtil.ClassUtil classLoader : {}",classLoader.getClass().getName());
        this.classLoader = classLoader;
    }

    /**
     * 该方法通过传入的包对象，获取该包对象下，所有的 class 类对象
     *
     * @param pkg {@link Package}
     * @return {@link List}&lt;Class&lt;?&gt;&gt;
     */
    public List<Class<?>> getClasses(Package pkg){
        String packageName = pkg.getName();
        return getClasses(packageName);
    }

    /**
     * 该方法通过传入的包名称，获取该包对象下，所有的 class 类对象
     * 包名必须是 xx.xx.xx.xx 的结构, 分隔符为 "." , "*" 代表匿名匹配所有 <br>
     *
     * @param pkgName 包名
     * @return {@link List}&lt;Class&lt;?&gt;&gt;
     */
    public List<Class<?>> getClasses(String pkgName){
        return this.getClasses(pkgName , null);
    }

    /**
     * 该方法通过传入的包名称，获取该包对象下，所有的 class 类对象<br>
     * 并通过 annotationClass 获取带有对应注释的类<br>
     * 包名必须是 <i>xx.xx.xx.xx</i> 的结构, 分隔符为 "." , "*" 代表匿名匹配所有 <br>
     *
     * @param pkgName 包名
     * @param annotationClass 注释名
     * @return {@link List}&lt;Class&lt;?&gt;&gt;
     */
    public List<Class<?>> getClasses(String pkgName , Class annotationClass){
        return this.findClassWithPackageName(pkgName , annotationClass);
    }

    /**
     * 通过包名查找所有 class
     *
     * @param packageName 包名
     * @param annotationClass 注释类
     * @return 待注释的 class 类
     */
    private List<Class<?>> findClassWithPackageName(String packageName , Class annotationClass){

        if(annotationClass == null){
            logDetail("findClassWithPackageName 查找 [{}] 内所有类.",packageName);
        }else{
            logDetail("findClassWithPackageName 查找 [{}] 内，带 [{}] 注解的类.",packageName , annotationClass.getName());
        }

        // 返回值
        List<Class<?>> classes = new ArrayList<Class<?>>();

        // 替换包结构
        String packageDirName = convertClassPathToResourcePath(packageName);

        Enumeration<URL> urls;
        URL url;
        try {
            urls = this.getClassLoader().getResources(packageDirName);

            log.info("findClassWithPackageName 获取 ["+packageDirName+"] 下 resource URL : ",urls);

            if(urls == null){
                log.warn("findClassWithPackageName [{}] 包下不存在 Resource。请核实路径.", packageName);
            }

            // 循环迭代下去
            while (AssertUtil.notNull(urls).hasMoreElements()) {

                url = urls.nextElement();

                // 获取协议
                String protocol = url.getProtocol();

                log.info("findClassWithPackageName url [{}] , 文件协议 [{}] ",url.getPath() , protocol);

                logDetail("findClassWithPackageName 查找URL [{}] 下符合条件的 Class. START",url);

                if(protocol.equalsIgnoreCase(FILE_PROTOCOL)){
                    // 转化为路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    logDetail("findClassWithPackageName 转化 URL [{}] 为文件路径 [{}]",url , filePath);

                    // 过滤路径中的文件并转化为 class
                    classes.addAll(filterFile2Class(packageName , filePath , annotationClass));

                }else if(protocol.equalsIgnoreCase(JAR_PROTOCOL)){
                    // JAR 文件
//                    Resource[] resources = findAllClassPathResources(packageDirName);
                    classes.addAll(findAllClassFromJar(url.getPath(), packageDirName , annotationClass));
                }

                logDetail("findClassWithPackageName 查找URL [{}] 下符合条件的 Class. END",url);


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return classes;
    }


    /**
     * 过滤文件，将 java 文件转化为 Class
     * @param packageName 包名
     * @param filePath 文件路径
     * @throws ClassNotFoundException 如果文件找不到将抛出该异常
     */
    private List<Class<?>> filterFile2Class(String packageName ,String filePath, Class annotationClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        filterFile2ClassDo(packageName, filePath,classes , annotationClass);

        return classes;
    }

    private void filterFile2ClassDo(String packageName ,String filePath, List<Class<?>> classes , Class annotationClass) throws ClassNotFoundException {
        File file = new File(filePath);
        if(!file.exists() || !file.isDirectory()){
            log.warn("包 {} 不存在，或者不是文件夹。",packageName);
            return;
        }

        // 获取所有文件夹 和 class 文件
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File childrenFile) {
                return (childrenFile.isDirectory() || childrenFile.getName().endsWith(CLASS_FILE_SUFFIX));
            }
        });

        if(files == null || files.length == 0 ){
            logDetail("包 {} 内没有文件，执行下一个",packageName);
            return;
        }

        String className;
        String tempPackageName;
        Class targetClass;

        for(File f : files){

            if(f.isDirectory()){

                tempPackageName = packageName + PACKAGE_SEPARATOR + f.getName();
                logDetail("包 {} 内 {} 是文件夹，路径 = {}，递归搜索.",packageName , f.getName() , tempPackageName);
                filterFile2ClassDo(tempPackageName , f.getAbsolutePath() , classes , annotationClass);
            }else{

                className = f.getName().substring(0 , f.getName().length() - CLASS_FILE_SUFFIX.length());

                // 转化 classLoader ，封装成 jar 包后 ，外部调用 jar 类函数。
                // 可能会 classLoader 不一致。不同的 classLoader 会导致自定义注解不适配
                // 重新将注解类 用当前类 classLoader 加载，保证 classLoader 一致
                targetClass = getClassLoader().loadClass(packageName + "." + className);
//                targetClass = Class.forName(packageName + "." + className);

                if(annotationClass == null){
                    classes.add(targetClass);
                    log.info("包 {} 内 Class : {} , 符合条件。",packageName , className);
                }else{
                    annotationClass = getClassLoader().loadClass(annotationClass.getName());
                    // 判断是否有该注释
                    Annotation annotation = targetClass.getAnnotation(annotationClass);
                    if(annotation != null){
                        classes.add(targetClass);
                        log.info("包 {} 内 Class : {} 带有 {} 注解, 符合条件。",packageName , className , annotationClass.getName());
                    }
                }

            }

        }
    }

    private Resource[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (location.startsWith("/")) {
            path = location.substring(1);
            logDetail("findAllClassPathResources convert location [{}] to [{}] ",location ,path );
        }
        String packageSearchPath = JAR_FILE_FIND_PRE + path + DEFAULT_RESOURCE_PATTERN;
        logDetail("findAllClassPathResources find resources in [{}] ",packageSearchPath);
        return getResourcePatternResolver().getResources(packageSearchPath);
    }

    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }

    private List<Class<?>> findAllClassFromJar(String jarFilePath,String packageDirName, Class<?> findAnno) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String jarFileUrl , rootEntryPath;
        JarFile jarFile = null;

        try {
            int separatorIndex = jarFilePath.indexOf("*/");

            logDetail("findAllClassFromResources jarFilePath [{}] index of '*/' = {} " ,jarFilePath , separatorIndex);

            if (separatorIndex == -1) {
                separatorIndex = jarFilePath.indexOf("!/");

                logDetail("findAllClassFromResources jarFilePath [{}] index of '!/' = {} " ,jarFilePath , separatorIndex);
            }


            if (separatorIndex != -1) {
                // 截取 jar 路径
                jarFileUrl = jarFilePath.substring(0, separatorIndex);
                // 截取当前调用的类路径
                rootEntryPath = jarFilePath.substring(separatorIndex + 2);
                jarFile = getJarFile(jarFileUrl);
            } else {
                jarFile = new JarFile(jarFilePath);
                jarFileUrl = jarFilePath;
                rootEntryPath = "";
            }

            logDetail("findAllClassFromResources jarFileUrl [{}] rootEntryPath = [{}] " ,jarFileUrl , rootEntryPath);

        } catch (ZipException var17) {
            logDetail("Skipping invalid jar classpath entry [" + jarFilePath + "]");

        }

        return resolverJarFileFindClass(jarFile , packageDirName , findAnno);
    }

    private List<Class<?>> resolverJarFileFindClass(JarFile jarFile, String findClassPattern, Class<?> findAnnotation) throws ClassNotFoundException, SecurityException, IllegalArgumentException {

        List<Class<?>> classes = new ArrayList<>();

        if(jarFile == null){
            log.warn("resolverJarFileFindClass jarFile is null ,Return.");
            return classes;
        }

        Enumeration<JarEntry> files  = jarFile.entries();
        JarResourceData resourceData;
        while (files.hasMoreElements()) {

            resourceData = convertToResourceData(files.nextElement());

            if(!regxResourceIsClass(resourceData)){
                logDetail("resolverJarFileFindClass resource [{}] is not class ,Return.",resourceData.getPath());
                continue;
            }

            if(!regxResourceIsFind(resourceData , findClassPattern)){
                logDetail("resolverJarFileFindClass resource [{}] is not find ,Return.",resourceData.getPath());
                continue;
            }

            String filePath = this.convertResourcePathToClassPath(resourceData.getFilePath());
            logDetail("resolverJarFileFindClass filePath [{}]",filePath);
            Class<?> classz = this.getClassLoader().loadClass(filePath);

            if(resolverClassHasAnnotation(classz, findAnnotation))
                classes.add(classz);
        }

        return classes;
    }

    private JarResourceData convertToResourceData(JarEntry entry) {

        logDetail("resolverResourceIsFind jarEntry name [{}]",entry.getName());

        return new JarResourceData(entry.getName());
    }

    /**
     * 获取jarFile
     * @param jarFileUrl 路径
     * @return
     * @throws IOException
     */
    private static JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith("file:")) {
            try {
                return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
            } catch (URISyntaxException var3) {
                return new JarFile(jarFileUrl.substring("file:".length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }


    private boolean regxResourceIsClass(JarResourceData resourceData){

        return resourceData.getFileType() != null ? resourceData.getFileType().equalsIgnoreCase("class") : Boolean.FALSE;

    }

    private boolean regxResourceIsFind(JarResourceData resourceData , String findClassPattern){
        return resourceData.getPath().contains(findClassPattern);
    }

    /**
     * 解析 class 是否包含对应的注解类
     * @param classz class
     * @param annotationClassz 注解类 class
     * @return boolean
     */
    private boolean resolverClassHasAnnotation(Class<?> classz , Class annotationClassz){
        Annotation annotation = classz.getAnnotation(annotationClassz);
        if(annotation != null){
            logDetail("resolverClassHasAnnotation class [{}] has Annotation [{}]",classz.getName(),annotationClassz.getName());
            return true;
        }
        return false;
    }

    /**
     * 转化资源路径为 class 路径
     * <pre>
     *     比如 com/liuqi/app 转化为 com.liuqi.app
     * </pre>
     * @param path 资源路径
     * @return 转化后的路径
     */
    private String convertResourcePathToClassPath(String path){
        String pathTo = path.replace(PACKAGE_PATH_SEPARATOR,PACKAGE_SEPARATOR);
        logDetail("convertResourcePathToClassPath 替换路径 [{}] 为 [{}]",path , pathTo);
        return pathTo;
    }

    /**
     * 转化class 路径为资源路径
     * <pre>
     *     比如 com.liuqi.app 转化为 com/liuqi/app
     * </pre>
     * @param path 资源路径
     * @return 转化后的路径
     */
    private String convertClassPathToResourcePath(String path){
        String pathTo = path.replace(PACKAGE_SEPARATOR,PACKAGE_PATH_SEPARATOR);

        logDetail("convertClassPathToResourcePath 替换路径 [{}] 为 [{}]",path , pathTo);
        return pathTo;
    }

    protected Resource convertClassLoaderURL(URL url) {
        return new UrlResource(url);
    }

    private Resource[] getJarResources(String pkg) throws IOException {
        String packageSearchPath = JAR_FILE_FIND_PRE + pkg + DEFAULT_RESOURCE_PATTERN;
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources(packageSearchPath);
    }


    private ClassLoader getClassLoader() {
        ClassLoader classLoader;
        log.info("获取 ClassLoader.");
        if(this.classLoader == null){
            classLoader = this.getClass().getClassLoader();
            log.info("获取 ClassLoader. 使用当前默认 classLoader . {}",classLoader.getClass());
        }else{
            classLoader = (URLClassLoader) SpringbootSecurityRedisNginxApplication.class.getClassLoader();
            log.info("获取 ClassLoader. 使用 URLClassLoader . {}",classLoader.getClass());
        }

        return classLoader;
    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }


    private void logDetail(String msg , Object... args){

        if(logTraceEnabled){
            log.trace(msg,args);
        }

        if(logDebugEnabled){
            log.info(msg,args);
        }

    }

}
