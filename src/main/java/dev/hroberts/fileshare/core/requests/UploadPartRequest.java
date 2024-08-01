package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.FileshareConfig;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import java.util.UUID;

public class UploadPartRequest extends BaseRequest {
    private final UUID uploadId;
    public UploadPartRequest(FileshareConfig config, UUID uploadId) {
        super(config);
        this.uploadId = uploadId;
    }

    @Override
    protected String getRequestPath() {
        return "/upload/" + uploadId;
    }

    @Override
    protected HttpUriRequest getRequest() {
        return null;
    }

    @Override
    protected void buildHeaders() {

    }

    @Override
    protected void buildBody() {

    }
}
