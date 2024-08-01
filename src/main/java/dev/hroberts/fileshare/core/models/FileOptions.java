package dev.hroberts.fileshare.core.models;

public class FileOptions {
    public static FileOptions getDefaultOptions() {
        var defaultOptions = new FileOptions();
        defaultOptions.downloadLimit = -1;
        return defaultOptions;
    }
    public int downloadLimit;
}
