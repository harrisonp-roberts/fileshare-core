package dev.hroberts.fileshare.core.requests;

import com.google.gson.Gson;
import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseRequest {
    protected Gson gson = new Gson();
    protected int tries = 0;
    protected String URL;
    protected boolean successStatus;
    protected Integer responseCode = null;
    protected HttpUriRequest request;
    protected HttpEntity body;
    protected FileshareConfig config;

    public BaseRequest(FileshareConfig config) {
        this.config = config;
        URL = config.getBaseUri() + getRequestPath();
        request = getRequest();
        buildHeaders();
        buildBody();
    }

    protected String sendRequest() throws FailedToInitiateUploadException {
        Logger.info("sending http " + request.getMethod());

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final var responseCode = new AtomicInteger();
            var response = httpClient.execute(request, (httpResponse -> {
                responseCode.set(httpResponse.getCode());
                return new String(httpResponse.getEntity().getContent().readAllBytes());
            })); ;
            successStatus = true;
            this.responseCode = responseCode.get();

            return response;
        } catch (IOException e) {
            Logger.error("error sending request");
            Logger.error(e);
            successStatus = false;
            throw new FailedToInitiateUploadException();
        } finally {
            tries++;
        }
    }

    protected abstract String getRequestPath();
    protected abstract HttpUriRequest getRequest();
    protected abstract void buildHeaders();
    protected abstract void buildBody();
}
