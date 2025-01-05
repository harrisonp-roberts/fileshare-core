package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.config.FileshareConfig;
import dev.hroberts.fileshare.core.dtos.NoContentResponseDto;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CompleteUploadRequest extends BaseRequest<NoContentResponseDto> {
    private final UUID uploadId;

    public CompleteUploadRequest(FileshareConfig config, UUID uploadId, boolean enableHashing) {
        super(config);
        this.uploadId = uploadId;
    }

    @Override
    public CompletableFuture<NoContentResponseDto> execute() {
        return execute(NoContentResponseDto.class);
    }

    @Override
    protected HttpUriRequest getRequest() {
        return new HttpPut(URL + "/files/complete/" + uploadId);
    }

    @Override
    protected void buildHeaders(HttpUriRequest request) {
    }

    @Override
    protected void buildBody(HttpUriRequest request) {
    }
}
