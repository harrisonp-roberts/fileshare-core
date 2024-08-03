package dev.hroberts.fileshare.core.requests;

import com.google.gson.Gson;
import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.dtos.InitiateMultipartDto;
import dev.hroberts.fileshare.core.dtos.InitiateMultipartResponseDto;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import dev.hroberts.fileshare.core.models.UploadableFile;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.tinylog.Logger;

import java.util.UUID;

public class InititiateUploadRequest extends BaseRequest <InitiateMultipartResponseDto> {
    private final String fileName;
    private final long size;

    public InititiateUploadRequest(FileshareConfig config, UploadableFile uploadableFile) {
        super(config);
        fileName = uploadableFile.getFileName();
        size = uploadableFile.getSize();
    }

    @Override
    public InitiateMultipartResponseDto execute() throws FailedToInitiateUploadException {
        return execute(InitiateMultipartResponseDto.class);
    }

    @Override
    protected void buildBody(HttpUriRequest request) {
        var dto = new InitiateMultipartDto();
        dto.fileName = fileName;
        dto.size = size;
        dto.downloadLimit = -1;

        Gson gson = new Gson();
        var jsonBody = gson.toJson(dto);

        body = new StringEntity(jsonBody);
        request.setEntity(body);
    }

    @Override
    protected HttpUriRequest getRequest() {
        return new HttpPost("/files/initiate-multipart");
    }

    protected void buildHeaders(HttpUriRequest request) {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
    }
}
