package dev.hroberts.fileshare.core.models;

public class UploadOptions {
    public static UploadOptions getDefaultOptions() {
        var defaultOptions = new UploadOptions();
        defaultOptions.downloadLimit = -1;
        defaultOptions.enableHashing = false;
        return defaultOptions;
    }
    public boolean enableHashing;
    public int downloadLimit;
}
