package dev.hroberts.fileshare.core;

import dev.hroberts.fileshare.core.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadableFile;
import dev.hroberts.fileshare.core.requests.InititiateUploadRequest;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.util.UUID;

public class Fileshare {
    private FileshareConfig config;
    public Fileshare(FileshareConfig config) {
        this.config = config;
    }

    //todo return id
    public UUID shareFile(Path filepath) throws FailedToInitiateUploadException {
        Logger.info("Sharing file", filepath);
        var file = new UploadableFile(filepath);
        return initiateUpload(file);
    }

    private UUID initiateUpload(UploadableFile file) throws FailedToInitiateUploadException {
        var request = new InititiateUploadRequest(config, file.getFilePath().toFile());
        //todo fix the file path input
        return request.initiate();
    }


}
