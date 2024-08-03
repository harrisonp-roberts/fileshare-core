package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.requests.UploadPartRequest;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

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
        var virtualThreadFactory = Thread.ofVirtual().factory();
        try(var executor = Executors.newFixedThreadPool(10, virtualThreadFactory)) {
            var futures = IntStream.range(0, file.getNumChunks())
                    .mapToObj(i -> {
                        try {
                            var chunk = file.getChunk(i);
                            new UploadPartRequest()
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }


    }

    private void uploadChunk(UUID uploadId, byte[] payload) {
        //
    }

    private UUID initiateUpload(UploadableFile file) throws FailedToInitiateUploadException {
        Logger.info("initiating upload");
        var request = new InititiateUploadRequest(config, file);
        var responseDto = request.execute();
        return responseDto.id;
    }


}
