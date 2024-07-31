package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.exceptions.FailedToInitiateUploadException;
import org.tinylog.Logger;

import java.io.File;
import java.nio.file.Path;

public class Main {
    /**
     * Todo:
     * 1. First, implement some sort of logging
     * 2. Second, try compiling this as a native image
     * 3. Third, refactor the initiate method so that it works cleanly
     * 4. Fourth, build the structure for the chunked file itself
     *  a. How do we keep track of chunks?
     *      if we are doing 10 parallel uploads, we keep each of those 10 in memory until the upload succeeds
     *  b. How do we parallelize the uploads?
     *  c. How do we know when to retry?
     *  d. Will it be easy to add in the hashing?
     */
    public static void main(String[] args) {
        Logger.info("Initializing fileshare core");
        var config = new FileshareConfig("https://files.hroberts.dev");
        var core = new Fileshare(config);
        Logger.info("Fileshare core initialized");
        try {
            Logger.info("Starting test upload");
            core.shareFile(Path.of("/home/hroberts/dump.rdb"));
        } catch (FailedToInitiateUploadException e) {
            throw new RuntimeException(e);
        }
    }
}
