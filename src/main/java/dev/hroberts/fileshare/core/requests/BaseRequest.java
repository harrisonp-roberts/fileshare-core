package dev.hroberts.fileshare.core.requests;

import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.exceptions.FailedToInitiateUploadException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.IOException;

public abstract class BaseRequest {
    protected int tries = 0;
    protected String URL;
    protected boolean successStatus;
    protected ClassicHttpRequest request;
    protected HttpEntity body;
    protected FileshareConfig config;

    public BaseRequest(FileshareConfig config) {
        this.config = config;
    }

    protected JSONObject sendRequest() throws FailedToInitiateUploadException {
        Logger.info("sending http " + request.getMethod());
        tries++;
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            var responseString = httpClient.execute(request, (response -> new String(response.getEntity().getContent().readAllBytes())));
            successStatus = true;
            return new JSONObject(responseString);
        } catch (IOException e) {
            successStatus = false;
            System.err.println(e.getMessage());
            throw new FailedToInitiateUploadException();
        }
    }

    protected abstract void buildHeaders();
    protected abstract void buildBody();
}
