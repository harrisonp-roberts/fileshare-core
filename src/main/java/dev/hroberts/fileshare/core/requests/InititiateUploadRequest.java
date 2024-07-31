package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.exceptions.FailedToInitiateUploadException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

public class InititiateUploadRequest extends BaseRequest {
    private final String fileName;
    private final long size;

    public InititiateUploadRequest(FileshareConfig config, File file) {
        super(config);
        this.fileName = file.getName();
        this.size = file.length();
        URL = config.getBaseUri() + "/files/initiate-multipart";
        request = new HttpPost(URL);
        buildHeaders();
        buildBody();
    }

    //todo maybe change how this is inserted
    public UUID initiate() throws FailedToInitiateUploadException {
        var responseJson = sendRequest();
        if(responseJson.has("id")) {
            return UUID.fromString(responseJson.getString("id"));
        } else {
            throw new FailedToInitiateUploadException();
        }
    }

    protected void buildBody() {
        var jsonBody = new JSONObject()
                .put("name", fileName)
                .put("size", size)
                .put("downloadLimit", -1);

        body = new StringEntity(jsonBody.toString());
        request.setEntity(body);
    }

    protected void buildHeaders() {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
    }
}
