/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.equinox.launcher.JNIBridge
 *  org.eclipse.equinox.launcher.Main$EclipsePolicy
 *  org.eclipse.equinox.launcher.Main$Identifier
 *  org.eclipse.equinox.launcher.Main$SplashHandler
 *  org.eclipse.equinox.launcher.Main$StartupClassLoader
 */
package org.eclipse.equinox.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.CodeSource;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.eclipse.equinox.launcher.JNIBridge;
import org.eclipse.equinox.launcher.Main;

public class Main {
    protected boolean debug = false;
    protected String bootLocation = null;
    protected URL installLocation = null;
    protected URL configurationLocation = null;
    protected String parentConfigurationLocation = null;
    protected String framework = "org.eclipse.osgi";
    protected String devClassPath = null;
    private Properties devClassPathProps = null;
    protected boolean inDevelopmentMode = false;
    protected String os = null;
    protected String ws = null;
    protected String arch = null;
    private String library = null;
    private String exitData = null;
    private String vm = null;
    private String[] vmargs = null;
    private String[] commands = null;
    String[] extensionPaths = null;
    JNIBridge bridge = null;
    private boolean showSplash = false;
    private String splashLocation = null;
    private String endSplash = null;
    private boolean initialize = false;
    protected boolean splashDown = false;
    private final Thread splashHandler = new SplashHandler(this);
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
    private static final String[] SPLASH_IMAGES = new String[]{"splash.png", "splash.jpg", "splash.jpeg", "splash.gif", "splash.bmp"};
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
    protected File logFile = null;
    protected BufferedWriter log = null;
    protected boolean newSession = true;
    private boolean protectBase = false;
    public static final String VARIABLE_DELIM_STRING = "$";
    public static final char VARIABLE_DELIM_CHAR = '$';
    private static final long NO_TIMESTAMP = -1L;
    private static final String BASE_TIMESTAMP_FILE_CONFIGINI = ".baseConfigIniTimestamp";
    private static final String KEY_CONFIGINI_TIMESTAMP = "configIniTimestamp";
    private static final String PROP_IGNORE_USER_CONFIGURATION = "eclipse.ignoreUserConfiguration";

    private String getWS() {
        if (this.ws != null) {
            return this.ws;
        }
        String osgiWs = System.getProperty(PROP_WS);
        if (osgiWs != null) {
            this.ws = osgiWs;
            return this.ws;
        }
        String osName = this.getOS();
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
        String osgiOs = System.getProperty(PROP_OS);
        if (osgiOs != null) {
            this.os = osgiOs;
            return this.os;
        }
        String osName = System.getProperty("os.name");
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
        String osgiArch = System.getProperty(PROP_ARCH);
        if (osgiArch != null) {
            this.arch = osgiArch;
            return this.arch;
        }
        String name = System.getProperty("os.arch");
        if (name.equalsIgnoreCase("amd64")) {
            return "x86_64";
        }
        return name;
    }

    private String getFragmentString(String fragmentOS, String fragmentWS, String fragmentArch) {
        StringJoiner buffer = new StringJoiner(".");
        buffer.add(PLUGIN_ID).add(fragmentWS).add(fragmentOS);
        if (!fragmentOS.equals("macosx") || "x86_64".equals(fragmentArch)) {
            buffer.add(fragmentArch);
        }
        return buffer.toString();
    }

    private void setupJNI(URL[] defaultPath) {
        if (this.bridge != null) {
            return;
        }
        String libPath = null;
        if (this.library != null) {
            File lib = new File(this.library);
            if (lib.isDirectory()) {
                libPath = this.searchFor(ECLIPSE, lib.getAbsolutePath());
            } else if (lib.exists()) {
                libPath = lib.getAbsolutePath();
            }
        }
        if (libPath == null) {
            String fragmentOS = this.getOS();
            String fragmentWS = this.getWS();
            String fragmentArch = this.getArch();
            libPath = this.getLibraryPath(this.getFragmentString(fragmentOS, fragmentWS, fragmentArch), defaultPath);
        }
        this.library = libPath;
        if (this.library != null) {
            this.bridge = new JNIBridge(this.library);
        }
    }

    private String getLibraryPath(String fragmentName, URL[] defaultPath) {
        URL[] urls;
        String dir;
        File location;
        String devPathList;
        String[] locations;
        String libPath = null;
        String fragment = null;
        if (this.inDevelopmentMode && this.devClassPathProps != null && (locations = this.getArrayFromList(devPathList = this.devClassPathProps.getProperty(PLUGIN_ID))).length > 0 && (location = new File(locations[0])).isAbsolute() && (fragment = this.searchFor(fragmentName, dir = location.getParent())) != null) {
            libPath = this.getLibraryFromFragment(fragment);
        }
        if (libPath == null && this.bootLocation != null && (urls = defaultPath) != null && urls.length > 0) {
            for (int i = urls.length - 1; i >= 0 && libPath == null; --i) {
                File entryFile = new File(urls[i].getFile());
                dir = entryFile.getParent();
                if (this.inDevelopmentMode) {
                    String devDir = String.valueOf(dir) + "/" + PLUGIN_ID + "/fragments";
                    fragment = this.searchFor(fragmentName, devDir);
                }
                if (fragment == null) {
                    fragment = this.searchFor(fragmentName, dir);
                }
                if (fragment == null) continue;
                libPath = this.getLibraryFromFragment(fragment);
            }
        }
        if (libPath == null) {
            URL install = this.getInstallLocation();
            String location2 = install.getFile();
            fragment = this.searchFor(fragmentName, location2 = String.valueOf(location2) + "/plugins/");
            if (fragment != null) {
                libPath = this.getLibraryFromFragment(fragment);
            }
        }
        return libPath;
    }

    private String getLibraryFromFragment(String fragment) {
        File frag;
        if (fragment.startsWith(FILE_SCHEME)) {
            fragment = fragment.substring(5);
        }
        if (!(frag = new File(fragment)).exists()) {
            return null;
        }
        if (frag.isDirectory()) {
            return this.searchFor(ECLIPSE, fragment);
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
        Enumeration<? extends ZipEntry> entries = fragmentJar.entries();
        String entry = null;
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.getName().startsWith("eclipse_")) continue;
            entry = zipEntry.getName();
            try {
                fragmentJar.close();
            }
            catch (IOException iOException) {}
            break;
        }
        if (entry != null) {
            String lib = this.extractFromJAR(fragment, entry);
            if (!this.getOS().equals("win32")) {
                try {
                    Runtime.getRuntime().exec(new String[]{"chmod", "755", lib}).waitFor();
                }
                catch (Throwable throwable) {}
            }
            return lib;
        }
        return null;
    }

    protected void basicRun(String[] args) throws Exception {
        System.setProperty("eclipse.startTime", Long.toString(System.currentTimeMillis()));
        this.commands = args;
        String[] passThruArgs = this.processCommandLine(args);
        if (!this.debug) {
            this.debug = System.getProperty(PROP_DEBUG) != null;
        }
        this.setupVMProperties();
        this.processConfiguration();
        if (this.protectBase && System.getProperty(PROP_SHARED_CONFIG_AREA) == null) {
            System.err.println("This application is configured to run in a cascaded mode only.");
            System.setProperty(PROP_EXITCODE, Integer.toString(14));
            return;
        }
        this.getInstallLocation();
        URL[] bootPath = this.getBootPath(this.bootLocation);
        this.setupJNI(bootPath);
        if (!this.checkVersion(System.getProperty("java.version"), System.getProperty(PROP_REQUIRED_JAVA_VERSION))) {
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

    protected void setSecurityPolicy(URL[] bootPath) {
        String eclipseSecurity = System.getProperty(PROP_ECLIPSESECURITY);
        if (eclipseSecurity != null) {
            ProtectionDomain domain = Main.class.getProtectionDomain();
            CodeSource source = null;
            if (domain != null) {
                source = Main.class.getProtectionDomain().getCodeSource();
            }
            if (domain == null || source == null) {
                this.log("Can not automatically set the security manager. Please use a policy file.");
                return;
            }
            URL[] rootURLs = new URL[bootPath.length + 1];
            rootURLs[0] = source.getLocation();
            System.arraycopy(bootPath, 0, rootURLs, 1, bootPath.length);
            EclipsePolicy eclipsePolicy = new EclipsePolicy(this, Policy.getPolicy(), rootURLs);
            Policy.setPolicy((Policy)eclipsePolicy);
        }
    }

    private void invokeFramework(String[] passThruArgs, URL[] bootPath) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, Error, Exception, InvocationTargetException {
        String type = PARENT_CLASSLOADER_BOOT;
        try {
            String javaVersion = System.getProperty("java.version");
            if (javaVersion != null && new Identifier(javaVersion).isGreaterEqualTo(new Identifier("1.9"))) {
                type = PARENT_CLASSLOADER_EXT;
            }
        }
        catch (NumberFormatException | SecurityException runtimeException) {}
        type = System.getProperty(PROP_PARENT_CLASSLOADER, type);
        type = System.getProperty(PROP_FRAMEWORK_PARENT_CLASSLOADER, type);
        ClassLoader parent = null;
        if (PARENT_CLASSLOADER_APP.equalsIgnoreCase(type)) {
            parent = ClassLoader.getSystemClassLoader();
        } else if (PARENT_CLASSLOADER_EXT.equalsIgnoreCase(type)) {
            ClassLoader appCL = ClassLoader.getSystemClassLoader();
            if (appCL != null) {
                parent = appCL.getParent();
            }
        } else if (PARENT_CLASSLOADER_CURRENT.equalsIgnoreCase(type)) {
            parent = this.getClass().getClassLoader();
        }
        StartupClassLoader loader = new StartupClassLoader(this, bootPath, parent);
        Class<?> clazz = loader.loadClass(STARTER);
        Method method = clazz.getDeclaredMethod("run", String[].class, Runnable.class);
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

    private boolean checkVersion(String availableVersion, String requiredVersion) {
        if (requiredVersion == null || availableVersion == null) {
            return true;
        }
        try {
            Identifier required = new Identifier(requiredVersion);
            Identifier available = new Identifier(availableVersion);
            boolean compatible = available.isGreaterEqualTo(required);
            if (!compatible) {
                System.setProperty(PROP_EXITCODE, "14");
                System.setProperty(PROP_EXITDATA, "<title>Incompatible JVM</title>Version " + availableVersion + " of the JVM is not suitable for this product. Version: " + requiredVersion + " or greater is required.");
            }
            return compatible;
        }
        catch (NumberFormatException | SecurityException runtimeException) {
            return true;
        }
    }

    private boolean checkConfigurationLocation(URL locationUrl) {
        if (locationUrl == null || !"file".equals(locationUrl.getProtocol())) {
            return true;
        }
        if (Boolean.parseBoolean(System.getProperty("osgi.configuration.area.readOnly"))) {
            return true;
        }
        File configDir = new File(locationUrl.getFile()).getAbsoluteFile();
        if (!configDir.exists()) {
            configDir.mkdirs();
            if (!configDir.exists()) {
                System.setProperty(PROP_EXITCODE, "15");
                System.setProperty(PROP_EXITDATA, "<title>Invalid Configuration Location</title>The configuration area at '" + configDir + "' could not be created.  Please choose a writable location using the '-configuration' command line option.");
                return false;
            }
        }
        if (!Main.canWrite(configDir)) {
            System.setProperty(PROP_EXITCODE, "15");
            System.setProperty(PROP_EXITDATA, "<title>Invalid Configuration Location</title>The configuration area at '" + configDir + "' is not writable.  Please choose a writable location using the '-configuration' command line option.");
            return false;
        }
        return true;
    }

    protected String decode(String urlString) {
        try {
            if (urlString.indexOf(43) >= 0) {
                int len = urlString.length();
                StringBuilder buf = new StringBuilder(len);
                for (int i = 0; i < len; ++i) {
                    char c = urlString.charAt(i);
                    if (c == '+') {
                        buf.append("%2B");
                        continue;
                    }
                    buf.append(c);
                }
                urlString = buf.toString();
            }
            return URLDecoder.decode(urlString, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    protected String[] getArrayFromList(String prop) {
        if (prop == null || prop.trim().equals("")) {
            return new String[0];
        }
        ArrayList<String> list = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(prop, ",");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.isEmpty()) continue;
            list.add(token);
        }
        return list.isEmpty() ? new String[0] : list.toArray(new String[list.size()]);
    }

    private URL[] getDevPath(URL base) throws IOException {
        ArrayList<URL> result = new ArrayList<URL>(5);
        if (this.inDevelopmentMode) {
            this.addDevEntries(base, result, OSGI);
        }
        this.addBaseJars(base, result);
        return result.toArray(new URL[result.size()]);
    }

    URL constructURL(URL url, String name) {
        String externalForm = url.toExternalForm();
        if (externalForm.endsWith(".jar")) {
            try {
                return new URL(JAR_SCHEME + url + "!/" + name);
            }
            catch (MalformedURLException malformedURLException) {}
        }
        try {
            return new URL(url, name);
        }
        catch (MalformedURLException malformedURLException) {
            return null;
        }
    }

    private void readFrameworkExtensions(URL base, ArrayList<URL> result) throws IOException {
        String[] extensions = this.getArrayFromList(System.getProperty(PROP_EXTENSIONS));
        String parent = new File(base.getFile()).getParent();
        ArrayList<String> extensionResults = new ArrayList<String>(extensions.length);
        String[] arrstring = extensions;
        int n = extensions.length;
        for (int i = 0; i < n; ++i) {
            String[] arrstring2;
            Properties extensionProperties;
            URL extensionURL;
            String path;
            String extension;
            block13: {
                extension = arrstring[i];
                path = this.searchForBundle(extension, parent);
                if (path == null) {
                    this.log("Could not find extension: " + extension);
                    continue;
                }
                if (this.debug) {
                    System.out.println("Loading extension: " + extension);
                }
                extensionURL = null;
                if (this.installLocation.getProtocol().equals("file")) {
                    extensionResults.add(path);
                    extensionURL = new File(path).toURL();
                } else {
                    extensionURL = new URL(this.installLocation.getProtocol(), this.installLocation.getHost(), this.installLocation.getPort(), path);
                }
                extensionProperties = null;
                try {
                    extensionProperties = this.loadProperties(this.constructURL(extensionURL, ECLIPSE_PROPERTIES));
                }
                catch (IOException iOException) {
                    if (!this.debug) break block13;
                    System.out.println("\teclipse.properties not found");
                }
            }
            String extensionClassPath = null;
            if (extensionProperties != null) {
                extensionClassPath = extensionProperties.getProperty(PROP_CLASSPATH);
            } else {
                extensionProperties = new Properties();
            }
            if (extensionClassPath == null || extensionClassPath.length() == 0) {
                String[] arrstring3 = new String[1];
                arrstring2 = arrstring3;
                arrstring3[0] = "";
            } else {
                arrstring2 = this.getArrayFromList(extensionClassPath);
            }
            String[] entries = arrstring2;
            String qualifiedPath = System.getProperty(PROP_CLASSPATH) == null ? "." : "";
            String[] arrstring4 = entries;
            int n2 = entries.length;
            for (int j = 0; j < n2; ++j) {
                String entry = arrstring4[j];
                qualifiedPath = String.valueOf(qualifiedPath) + ", file:" + path + entry;
            }
            extensionProperties.put(PROP_CLASSPATH, qualifiedPath);
            this.mergeWithSystemProperties(extensionProperties, null);
            if (!this.inDevelopmentMode) continue;
            String name = extension;
            if (name.startsWith(REFERENCE_SCHEME)) {
                name = new File(path).getName();
            }
            this.addDevEntries(extensionURL, result, name);
        }
        this.extensionPaths = extensionResults.toArray(new String[extensionResults.size()]);
    }

    private void addBaseJars(URL base, ArrayList<URL> result) throws IOException {
        File fwkFile;
        boolean fwkIsDirectory;
        String baseJarList = System.getProperty(PROP_CLASSPATH);
        if (baseJarList == null) {
            this.readFrameworkExtensions(base, result);
            baseJarList = System.getProperty(PROP_CLASSPATH);
        }
        if (fwkIsDirectory = (fwkFile = new File(base.getFile())).isDirectory()) {
            System.setProperty(PROP_FRAMEWORK_SHAPE, "folder");
        } else {
            System.setProperty(PROP_FRAMEWORK_SHAPE, "jar");
        }
        String fwkPath = new File(new File(base.getFile()).getParent()).getAbsolutePath();
        if (Character.isUpperCase(fwkPath.charAt(0))) {
            char[] chars = fwkPath.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            fwkPath = new String(chars);
        }
        System.setProperty(PROP_FRAMEWORK_SYSPATH, fwkPath);
        String[] baseJars = this.getArrayFromList(baseJarList);
        if (baseJars.length == 0) {
            if (!this.inDevelopmentMode && new File(base.getFile()).isDirectory()) {
                throw new IOException("Unable to initialize osgi.frameworkClassPath");
            }
            this.addEntry(base, result);
            return;
        }
        String[] arrstring = baseJars;
        int n = baseJars.length;
        for (int i = 0; i < n; ++i) {
            String string = arrstring[i];
            try {
                if (string.equals(".")) {
                    this.addEntry(base, result);
                }
                URL url = null;
                url = string.startsWith(FILE_SCHEME) ? new File(string.substring(5)).toURL() : new URL(string);
                this.addEntry(url, result);
                continue;
            }
            catch (MalformedURLException malformedURLException) {
                this.addEntry(new URL(base, string), result);
            }
        }
    }

    protected void addEntry(URL url, List<URL> result) {
        if (new File(url.getFile()).exists()) {
            result.add(url);
        }
    }

    private void addDevEntries(URL base, List<URL> result, String symbolicName) throws MalformedURLException {
        String[] locations;
        if (this.devClassPathProps == null) {
            return;
        }
        String devPathList = this.devClassPathProps.getProperty(symbolicName);
        if (devPathList == null) {
            devPathList = this.devClassPathProps.getProperty("*");
        }
        String[] arrstring = locations = this.getArrayFromList(devPathList);
        int n = locations.length;
        for (int i = 0; i < n; ++i) {
            URL url;
            String location = arrstring[i];
            File path = new File(location);
            if (path.isAbsolute()) {
                url = path.toURL();
            } else {
                char lastChar = location.charAt(location.length() - 1);
                url = location.endsWith(".jar") || lastChar == '/' || lastChar == '\\' ? new URL(base, location) : new URL(base, String.valueOf(location) + "/");
            }
            this.addEntry(url, result);
        }
    }

    private URL[] getBootPath(String base) throws IOException {
        URL url = null;
        if (base != null) {
            url = Main.buildURL(base, true);
        } else {
            url = this.getInstallLocation();
            String pluginsLocation = new File(url.getFile(), "plugins").toString();
            String path = this.searchFor(this.framework, pluginsLocation);
            if (path == null) {
                throw new FileNotFoundException(String.format("Could not find framework under %s", pluginsLocation));
            }
            url = url.getProtocol().equals("file") ? new File(path).toURL() : new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
        }
        if (System.getProperty(PROP_FRAMEWORK) == null) {
            System.setProperty(PROP_FRAMEWORK, url.toExternalForm());
        }
        if (this.debug) {
            System.out.println("Framework located:\n    " + url.toExternalForm());
        }
        URL[] result = this.getDevPath(url);
        if (this.debug) {
            System.out.println("Framework classpath:");
            URL[] arruRL = result;
            int n = result.length;
            for (int i = 0; i < n; ++i) {
                URL devPath = arruRL[i];
                System.out.println("    " + devPath.toExternalForm());
            }
        }
        return result;
    }

    protected String searchFor(String target, String start) {
        File root = Main.resolveFile(new File(start));
        String[] candidates = root.list();
        if (candidates == null) {
            return null;
        }
        ArrayList<String> matches = new ArrayList<String>(2);
        String[] arrstring = candidates;
        int n = candidates.length;
        for (int i = 0; i < n; ++i) {
            String candidate = arrstring[i];
            if (!this.isMatchingCandidate(target, candidate, root)) continue;
            matches.add(candidate);
        }
        String[] names = matches.toArray(new String[matches.size()]);
        int result = this.findMax(target, names);
        if (result == -1) {
            return null;
        }
        File candidate = new File(start, names[result]);
        return String.valueOf(candidate.getAbsolutePath().replace(File.separatorChar, '/')) + (candidate.isDirectory() ? "/" : "");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean isMatchingCandidate(String target, String candidate, File root) {
        if (candidate.equals(target)) {
            return true;
        }
        if (!candidate.startsWith(String.valueOf(target) + "_")) {
            return false;
        }
        targetLength = target.length();
        lastUnderscore = candidate.lastIndexOf(95);
        candidateFile = new File(root, candidate);
        if (candidateFile.isFile() && (candidate.endsWith(".jar") || candidate.endsWith(".zip"))) {
            extension = candidate.lastIndexOf(46);
            candidate = candidate.substring(0, extension);
        }
        if ((lastDot = candidate.lastIndexOf(46)) >= targetLength) ** GOTO lbl14
        return false;
lbl-1000:
        // 1 sources

        {
            lastUnderscore = candidate.lastIndexOf(95, lastUnderscore - 1);
lbl14:
            // 2 sources

            ** while (lastUnderscore > lastDot)
        }
lbl15:
        // 1 sources

        if (lastUnderscore != targetLength) return false;
        return true;
    }

    private String searchForBundle(String target, String start) {
        if (target.startsWith(REFERENCE_SCHEME)) {
            File child;
            if (!(target = target.substring(REFERENCE_SCHEME.length())).startsWith(FILE_SCHEME)) {
                throw new IllegalArgumentException("Bundle URL is invalid: " + target);
            }
            target = target.substring(FILE_SCHEME.length());
            File fileLocation = child = new File(target);
            if (!child.isAbsolute()) {
                File parent = Main.resolveFile(new File(start));
                fileLocation = new File(parent, child.getPath());
            }
            return this.searchFor(fileLocation.getName(), fileLocation.getParentFile().getAbsolutePath());
        }
        return this.searchFor(target, start);
    }

    protected int findMax(String prefix, String[] candidates) {
        int result = -1;
        Object[] maxVersion = null;
        for (int i = 0; i < candidates.length; ++i) {
            String name = candidates[i] != null ? candidates[i] : "";
            String version = "";
            if (prefix == null) {
                version = name;
            } else if (name.startsWith(String.valueOf(prefix) + "_")) {
                version = name.substring(prefix.length() + 1);
            }
            Object[] currentVersion = this.getVersionElements(version);
            if (maxVersion == null) {
                result = i;
                maxVersion = currentVersion;
                continue;
            }
            if (this.compareVersion(maxVersion, currentVersion) >= 0) continue;
            result = i;
            maxVersion = currentVersion;
        }
        return result;
    }

    private int compareVersion(Object[] left, Object[] right) {
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
        Object[] result = new Object[]{0, 0, 0, ""};
        StringTokenizer t = new StringTokenizer(version, ".");
        int i = 0;
        while (t.hasMoreTokens() && i < 4) {
            String token = t.nextToken();
            if (i < 3) {
                try {
                    result[i++] = Integer.valueOf(token);
                    continue;
                }
                catch (Exception exception) {
                    break;
                }
            }
            result[i++] = token;
        }
        return result;
    }

    private static URL buildURL(String spec, boolean trailingSlash) {
        if (spec == null) {
            return null;
        }
        if (File.separatorChar == '\\') {
            spec = spec.trim();
        }
        boolean isFile = spec.startsWith(FILE_SCHEME);
        try {
            if (isFile) {
                File toAdjust = new File(spec.substring(5));
                if ((toAdjust = Main.resolveFile(toAdjust)).isDirectory()) {
                    return Main.adjustTrailingSlash(toAdjust.toURL(), trailingSlash);
                }
                return toAdjust.toURL();
            }
            return new URL(spec);
        }
        catch (MalformedURLException malformedURLException) {
            if (isFile) {
                return null;
            }
            try {
                File toAdjust = new File(spec);
                if (toAdjust.isDirectory()) {
                    return Main.adjustTrailingSlash(toAdjust.toURL(), trailingSlash);
                }
                return toAdjust.toURL();
            }
            catch (MalformedURLException malformedURLException2) {
                return null;
            }
        }
    }

    private static File resolveFile(File toAdjust) {
        String installArea;
        if (!toAdjust.isAbsolute() && (installArea = System.getProperty(PROP_INSTALL_AREA)) != null) {
            if (installArea.startsWith(FILE_SCHEME)) {
                toAdjust = new File(installArea.substring(5), toAdjust.getPath());
            } else if (new File(installArea).exists()) {
                toAdjust = new File(installArea, toAdjust.getPath());
            }
        }
        return toAdjust;
    }

    private static URL adjustTrailingSlash(URL url, boolean trailingSlash) throws MalformedURLException {
        String file = url.getFile();
        if (trailingSlash == file.endsWith("/")) {
            return url;
        }
        file = trailingSlash ? String.valueOf(file) + "/" : file.substring(0, file.length() - 1);
        return new URL(url.getProtocol(), url.getHost(), file);
    }

    private URL buildLocation(String property, URL defaultLocation, String userDefaultAppendage) {
        URL result = null;
        String location = System.getProperty(property);
        System.clearProperty(property);
        try {
            if (location == null) {
                result = defaultLocation;
            } else {
                if (location.equalsIgnoreCase(NONE)) {
                    return null;
                }
                if (location.equalsIgnoreCase(NO_DEFAULT)) {
                    result = Main.buildURL(location, true);
                } else {
                    String base;
                    if (location.startsWith(USER_HOME)) {
                        base = this.substituteVar(location, USER_HOME, PROP_USER_HOME);
                        location = new File(base, userDefaultAppendage).getAbsolutePath();
                    } else if (location.startsWith(USER_DIR)) {
                        base = this.substituteVar(location, USER_DIR, PROP_USER_DIR);
                        location = new File(base, userDefaultAppendage).getAbsolutePath();
                    }
                    int idx = location.indexOf(INSTALL_HASH_PLACEHOLDER);
                    if (idx == 0) {
                        throw new RuntimeException("The location cannot start with '@install.hash': " + location);
                    }
                    if (idx > 0) {
                        location = String.valueOf(location.substring(0, idx)) + this.getInstallDirHash() + location.substring(idx + INSTALL_HASH_PLACEHOLDER.length());
                    }
                    result = Main.buildURL(location, true);
                }
            }
        }
        finally {
            if (result != null) {
                System.setProperty(property, result.toExternalForm());
            }
        }
        return result;
    }

    private String substituteVar(String source, String var, String prop) {
        String value = System.getProperty(prop, "");
        return String.valueOf(value) + source.substring(var.length());
    }

    private String computeDefaultConfigurationLocation() {
        File installDir;
        URL install = this.getInstallLocation();
        if (this.protectBase) {
            return this.computeDefaultUserAreaLocation(CONFIG_DIR);
        }
        if (install.getProtocol().equals("file") && Main.canWrite(installDir = new File(install.getFile()))) {
            return String.valueOf(installDir.getAbsolutePath()) + File.separator + CONFIG_DIR;
        }
        return this.computeDefaultUserAreaLocation(CONFIG_DIR);
    }

    private static boolean canWrite(File installDir) {
        if (!installDir.isDirectory()) {
            return false;
        }
        if (Files.isWritable(installDir.toPath())) {
            return true;
        }
        File fileTest = null;
        try {
            try {
                fileTest = File.createTempFile("writableArea", ".dll", installDir);
            }
            catch (IOException iOException) {
                if (fileTest != null) {
                    fileTest.delete();
                }
                return false;
            }
        }
        finally {
            if (fileTest != null) {
                fileTest.delete();
            }
        }
        return true;
    }

    private String computeDefaultUserAreaLocation(String pathAppendage) {
        URL installURL = this.getInstallLocation();
        if (installURL == null) {
            return null;
        }
        File installDir = new File(installURL.getFile());
        String installDirHash = this.getInstallDirHash();
        if (this.protectBase && "macosx".equals(this.os)) {
            this.initializeBridgeEarly();
            String macConfiguration = this.computeConfigurationLocationForMacOS();
            if (macConfiguration != null) {
                return macConfiguration;
            }
            if (this.debug) {
                System.out.println("Computation of Mac specific configuration folder failed.");
            }
        }
        String appName = ".eclipse";
        File eclipseProduct = new File(installDir, PRODUCT_SITE_MARKER);
        if (eclipseProduct.exists()) {
            Properties props = new Properties();
            try {
                String appVersion;
                props.load(new FileInputStream(eclipseProduct));
                String appId = props.getProperty(PRODUCT_SITE_ID);
                if (appId == null || appId.trim().length() == 0) {
                    appId = ECLIPSE;
                }
                if ((appVersion = props.getProperty(PRODUCT_SITE_VERSION)) == null || appVersion.trim().length() == 0) {
                    appVersion = "";
                }
                appName = String.valueOf(appName) + File.separator + appId + "_" + appVersion + "_" + installDirHash;
            }
            catch (IOException iOException) {
                appName = String.valueOf(appName) + File.separator + installDirHash;
            }
        } else {
            appName = String.valueOf(appName) + File.separator + installDirHash;
        }
        appName = String.valueOf(appName) + '_' + this.OS_WS_ARCHToString();
        String userHome = System.getProperty(PROP_USER_HOME);
        return new File(userHome, String.valueOf(appName) + "/" + pathAppendage).getAbsolutePath();
    }

    private String computeConfigurationLocationForMacOS() {
        if (this.bridge != null) {
            String folder = this.bridge.getOSRecommendedFolder();
            if (this.debug) {
                System.out.println("App folder provided by MacOS is: " + folder);
            }
            if (folder != null) {
                return String.valueOf(folder) + '/' + CONFIG_DIR;
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
        int hashCode;
        URL installURL = this.getInstallLocation();
        if (installURL == null) {
            return "";
        }
        File installDir = new File(installURL.getFile());
        try {
            hashCode = installDir.getCanonicalPath().hashCode();
        }
        catch (IOException iOException) {
            hashCode = installDir.getAbsolutePath().hashCode();
        }
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        return String.valueOf(hashCode);
    }

    public static void main(String argString) {
        ArrayList<String> list = new ArrayList<String>(5);
        StringTokenizer tokens = new StringTokenizer(argString, " ");
        while (tokens.hasMoreElements()) {
            list.add(tokens.nextToken());
        }
        Main.main(list.toArray(new String[list.size()]));
    }

    public static void main(String[] args) {
        int result = 0;
        try {
            try {
                result = new Main().run(args);
            }
            catch (Throwable t) {
                t.printStackTrace();
                if (!Boolean.getBoolean(PROP_NOSHUTDOWN) || result == 23) {
                    System.exit(result);
                }
            }
        }
        finally {
            if (!Boolean.getBoolean(PROP_NOSHUTDOWN) || result == 23) {
                System.exit(result);
            }
        }
    }

    public int run(String[] args) {
        int result;
        block12: {
            result = 0;
            try {
                try {
                    this.basicRun(args);
                    String exitCode = System.getProperty(PROP_EXITCODE);
                    try {
                        result = exitCode == null ? 0 : Integer.parseInt(exitCode);
                    }
                    catch (NumberFormatException numberFormatException) {
                        result = 17;
                    }
                }
                catch (Throwable e) {
                    if (!"13".equals(System.getProperty(PROP_EXITCODE))) {
                        this.log("Exception launching the Eclipse Platform:");
                        this.log(e);
                        String message = "An error has occurred";
                        message = this.logFile == null ? String.valueOf(message) + " and could not be logged: \n" + e.getMessage() : String.valueOf(message) + ".  See the log file\n" + this.logFile.getAbsolutePath();
                        System.setProperty(PROP_EXITDATA, message);
                    } else {
                        this.log("Are you trying to start an 64/32-bit Eclipse on a 32/64-JVM? These must be the same, as Eclipse uses native code.");
                    }
                    result = 13;
                    this.takeDownSplash();
                    if (this.bridge != null) {
                        this.bridge.uninitialize();
                    }
                    break block12;
                }
            }
            catch (Throwable throwable) {
                this.takeDownSplash();
                if (this.bridge != null) {
                    this.bridge.uninitialize();
                }
                throw throwable;
            }
            this.takeDownSplash();
            if (this.bridge != null) {
                this.bridge.uninitialize();
            }
        }
        System.setProperty(PROP_EXITCODE, Integer.toString(result));
        this.setExitData();
        return result;
    }

    private void setExitData() {
        String data = System.getProperty(PROP_EXITDATA);
        if (data == null) {
            return;
        }
        if (this.bridge == null || this.bridge.isLibraryLoadedByJava() && this.exitData == null) {
            System.out.println(data);
        } else {
            this.bridge.setExitData(this.exitData, data);
        }
    }

    protected String[] processCommandLine(String[] args) {
        if (args.length == 0) {
            return args;
        }
        int[] configArgs = new int[args.length];
        configArgs[0] = -1;
        int configArgIndex = 0;
        for (int i = 0; i < args.length; ++i) {
            boolean found = false;
            if (args[i].equalsIgnoreCase(DEBUG)) {
                this.debug = true;
                continue;
            }
            if (args[i].equalsIgnoreCase(NOSPLASH)) {
                this.splashDown = true;
                found = true;
            }
            if (args[i].equalsIgnoreCase(NOEXIT)) {
                System.setProperty(PROP_NOSHUTDOWN, "true");
                found = true;
            }
            if (args[i].equalsIgnoreCase(APPEND_VMARGS) || args[i].equalsIgnoreCase(OVERRIDE_VMARGS)) {
                found = true;
            }
            if (args[i].equalsIgnoreCase(INITIALIZE)) {
                this.initialize = true;
                continue;
            }
            if (args[i].equalsIgnoreCase(DEV) && (i + 1 == args.length || i + 1 < args.length && args[i + 1].startsWith("-"))) {
                this.inDevelopmentMode = true;
                continue;
            }
            if (args[i].equalsIgnoreCase(SHOWSPLASH)) {
                this.showSplash = true;
                found = true;
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    configArgs[configArgIndex++] = i++;
                    this.splashLocation = args[i];
                }
            }
            if (args[i].equalsIgnoreCase(PROTECT)) {
                found = true;
                configArgs[configArgIndex++] = i++;
                if (args[i].equalsIgnoreCase(PROTECT_MASTER) || args[i].equalsIgnoreCase(PROTECT_BASE)) {
                    this.protectBase = true;
                }
            }
            if (found) {
                configArgs[configArgIndex++] = i;
                continue;
            }
            if (args[i].equalsIgnoreCase(VMARGS)) {
                args[i] = null;
                this.vmargs = new String[args.length - ++i];
                int j = 0;
                while (i < args.length) {
                    this.vmargs[j++] = args[i];
                    args[i] = null;
                    ++i;
                }
                continue;
            }
            if (i == args.length - 1 || args[i + 1].startsWith("-")) continue;
            String arg = args[++i];
            if (args[i - 1].equalsIgnoreCase(DEV)) {
                this.inDevelopmentMode = true;
                this.devClassPathProps = this.processDevArg(arg);
                if (this.devClassPathProps == null) continue;
                this.devClassPath = this.devClassPathProps.getProperty(OSGI);
                if (this.devClassPath != null) continue;
                this.devClassPath = this.devClassPathProps.getProperty("*");
                continue;
            }
            if (args[i - 1].equalsIgnoreCase(FRAMEWORK)) {
                this.framework = arg;
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(OS)) {
                this.os = arg;
                continue;
            }
            if (args[i - 1].equalsIgnoreCase(WS)) {
                this.ws = arg;
                continue;
            }
            if (args[i - 1].equalsIgnoreCase(ARCH)) {
                this.arch = arg;
                continue;
            }
            if (args[i - 1].equalsIgnoreCase(INSTALL)) {
                System.setProperty(PROP_INSTALL_AREA, arg);
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(CONFIGURATION)) {
                System.setProperty(PROP_CONFIG_AREA, arg);
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(EXITDATA)) {
                this.exitData = arg;
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(NAME)) {
                System.setProperty(PROP_LAUNCHER_NAME, arg);
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(STARTUP)) {
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(LAUNCHER)) {
                System.setProperty(PROP_LAUNCHER, arg);
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(LIBRARY)) {
                this.library = arg;
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(ENDSPLASH)) {
                this.endSplash = arg;
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(VM)) {
                this.vm = arg;
                found = true;
            }
            if (args[i - 1].equalsIgnoreCase(NL)) {
                System.setProperty(PROP_NL, arg);
                found = true;
            }
            if (!found) continue;
            configArgs[configArgIndex++] = i - 1;
            configArgs[configArgIndex++] = i;
        }
        String[] passThruArgs = new String[args.length - configArgIndex - (this.vmargs == null ? 0 : this.vmargs.length + 1)];
        configArgIndex = 0;
        int j = 0;
        for (int i = 0; i < args.length; ++i) {
            if (i == configArgs[configArgIndex]) {
                ++configArgIndex;
                continue;
            }
            if (args[i] == null) continue;
            passThruArgs[j++] = args[i];
        }
        return passThruArgs;
    }

    private Properties processDevArg(String arg) {
        if (arg == null) {
            return null;
        }
        try {
            URL location = new URL(arg);
            return this.load(location, null);
        }
        catch (MalformedURLException malformedURLException) {
            Properties result = new Properties();
            result.put("*", arg);
            return result;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    private URL getConfigurationLocation() {
        if (this.configurationLocation != null) {
            return this.configurationLocation;
        }
        this.configurationLocation = this.buildLocation(PROP_CONFIG_AREA, null, "");
        if (this.configurationLocation == null) {
            this.configurationLocation = this.buildLocation(PROP_CONFIG_AREA_DEFAULT, null, "");
            if (this.configurationLocation == null) {
                this.configurationLocation = Main.buildURL(this.computeDefaultConfigurationLocation(), true);
            }
        }
        if (this.configurationLocation != null) {
            System.setProperty(PROP_CONFIG_AREA, this.configurationLocation.toExternalForm());
        }
        if (this.debug) {
            System.out.println("Configuration location:\n    " + this.configurationLocation);
        }
        return this.configurationLocation;
    }

    private void processConfiguration() {
        Properties configuration;
        URL baseConfigurationLocation = null;
        Properties baseConfiguration = null;
        if (System.getProperty(PROP_CONFIG_AREA) == null) {
            this.ensureAbsolute(PROP_BASE_CONFIG_AREA);
            String baseLocation = System.getProperty(PROP_BASE_CONFIG_AREA);
            if (baseLocation != null) {
                baseConfigurationLocation = Main.buildURL(baseLocation, true);
            }
            if (baseConfigurationLocation == null) {
                try {
                    baseConfigurationLocation = new URL(this.getInstallLocation(), CONFIG_DIR);
                }
                catch (MalformedURLException malformedURLException) {}
            }
            if ((baseConfiguration = this.loadConfiguration(baseConfigurationLocation)) != null) {
                String location = baseConfiguration.getProperty(PROP_CONFIG_AREA);
                if (location != null) {
                    System.setProperty(PROP_CONFIG_AREA, location);
                }
                if ((location = baseConfiguration.getProperty(PROP_INSTALL_AREA)) != null && System.getProperty(PROP_INSTALL_AREA) == null) {
                    System.setProperty(PROP_INSTALL_AREA, location);
                }
            }
        }
        if ((configuration = baseConfiguration) == null || !this.getConfigurationLocation().equals(baseConfigurationLocation)) {
            configuration = this.loadConfiguration(this.getConfigurationLocation());
        }
        if (configuration != null && "false".equalsIgnoreCase(configuration.getProperty(PROP_CONFIG_CASCADED))) {
            System.clearProperty(PROP_SHARED_CONFIG_AREA);
            configuration.remove(PROP_SHARED_CONFIG_AREA);
            this.mergeWithSystemProperties(configuration, null);
        } else {
            this.ensureAbsolute(PROP_SHARED_CONFIG_AREA);
            URL sharedConfigURL = this.buildLocation(PROP_SHARED_CONFIG_AREA, null, "");
            if (sharedConfigURL == null) {
                try {
                    sharedConfigURL = new URL(this.getInstallLocation(), CONFIG_DIR);
                }
                catch (MalformedURLException malformedURLException) {}
            }
            if (sharedConfigURL != null) {
                if (sharedConfigURL.equals(this.getConfigurationLocation())) {
                    System.clearProperty(PROP_SHARED_CONFIG_AREA);
                    this.mergeWithSystemProperties(configuration, null);
                } else {
                    Properties sharedConfiguration = baseConfiguration;
                    if (!sharedConfigURL.equals(baseConfigurationLocation)) {
                        sharedConfiguration = this.loadConfiguration(sharedConfigURL);
                    }
                    long sharedConfigTimestamp = this.getCurrentConfigIniBaseTimestamp(sharedConfigURL);
                    long lastKnownBaseTimestamp = this.getLastKnownConfigIniBaseTimestamp();
                    if (this.debug) {
                        System.out.println("Timestamps found: \n\t config.ini in the base: " + sharedConfigTimestamp + "\n\t remembered " + lastKnownBaseTimestamp);
                    }
                    if (lastKnownBaseTimestamp == sharedConfigTimestamp || lastKnownBaseTimestamp == -1L) {
                        this.mergeWithSystemProperties(configuration, null);
                    } else {
                        configuration = null;
                        System.setProperty(PROP_IGNORE_USER_CONFIGURATION, Boolean.TRUE.toString());
                    }
                    this.mergeWithSystemProperties(sharedConfiguration, configuration);
                    System.setProperty(PROP_SHARED_CONFIG_AREA, sharedConfigURL.toExternalForm());
                    if (this.debug) {
                        System.out.println("Shared configuration location:\n    " + sharedConfigURL.toExternalForm());
                    }
                }
            }
        }
        String urlString = System.getProperty(PROP_FRAMEWORK, null);
        if (urlString != null) {
            urlString = this.resolve(urlString);
            this.getInstallLocation();
            URL url = Main.buildURL(urlString, true);
            urlString = url.toExternalForm();
            System.setProperty(PROP_FRAMEWORK, urlString);
            this.bootLocation = urlString;
        }
    }

    private long getCurrentConfigIniBaseTimestamp(URL url) {
        try {
            url = new URL(url, CONFIG_FILE);
        }
        catch (MalformedURLException malformedURLException) {
            return -1L;
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        }
        catch (IOException iOException) {
            return -1L;
        }
        return connection.getLastModified();
    }

    private long getLastKnownConfigIniBaseTimestamp() {
        Properties result;
        if (this.debug) {
            System.out.println("Loading timestamp file from:\n\t " + this.getConfigurationLocation() + "   " + BASE_TIMESTAMP_FILE_CONFIGINI);
        }
        try {
            result = this.load(this.getConfigurationLocation(), BASE_TIMESTAMP_FILE_CONFIGINI);
        }
        catch (IOException iOException) {
            if (this.debug) {
                System.out.println("\tNo timestamp file found");
            }
            return -1L;
        }
        String timestamp = result.getProperty(KEY_CONFIGINI_TIMESTAMP);
        return Long.parseLong(timestamp);
    }

    private void ensureAbsolute(String locationProperty) {
        String propertyValue = System.getProperty(locationProperty);
        if (propertyValue == null) {
            return;
        }
        URL locationURL = null;
        try {
            locationURL = new URL(propertyValue);
        }
        catch (MalformedURLException malformedURLException) {
            return;
        }
        String locationPath = locationURL.getPath();
        if (locationPath.startsWith("/")) {
            return;
        }
        URL installURL = this.getInstallLocation();
        if (!locationURL.getProtocol().equals(installURL.getProtocol())) {
            return;
        }
        try {
            URL absoluteURL = new URL(installURL, locationPath);
            System.setProperty(locationProperty, absoluteURL.toExternalForm());
        }
        catch (MalformedURLException malformedURLException) {}
    }

    private URL getInstallLocation() {
        if (this.installLocation != null) {
            return this.installLocation;
        }
        String installArea = System.getProperty(PROP_INSTALL_AREA);
        if (installArea != null) {
            if (installArea.startsWith(LAUNCHER_DIR)) {
                String launcher = System.getProperty(PROP_LAUNCHER);
                if (launcher == null) {
                    throw new IllegalStateException("Install location depends on launcher, but launcher is not defined");
                }
                installArea = installArea.replace(LAUNCHER_DIR, new File(launcher).getParent());
            }
            this.installLocation = Main.buildURL(installArea, true);
            if (this.installLocation == null) {
                throw new IllegalStateException("Install location is invalid: " + installArea);
            }
            System.setProperty(PROP_INSTALL_AREA, this.installLocation.toExternalForm());
            if (this.debug) {
                System.out.println("Install location:\n    " + this.installLocation);
            }
            return this.installLocation;
        }
        ProtectionDomain domain = Main.class.getProtectionDomain();
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
            catch (MalformedURLException malformedURLException) {}
        }
        if (source != null) {
            result = source.getLocation();
        }
        String path = this.decode(result.getFile());
        File file = new File(path);
        path = file.toString().replace('\\', '/');
        if (File.separatorChar == '\\' && Character.isUpperCase(path.charAt(0))) {
            char[] chars = path.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            path = new String(chars);
        }
        if (path.toLowerCase().endsWith(".jar")) {
            path = path.substring(0, path.lastIndexOf(47) + 1);
        }
        if (path.toLowerCase().endsWith("/plugins/")) {
            path = path.substring(0, path.length() - "/plugins/".length());
        }
        try {
            try {
                path = new File(path).toURL().getFile();
            }
            catch (MalformedURLException malformedURLException) {}
            this.installLocation = new URL(result.getProtocol(), result.getHost(), result.getPort(), path);
            System.setProperty(PROP_INSTALL_AREA, this.installLocation.toExternalForm());
        }
        catch (MalformedURLException malformedURLException) {}
        if (this.debug) {
            System.out.println("Install location:\n    " + this.installLocation);
        }
        return this.installLocation;
    }

    private Properties loadConfiguration(URL url) {
        Properties result;
        block6: {
            result = null;
            try {
                url = new URL(url, CONFIG_FILE);
            }
            catch (MalformedURLException malformedURLException) {
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
            catch (IOException iOException) {
                if (!this.debug) break block6;
                System.out.println(" not found or not read");
            }
        }
        return this.substituteVars(result);
    }

    private Properties loadProperties(URL url) throws IOException {
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
                result = this.load(url, CONFIG_FILE_TEMP_SUFFIX);
            }
            catch (IOException iOException) {
                try {
                    result = this.load(url, CONFIG_FILE_BAK_SUFFIX);
                }
                catch (IOException iOException2) {
                    throw originalException;
                }
            }
        }
        return result;
    }

    private Properties load(URL url, String suffix) throws IOException {
        if (suffix != null && !suffix.equals("")) {
            url = new URL(url.getProtocol(), url.getHost(), url.getPort(), String.valueOf(url.getFile()) + suffix);
        }
        Properties props = new Properties();
        Throwable throwable = null;
        Object var5_6 = null;
        try (InputStream is = this.getStream(url);){
            props.load(is);
        }
        catch (Throwable throwable2) {
            if (throwable == null) {
                throwable = throwable2;
            } else if (throwable != throwable2) {
                throwable.addSuppressed(throwable2);
            }
            throw throwable;
        }
        return props;
    }

    private InputStream getStream(URL location) throws IOException {
        File f;
        if ("file".equalsIgnoreCase(location.getProtocol()) && (f = new File(location.getPath())).exists()) {
            return new FileInputStream(f);
        }
        return location.openStream();
    }

    private void handleSplash(URL[] defaultPath) {
        if (this.initialize || this.splashDown || this.bridge == null) {
            this.showSplash = false;
            this.endSplash = null;
            return;
        }
        if (this.showSplash || this.endSplash != null) {
            try {
                Runtime.getRuntime().addShutdownHook(this.splashHandler);
            }
            catch (Throwable throwable) {}
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
        this.bridge.setLauncherInfo(System.getProperty(PROP_LAUNCHER), System.getProperty(PROP_LAUNCHER_NAME));
        this.bridge.showSplash(this.splashLocation);
        long handle = this.bridge.getSplashHandle();
        if (handle != 0L && handle != -1L) {
            System.setProperty(SPLASH_HANDLE, String.valueOf(handle));
            System.setProperty(SPLASH_LOCATION, this.splashLocation);
            this.bridge.updateSplash();
        } else {
            this.splashDown = true;
        }
    }

    protected void takeDownSplash() {
        if (this.splashDown || this.bridge == null) {
            return;
        }
        this.splashDown = this.bridge.takeDownSplash();
        System.clearProperty(SPLASH_HANDLE);
        try {
            Runtime.getRuntime().removeShutdownHook(this.splashHandler);
        }
        catch (Throwable throwable) {}
    }

    private String getSplashLocation(URL[] bootPath) {
        if (this.splashLocation != null && !Character.isDigit(this.splashLocation.charAt(0)) && new File(this.splashLocation).exists()) {
            System.setProperty(PROP_SPLASHLOCATION, this.splashLocation);
            return this.splashLocation;
        }
        String result = System.getProperty(PROP_SPLASHLOCATION);
        if (result != null) {
            return result;
        }
        String splashPath = System.getProperty(PROP_SPLASHPATH);
        if (splashPath != null) {
            String[] entries = this.getArrayFromList(splashPath);
            ArrayList<String> path = new ArrayList<String>(entries.length);
            String[] arrstring = entries;
            int n = entries.length;
            for (int i = 0; i < n; ++i) {
                String e = arrstring[i];
                String entry = this.resolve(e);
                if (entry != null && entry.startsWith(FILE_SCHEME)) {
                    File entryFile = new File(entry.substring(5).replace('/', File.separatorChar));
                    if ((entry = this.searchFor(entryFile.getName(), entryFile.getParent())) == null) continue;
                    path.add(entry);
                    continue;
                }
                this.log("Invalid splash path entry: " + e);
            }
            result = this.searchForSplash(path.toArray(new String[path.size()]));
            if (result != null) {
                System.setProperty(PROP_SPLASHLOCATION, result);
                return result;
            }
        }
        return result;
    }

    private String searchForSplash(String[] searchPath) {
        String[] nlVariants;
        if (searchPath == null) {
            return null;
        }
        String locale = System.getProperty(PROP_NL);
        if (locale == null) {
            locale = Locale.getDefault().toString();
        }
        String[] arrstring = nlVariants = Main.buildNLVariants(locale);
        int n = nlVariants.length;
        for (int i = 0; i < n; ++i) {
            String nlVariant = arrstring[i];
            String[] arrstring2 = searchPath;
            int n2 = searchPath.length;
            for (int j = 0; j < n2; ++j) {
                Object result;
                String path = arrstring2[j];
                if (path.startsWith(FILE_SCHEME)) {
                    path = path.substring(5);
                }
                if (this.isJAR(path)) {
                    result = this.extractFromJAR(path, nlVariant);
                    if (result == null) continue;
                    return result;
                }
                if (!path.endsWith(File.separator)) {
                    path = String.valueOf(path) + File.separator;
                }
                if (!((File)(result = new File(path = String.valueOf(path) + nlVariant))).exists()) continue;
                return ((File)result).getAbsolutePath();
            }
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private String extractFromJAR(String jarPath, String jarEntry) {
        String configLocation = System.getProperty(PROP_CONFIG_AREA);
        if (configLocation == null) {
            this.log("Configuration area not set yet. Unable to extract " + jarEntry + " from JAR'd plug-in: " + jarPath);
            return null;
        }
        URL configURL = Main.buildURL(configLocation, false);
        if (configURL == null) {
            return null;
        }
        File splash = new File(configURL.getPath(), PLUGIN_ID);
        File jarFile = new File(jarPath);
        String cache = jarFile.getName();
        if (cache.endsWith(".jar")) {
            cache = cache.substring(0, cache.length() - 4);
        }
        splash = new File(splash, cache);
        if ((splash = new File(splash, jarEntry)).exists()) {
            boolean clean = false;
            String[] arrstring = this.commands;
            int n = this.commands.length;
            for (int i = 0; i < n; ++i) {
                String command = arrstring[i];
                if (!CLEAN.equalsIgnoreCase(command)) continue;
                clean = true;
                splash.delete();
                break;
            }
            if (!clean) {
                return splash.getAbsolutePath();
            }
        }
        try {
            Throwable clean = null;
            Object var9_17 = null;
            try (ZipFile file = new ZipFile(jarPath);){
                String string;
                ZipEntry entry = file.getEntry(jarEntry.replace(File.separatorChar, '/'));
                if (entry == null) {
                    return null;
                }
                Path outputFile = splash.toPath();
                Files.createDirectories(outputFile.getParent(), new FileAttribute[0]);
                try {
                    Throwable throwable = null;
                    Object var14_22 = null;
                    try (InputStream input = file.getInputStream(entry);){
                        Files.copy(input, outputFile, new CopyOption[0]);
                    }
                    catch (Throwable throwable2) {
                        if (throwable == null) {
                            throwable = throwable2;
                            throw throwable;
                        }
                        if (throwable == throwable2) throw throwable;
                        throwable.addSuppressed(throwable2);
                        throw throwable;
                    }
                }
                catch (IOException e) {
                    this.log("Exception opening splash: " + entry.getName() + " in JAR file: " + jarPath);
                    this.log(e);
                    if (file == null) return null;
                    file.close();
                    return null;
                }
                if (splash.exists()) {
                    string = splash.getAbsolutePath();
                    return string;
                }
                string = null;
                return string;
            }
            catch (Throwable throwable) {
                if (clean == null) {
                    clean = throwable;
                    throw clean;
                }
                if (clean == throwable) throw clean;
                clean.addSuppressed(throwable);
                throw clean;
            }
        }
        catch (IOException e) {
            this.log("Exception looking for " + jarEntry + " in JAR file: " + jarPath);
            this.log(e);
            return null;
        }
    }

    private boolean isJAR(String path) {
        return new File(path).isFile();
    }

    private static String[] buildNLVariants(String locale) {
        String name;
        int n;
        int n2;
        String[] arrstring;
        String nl = locale;
        ArrayList<String> result = new ArrayList<String>(4);
        while (true) {
            arrstring = SPLASH_IMAGES;
            n2 = SPLASH_IMAGES.length;
            for (n = 0; n < n2; ++n) {
                name = arrstring[n];
                result.add("nl" + File.separatorChar + nl.replace('_', File.separatorChar) + File.separatorChar + name);
            }
            int lastSeparator = nl.lastIndexOf(95);
            if (lastSeparator == -1) break;
            nl = nl.substring(0, lastSeparator);
        }
        arrstring = SPLASH_IMAGES;
        n2 = SPLASH_IMAGES.length;
        for (n = 0; n < n2; ++n) {
            name = arrstring[n];
            result.add(name);
        }
        return result.toArray(new String[result.size()]);
    }

    private String resolve(String urlString) {
        if (urlString.startsWith(REFERENCE_SCHEME)) {
            urlString = urlString.substring(10);
        }
        if (urlString.startsWith(PLATFORM_URL)) {
            String path = urlString.substring(PLATFORM_URL.length());
            return this.getInstallLocation() + path;
        }
        return urlString;
    }

    protected synchronized void log(Object obj) {
        if (obj == null) {
            return;
        }
        try {
            try {
                this.openLogFile();
                try {
                    if (this.newSession) {
                        this.log.write(SESSION);
                        this.log.write(32);
                        String timestamp = new Date().toString();
                        this.log.write(timestamp);
                        this.log.write(32);
                        for (int i = SESSION.length() + timestamp.length(); i < 78; ++i) {
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
                    } else {
                        this.closeLogFile();
                    }
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
                this.log = null;
            }
        }
        finally {
            this.log = null;
        }
    }

    private void write(Object obj) throws IOException {
        if (obj == null) {
            return;
        }
        if (obj instanceof Throwable) {
            this.log.write(STACK);
            this.log.newLine();
            ((Throwable)obj).printStackTrace(new PrintWriter(this.log));
        } else {
            this.log.write(ENTRY);
            this.log.write(32);
            this.log.write(PLUGIN_ID);
            this.log.write(32);
            this.log.write(String.valueOf(4));
            this.log.write(32);
            this.log.write(String.valueOf(0));
            this.log.write(32);
            this.log.write(this.getDate(new Date()));
            this.log.newLine();
            this.log.write(MESSAGE);
            this.log.write(32);
            this.log.write(String.valueOf(obj));
        }
        this.log.newLine();
    }

    protected String getDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        StringBuilder sb = new StringBuilder();
        this.appendPaddedInt(c.get(1), 4, sb).append('-');
        this.appendPaddedInt(c.get(2) + 1, 2, sb).append('-');
        this.appendPaddedInt(c.get(5), 2, sb).append(' ');
        this.appendPaddedInt(c.get(11), 2, sb).append(':');
        this.appendPaddedInt(c.get(12), 2, sb).append(':');
        this.appendPaddedInt(c.get(13), 2, sb).append('.');
        this.appendPaddedInt(c.get(14), 3, sb);
        return sb.toString();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private StringBuilder appendPaddedInt(int value, int pad, StringBuilder buffer) {
        if (--pad == 0) {
            return buffer.append(value);
        }
        padding = (int)Math.pow(10.0, pad);
        if (value < padding) ** GOTO lbl9
        return buffer.append(value);
lbl-1000:
        // 1 sources

        {
            buffer.append('0');
            padding /= 10;
lbl9:
            // 2 sources

            ** while (padding > value && padding > 1)
        }
lbl10:
        // 1 sources

        buffer.append(value);
        return buffer;
    }

    private void computeLogFileLocation() {
        String logFileProp = System.getProperty(PROP_LOGFILE);
        if (logFileProp != null) {
            if (this.logFile == null || !logFileProp.equals(this.logFile.getAbsolutePath())) {
                this.logFile = new File(logFileProp);
                new File(this.logFile.getParent()).mkdirs();
            }
            return;
        }
        URL base = Main.buildURL(System.getProperty(PROP_CONFIG_AREA), false);
        if (base == null) {
            return;
        }
        this.logFile = new File(base.getPath(), String.valueOf(System.currentTimeMillis()) + ".log");
        new File(this.logFile.getParent()).mkdirs();
        System.setProperty(PROP_LOGFILE, this.logFile.getAbsolutePath());
    }

    private void openLogFile() throws IOException {
        this.computeLogFileLocation();
        try {
            this.log = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.logFile.getAbsolutePath(), true), StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            this.logFile = null;
            throw e;
        }
    }

    private BufferedWriter logForStream(OutputStream output) {
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
    }

    private void mergeWithSystemProperties(Properties source, Properties userConfiguration) {
        if (source == null) {
            return;
        }
        Enumeration<Object> e = source.keys();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            if (key.equals(PROP_CLASSPATH)) {
                String destinationClasspath = System.getProperty(PROP_CLASSPATH);
                String sourceClasspath = source.getProperty(PROP_CLASSPATH);
                destinationClasspath = destinationClasspath == null ? sourceClasspath : String.valueOf(destinationClasspath) + sourceClasspath;
                System.setProperty(PROP_CLASSPATH, destinationClasspath);
                continue;
            }
            String value = source.getProperty(key);
            if (userConfiguration != null && !key.endsWith(".override.user")) {
                boolean shouldOverride;
                String overrideKey = String.valueOf(key) + ".override.user";
                boolean bl = shouldOverride = System.getProperty(overrideKey) != null || source.getProperty(overrideKey) != null;
                if (shouldOverride && !userConfiguration.contains(key)) {
                    System.setProperty(key, value);
                    continue;
                }
            }
            if (System.getProperty(key) != null) continue;
            System.setProperty(key, value);
        }
    }

    private void setupVMProperties() {
        if (this.vm != null) {
            System.setProperty(PROP_VM, this.vm);
        }
        this.setMultiValueProperty(PROP_VMARGS, this.vmargs);
        this.setMultiValueProperty(PROP_COMMANDS, this.commands);
    }

    private void setMultiValueProperty(String property, String[] values) {
        if (values != null) {
            StringBuilder result = new StringBuilder(300);
            String[] arrstring = values;
            int n = values.length;
            for (int i = 0; i < n; ++i) {
                String value = arrstring[i];
                if (value == null) continue;
                result.append(value);
                result.append('\n');
            }
            System.setProperty(property, result.toString());
        }
    }

    private Properties substituteVars(Properties result) {
        if (result == null) {
            return null;
        }
        Enumeration<Object> eKeys = result.keys();
        while (eKeys.hasMoreElements()) {
            String value;
            Object key = eKeys.nextElement();
            if (!(key instanceof String) || (value = result.getProperty((String)key)) == null) continue;
            result.put(key, Main.substituteVars(value));
        }
        return result;
    }

    public static String substituteVars(String path) {
        StringBuilder buf = new StringBuilder(path.length());
        StringTokenizer st = new StringTokenizer(path, VARIABLE_DELIM_STRING, true);
        boolean varStarted = false;
        String var = null;
        while (st.hasMoreElements()) {
            String tok = st.nextToken();
            if (VARIABLE_DELIM_STRING.equals(tok)) {
                if (!varStarted) {
                    varStarted = true;
                    var = "";
                    continue;
                }
                String prop = null;
                if (var != null && var.length() > 0) {
                    prop = System.getProperty(var);
                }
                if (prop == null) {
                    prop = System.getenv(var);
                }
                if (prop != null) {
                    buf.append(prop);
                } else {
                    buf.append('$');
                    buf.append(var == null ? "" : var);
                    buf.append('$');
                }
                varStarted = false;
                var = null;
                continue;
            }
            if (!varStarted) {
                buf.append(tok);
                continue;
            }
            var = tok;
        }
        if (var != null) {
            buf.append('$').append(var);
        }
        return buf.toString();
    }
}
