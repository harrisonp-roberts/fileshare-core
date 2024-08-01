package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.requests.UploadPartRequest;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.UUID;

class UploadManager {
    private FileshareConfig config;

    public UploadManager(FileshareConfig config) {
        Logger.info("creating upload manager");
        this.config = config;
    }

    UUID doUpload(UploadableFile file) throws FailedToInitiateUploadException {
        var uploadId = initiateUpload(file);
        Logger.info("initiated request with upload ID " + uploadId);


        //upload them
        return null;
    }

    private void uploadChunks(UUID uploadId, UploadableFile file) {
        //assume 10 parallel chunks of 1024 * 1024 * 10 (10MB)
        Thread.Builder builder = Thread.ofVirtual().name("worker-", 0);
        int parallelUploads = 10;
        var runnables = new ArrayList<Runnable>();

        //while upload is not complete

    }

    private void uploadChunk(UUID uploadId, byte[] payload) {
        //
    }

    private UUID initiateUpload(UploadableFile file) throws FailedToInitiateUploadException {
        Logger.info("initiating upload");
        var request = new InititiateUploadRequest(config, file);
        return request.initiate();
    }


}
