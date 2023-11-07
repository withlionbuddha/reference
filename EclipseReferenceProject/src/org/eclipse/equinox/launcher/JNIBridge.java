// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.equinox.launcher;

public class JNIBridge
{
    private String library;
    private boolean libraryLoaded;
    
    private native void _set_exit_data(final String p0, final String p1);
    
    private native void _set_launcher_info(final String p0, final String p1);
    
    private native void _update_splash();
    
    private native long _get_splash_handle();
    
    private native void _show_splash(final String p0);
    
    private native void _takedown_splash();
    
    private native String _get_os_recommended_folder();
    
    private native int OleInitialize(final int p0);
    
    private native void OleUninitialize();
    
    public JNIBridge(final String library) {
        this.libraryLoaded = false;
        this.library = library;
    }
    
    private void loadLibrary() {
        if (this.library != null) {
            try {
                if (this.library.contains("wpf")) {
                    final int idx = this.library.indexOf("eclipse_");
                    if (idx != -1) {
                        String comLibrary = String.valueOf(this.library.substring(0, idx)) + "com_";
                        comLibrary = String.valueOf(comLibrary) + this.library.substring(idx + 8, this.library.length());
                        Runtime.getRuntime().load(comLibrary);
                        this.OleInitialize(0);
                    }
                }
                Runtime.getRuntime().load(this.library);
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        this.libraryLoaded = true;
    }
    
    public boolean setExitData(final String sharedId, final String data) {
        try {
            this._set_exit_data(sharedId, data);
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.setExitData(sharedId, data);
            }
            return false;
        }
    }
    
    public boolean setLauncherInfo(final String launcher, final String name) {
        try {
            this._set_launcher_info(launcher, name);
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.setLauncherInfo(launcher, name);
            }
            return false;
        }
    }
    
    public boolean showSplash(final String bitmap) {
        try {
            this._show_splash(bitmap);
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.showSplash(bitmap);
            }
            return false;
        }
    }
    
    public boolean updateSplash() {
        try {
            this._update_splash();
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.updateSplash();
            }
            return false;
        }
    }
    
    public long getSplashHandle() {
        try {
            return this._get_splash_handle();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.getSplashHandle();
            }
            return -1L;
        }
    }
    
    boolean isLibraryLoadedByJava() {
        return this.libraryLoaded;
    }
    
    public boolean takeDownSplash() {
        try {
            this._takedown_splash();
            return true;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.takeDownSplash();
            }
            return false;
        }
    }
    
    public boolean uninitialize() {
        if (this.libraryLoaded && this.library != null && this.library.contains("wpf")) {
            try {
                this.OleUninitialize();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                return false;
            }
        }
        return true;
    }
    
    public String getOSRecommendedFolder() {
        try {
            return this._get_os_recommended_folder();
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (!this.libraryLoaded) {
                this.loadLibrary();
                return this.getOSRecommendedFolder();
            }
            return null;
        }
    }
}
