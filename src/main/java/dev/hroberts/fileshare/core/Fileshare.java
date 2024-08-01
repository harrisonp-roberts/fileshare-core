package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.FileOptions;
import dev.hroberts.fileshare.core.models.UploadableFile;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.util.UUID;

public class Fileshare {
    private UploadManager uploadManager;

    public Fileshare(FileshareConfig config) {
        Logger.info("initializing fileshare core");
        this.uploadManager = new UploadManager(config);
    }

    public void getFile() {
        Logger.error("not yet implemented");
    }

    public UUID putFile(Path filePath) throws FailedToInitiateUploadException {
        Logger.info("putting file " + filePath.getFileName() + " with default options");
        var options = FileOptions.getDefaultOptions();
        return putFile(filePath, options);

    }

    public UUID putFile(Path filePath, FileOptions fileShareOptions) throws FailedToInitiateUploadException {
        Logger.info("putting file " + filePath.getFileName());
        var file = new UploadableFile(filePath);
        return uploadManager.doUpload(file);
    }
}
