package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import org.tinylog.Logger;

import java.util.UUID;

class UploadManager {
    private FileshareConfig config;

    public UploadManager(FileshareConfig config) {
        Logger.info("creating upload manager");
        this.config = config;
    }

    UUID doUpload(UploadableFile file) throws FailedToInitiateUploadException {
        initiateUpload(file);
        //initiate upload
        //generate x chunks
        //upload them
        return null;
    }

    private UUID initiateUpload(UploadableFile file) throws FailedToInitiateUploadException {
        Logger.info("initiating upload");
        var request = new InititiateUploadRequest(config, file);
        //todo fix the file path input
        return request.initiate();
    }
}
