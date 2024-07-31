package dev.hroberts.fileshare.core.models;

import java.io.File;
import java.nio.file.Path;

public class UploadableFile {
    private Path filePath;
    private long size;

    public UploadableFile(Path filePath) {
       this.filePath = filePath;
    }

    public UploadableFile(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public UploadableFile(File file) {
        this.filePath = file.toPath();
    }

    public Path getFilePath() {
        return filePath;
    }
}
