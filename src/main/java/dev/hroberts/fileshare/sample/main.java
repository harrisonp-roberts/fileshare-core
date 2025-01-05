package dev.hroberts.fileshare.sample;

import dev.hroberts.fileshare.core.Fileshare;
import dev.hroberts.fileshare.core.config.FileshareConfig;
import dev.hroberts.fileshare.core.models.UploadOptions;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;

import java.nio.file.Path;

public class main {
    public static void main(String[] args) throws FailedToInitiateUploadException {
        //chunk 1mb
        var fsconfig = new FileshareConfig("http://localhost:8080", 1024 * 1024);
        var fileshare = new Fileshare(fsconfig);
        var fileToUpload = Path.of("/home/hroberts/Downloads/TempleOS.ISO");
        var uploadOptions = new UploadOptions();
        uploadOptions.enableHashing = true;
        var id = fileshare.putFile(fileToUpload, uploadOptions);
        System.out.println("Uploaded file with id " + id);
    }
}
