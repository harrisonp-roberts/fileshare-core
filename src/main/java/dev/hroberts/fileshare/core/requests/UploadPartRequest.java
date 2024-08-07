package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.dtos.NoContentResponseDto;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UploadPartRequest extends BaseRequest<NoContentResponseDto> {
    private final UUID uploadId;
    private final byte[] payload;
    private final int position;

    public UploadPartRequest(FileshareConfig config, int position, UUID uploadId, byte[] payload) {
        super(config);
        this.uploadId = uploadId;
        this.payload = payload;
        this.position = position;
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
        request.setEntity(builder.build());
    }
}
