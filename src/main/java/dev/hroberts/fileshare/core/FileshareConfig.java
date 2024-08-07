package dev.hroberts.fileshare.core;

public class FileshareConfig {
    private String baseUri;
    private long chunkSize;
    private int parallelUploads;
    private int maxRetries = 5;

    public FileshareConfig(String baseUri) {
        this.baseUri = baseUri;
        //Default chunk size 50M
        this.chunkSize = 50 * 1024 * 1024;
        this.parallelUploads = 10;
    }

    public FileshareConfig(String baseUri, long chunkSize) {
        this.baseUri = baseUri;
        this.chunkSize = chunkSize;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getParallelUploads() {
        return parallelUploads;
    }
}
