// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.equinox.launcher;

import java.util.jar.Manifest;
import java.util.StringTokenizer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.zip.ZipFile;
import java.util.jar.JarFile;
import java.net.URLConnection;
import java.net.JarURLConnection;
import java.util.Enumeration;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Deprecated
public class WebStartMain extends Main
{
    private static final String PROP_WEBSTART_AUTOMATIC_INSTALLATION = "eclipse.webstart.automaticInstallation";
    private static final String DEFAULT_OSGI_BUNDLES = "org.eclipse.equinox.common@2:start, org.eclipse.core.runtime@start";
    private static final String PROP_OSGI_BUNDLES = "osgi.bundles";
    private static final String PROP_CHECK_CONFIG = "osgi.checkConfiguration";
    private Map<String, List<BundleInfo>> allBundles;
    private List<BundleInfo> bundleList;
    
    public WebStartMain() {
        this.allBundles = null;
        this.bundleList = null;
    }
    
    public static void main(final String[] args) {
        System.setSecurityManager(null);
        final int result = new WebStartMain().run(args);
        if (!Boolean.getBoolean("osgi.noShutdown")) {
            System.exit(result);
        }
    }
    
    private void setDefaultBundles() {
        if (System.getProperty("osgi.bundles") != null) {
            return;
        }
        System.getProperties().put("osgi.bundles", "org.eclipse.equinox.common@2:start, org.eclipse.core.runtime@start");
    }
    
    @Override
    protected void basicRun(final String[] args) throws Exception {
        this.setDefaultBundles();
        this.initializeBundleListStructure();
        this.discoverBundles();
        final String fwkURL = this.searchFor(this.framework, null);
        this.allBundles.remove(this.framework);
        System.getProperties().put("osgi.framework", fwkURL);
        super.basicRun(args);
    }
    
    @Override
    protected void beforeFwkInvocation() {
        if (System.getProperty("osgi.checkConfiguration") == null) {
            System.getProperties().put("osgi.checkConfiguration", "true");
        }
        this.buildOSGiBundleList();
        this.cleanup();
    }
    
    private void cleanup() {
        this.allBundles = null;
        this.bundleList = null;
    }
    
    @Override
    protected String searchFor(final String target, final String start) {
        final List<BundleInfo> matches = this.allBundles.get(target);
        if (matches == null) {
            return null;
        }
        final int numberOfMatches = matches.size();
        if (numberOfMatches == 1) {
            return matches.get(0).location;
        }
        if (numberOfMatches == 0) {
            return null;
        }
        final String[] versions = new String[numberOfMatches];
        int highest = 0;
        for (int i = 0; i < versions.length; ++i) {
            versions[i] = matches.get(i).version;
        }
        highest = this.findMax(null, versions);
        return matches.get(highest).location;
    }
    
    private BundleInfo findBundle(final String target, final String version, final boolean removeMatch) {
        final List<BundleInfo> matches = this.allBundles.get(target);
        final int numberOfMatches = (matches != null) ? matches.size() : 0;
        if (numberOfMatches == 1) {
            return removeMatch ? matches.remove(0) : matches.get(0);
        }
        if (numberOfMatches == 0) {
            return null;
        }
        if (version != null) {
            final Iterator<BundleInfo> iterator = matches.iterator();
            while (iterator.hasNext()) {
                final BundleInfo bi = iterator.next();
                if (bi.version.equals(version)) {
                    if (removeMatch) {
                        iterator.remove();
                    }
                    return bi;
                }
            }
            return null;
        }
        final String[] versions = new String[numberOfMatches];
        int highest = 0;
        for (int i = 0; i < versions.length; ++i) {
            versions[i] = matches.get(i).version;
        }
        highest = this.findMax(null, versions);
        return removeMatch ? matches.remove(highest) : matches.get(highest);
    }
    
    private void discoverBundles() {
        this.allBundles = new HashMap<String, List<BundleInfo>>();
        try {
            final Enumeration<URL> resources = WebStartMain.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                final BundleInfo found = this.getBundleInfo(resources.nextElement());
                if (found == null) {
                    continue;
                }
                List<BundleInfo> matching = this.allBundles.get(found.bsn);
                if (matching == null) {
                    matching = new ArrayList<BundleInfo>(1);
                    this.allBundles.put(found.bsn, matching);
                }
                matching.add(found);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String extractInnerURL(final URL url) {
        try {
            URLConnection connection = null;
            try {
                connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    final JarFile jarFile = ((JarURLConnection)connection).getJarFile();
                    String name = jarFile.getName();
                    if (name == null || name.length() == 0) {
                        name = this.getJarNameByReflection(jarFile);
                    }
                    if (name != null && name.length() > 0) {
                        return "file:" + name;
                    }
                }
            }
            finally {
                if (connection != null) {
                    connection.getInputStream().close();
                }
            }
            if (connection != null) {
                connection.getInputStream().close();
            }
        }
        catch (IOException ex) {}
        return url.toExternalForm();
    }
    
    private String getJarNameByReflection(final JarFile jarFile) {
        if (jarFile == null) {
            return null;
        }
        Field nameField = null;
        try {
            nameField = ZipFile.class.getDeclaredField("name");
        }
        catch (NoSuchFieldException ex) {
            try {
                nameField = ZipFile.class.getDeclaredField("fileName");
            }
            catch (NoSuchFieldException ex2) {}
        }
        if (nameField == null || Modifier.isStatic(nameField.getModifiers()) || nameField.getType() != String.class) {
            return null;
        }
        try {
            nameField.setAccessible(true);
            return (String)nameField.get(jarFile);
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException ex3) {
            return null;
        }
    }
    
    private void initializeBundleListStructure() {
        final String prop = System.getProperty("osgi.bundles");
        if (prop == null || prop.trim().equals("")) {
            this.bundleList = new ArrayList<BundleInfo>(0);
            return;
        }
        this.bundleList = new ArrayList<BundleInfo>(10);
        final StringTokenizer tokens = new StringTokenizer(prop, ",");
        while (tokens.hasMoreTokens()) {
            final String bundleId;
            final String token = bundleId = tokens.nextToken().trim();
            if (token.equals("")) {
                continue;
            }
            final BundleInfo toAdd = new BundleInfo();
            toAdd.bsn = bundleId;
            final int startLevelSeparator;
            if ((startLevelSeparator = token.lastIndexOf(64)) != -1) {
                toAdd.bsn = token.substring(0, startLevelSeparator);
                toAdd.startData = token.substring(startLevelSeparator);
            }
            this.bundleList.add(toAdd);
        }
    }
    
    private BundleInfo getBundleInfo(final URL manifestURL) {
        try {
            final Manifest mf = new Manifest(manifestURL.openStream());
            final String symbolicNameString = mf.getMainAttributes().getValue("Bundle-SymbolicName");
            if (symbolicNameString == null) {
                return null;
            }
            final BundleInfo result = new BundleInfo();
            final String version = mf.getMainAttributes().getValue("Bundle-Version");
            result.version = ((version != null) ? version : "0.0.0");
            result.location = this.extractInnerURL(manifestURL);
            final int pos = symbolicNameString.lastIndexOf(59);
            if (pos != -1) {
                result.bsn = symbolicNameString.substring(0, pos);
                return result;
            }
            result.bsn = symbolicNameString;
            return result;
        }
        catch (IOException e) {
            if (this.debug) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    private void buildOSGiBundleList() {
        final StringBuilder finalBundleList = new StringBuilder(this.allBundles.size() * 30);
        for (final BundleInfo searched : this.bundleList) {
            final BundleInfo found = this.findBundle(searched.bsn, searched.version, true);
            if (found != null) {
                finalBundleList.append("reference:").append(found.location).append(searched.startData).append(',');
            }
        }
        if (!Boolean.FALSE.toString().equalsIgnoreCase(System.getProperties().getProperty("eclipse.webstart.automaticInstallation"))) {
            for (final List<BundleInfo> toAdd : this.allBundles.values()) {
                for (final BundleInfo bi : toAdd) {
                    finalBundleList.append("reference:").append(bi.location).append(',');
                }
            }
        }
        System.getProperties().put("osgi.bundles", finalBundleList.toString());
    }
    
    protected class BundleInfo
    {
        String bsn;
        String version;
        String startData;
        String location;
    }
}
