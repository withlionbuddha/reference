// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.equinox.launcher;

import java.net.URLStreamHandlerFactory;
import java.util.Collection;
import java.util.Collections;
import java.security.AllPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.Date;
import java.nio.file.Path;
import java.nio.file.CopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Locale;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Policy;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.StringJoiner;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Properties;
import java.net.URL;

public class Main
{
    protected boolean debug;
    protected String bootLocation;
    protected URL installLocation;
    protected URL configurationLocation;
    protected String parentConfigurationLocation;
    protected String framework;
    protected String devClassPath;
    private Properties devClassPathProps;
    protected boolean inDevelopmentMode;
    protected String os;
    protected String ws;
    protected String arch;
    private String library;
    private String exitData;
    private String vm;
    private String[] vmargs;
    private String[] commands;
    String[] extensionPaths;
    JNIBridge bridge;
    private boolean showSplash;
    private String splashLocation;
    private String endSplash;
    private boolean initialize;
    protected boolean splashDown;
    private final Thread splashHandler;
    public static final String SPLASH_HANDLE = "org.eclipse.equinox.launcher.splash.handle";
    public static final String SPLASH_LOCATION = "org.eclipse.equinox.launcher.splash.location";
    private static final String FRAMEWORK = "-framework";
    private static final String INSTALL = "-install";
    private static final String INITIALIZE = "-initialize";
    private static final String VM = "-vm";
    private static final String VMARGS = "-vmargs";
    private static final String DEBUG = "-debug";
    private static final String DEV = "-dev";
    private static final String CONFIGURATION = "-configuration";
    private static final String NOSPLASH = "-nosplash";
    private static final String SHOWSPLASH = "-showsplash";
    private static final String EXITDATA = "-exitdata";
    private static final String NAME = "-name";
    private static final String LAUNCHER = "-launcher";
    private static final String PROTECT = "-protect";
    private static final String PROTECT_MASTER = "master";
    private static final String PROTECT_BASE = "base";
    private static final String LIBRARY = "--launcher.library";
    private static final String APPEND_VMARGS = "--launcher.appendVmargs";
    private static final String OVERRIDE_VMARGS = "--launcher.overrideVmargs";
    private static final String NL = "-nl";
    private static final String ENDSPLASH = "-endsplash";
    private static final String[] SPLASH_IMAGES;
    private static final String CLEAN = "-clean";
    private static final String NOEXIT = "-noExit";
    private static final String OS = "-os";
    private static final String WS = "-ws";
    private static final String ARCH = "-arch";
    private static final String STARTUP = "-startup";
    private static final String OSGI = "org.eclipse.osgi";
    private static final String STARTER = "org.eclipse.core.runtime.adaptor.EclipseStarter";
    private static final String PLATFORM_URL = "platform:/base/";
    private static final String ECLIPSE_PROPERTIES = "eclipse.properties";
    private static final String FILE_SCHEME = "file:";
    protected static final String REFERENCE_SCHEME = "reference:";
    protected static final String JAR_SCHEME = "jar:";
    private static final String CONFIG_DIR = "configuration/";
    private static final String CONFIG_FILE = "config.ini";
    private static final String CONFIG_FILE_TEMP_SUFFIX = ".tmp";
    private static final String CONFIG_FILE_BAK_SUFFIX = ".bak";
    private static final String ECLIPSE = "eclipse";
    private static final String PRODUCT_SITE_MARKER = ".eclipseproduct";
    private static final String PRODUCT_SITE_ID = "id";
    private static final String PRODUCT_SITE_VERSION = "version";
    private static final String PROP_USER_HOME = "user.home";
    private static final String PROP_USER_DIR = "user.dir";
    private static final String PROP_INSTALL_AREA = "osgi.install.area";
    private static final String PROP_CONFIG_AREA = "osgi.configuration.area";
    private static final String PROP_CONFIG_AREA_DEFAULT = "osgi.configuration.area.default";
    private static final String PROP_BASE_CONFIG_AREA = "osgi.baseConfiguration.area";
    private static final String PROP_SHARED_CONFIG_AREA = "osgi.sharedConfiguration.area";
    private static final String PROP_CONFIG_CASCADED = "osgi.configuration.cascaded";
    protected static final String PROP_FRAMEWORK = "osgi.framework";
    private static final String PROP_SPLASHPATH = "osgi.splashPath";
    private static final String PROP_SPLASHLOCATION = "osgi.splashLocation";
    private static final String PROP_CLASSPATH = "osgi.frameworkClassPath";
    private static final String PROP_EXTENSIONS = "osgi.framework.extensions";
    private static final String PROP_FRAMEWORK_SYSPATH = "osgi.syspath";
    private static final String PROP_FRAMEWORK_SHAPE = "osgi.framework.shape";
    private static final String PROP_LOGFILE = "osgi.logfile";
    private static final String PROP_REQUIRED_JAVA_VERSION = "osgi.requiredJavaVersion";
    private static final String PROP_PARENT_CLASSLOADER = "osgi.parentClassloader";
    private static final String PROP_FRAMEWORK_PARENT_CLASSLOADER = "osgi.frameworkParentClassloader";
    private static final String PROP_NL = "osgi.nl";
    static final String PROP_NOSHUTDOWN = "osgi.noShutdown";
    private static final String PROP_DEBUG = "osgi.debug";
    private static final String PROP_OS = "osgi.os";
    private static final String PROP_WS = "osgi.ws";
    private static final String PROP_ARCH = "osgi.arch";
    private static final String PROP_EXITCODE = "eclipse.exitcode";
    private static final String PROP_EXITDATA = "eclipse.exitdata";
    private static final String PROP_LAUNCHER = "eclipse.launcher";
    private static final String PROP_LAUNCHER_NAME = "eclipse.launcher.name";
    private static final String PROP_VM = "eclipse.vm";
    private static final String PROP_VMARGS = "eclipse.vmargs";
    private static final String PROP_COMMANDS = "eclipse.commands";
    private static final String PROP_ECLIPSESECURITY = "eclipse.security";
    private static final String READ_ONLY_AREA_SUFFIX = ".readOnly";
    private static final String NONE = "@none";
    private static final String NO_DEFAULT = "@noDefault";
    private static final String USER_HOME = "@user.home";
    private static final String USER_DIR = "@user.dir";
    private static final String INSTALL_HASH_PLACEHOLDER = "@install.hash";
    private static final String LAUNCHER_DIR = "@launcher.dir";
    private static final String PARENT_CLASSLOADER_APP = "app";
    private static final String PARENT_CLASSLOADER_EXT = "ext";
    private static final String PARENT_CLASSLOADER_BOOT = "boot";
    private static final String PARENT_CLASSLOADER_CURRENT = "current";
    protected static final String SESSION = "!SESSION";
    protected static final String ENTRY = "!ENTRY";
    protected static final String MESSAGE = "!MESSAGE";
    protected static final String STACK = "!STACK";
    protected static final int ERROR = 4;
    protected static final String PLUGIN_ID = "org.eclipse.equinox.launcher";
    protected File logFile;
    protected BufferedWriter log;
    protected boolean newSession;
    private boolean protectBase;
    public static final String VARIABLE_DELIM_STRING = "$";
    public static final char VARIABLE_DELIM_CHAR = '$';
    private static final long NO_TIMESTAMP = -1L;
    private static final String BASE_TIMESTAMP_FILE_CONFIGINI = ".baseConfigIniTimestamp";
    private static final String KEY_CONFIGINI_TIMESTAMP = "configIniTimestamp";
    private static final String PROP_IGNORE_USER_CONFIGURATION = "eclipse.ignoreUserConfiguration";
    
    static {
        SPLASH_IMAGES = new String[] { "splash.png", "splash.jpg", "splash.jpeg", "splash.gif", "splash.bmp" };
    }
    
    public Main() {
        this.debug = false;
        this.bootLocation = null;
        this.installLocation = null;
        this.configurationLocation = null;
        this.parentConfigurationLocation = null;
        this.framework = "org.eclipse.osgi";
        this.devClassPath = null;
        this.devClassPathProps = null;
        this.inDevelopmentMode = false;
        this.os = null;
        this.ws = null;
        this.arch = null;
        this.library = null;
        this.exitData = null;
        this.vm = null;
        this.vmargs = null;
        this.commands = null;
        this.extensionPaths = null;
        this.bridge = null;
        this.showSplash = false;
        this.splashLocation = null;
        this.endSplash = null;
        this.initialize = false;
        this.splashDown = false;
        this.splashHandler = new SplashHandler();
        this.logFile = null;
        this.log = null;
        this.newSession = true;
        this.protectBase = false;
    }
    
    private String getWS() {
        if (this.ws != null) {
            return this.ws;
        }
        final String osgiWs = System.getProperty("osgi.ws");
        if (osgiWs != null) {
            return this.ws = osgiWs;
        }
        final String osName = this.getOS();
        if (osName.equals("win32")) {
            return "win32";
        }
        if (osName.equals("linux")) {
            return "gtk";
        }
        if (osName.equals("macosx")) {
            return "cocoa";
        }
        if (osName.equals("hpux")) {
            return "gtk";
        }
        if (osName.equals("aix")) {
            return "gtk";
        }
        if (osName.equals("solaris")) {
            return "gtk";
        }
        if (osName.equals("qnx")) {
            return "photon";
        }
        return "unknown";
    }
    
    private String getOS() {
        if (this.os != null) {
            return this.os;
        }
        final String osgiOs = System.getProperty("osgi.os");
        if (osgiOs != null) {
            return this.os = osgiOs;
        }
        final String osName = System.getProperty("os.name");
        if (osName.regionMatches(true, 0, "win32", 0, 3)) {
            return "win32";
        }
        if (osName.equalsIgnoreCase("SunOS")) {
            return "solaris";
        }
        if (osName.equalsIgnoreCase("Linux")) {
            return "linux";
        }
        if (osName.equalsIgnoreCase("QNX")) {
            return "qnx";
        }
        if (osName.equalsIgnoreCase("AIX")) {
            return "aix";
        }
        if (osName.equalsIgnoreCase("HP-UX")) {
            return "hpux";
        }
        if (osName.equalsIgnoreCase("OS/400")) {
            return "os/400";
        }
        if (osName.equalsIgnoreCase("OS/390")) {
            return "os/390";
        }
        if (osName.equalsIgnoreCase("z/OS")) {
            return "z/os";
        }
        if (osName.regionMatches(true, 0, "Mac OS", 0, "Mac OS".length())) {
            return "macosx";
        }
        return "unknown";
    }
    
    private String getArch() {
        if (this.arch != null) {
            return this.arch;
        }
        final String osgiArch = System.getProperty("osgi.arch");
        if (osgiArch != null) {
            return this.arch = osgiArch;
        }
        final String name = System.getProperty("os.arch");
        if (name.equalsIgnoreCase("amd64")) {
            return "x86_64";
        }
        return name;
    }
    
    private String getFragmentString(final String fragmentOS, final String fragmentWS, final String fragmentArch) {
        final StringJoiner buffer = new StringJoiner(".");
        buffer.add("org.eclipse.equinox.launcher").add(fragmentWS).add(fragmentOS);
        if (!fragmentOS.equals("macosx") || "x86_64".equals(fragmentArch)) {
            buffer.add(fragmentArch);
        }
        return buffer.toString();
    }
    
    private void setupJNI(final URL[] defaultPath) {
        if (this.bridge != null) {
            return;
        }
        String libPath = null;
        if (this.library != null) {
            final File lib = new File(this.library);
            if (lib.isDirectory()) {
                libPath = this.searchFor("eclipse", lib.getAbsolutePath());
            }
            else if (lib.exists()) {
                libPath = lib.getAbsolutePath();
            }
        }
        if (libPath == null) {
            final String fragmentOS = this.getOS();
            final String fragmentWS = this.getWS();
            final String fragmentArch = this.getArch();
            libPath = this.getLibraryPath(this.getFragmentString(fragmentOS, fragmentWS, fragmentArch), defaultPath);
        }
        this.library = libPath;
        if (this.library != null) {
            this.bridge = new JNIBridge(this.library);
        }
    }
    
    private String getLibraryPath(final String fragmentName, final URL[] defaultPath) {
        String libPath = null;
        String fragment = null;
        if (this.inDevelopmentMode && this.devClassPathProps != null) {
            final String devPathList = this.devClassPathProps.getProperty("org.eclipse.equinox.launcher");
            final String[] locations = this.getArrayFromList(devPathList);
            if (locations.length > 0) {
                final File location = new File(locations[0]);
                if (location.isAbsolute()) {
                    final String dir = location.getParent();
                    fragment = this.searchFor(fragmentName, dir);
                    if (fragment != null) {
                        libPath = this.getLibraryFromFragment(fragment);
                    }
                }
            }
        }
        if (libPath == null && this.bootLocation != null) {
            final URL[] urls = defaultPath;
            if (urls != null && urls.length > 0) {
                for (int i = urls.length - 1; i >= 0 && libPath == null; --i) {
                    final File entryFile = new File(urls[i].getFile());
                    final String dir = entryFile.getParent();
                    if (this.inDevelopmentMode) {
                        final String devDir = String.valueOf(dir) + "/" + "org.eclipse.equinox.launcher" + "/fragments";
                        fragment = this.searchFor(fragmentName, devDir);
                    }
                    if (fragment == null) {
                        fragment = this.searchFor(fragmentName, dir);
                    }
                    if (fragment != null) {
                        libPath = this.getLibraryFromFragment(fragment);
                    }
                }
            }
        }
        if (libPath == null) {
            final URL install = this.getInstallLocation();
            String location2 = install.getFile();
            location2 = String.valueOf(location2) + "/plugins/";
            fragment = this.searchFor(fragmentName, location2);
            if (fragment != null) {
                libPath = this.getLibraryFromFragment(fragment);
            }
        }
        return libPath;
    }
    
    private String getLibraryFromFragment(String fragment) {
        if (fragment.startsWith("file:")) {
            fragment = fragment.substring(5);
        }
        final File frag = new File(fragment);
        if (!frag.exists()) {
            return null;
        }
        if (frag.isDirectory()) {
            return this.searchFor("eclipse", fragment);
        }
        ZipFile fragmentJar = null;
        try {
            fragmentJar = new ZipFile(frag);
        }
        catch (IOException e) {
            this.log("Exception opening JAR file: " + fragment);
            this.log(e);
            return null;
        }
        final Enumeration<? extends ZipEntry> entries = fragmentJar.entries();
        String entry = null;
        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
            if (zipEntry.getName().startsWith("eclipse_")) {
                entry = zipEntry.getName();
                try {
                    fragmentJar.close();
                }
                catch (IOException ex) {}
                break;
            }
        }
        if (entry != null) {
            final String lib = this.extractFromJAR(fragment, entry);
            if (!this.getOS().equals("win32")) {
                try {
                    Runtime.getRuntime().exec(new String[] { "chmod", "755", lib }).waitFor();
                }
                catch (Throwable t) {}
            }
            return lib;
        }
        return null;
    }
    
    protected void basicRun(final String[] args) throws Exception {
        System.setProperty("eclipse.startTime", Long.toString(System.currentTimeMillis()));
        this.commands = args;
        final String[] passThruArgs = this.processCommandLine(args);
        if (!this.debug) {
            this.debug = (System.getProperty("osgi.debug") != null);
        }
        this.setupVMProperties();
        this.processConfiguration();
        if (this.protectBase && System.getProperty("osgi.sharedConfiguration.area") == null) {
            System.err.println("This application is configured to run in a cascaded mode only.");
            System.setProperty("eclipse.exitcode", Integer.toString(14));
            return;
        }
        this.getInstallLocation();
        final URL[] bootPath = this.getBootPath(this.bootLocation);
        this.setupJNI(bootPath);
        if (!this.checkVersion(System.getProperty("java.version"), System.getProperty("osgi.requiredJavaVersion"))) {
            return;
        }
        if (!this.checkConfigurationLocation(this.configurationLocation)) {
            return;
        }
        this.setSecurityPolicy(bootPath);
        this.handleSplash(bootPath);
        this.beforeFwkInvocation();
        this.invokeFramework(passThruArgs, bootPath);
    }
    
    protected void beforeFwkInvocation() {
    }
    
    protected void setSecurityPolicy(final URL[] bootPath) {
        final String eclipseSecurity = System.getProperty("eclipse.security");
        if (eclipseSecurity != null) {
            final ProtectionDomain domain = Main.class.getProtectionDomain();
            CodeSource source = null;
            if (domain != null) {
                source = Main.class.getProtectionDomain().getCodeSource();
            }
            if (domain == null || source == null) {
                this.log("Can not automatically set the security manager. Please use a policy file.");
                return;
            }
            final URL[] rootURLs = new URL[bootPath.length + 1];
            rootURLs[0] = source.getLocation();
            System.arraycopy(bootPath, 0, rootURLs, 1, bootPath.length);
            final Policy eclipsePolicy = new EclipsePolicy(Policy.getPolicy(), rootURLs);
            Policy.setPolicy(eclipsePolicy);
        }
    }
    
    private void invokeFramework(final String[] passThruArgs, final URL[] bootPath) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, Error, Exception, InvocationTargetException {
        String type = "boot";
        try {
            final String javaVersion = System.getProperty("java.version");
            if (javaVersion != null && new Identifier(javaVersion).isGreaterEqualTo(new Identifier("1.9"))) {
                type = "ext";
            }
        }
        catch (SecurityException | NumberFormatException ex) {}
        type = System.getProperty("osgi.parentClassloader", type);
        type = System.getProperty("osgi.frameworkParentClassloader", type);
        ClassLoader parent = null;
        if ("app".equalsIgnoreCase(type)) {
            parent = ClassLoader.getSystemClassLoader();
        }
        else if ("ext".equalsIgnoreCase(type)) {
            final ClassLoader appCL = ClassLoader.getSystemClassLoader();
            if (appCL != null) {
                parent = appCL.getParent();
            }
        }
        else if ("current".equalsIgnoreCase(type)) {
            parent = this.getClass().getClassLoader();
        }
        final URLClassLoader loader = new StartupClassLoader(bootPath, parent);
        final Class<?> clazz = loader.loadClass("org.eclipse.core.runtime.adaptor.EclipseStarter");
        final Method method = clazz.getDeclaredMethod("run", String[].class, Runnable.class);
        try {
            method.invoke(clazz, passThruArgs, this.splashHandler);
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Error) {
                throw (Error)e.getTargetException();
            }
            if (e.getTargetException() instanceof Exception) {
                throw (Exception)e.getTargetException();
            }
            throw e;
        }
    }
    
    private boolean checkVersion(final String availableVersion, final String requiredVersion) {
        if (requiredVersion == null || availableVersion == null) {
            return true;
        }
        try {
            final Identifier required = new Identifier(requiredVersion);
            final Identifier available = new Identifier(availableVersion);
            final boolean compatible = available.isGreaterEqualTo(required);
            if (!compatible) {
                System.setProperty("eclipse.exitcode", "14");
                System.setProperty("eclipse.exitdata", "<title>Incompatible JVM</title>Version " + availableVersion + " of the JVM is not suitable for this product. Version: " + requiredVersion + " or greater is required.");
            }
            return compatible;
        }
        catch (SecurityException | NumberFormatException ex) {
            return true;
        }
    }
    
    private boolean checkConfigurationLocation(final URL locationUrl) {
        if (locationUrl == null || !"file".equals(locationUrl.getProtocol())) {
            return true;
        }
        if (Boolean.parseBoolean(System.getProperty("osgi.configuration.area.readOnly"))) {
            return true;
        }
        final File configDir = new File(locationUrl.getFile()).getAbsoluteFile();
        if (!configDir.exists()) {
            configDir.mkdirs();
            if (!configDir.exists()) {
                System.setProperty("eclipse.exitcode", "15");
                System.setProperty("eclipse.exitdata", "<title>Invalid Configuration Location</title>The configuration area at '" + configDir + "' could not be created.  Please choose a writable location using the '-configuration' command line option.");
                return false;
            }
        }
        if (!canWrite(configDir)) {
            System.setProperty("eclipse.exitcode", "15");
            System.setProperty("eclipse.exitdata", "<title>Invalid Configuration Location</title>The configuration area at '" + configDir + "' is not writable.  Please choose a writable location using the '-configuration' command line option.");
            return false;
        }
        return true;
    }
    
    protected String decode(String urlString) {
        try {
            if (urlString.indexOf(43) >= 0) {
                final int len = urlString.length();
                final StringBuilder buf = new StringBuilder(len);
                for (int i = 0; i < len; ++i) {
                    final char c = urlString.charAt(i);
                    if (c == '+') {
                        buf.append("%2B");
                    }
                    else {
                        buf.append(c);
                    }
                }
                urlString = buf.toString();
            }
            return URLDecoder.decode(urlString, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    protected String[] getArrayFromList(final String prop) {
        if (prop == null || prop.trim().equals("")) {
            return new String[0];
        }
        final ArrayList<String> list = new ArrayList<String>();
        final StringTokenizer tokens = new StringTokenizer(prop, ",");
        while (tokens.hasMoreTokens()) {
            final String token = tokens.nextToken().trim();
            if (!token.isEmpty()) {
                list.add(token);
            }
        }
        return list.isEmpty() ? new String[0] : list.toArray(new String[list.size()]);
    }
    
    private URL[] getDevPath(final URL base) throws IOException {
        final ArrayList<URL> result = new ArrayList<URL>(5);
        if (this.inDevelopmentMode) {
            this.addDevEntries(base, result, "org.eclipse.osgi");
        }
        this.addBaseJars(base, result);
        return result.toArray(new URL[result.size()]);
    }
    
    URL constructURL(final URL url, final String name) {
        final String externalForm = url.toExternalForm();
        if (externalForm.endsWith(".jar")) {
            try {
                return new URL("jar:" + url + "!/" + name);
            }
            catch (MalformedURLException ex) {}
        }
        try {
            return new URL(url, name);
        }
        catch (MalformedURLException ex2) {
            return null;
        }
    }
    
    private void readFrameworkExtensions(final URL base, final ArrayList<URL> result) throws IOException {
        final String[] extensions = this.getArrayFromList(System.getProperty("osgi.framework.extensions"));
        final String parent = new File(base.getFile()).getParent();
        final ArrayList<String> extensionResults = new ArrayList<String>(extensions.length);
        String[] array;
        for (int length = (array = extensions).length, i = 0; i < length; ++i) {
            final String extension = array[i];
            final String path = this.searchForBundle(extension, parent);
            if (path == null) {
                this.log("Could not find extension: " + extension);
            }
            else {
                if (this.debug) {
                    System.out.println("Loading extension: " + extension);
                }
                URL extensionURL = null;
                if (this.installLocation.getProtocol().equals("file")) {
                    extensionResults.add(path);
                    extensionURL = new File(path).toURL();
                }
                else {
                    extensionURL = new URL(this.installLocation.getProtocol(), this.installLocation.getHost(), this.installLocation.getPort(), path);
                }
                Properties extensionProperties = null;
                try {
                    extensionProperties = this.loadProperties(this.constructURL(extensionURL, "eclipse.properties"));
                }
                catch (IOException ex) {
                    if (this.debug) {
                        System.out.println("\teclipse.properties not found");
                    }
                }
                String extensionClassPath = null;
                if (extensionProperties != null) {
                    extensionClassPath = extensionProperties.getProperty("osgi.frameworkClassPath");
                }
                else {
                    extensionProperties = new Properties();
                }
                final String[] entries = (extensionClassPath == null || extensionClassPath.length() == 0) ? new String[] { "" } : this.getArrayFromList(extensionClassPath);
                String qualifiedPath;
                if (System.getProperty("osgi.frameworkClassPath") == null) {
                    qualifiedPath = ".";
                }
                else {
                    qualifiedPath = "";
                }
                String[] array2;
                for (int length2 = (array2 = entries).length, j = 0; j < length2; ++j) {
                    final String entry = array2[j];
                    qualifiedPath = String.valueOf(qualifiedPath) + ", file:" + path + entry;
                }
                extensionProperties.put("osgi.frameworkClassPath", qualifiedPath);
                this.mergeWithSystemProperties(extensionProperties, null);
                if (this.inDevelopmentMode) {
                    String name = extension;
                    if (name.startsWith("reference:")) {
                        name = new File(path).getName();
                    }
                    this.addDevEntries(extensionURL, result, name);
                }
            }
        }
        this.extensionPaths = extensionResults.toArray(new String[extensionResults.size()]);
    }
    
    private void addBaseJars(final URL base, final ArrayList<URL> result) throws IOException {
        String baseJarList = System.getProperty("osgi.frameworkClassPath");
        if (baseJarList == null) {
            this.readFrameworkExtensions(base, result);
            baseJarList = System.getProperty("osgi.frameworkClassPath");
        }
        final File fwkFile = new File(base.getFile());
        final boolean fwkIsDirectory = fwkFile.isDirectory();
        if (fwkIsDirectory) {
            System.setProperty("osgi.framework.shape", "folder");
        }
        else {
            System.setProperty("osgi.framework.shape", "jar");
        }
        String fwkPath = new File(new File(base.getFile()).getParent()).getAbsolutePath();
        if (Character.isUpperCase(fwkPath.charAt(0))) {
            final char[] chars = fwkPath.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            fwkPath = new String(chars);
        }
        System.setProperty("osgi.syspath", fwkPath);
        final String[] baseJars = this.getArrayFromList(baseJarList);
        if (baseJars.length != 0) {
            String[] array;
            for (int length = (array = baseJars).length, i = 0; i < length; ++i) {
                final String string = array[i];
                try {
                    if (string.equals(".")) {
                        this.addEntry(base, result);
                    }
                    URL url = null;
                    if (string.startsWith("file:")) {
                        url = new File(string.substring(5)).toURL();
                    }
                    else {
                        url = new URL(string);
                    }
                    this.addEntry(url, result);
                }
                catch (MalformedURLException ex) {
                    this.addEntry(new URL(base, string), result);
                }
            }
            return;
        }
        if (!this.inDevelopmentMode && new File(base.getFile()).isDirectory()) {
            throw new IOException("Unable to initialize osgi.frameworkClassPath");
        }
        this.addEntry(base, result);
    }
    
    protected void addEntry(final URL url, final List<URL> result) {
        if (new File(url.getFile()).exists()) {
            result.add(url);
        }
    }
    
    private void addDevEntries(final URL base, final List<URL> result, final String symbolicName) throws MalformedURLException {
        if (this.devClassPathProps == null) {
            return;
        }
        String devPathList = this.devClassPathProps.getProperty(symbolicName);
        if (devPathList == null) {
            devPathList = this.devClassPathProps.getProperty("*");
        }
        final String[] locations = this.getArrayFromList(devPathList);
        String[] array;
        for (int length = (array = locations).length, i = 0; i < length; ++i) {
            final String location = array[i];
            final File path = new File(location);
            URL url;
            if (path.isAbsolute()) {
                url = path.toURL();
            }
            else {
                final char lastChar = location.charAt(location.length() - 1);
                if (location.endsWith(".jar") || lastChar == '/' || lastChar == '\\') {
                    url = new URL(base, location);
                }
                else {
                    url = new URL(base, String.valueOf(location) + "/");
                }
            }
            this.addEntry(url, result);
        }
    }
    
    private URL[] getBootPath(final String base) throws IOException {
        URL url = null;
        if (base != null) {
            url = buildURL(base, true);
        }
        else {
            url = this.getInstallLocation();
            final String pluginsLocation = new File(url.getFile(), "plugins").toString();
            final String path = this.searchFor(this.framework, pluginsLocation);
            if (path == null) {
                throw new FileNotFoundException(String.format("Could not find framework under %s", pluginsLocation));
            }
            if (url.getProtocol().equals("file")) {
                url = new File(path).toURL();
            }
            else {
                url = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
            }
        }
        if (System.getProperty("osgi.framework") == null) {
            System.setProperty("osgi.framework", url.toExternalForm());
        }
        if (this.debug) {
            System.out.println("Framework located:\n    " + url.toExternalForm());
        }
        final URL[] result = this.getDevPath(url);
        if (this.debug) {
            System.out.println("Framework classpath:");
            URL[] array;
            for (int length = (array = result).length, i = 0; i < length; ++i) {
                final URL devPath = array[i];
                System.out.println("    " + devPath.toExternalForm());
            }
        }
        return result;
    }
    
    protected String searchFor(final String target, final String start) {
        final File root = resolveFile(new File(start));
        final String[] candidates = root.list();
        if (candidates == null) {
            return null;
        }
        final ArrayList<String> matches = new ArrayList<String>(2);
        String[] array;
        for (int length = (array = candidates).length, i = 0; i < length; ++i) {
            final String candidate = array[i];
            if (this.isMatchingCandidate(target, candidate, root)) {
                matches.add(candidate);
            }
        }
        final String[] names = matches.toArray(new String[matches.size()]);
        final int result = this.findMax(target, names);
        if (result == -1) {
            return null;
        }
        final File candidate2 = new File(start, names[result]);
        return String.valueOf(candidate2.getAbsolutePath().replace(File.separatorChar, '/')) + (candidate2.isDirectory() ? "/" : "");
    }
    
    private boolean isMatchingCandidate(final String target, String candidate, final File root) {
        if (candidate.equals(target)) {
            return true;
        }
        if (!candidate.startsWith(String.valueOf(target) + "_")) {
            return false;
        }
        final int targetLength = target.length();
        int lastUnderscore = candidate.lastIndexOf(95);
        final File candidateFile = new File(root, candidate);
        if (candidateFile.isFile() && (candidate.endsWith(".jar") || candidate.endsWith(".zip"))) {
            final int extension = candidate.lastIndexOf(46);
            candidate = candidate.substring(0, extension);
        }
        final int lastDot = candidate.lastIndexOf(46);
        if (lastDot < targetLength) {
            return false;
        }
        while (lastUnderscore > lastDot) {
            lastUnderscore = candidate.lastIndexOf(95, lastUnderscore - 1);
        }
        return lastUnderscore == targetLength;
    }
    
    private String searchForBundle(String target, final String start) {
        if (!target.startsWith("reference:")) {
            return this.searchFor(target, start);
        }
        target = target.substring("reference:".length());
        if (!target.startsWith("file:")) {
            throw new IllegalArgumentException("Bundle URL is invalid: " + target);
        }
        target = target.substring("file:".length());
        File fileLocation;
        final File child = fileLocation = new File(target);
        if (!child.isAbsolute()) {
            final File parent = resolveFile(new File(start));
            fileLocation = new File(parent, child.getPath());
        }
        return this.searchFor(fileLocation.getName(), fileLocation.getParentFile().getAbsolutePath());
    }
    
    protected int findMax(final String prefix, final String[] candidates) {
        int result = -1;
        Object maxVersion = null;
        for (int i = 0; i < candidates.length; ++i) {
            final String name = (candidates[i] != null) ? candidates[i] : "";
            String version = "";
            if (prefix == null) {
                version = name;
            }
            else if (name.startsWith(String.valueOf(prefix) + "_")) {
                version = name.substring(prefix.length() + 1);
            }
            final Object currentVersion = this.getVersionElements(version);
            if (maxVersion == null) {
                result = i;
                maxVersion = currentVersion;
            }
            else if (this.compareVersion((Object[])maxVersion, (Object[])currentVersion) < 0) {
                result = i;
                maxVersion = currentVersion;
            }
        }
        return result;
    }
    
    private int compareVersion(final Object[] left, final Object[] right) {
        int result = ((Integer)left[0]).compareTo((Integer)right[0]);
        if (result != 0) {
            return result;
        }
        result = ((Integer)left[1]).compareTo((Integer)right[1]);
        if (result != 0) {
            return result;
        }
        result = ((Integer)left[2]).compareTo((Integer)right[2]);
        if (result != 0) {
            return result;
        }
        return ((String)left[3]).compareTo((String)right[3]);
    }
    
    private Object[] getVersionElements(String version) {
        if (version.endsWith(".jar")) {
            version = version.substring(0, version.length() - 4);
        }
        final Object[] result = { 0, 0, 0, "" };
        final StringTokenizer t = new StringTokenizer(version, ".");
        int i = 0;
        while (t.hasMoreTokens() && i < 4) {
            final String token = t.nextToken();
            if (i < 3) {
                try {
                    result[i++] = Integer.valueOf(token);
                    continue;
                }
                catch (Exception ex) {
                    break;
                }
            }
            result[i++] = token;
        }
        return result;
    }
    
    private static URL buildURL(String spec, final boolean trailingSlash) {
        if (spec == null) {
            return null;
        }
        if (File.separatorChar == '\\') {
            spec = spec.trim();
        }
        final boolean isFile = spec.startsWith("file:");
        try {
            if (!isFile) {
                return new URL(spec);
            }
            File toAdjust = new File(spec.substring(5));
            toAdjust = resolveFile(toAdjust);
            if (toAdjust.isDirectory()) {
                return adjustTrailingSlash(toAdjust.toURL(), trailingSlash);
            }
            return toAdjust.toURL();
        }
        catch (MalformedURLException ex) {
            if (isFile) {
                return null;
            }
            try {
                final File toAdjust = new File(spec);
                if (toAdjust.isDirectory()) {
                    return adjustTrailingSlash(toAdjust.toURL(), trailingSlash);
                }
                return toAdjust.toURL();
            }
            catch (MalformedURLException ex2) {
                return null;
            }
        }
    }
    
    private static File resolveFile(File toAdjust) {
        if (!toAdjust.isAbsolute()) {
            final String installArea = System.getProperty("osgi.install.area");
            if (installArea != null) {
                if (installArea.startsWith("file:")) {
                    toAdjust = new File(installArea.substring(5), toAdjust.getPath());
                }
                else if (new File(installArea).exists()) {
                    toAdjust = new File(installArea, toAdjust.getPath());
                }
            }
        }
        return toAdjust;
    }
    
    private static URL adjustTrailingSlash(final URL url, final boolean trailingSlash) throws MalformedURLException {
        String file = url.getFile();
        if (trailingSlash == file.endsWith("/")) {
            return url;
        }
        file = (trailingSlash ? (String.valueOf(file) + "/") : file.substring(0, file.length() - 1));
        return new URL(url.getProtocol(), url.getHost(), file);
    }
    
    private URL buildLocation(final String property, final URL defaultLocation, final String userDefaultAppendage) {
        URL result = null;
        String location = System.getProperty(property);
        System.clearProperty(property);
        try {
            if (location == null) {
                result = defaultLocation;
            }
            else {
                if (location.equalsIgnoreCase("@none")) {
                    return null;
                }
                if (location.equalsIgnoreCase("@noDefault")) {
                    result = buildURL(location, true);
                }
                else {
                    if (location.startsWith("@user.home")) {
                        final String base = this.substituteVar(location, "@user.home", "user.home");
                        location = new File(base, userDefaultAppendage).getAbsolutePath();
                    }
                    else if (location.startsWith("@user.dir")) {
                        final String base = this.substituteVar(location, "@user.dir", "user.dir");
                        location = new File(base, userDefaultAppendage).getAbsolutePath();
                    }
                    final int idx = location.indexOf("@install.hash");
                    if (idx == 0) {
                        throw new RuntimeException("The location cannot start with '@install.hash': " + location);
                    }
                    if (idx > 0) {
                        location = String.valueOf(location.substring(0, idx)) + this.getInstallDirHash() + location.substring(idx + "@install.hash".length());
                    }
                    result = buildURL(location, true);
                }
            }
        }
        finally {
            if (result != null) {
                System.setProperty(property, result.toExternalForm());
            }
        }
        if (result != null) {
            System.setProperty(property, result.toExternalForm());
        }
        return result;
    }
    
    private String substituteVar(final String source, final String var, final String prop) {
        final String value = System.getProperty(prop, "");
        return String.valueOf(value) + source.substring(var.length());
    }
    
    private String computeDefaultConfigurationLocation() {
        final URL install = this.getInstallLocation();
        if (this.protectBase) {
            return this.computeDefaultUserAreaLocation("configuration/");
        }
        if (install.getProtocol().equals("file")) {
            final File installDir = new File(install.getFile());
            if (canWrite(installDir)) {
                return String.valueOf(installDir.getAbsolutePath()) + File.separator + "configuration/";
            }
        }
        return this.computeDefaultUserAreaLocation("configuration/");
    }
    
    private static boolean canWrite(final File installDir) {
        if (!installDir.isDirectory()) {
            return false;
        }
        if (Files.isWritable(installDir.toPath())) {
            return true;
        }
        File fileTest = null;
        try {
            fileTest = File.createTempFile("writableArea", ".dll", installDir);
        }
        catch (IOException ex) {
            return false;
        }
        finally {
            if (fileTest != null) {
                fileTest.delete();
            }
        }
        if (fileTest != null) {
            fileTest.delete();
        }
        return true;
    }
    
    private String computeDefaultUserAreaLocation(final String pathAppendage) {
        final URL installURL = this.getInstallLocation();
        if (installURL == null) {
            return null;
        }
        final File installDir = new File(installURL.getFile());
        final String installDirHash = this.getInstallDirHash();
        if (this.protectBase && "macosx".equals(this.os)) {
            this.initializeBridgeEarly();
            final String macConfiguration = this.computeConfigurationLocationForMacOS();
            if (macConfiguration != null) {
                return macConfiguration;
            }
            if (this.debug) {
                System.out.println("Computation of Mac specific configuration folder failed.");
            }
        }
        String appName = ".eclipse";
        final File eclipseProduct = new File(installDir, ".eclipseproduct");
        if (eclipseProduct.exists()) {
            final Properties props = new Properties();
            try {
                props.load(new FileInputStream(eclipseProduct));
                String appId = props.getProperty("id");
                if (appId == null || appId.trim().length() == 0) {
                    appId = "eclipse";
                }
                String appVersion = props.getProperty("version");
                if (appVersion == null || appVersion.trim().length() == 0) {
                    appVersion = "";
                }
                appName = String.valueOf(appName) + File.separator + appId + "_" + appVersion + "_" + installDirHash;
            }
            catch (IOException ex) {
                appName = String.valueOf(appName) + File.separator + installDirHash;
            }
        }
        else {
            appName = String.valueOf(appName) + File.separator + installDirHash;
        }
        appName = String.valueOf(appName) + '_' + this.OS_WS_ARCHToString();
        final String userHome = System.getProperty("user.home");
        return new File(userHome, String.valueOf(appName) + "/" + pathAppendage).getAbsolutePath();
    }
    
    private String computeConfigurationLocationForMacOS() {
        if (this.bridge != null) {
            final String folder = this.bridge.getOSRecommendedFolder();
            if (this.debug) {
                System.out.println("App folder provided by MacOS is: " + folder);
            }
            if (folder != null) {
                return String.valueOf(folder) + '/' + "configuration/";
            }
        }
        return null;
    }
    
    private String OS_WS_ARCHToString() {
        return String.valueOf(this.getOS()) + '_' + this.getWS() + '_' + this.getArch();
    }
    
    private void initializeBridgeEarly() {
        this.setupJNI(null);
    }
    
    private String getInstallDirHash() {
        final URL installURL = this.getInstallLocation();
        if (installURL == null) {
            return "";
        }
        final File installDir = new File(installURL.getFile());
        int hashCode;
        try {
            hashCode = installDir.getCanonicalPath().hashCode();
        }
        catch (IOException ex) {
            hashCode = installDir.getAbsolutePath().hashCode();
        }
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        return String.valueOf(hashCode);
    }
    
    public static void main(final String argString) {
        final ArrayList<String> list = new ArrayList<String>(5);
        final StringTokenizer tokens = new StringTokenizer(argString, " ");
        while (tokens.hasMoreElements()) {
            list.add(tokens.nextToken());
        }
        main(list.toArray(new String[list.size()]));
    }
    
    public static void main(final String[] args) {
        int result = 0;
        try {
            result = new Main().run(args);
        }
        catch (Throwable t) {
            t.printStackTrace();
            return;
        }
        finally {
            if (!Boolean.getBoolean("osgi.noShutdown") || result == 23) {
                System.exit(result);
            }
        }
        if (!Boolean.getBoolean("osgi.noShutdown") || result == 23) {
            System.exit(result);
        }
    }
    
    public int run(final String[] args) {
        int result = 0;
        Label_0227: {
            try {
                this.basicRun(args);
                final String exitCode = System.getProperty("eclipse.exitcode");
                try {
                    result = ((exitCode == null) ? 0 : Integer.parseInt(exitCode));
                }
                catch (NumberFormatException ex) {
                    result = 17;
                }
            }
            catch (Throwable e) {
                if (!"13".equals(System.getProperty("eclipse.exitcode"))) {
                    this.log("Exception launching the Eclipse Platform:");
                    this.log(e);
                    String message = "An error has occurred";
                    if (this.logFile == null) {
                        message = String.valueOf(message) + " and could not be logged: \n" + e.getMessage();
                    }
                    else {
                        message = String.valueOf(message) + ".  See the log file\n" + this.logFile.getAbsolutePath();
                    }
                    System.setProperty("eclipse.exitdata", message);
                }
                else {
                    this.log("Are you trying to start an 64/32-bit Eclipse on a 32/64-JVM? These must be the same, as Eclipse uses native code.");
                }
                result = 13;
                break Label_0227;
            }
            finally {
                this.takeDownSplash();
                if (this.bridge != null) {
                    this.bridge.uninitialize();
                }
            }
            this.takeDownSplash();
            if (this.bridge != null) {
                this.bridge.uninitialize();
            }
        }
        System.setProperty("eclipse.exitcode", Integer.toString(result));
        this.setExitData();
        return result;
    }
    
    private void setExitData() {
        final String data = System.getProperty("eclipse.exitdata");
        if (data == null) {
            return;
        }
        if (this.bridge == null || (this.bridge.isLibraryLoadedByJava() && this.exitData == null)) {
            System.out.println(data);
        }
        else {
            this.bridge.setExitData(this.exitData, data);
        }
    }
    
    protected String[] processCommandLine(final String[] args) {
        if (args.length == 0) {
            return args;
        }
        final int[] configArgs = new int[args.length];
        configArgs[0] = -1;
        int configArgIndex = 0;
        for (int i = 0; i < args.length; ++i) {
            boolean found = false;
            if (args[i].equalsIgnoreCase("-debug")) {
                this.debug = true;
            }
            else {
                if (args[i].equalsIgnoreCase("-nosplash")) {
                    this.splashDown = true;
                    found = true;
                }
                if (args[i].equalsIgnoreCase("-noExit")) {
                    System.setProperty("osgi.noShutdown", "true");
                    found = true;
                }
                if (args[i].equalsIgnoreCase("--launcher.appendVmargs") || args[i].equalsIgnoreCase("--launcher.overrideVmargs")) {
                    found = true;
                }
                if (args[i].equalsIgnoreCase("-initialize")) {
                    this.initialize = true;
                }
                else if (args[i].equalsIgnoreCase("-dev") && (i + 1 == args.length || (i + 1 < args.length && args[i + 1].startsWith("-")))) {
                    this.inDevelopmentMode = true;
                }
                else {
                    if (args[i].equalsIgnoreCase("-showsplash")) {
                        this.showSplash = true;
                        found = true;
                        if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            configArgs[configArgIndex++] = i++;
                            this.splashLocation = args[i];
                        }
                    }
                    if (args[i].equalsIgnoreCase("-protect")) {
                        found = true;
                        configArgs[configArgIndex++] = i++;
                        if (args[i].equalsIgnoreCase("master") || args[i].equalsIgnoreCase("base")) {
                            this.protectBase = true;
                        }
                    }
                    if (found) {
                        configArgs[configArgIndex++] = i;
                    }
                    else if (args[i].equalsIgnoreCase("-vmargs")) {
                        args[i] = null;
                        ++i;
                        this.vmargs = new String[args.length - i];
                        int j = 0;
                        while (i < args.length) {
                            this.vmargs[j++] = args[i];
                            args[i] = null;
                            ++i;
                        }
                    }
                    else if (i != args.length - 1) {
                        if (!args[i + 1].startsWith("-")) {
                            final String arg = args[++i];
                            if (args[i - 1].equalsIgnoreCase("-dev")) {
                                this.inDevelopmentMode = true;
                                this.devClassPathProps = this.processDevArg(arg);
                                if (this.devClassPathProps != null) {
                                    this.devClassPath = this.devClassPathProps.getProperty("org.eclipse.osgi");
                                    if (this.devClassPath == null) {
                                        this.devClassPath = this.devClassPathProps.getProperty("*");
                                    }
                                }
                            }
                            else {
                                if (args[i - 1].equalsIgnoreCase("-framework")) {
                                    this.framework = arg;
                                    found = true;
                                }
                                if (args[i - 1].equalsIgnoreCase("-os")) {
                                    this.os = arg;
                                }
                                else if (args[i - 1].equalsIgnoreCase("-ws")) {
                                    this.ws = arg;
                                }
                                else if (args[i - 1].equalsIgnoreCase("-arch")) {
                                    this.arch = arg;
                                }
                                else {
                                    if (args[i - 1].equalsIgnoreCase("-install")) {
                                        System.setProperty("osgi.install.area", arg);
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-configuration")) {
                                        System.setProperty("osgi.configuration.area", arg);
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-exitdata")) {
                                        this.exitData = arg;
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-name")) {
                                        System.setProperty("eclipse.launcher.name", arg);
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-startup")) {
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-launcher")) {
                                        System.setProperty("eclipse.launcher", arg);
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("--launcher.library")) {
                                        this.library = arg;
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-endsplash")) {
                                        this.endSplash = arg;
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-vm")) {
                                        this.vm = arg;
                                        found = true;
                                    }
                                    if (args[i - 1].equalsIgnoreCase("-nl")) {
                                        System.setProperty("osgi.nl", arg);
                                        found = true;
                                    }
                                    if (found) {
                                        configArgs[configArgIndex++] = i - 1;
                                        configArgs[configArgIndex++] = i;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final String[] passThruArgs = new String[args.length - configArgIndex - ((this.vmargs == null) ? 0 : (this.vmargs.length + 1))];
        configArgIndex = 0;
        int k = 0;
        for (int l = 0; l < args.length; ++l) {
            if (l == configArgs[configArgIndex]) {
                ++configArgIndex;
            }
            else if (args[l] != null) {
                passThruArgs[k++] = args[l];
            }
        }
        return passThruArgs;
    }
    
    private Properties processDevArg(final String arg) {
        if (arg == null) {
            return null;
        }
        try {
            final URL location = new URL(arg);
            return this.load(location, null);
        }
        catch (MalformedURLException ex) {
            final Properties result = new Properties();
            result.put("*", arg);
            return result;
        }
        catch (IOException ex2) {
            return null;
        }
    }
    
    private URL getConfigurationLocation() {
        if (this.configurationLocation != null) {
            return this.configurationLocation;
        }
        this.configurationLocation = this.buildLocation("osgi.configuration.area", null, "");
        if (this.configurationLocation == null) {
            this.configurationLocation = this.buildLocation("osgi.configuration.area.default", null, "");
            if (this.configurationLocation == null) {
                this.configurationLocation = buildURL(this.computeDefaultConfigurationLocation(), true);
            }
        }
        if (this.configurationLocation != null) {
            System.setProperty("osgi.configuration.area", this.configurationLocation.toExternalForm());
        }
        if (this.debug) {
            System.out.println("Configuration location:\n    " + this.configurationLocation);
        }
        return this.configurationLocation;
    }
    
    private void processConfiguration() {
        URL baseConfigurationLocation = null;
        Properties baseConfiguration = null;
        if (System.getProperty("osgi.configuration.area") == null) {
            this.ensureAbsolute("osgi.baseConfiguration.area");
            final String baseLocation = System.getProperty("osgi.baseConfiguration.area");
            if (baseLocation != null) {
                baseConfigurationLocation = buildURL(baseLocation, true);
            }
            if (baseConfigurationLocation == null) {
                try {
                    baseConfigurationLocation = new URL(this.getInstallLocation(), "configuration/");
                }
                catch (MalformedURLException ex) {}
            }
            baseConfiguration = this.loadConfiguration(baseConfigurationLocation);
            if (baseConfiguration != null) {
                String location = baseConfiguration.getProperty("osgi.configuration.area");
                if (location != null) {
                    System.setProperty("osgi.configuration.area", location);
                }
                location = baseConfiguration.getProperty("osgi.install.area");
                if (location != null && System.getProperty("osgi.install.area") == null) {
                    System.setProperty("osgi.install.area", location);
                }
            }
        }
        Properties configuration = baseConfiguration;
        if (configuration == null || !this.getConfigurationLocation().equals(baseConfigurationLocation)) {
            configuration = this.loadConfiguration(this.getConfigurationLocation());
        }
        if (configuration != null && "false".equalsIgnoreCase(configuration.getProperty("osgi.configuration.cascaded"))) {
            System.clearProperty("osgi.sharedConfiguration.area");
            configuration.remove("osgi.sharedConfiguration.area");
            this.mergeWithSystemProperties(configuration, null);
        }
        else {
            this.ensureAbsolute("osgi.sharedConfiguration.area");
            URL sharedConfigURL = this.buildLocation("osgi.sharedConfiguration.area", null, "");
            if (sharedConfigURL == null) {
                try {
                    sharedConfigURL = new URL(this.getInstallLocation(), "configuration/");
                }
                catch (MalformedURLException ex2) {}
            }
            if (sharedConfigURL != null) {
                if (sharedConfigURL.equals(this.getConfigurationLocation())) {
                    System.clearProperty("osgi.sharedConfiguration.area");
                    this.mergeWithSystemProperties(configuration, null);
                }
                else {
                    Properties sharedConfiguration = baseConfiguration;
                    if (!sharedConfigURL.equals(baseConfigurationLocation)) {
                        sharedConfiguration = this.loadConfiguration(sharedConfigURL);
                    }
                    final long sharedConfigTimestamp = this.getCurrentConfigIniBaseTimestamp(sharedConfigURL);
                    final long lastKnownBaseTimestamp = this.getLastKnownConfigIniBaseTimestamp();
                    if (this.debug) {
                        System.out.println("Timestamps found: \n\t config.ini in the base: " + sharedConfigTimestamp + "\n\t remembered " + lastKnownBaseTimestamp);
                    }
                    if (lastKnownBaseTimestamp == sharedConfigTimestamp || lastKnownBaseTimestamp == -1L) {
                        this.mergeWithSystemProperties(configuration, null);
                    }
                    else {
                        configuration = null;
                        System.setProperty("eclipse.ignoreUserConfiguration", Boolean.TRUE.toString());
                    }
                    this.mergeWithSystemProperties(sharedConfiguration, configuration);
                    System.setProperty("osgi.sharedConfiguration.area", sharedConfigURL.toExternalForm());
                    if (this.debug) {
                        System.out.println("Shared configuration location:\n    " + sharedConfigURL.toExternalForm());
                    }
                }
            }
        }
        String urlString = System.getProperty("osgi.framework", null);
        if (urlString != null) {
            urlString = this.resolve(urlString);
            this.getInstallLocation();
            final URL url = buildURL(urlString, true);
            urlString = url.toExternalForm();
            System.setProperty("osgi.framework", urlString);
            this.bootLocation = urlString;
        }
    }
    
    private long getCurrentConfigIniBaseTimestamp(URL url) {
        try {
            url = new URL(url, "config.ini");
        }
        catch (MalformedURLException ex) {
            return -1L;
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        }
        catch (IOException ex2) {
            return -1L;
        }
        return connection.getLastModified();
    }
    
    private long getLastKnownConfigIniBaseTimestamp() {
        if (this.debug) {
            System.out.println("Loading timestamp file from:\n\t " + this.getConfigurationLocation() + "   " + ".baseConfigIniTimestamp");
        }
        Properties result;
        try {
            result = this.load(this.getConfigurationLocation(), ".baseConfigIniTimestamp");
        }
        catch (IOException ex) {
            if (this.debug) {
                System.out.println("\tNo timestamp file found");
            }
            return -1L;
        }
        final String timestamp = result.getProperty("configIniTimestamp");
        return Long.parseLong(timestamp);
    }
    
    private void ensureAbsolute(final String locationProperty) {
        final String propertyValue = System.getProperty(locationProperty);
        if (propertyValue == null) {
            return;
        }
        URL locationURL = null;
        try {
            locationURL = new URL(propertyValue);
        }
        catch (MalformedURLException ex) {
            return;
        }
        final String locationPath = locationURL.getPath();
        if (locationPath.startsWith("/")) {
            return;
        }
        final URL installURL = this.getInstallLocation();
        if (!locationURL.getProtocol().equals(installURL.getProtocol())) {
            return;
        }
        try {
            final URL absoluteURL = new URL(installURL, locationPath);
            System.setProperty(locationProperty, absoluteURL.toExternalForm());
        }
        catch (MalformedURLException ex2) {}
    }
    
    private URL getInstallLocation() {
        if (this.installLocation != null) {
            return this.installLocation;
        }
        String installArea = System.getProperty("osgi.install.area");
        if (installArea != null) {
            if (installArea.startsWith("@launcher.dir")) {
                final String launcher = System.getProperty("eclipse.launcher");
                if (launcher == null) {
                    throw new IllegalStateException("Install location depends on launcher, but launcher is not defined");
                }
                installArea = installArea.replace("@launcher.dir", new File(launcher).getParent());
            }
            this.installLocation = buildURL(installArea, true);
            if (this.installLocation == null) {
                throw new IllegalStateException("Install location is invalid: " + installArea);
            }
            System.setProperty("osgi.install.area", this.installLocation.toExternalForm());
            if (this.debug) {
                System.out.println("Install location:\n    " + this.installLocation);
            }
            return this.installLocation;
        }
        else {
            final ProtectionDomain domain = Main.class.getProtectionDomain();
            CodeSource source = null;
            URL result = null;
            if (domain != null) {
                source = domain.getCodeSource();
            }
            if (source == null || domain == null) {
                if (this.debug) {
                    System.out.println("CodeSource location is null. Defaulting the install location to file:startup.jar");
                }
                try {
                    result = new URL("file:startup.jar");
                }
                catch (MalformedURLException ex) {}
            }
            if (source != null) {
                result = source.getLocation();
            }
            String path = this.decode(result.getFile());
            final File file = new File(path);
            path = file.toString().replace('\\', '/');
            if (File.separatorChar == '\\' && Character.isUpperCase(path.charAt(0))) {
                final char[] chars = path.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                path = new String(chars);
            }
            if (path.toLowerCase().endsWith(".jar")) {
                path = path.substring(0, path.lastIndexOf(47) + 1);
            }
            while (true) {
                if (path.toLowerCase().endsWith("/plugins/")) {
                    path = path.substring(0, path.length() - "/plugins/".length());
                    try {
                        try {
                            path = new File(path).toURL().getFile();
                        }
                        catch (MalformedURLException ex2) {}
                        this.installLocation = new URL(result.getProtocol(), result.getHost(), result.getPort(), path);
                        System.setProperty("osgi.install.area", this.installLocation.toExternalForm());
                    }
                    catch (MalformedURLException ex3) {}
                    if (this.debug) {
                        System.out.println("Install location:\n    " + this.installLocation);
                    }
                    return this.installLocation;
                }
                continue;
            }
        }
    }
    
    private Properties loadConfiguration(URL url) {
        Properties result = null;
        try {
            url = new URL(url, "config.ini");
        }
        catch (MalformedURLException ex) {
            return null;
        }
        try {
            if (this.debug) {
                System.out.print("Configuration file:\n    " + url);
            }
            result = this.loadProperties(url);
            if (this.debug) {
                System.out.println(" loaded");
            }
        }
        catch (IOException ex2) {
            if (this.debug) {
                System.out.println(" not found or not read");
            }
        }
        return this.substituteVars(result);
    }
    
    private Properties loadProperties(final URL url) throws IOException {
        if (url == null) {
            return null;
        }
        Properties result = null;
        IOException originalException = null;
        try {
            result = this.load(url, null);
        }
        catch (IOException e1) {
            originalException = e1;
            try {
                result = this.load(url, ".tmp");
            }
            catch (IOException ex) {
                try {
                    result = this.load(url, ".bak");
                }
                catch (IOException ex2) {
                    throw originalException;
                }
            }
        }
        return result;
    }
    
    private Properties load(URL url, final String suffix) throws IOException {
        if (suffix != null && !suffix.equals("")) {
            url = new URL(url.getProtocol(), url.getHost(), url.getPort(), String.valueOf(url.getFile()) + suffix);
        }
        final Properties props = new Properties();
        Throwable t = null;
        try {
            final InputStream is = this.getStream(url);
            try {
                props.load(is);
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        finally {
            if (t == null) {
                final Throwable exception;
                t = exception;
            }
            else {
                final Throwable exception;
                if (t != exception) {
                    t.addSuppressed(exception);
                }
            }
        }
        return props;
    }
    
    private InputStream getStream(final URL location) throws IOException {
        if ("file".equalsIgnoreCase(location.getProtocol())) {
            final File f = new File(location.getPath());
            if (f.exists()) {
                return new FileInputStream(f);
            }
        }
        return location.openStream();
    }
    
    private void handleSplash(final URL[] defaultPath) {
        if (this.initialize || this.splashDown || this.bridge == null) {
            this.showSplash = false;
            this.endSplash = null;
            return;
        }
        Label_0060: {
            if (!this.showSplash) {
                if (this.endSplash == null) {
                    break Label_0060;
                }
            }
            try {
                Runtime.getRuntime().addShutdownHook(this.splashHandler);
            }
            catch (Throwable t) {}
        }
        if (this.endSplash != null) {
            this.showSplash = false;
            return;
        }
        if (!this.showSplash) {
            return;
        }
        this.splashLocation = this.getSplashLocation(defaultPath);
        if (this.debug) {
            System.out.println("Splash location:\n    " + this.splashLocation);
        }
        if (this.splashLocation == null) {
            return;
        }
        this.bridge.setLauncherInfo(System.getProperty("eclipse.launcher"), System.getProperty("eclipse.launcher.name"));
        this.bridge.showSplash(this.splashLocation);
        final long handle = this.bridge.getSplashHandle();
        if (handle != 0L && handle != -1L) {
            System.setProperty("org.eclipse.equinox.launcher.splash.handle", String.valueOf(handle));
            System.setProperty("org.eclipse.equinox.launcher.splash.location", this.splashLocation);
            this.bridge.updateSplash();
        }
        else {
            this.splashDown = true;
        }
    }
    
    protected void takeDownSplash() {
        if (this.splashDown || this.bridge == null) {
            return;
        }
        this.splashDown = this.bridge.takeDownSplash();
        System.clearProperty("org.eclipse.equinox.launcher.splash.handle");
        try {
            Runtime.getRuntime().removeShutdownHook(this.splashHandler);
        }
        catch (Throwable t) {}
    }
    
    private String getSplashLocation(final URL[] bootPath) {
        if (this.splashLocation != null && !Character.isDigit(this.splashLocation.charAt(0)) && new File(this.splashLocation).exists()) {
            System.setProperty("osgi.splashLocation", this.splashLocation);
            return this.splashLocation;
        }
        String result = System.getProperty("osgi.splashLocation");
        if (result != null) {
            return result;
        }
        final String splashPath = System.getProperty("osgi.splashPath");
        if (splashPath != null) {
            final String[] entries = this.getArrayFromList(splashPath);
            final ArrayList<String> path = new ArrayList<String>(entries.length);
            String[] array;
            for (int length = (array = entries).length, i = 0; i < length; ++i) {
                final String e = array[i];
                String entry = this.resolve(e);
                if (entry != null && entry.startsWith("file:")) {
                    final File entryFile = new File(entry.substring(5).replace('/', File.separatorChar));
                    entry = this.searchFor(entryFile.getName(), entryFile.getParent());
                    if (entry != null) {
                        path.add(entry);
                    }
                }
                else {
                    this.log("Invalid splash path entry: " + e);
                }
            }
            result = this.searchForSplash(path.toArray(new String[path.size()]));
            if (result != null) {
                System.setProperty("osgi.splashLocation", result);
                return result;
            }
        }
        return result;
    }
    
    private String searchForSplash(final String[] searchPath) {
        if (searchPath == null) {
            return null;
        }
        String locale = System.getProperty("osgi.nl");
        if (locale == null) {
            locale = Locale.getDefault().toString();
        }
        final String[] nlVariants = buildNLVariants(locale);
        String[] array;
        for (int length = (array = nlVariants).length, i = 0; i < length; ++i) {
            final String nlVariant = array[i];
            for (String path : searchPath) {
                if (path.startsWith("file:")) {
                    path = path.substring(5);
                }
                if (this.isJAR(path)) {
                    final String result = this.extractFromJAR(path, nlVariant);
                    if (result != null) {
                        return result;
                    }
                }
                else {
                    if (!path.endsWith(File.separator)) {
                        path = String.valueOf(path) + File.separator;
                    }
                    path = String.valueOf(path) + nlVariant;
                    final File result2 = new File(path);
                    if (result2.exists()) {
                        return result2.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }
    
    private String extractFromJAR(final String jarPath, final String jarEntry) {
        final String configLocation = System.getProperty("osgi.configuration.area");
        if (configLocation == null) {
            this.log("Configuration area not set yet. Unable to extract " + jarEntry + " from JAR'd plug-in: " + jarPath);
            return null;
        }
        final URL configURL = buildURL(configLocation, false);
        if (configURL == null) {
            return null;
        }
        File splash = new File(configURL.getPath(), "org.eclipse.equinox.launcher");
        final File jarFile = new File(jarPath);
        String cache = jarFile.getName();
        if (cache.endsWith(".jar")) {
            cache = cache.substring(0, cache.length() - 4);
        }
        splash = new File(splash, cache);
        splash = new File(splash, jarEntry);
        if (splash.exists()) {
            boolean clean = false;
            String[] commands;
            for (int length = (commands = this.commands).length, i = 0; i < length; ++i) {
                final String command = commands[i];
                if ("-clean".equalsIgnoreCase(command)) {
                    clean = true;
                    splash.delete();
                    break;
                }
            }
            if (!clean) {
                return splash.getAbsolutePath();
            }
        }
        try {
            Throwable t = null;
            try {
                final ZipFile file = new ZipFile(jarPath);
                try {
                    final ZipEntry entry = file.getEntry(jarEntry.replace(File.separatorChar, '/'));
                    if (entry == null) {
                        return null;
                    }
                    final Path outputFile = splash.toPath();
                    Files.createDirectories(outputFile.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                    try {
                        Throwable t2 = null;
                        try {
                            final InputStream input = file.getInputStream(entry);
                            try {
                                Files.copy(input, outputFile, new CopyOption[0]);
                            }
                            finally {
                                if (input != null) {
                                    input.close();
                                }
                            }
                        }
                        finally {
                            if (t2 == null) {
                                final Throwable exception;
                                t2 = exception;
                            }
                            else {
                                final Throwable exception;
                                if (t2 != exception) {
                                    t2.addSuppressed(exception);
                                }
                            }
                        }
                    }
                    catch (IOException e) {
                        this.log("Exception opening splash: " + entry.getName() + " in JAR file: " + jarPath);
                        this.log(e);
                        return null;
                    }
                    return splash.exists() ? splash.getAbsolutePath() : null;
                }
                finally {
                    if (file != null) {
                        file.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable exception2;
                    t = exception2;
                }
                else {
                    final Throwable exception2;
                    if (t != exception2) {
                        t.addSuppressed(exception2);
                    }
                }
            }
        }
        catch (IOException e2) {
            this.log("Exception looking for " + jarEntry + " in JAR file: " + jarPath);
            this.log(e2);
            return null;
        }
    }
    
    private boolean isJAR(final String path) {
        return new File(path).isFile();
    }
    
    private static String[] buildNLVariants(final String locale) {
        String nl = locale;
        final ArrayList<String> result = new ArrayList<String>(4);
        while (true) {
            String[] splash_IMAGES;
            for (int length = (splash_IMAGES = Main.SPLASH_IMAGES).length, i = 0; i < length; ++i) {
                final String name = splash_IMAGES[i];
                result.add("nl" + File.separatorChar + nl.replace('_', File.separatorChar) + File.separatorChar + name);
            }
            final int lastSeparator = nl.lastIndexOf(95);
            if (lastSeparator == -1) {
                break;
            }
            nl = nl.substring(0, lastSeparator);
        }
        String[] splash_IMAGES2;
        for (int length2 = (splash_IMAGES2 = Main.SPLASH_IMAGES).length, j = 0; j < length2; ++j) {
            final String name = splash_IMAGES2[j];
            result.add(name);
        }
        return result.toArray(new String[result.size()]);
    }
    
    private String resolve(String urlString) {
        if (urlString.startsWith("reference:")) {
            urlString = urlString.substring(10);
        }
        if (urlString.startsWith("platform:/base/")) {
            final String path = urlString.substring("platform:/base/".length());
            return this.getInstallLocation() + path;
        }
        return urlString;
    }
    
    protected synchronized void log(final Object obj) {
        if (obj == null) {
            return;
        }
        try {
            this.openLogFile();
            try {
                if (this.newSession) {
                    this.log.write("!SESSION");
                    this.log.write(32);
                    final String timestamp = new Date().toString();
                    this.log.write(timestamp);
                    this.log.write(32);
                    for (int i = "!SESSION".length() + timestamp.length(); i < 78; ++i) {
                        this.log.write(45);
                    }
                    this.log.newLine();
                    this.newSession = false;
                }
                this.write(obj);
            }
            finally {
                if (this.logFile == null) {
                    if (this.log != null) {
                        this.log.flush();
                    }
                }
                else {
                    this.closeLogFile();
                }
            }
            if (this.logFile == null) {
                if (this.log != null) {
                    this.log.flush();
                }
            }
            else {
                this.closeLogFile();
            }
        }
        catch (Exception e) {
            System.err.println("An exception occurred while writing to the platform log:");
            e.printStackTrace(System.err);
            System.err.println("Logging to the console instead.");
            try {
                this.log = this.logForStream(System.err);
                this.write(obj);
                this.log.flush();
            }
            catch (Exception e2) {
                System.err.println("An exception occurred while logging to the console:");
                e2.printStackTrace(System.err);
            }
            return;
        }
        finally {
            this.log = null;
        }
        this.log = null;
    }
    
    private void write(final Object obj) throws IOException {
        if (obj == null) {
            return;
        }
        if (obj instanceof Throwable) {
            this.log.write("!STACK");
            this.log.newLine();
            ((Throwable)obj).printStackTrace(new PrintWriter(this.log));
        }
        else {
            this.log.write("!ENTRY");
            this.log.write(32);
            this.log.write("org.eclipse.equinox.launcher");
            this.log.write(32);
            this.log.write(String.valueOf(4));
            this.log.write(32);
            this.log.write(String.valueOf(0));
            this.log.write(32);
            this.log.write(this.getDate(new Date()));
            this.log.newLine();
            this.log.write("!MESSAGE");
            this.log.write(32);
            this.log.write(String.valueOf(obj));
        }
        this.log.newLine();
    }
    
    protected String getDate(final Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final StringBuilder sb = new StringBuilder();
        this.appendPaddedInt(c.get(1), 4, sb).append('-');
        this.appendPaddedInt(c.get(2) + 1, 2, sb).append('-');
        this.appendPaddedInt(c.get(5), 2, sb).append(' ');
        this.appendPaddedInt(c.get(11), 2, sb).append(':');
        this.appendPaddedInt(c.get(12), 2, sb).append(':');
        this.appendPaddedInt(c.get(13), 2, sb).append('.');
        this.appendPaddedInt(c.get(14), 3, sb);
        return sb.toString();
    }
    
    private StringBuilder appendPaddedInt(final int value, int pad, final StringBuilder buffer) {
        if (--pad == 0) {
            return buffer.append(value);
        }
        int padding = (int)Math.pow(10.0, pad);
        if (value >= padding) {
            return buffer.append(value);
        }
        while (padding > value && padding > 1) {
            buffer.append('0');
            padding /= 10;
        }
        buffer.append(value);
        return buffer;
    }
    
    private void computeLogFileLocation() {
        final String logFileProp = System.getProperty("osgi.logfile");
        if (logFileProp != null) {
            if (this.logFile == null || !logFileProp.equals(this.logFile.getAbsolutePath())) {
                this.logFile = new File(logFileProp);
                new File(this.logFile.getParent()).mkdirs();
            }
            return;
        }
        final URL base = buildURL(System.getProperty("osgi.configuration.area"), false);
        if (base == null) {
            return;
        }
        this.logFile = new File(base.getPath(), String.valueOf(System.currentTimeMillis()) + ".log");
        new File(this.logFile.getParent()).mkdirs();
        System.setProperty("osgi.logfile", this.logFile.getAbsolutePath());
    }
    
    private void openLogFile() throws IOException {
        this.computeLogFileLocation();
        try {
            this.log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.logFile.getAbsolutePath(), true), StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            this.logFile = null;
            throw e;
        }
    }
    
    private BufferedWriter logForStream(final OutputStream output) {
        return new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
    }
    
    private void closeLogFile() throws IOException {
        try {
            if (this.log != null) {
                this.log.flush();
                this.log.close();
            }
        }
        finally {
            this.log = null;
        }
        this.log = null;
    }
    
    private void mergeWithSystemProperties(final Properties source, final Properties userConfiguration) {
        if (source == null) {
            return;
        }
        final Enumeration<?> e = source.keys();
        while (e.hasMoreElements()) {
            final String key = (String)e.nextElement();
            if (key.equals("osgi.frameworkClassPath")) {
                String destinationClasspath = System.getProperty("osgi.frameworkClassPath");
                final String sourceClasspath = source.getProperty("osgi.frameworkClassPath");
                if (destinationClasspath == null) {
                    destinationClasspath = sourceClasspath;
                }
                else {
                    destinationClasspath = String.valueOf(destinationClasspath) + sourceClasspath;
                }
                System.setProperty("osgi.frameworkClassPath", destinationClasspath);
            }
            else {
                final String value = source.getProperty(key);
                if (userConfiguration != null && !key.endsWith(".override.user")) {
                    final String overrideKey = String.valueOf(key) + ".override.user";
                    final boolean shouldOverride = System.getProperty(overrideKey) != null || source.getProperty(overrideKey) != null;
                    if (shouldOverride && !userConfiguration.contains(key)) {
                        System.setProperty(key, value);
                        continue;
                    }
                }
                if (System.getProperty(key) != null) {
                    continue;
                }
                System.setProperty(key, value);
            }
        }
    }
    
    private void setupVMProperties() {
        if (this.vm != null) {
            System.setProperty("eclipse.vm", this.vm);
        }
        this.setMultiValueProperty("eclipse.vmargs", this.vmargs);
        this.setMultiValueProperty("eclipse.commands", this.commands);
    }
    
    private void setMultiValueProperty(final String property, final String[] values) {
        if (values != null) {
            final StringBuilder result = new StringBuilder(300);
            for (final String value : values) {
                if (value != null) {
                    result.append(value);
                    result.append('\n');
                }
            }
            System.setProperty(property, result.toString());
        }
    }
    
    private Properties substituteVars(final Properties result) {
        if (result == null) {
            return null;
        }
        final Enumeration<?> eKeys = result.keys();
        while (eKeys.hasMoreElements()) {
            final Object key = eKeys.nextElement();
            if (key instanceof String) {
                final String value = result.getProperty((String)key);
                if (value == null) {
                    continue;
                }
                result.put(key, substituteVars(value));
            }
        }
        return result;
    }
    
    public static String substituteVars(final String path) {
        final StringBuilder buf = new StringBuilder(path.length());
        final StringTokenizer st = new StringTokenizer(path, "$", true);
        boolean varStarted = false;
        String var = null;
        while (st.hasMoreElements()) {
            final String tok = st.nextToken();
            if ("$".equals(tok)) {
                if (!varStarted) {
                    varStarted = true;
                    var = "";
                }
                else {
                    String prop = null;
                    if (var != null && var.length() > 0) {
                        prop = System.getProperty(var);
                    }
                    if (prop == null) {
                        prop = System.getenv(var);
                    }
                    if (prop != null) {
                        buf.append(prop);
                    }
                    else {
                        buf.append('$');
                        buf.append((var == null) ? "" : var);
                        buf.append('$');
                    }
                    varStarted = false;
                    var = null;
                }
            }
            else if (!varStarted) {
                buf.append(tok);
            }
            else {
                var = tok;
            }
        }
        if (var != null) {
            buf.append('$').append(var);
        }
        return buf.toString();
    }
    
    public final class SplashHandler extends Thread
    {
        @Override
        public void run() {
            Main.this.takeDownSplash();
        }
        
        public void updateSplash() {
            if (Main.this.bridge != null && !Main.this.splashDown) {
                Main.this.bridge.updateSplash();
            }
        }
    }
    
    static class Identifier
    {
        private static final String DELIM = ". _-";
        private int major;
        private int minor;
        private int service;
        
        Identifier(final int major, final int minor, final int service) {
            this.major = major;
            this.minor = minor;
            this.service = service;
        }
        
        Identifier(final String versionString) {
            final StringTokenizer tokenizer = new StringTokenizer(versionString, ". _-");
            if (tokenizer.hasMoreTokens()) {
                this.major = Integer.parseInt(tokenizer.nextToken());
            }
            try {
                if (tokenizer.hasMoreTokens()) {
                    this.minor = Integer.parseInt(tokenizer.nextToken());
                }
                if (tokenizer.hasMoreTokens()) {
                    this.service = Integer.parseInt(tokenizer.nextToken());
                }
            }
            catch (NumberFormatException ex) {}
        }
        
        boolean isGreaterEqualTo(final Identifier minimum) {
            return this.major >= minimum.major && (this.major > minimum.major || (this.minor >= minimum.minor && (this.minor > minimum.minor || this.service >= minimum.service)));
        }
    }
    
    private class EclipsePolicy extends Policy
    {
        private Policy policy;
        private URL[] urls;
        private PermissionCollection allPermissions;
        Permission allPermission;
        
        EclipsePolicy(final Policy policy, final URL[] urls) {
            this.allPermission = new AllPermission();
            this.policy = policy;
            this.urls = urls;
            this.allPermissions = new PermissionCollection() {
                private static final long serialVersionUID = 3258131349494708277L;
                
                @Override
                public void add(final Permission permission) {
                }
                
                @Override
                public boolean implies(final Permission permission) {
                    return true;
                }
                
                @Override
                public Enumeration<Permission> elements() {
                    return Collections.enumeration(Collections.singleton(EclipsePolicy.this.allPermission));
                }
            };
        }
        
        @Override
        public PermissionCollection getPermissions(final CodeSource codesource) {
            if (this.contains(codesource)) {
                return this.allPermissions;
            }
            return (this.policy == null) ? this.allPermissions : this.policy.getPermissions(codesource);
        }
        
        @Override
        public PermissionCollection getPermissions(final ProtectionDomain domain) {
            if (this.contains(domain.getCodeSource())) {
                return this.allPermissions;
            }
            return (this.policy == null) ? this.allPermissions : this.policy.getPermissions(domain);
        }
        
        @Override
        public boolean implies(final ProtectionDomain domain, final Permission permission) {
            return this.contains(domain.getCodeSource()) || this.policy == null || this.policy.implies(domain, permission);
        }
        
        @Override
        public void refresh() {
            if (this.policy != null) {
                this.policy.refresh();
            }
        }
        
        private boolean contains(final CodeSource codeSource) {
            if (codeSource == null) {
                return false;
            }
            final URL location = codeSource.getLocation();
            if (location == null) {
                return false;
            }
            URL[] urls;
            for (int length = (urls = this.urls).length, i = 0; i < length; ++i) {
                final URL url = urls[i];
                if (url == location) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public class StartupClassLoader extends URLClassLoader
    {
        public StartupClassLoader(final URL[] urls) {
            super(urls);
        }
        
        public StartupClassLoader(final URL[] urls, final ClassLoader parent) {
            super(urls, parent);
        }
        
        public StartupClassLoader(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
            super(urls, parent, factory);
        }
        
        @Override
        protected String findLibrary(final String name) {
            if (Main.this.extensionPaths == null) {
                return super.findLibrary(name);
            }
            final String libName = System.mapLibraryName(name);
            String[] extensionPaths;
            for (int length = (extensionPaths = Main.this.extensionPaths).length, i = 0; i < length; ++i) {
                final String extensionPath = extensionPaths[i];
                final File libFile = new File(extensionPath, libName);
                if (libFile.isFile()) {
                    return libFile.getAbsolutePath();
                }
            }
            return super.findLibrary(name);
        }
        
        public void addURL(final URL url) {
            super.addURL(url);
        }
        
        @Override
        protected URL findResource(final String moduleName, final String name) {
            return this.findResource(name);
        }
        
        @Override
        protected Class<?> findClass(final String moduleName, final String name) {
            try {
                return this.findClass(name);
            }
            catch (ClassNotFoundException ex) {
                return null;
            }
        }
    }
}
