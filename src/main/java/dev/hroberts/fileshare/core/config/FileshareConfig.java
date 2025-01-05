package dev.hroberts.fileshare.core.config;

public class FileshareConfig {
    public String baseUri;
    public int chunkSize;
    public int parallelUploads;
    public int maxRetries = 5;

    public FileshareConfig(String baseUri) {
        this.baseUri = baseUri;
        //Default chunk size 50M
        this.chunkSize = 50 * 1024 * 1024;
        this.parallelUploads = 10;
    }

    public FileshareConfig(String baseUri, int chunkSize) {
        this.baseUri = baseUri;
        this.chunkSize = chunkSize;
    }
}
