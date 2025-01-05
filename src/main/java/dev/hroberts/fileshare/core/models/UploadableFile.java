package dev.hroberts.fileshare.core.models;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class UploadableFile {
    private Path filePath;
    private String fileName;
    private long size;
    private int chunkSize;
    private int numChunks;

    public UploadableFile(Path filePath, int chunkSize) {
        this.filePath = filePath;
        this.fileName = filePath.getFileName().toString();
        this.size = filePath.toFile().length();
        this.chunkSize = chunkSize;
        this.numChunks = (int) Math.ceilDiv(size, chunkSize);
    }

    public byte[] getChunk(int index) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            file.seek((long) index * chunkSize);
            byte[] buffer = new byte[chunkSize];
            int bytesRead = file.read(buffer);
            if (bytesRead < chunkSize) {
                byte[] smallerBuffer = new byte[bytesRead];
                System.arraycopy(buffer, 0, smallerBuffer, 0, bytesRead);
                buffer = smallerBuffer;
            }
            return buffer;
        }
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

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public int getNumChunks() {
        return numChunks;
    }
}
