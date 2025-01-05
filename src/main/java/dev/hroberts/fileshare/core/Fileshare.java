package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.config.FileshareConfig;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadOptions;
import dev.hroberts.fileshare.core.models.UploadableFile;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.util.UUID;

public class Fileshare {
    private final UploadManager uploadManager;

    public Fileshare(FileshareConfig config) {
        Logger.info("initializing fileshare core");
        this.uploadManager = new UploadManager(config);
    }

    public void getFile() {
        Logger.error("not yet implemented");
    }

    public UUID putFile(Path filePath) throws FailedToInitiateUploadException {
        Logger.info("putting file " + filePath.getFileName() + " with default options");
        var options = UploadOptions.getDefaultOptions();
        return putFile(filePath, options);
    }

    public UUID putFile(Path filePath, UploadOptions uploadOptions) throws FailedToInitiateUploadException {
        Logger.info("putting file " + filePath.getFileName());
        return uploadManager.doUpload(filePath, uploadOptions);
    }
}
