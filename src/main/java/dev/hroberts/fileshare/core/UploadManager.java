package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.dtos.NoContentResponseDto;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.CompleteUploadRequest;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import dev.hroberts.fileshare.core.requests.UploadPartRequest;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

class UploadManager {
    private final FileshareConfig config;

    public UploadManager(FileshareConfig config) {
        Logger.info("creating upload manager");
        this.config = config;
    }

    UUID doUpload(UploadableFile file) throws FailedToInitiateUploadException {
        var uploadId = initiateUpload(file);
        Logger.info("initiated request with upload ID " + uploadId);

        uploadChunks(uploadId, file);
        Logger.info("uploaded chunks");

        completeUpload(uploadId);
        Logger.info("upload completed");

        return uploadId;
    }

    private void completeUpload(UUID uploadId) {
        Logger.info("completing upload");

        var completeUploadRequest = new CompleteUploadRequest(config, uploadId);
        completeUploadRequest.execute();
    }

    private void uploadChunks(UUID uploadId, UploadableFile file) {
        Logger.info("creating thread pool for chunks");
        var virtualThreadFactory = Thread.ofVirtual().factory();
        try (var executor = Executors.newFixedThreadPool(config.getParallelUploads(), virtualThreadFactory)) {
            var completionService = new ExecutorCompletionService<NoContentResponseDto>(executor);
            int activeTasks = 0;

            for (int i = 0; i < file.getNumChunks(); i++) {
                    Logger.info("creating upload chunk request " + i);
                    var chunk = file.getChunk(i);
                    var request = new UploadPartRequest(config, i, uploadId, chunk);

                    if(activeTasks == config.getParallelUploads()) {
                        Logger.info("removing completed upload chunk request from threadpool");
                        completionService.take();
                        activeTasks--;
                    }

                    Logger.info("pooling upload chunk request " + i);
                    completionService.submit(() -> request.execute(executor), null);
                    activeTasks++;


            }

            while(activeTasks > 0) {
                Logger.info("removing completed upload chunk request from threadpool");
                completionService.take();
                activeTasks--;
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private UUID initiateUpload(UploadableFile file) throws FailedToInitiateUploadException {
        Logger.info("initiating upload");
        var request = new InititiateUploadRequest(config, file);

        try {
            var responseDto = request.execute().get();
            return responseDto.id;
        } catch (InterruptedException | ExecutionException e) {
            throw new FailedToInitiateUploadException();
        }
    }
}
