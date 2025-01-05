package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.config.FileshareConfig;
import dev.hroberts.fileshare.core.dtos.NoContentResponseDto;
import dev.hroberts.fileshare.core.models.UploadOptions;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.CompleteUploadRequest;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import dev.hroberts.fileshare.core.requests.UploadPartRequest;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class UploadManager {
    private final FileshareConfig config;

    public UploadManager(FileshareConfig config) {
        Logger.info("creating upload manager");
        this.config = config;
    }

    UUID doUpload(Path filePath, UploadOptions options) throws FailedToInitiateUploadException {
        var file = new UploadableFile(filePath, config.chunkSize);
        var uploadId = initiateUpload(file, options);
        Logger.info("initiated request with upload ID " + uploadId);

        uploadChunks(uploadId, options, file);
        Logger.info("uploaded chunks");

        completeUpload(uploadId, options);
        Logger.info("upload completed");

        return uploadId;
    }

    private void completeUpload(UUID uploadId, UploadOptions options) {
        Logger.info("completing upload");

        var completeUploadRequest = new CompleteUploadRequest(config, uploadId, options.enableHashing);
                completeUploadRequest.execute().join();
    }

    private void uploadChunks(UUID uploadId,UploadOptions options, UploadableFile file) {
        try {
            var tasks = new ArrayList<CompletableFuture<NoContentResponseDto>>();

            for (int i = 0; i < file.getNumChunks(); i++) {
                var chunk = file.getChunk(i);
                var request = new UploadPartRequest(config, i, uploadId, chunk, options.enableHashing);

                tasks.add(request.execute());

                while(tasks.size() >= config.parallelUploads) {
                    //todo this probably won't handle errors
                    tasks.stream().filter(CompletableFuture::isDone).forEach(CompletableFuture::join);
                    tasks.removeIf(CompletableFuture::isDone);
                }
            }

            tasks.forEach(CompletableFuture::join);
        } catch (IOException ex) {
            Logger.error("e");
            throw new RuntimeException(ex);
        }
    }

    private UUID initiateUpload(UploadableFile file, UploadOptions options) throws FailedToInitiateUploadException {
        Logger.info("initiating upload");
        var request = new InititiateUploadRequest(config, options, file);

        try {
            var responseDto = request.execute().get();
            return responseDto.id;
        } catch (InterruptedException | ExecutionException e) {
            throw new FailedToInitiateUploadException();
        }
    }
}
