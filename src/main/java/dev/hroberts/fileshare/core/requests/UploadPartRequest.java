package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.config.FileshareConfig;
import dev.hroberts.fileshare.core.dtos.NoContentResponseDto;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UploadPartRequest extends BaseRequest<NoContentResponseDto> {
    private final UUID uploadId;
    private final byte[] payload;
    private final int position;
    private final boolean enableHashing;

    public UploadPartRequest(FileshareConfig config, int position, UUID uploadId, byte[] payload, boolean enableHashing) {
        super(config);
        this.uploadId = uploadId;
        this.payload = payload;
        this.position = position;
        this.enableHashing = enableHashing;
    }

    @Override
    public CompletableFuture<NoContentResponseDto> execute() {
        return execute(NoContentResponseDto.class);
    }

    @Override
    protected HttpUriRequest getRequest() {
        return new HttpPost(URL + "/files/upload/" + uploadId);
    }

    @Override
    protected void buildHeaders(HttpUriRequest request) {

    }

    @Override
    protected void buildBody(HttpUriRequest request) {
        var builder = MultipartEntityBuilder.create();
        builder.addTextBody("chunkIndex", String.valueOf(position));
        builder.addTextBody("size", String.valueOf(payload.length));
        builder.addBinaryBody("file", payload, ContentType.APPLICATION_OCTET_STREAM, "file.part");
        if(enableHashing) {
            builder.addTextBody("hash", getHash(payload));
            builder.addTextBody("hashAlgorithm", "CRC_32");
        }
        request.setEntity(builder.build());
    }
    
    private String getHash(byte[] payload) {
        var crc32 = new java.util.zip.CRC32();
        crc32.update(payload);
        return Long.toHexString(crc32.getValue());
    }
}
