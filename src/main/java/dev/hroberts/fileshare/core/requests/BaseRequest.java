package dev.hroberts.fileshare.core.requests;

import com.google.gson.Gson;
import dev.hroberts.fileshare.core.FileshareConfig;
import dev.hroberts.fileshare.core.requests.exceptions.FailedToInitiateUploadException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseRequest<T> {
    protected Gson gson = new Gson();
    protected String URL;
    protected HttpUriRequest request;
    protected HttpEntity body;
    protected FileshareConfig config;

    public BaseRequest(FileshareConfig config) {
        this.config = config;
        this.URL = config.getBaseUri();
    }

    /**
     * @return T where T is the dto returned by the API
     */
    protected CompletableFuture<T> execute(Class<T> clazz, ExecutorService es) {

        return CompletableFuture.supplyAsync(() -> {
            //todo if response errors, retry x times
            try {
                var responseString = sendRequest();
                return gson.fromJson(responseString, clazz);
            } catch (IOException e) {
                Logger.error("failed to execute request");
                throw new CompletionException(e);
            }
        }, es);
    }

    protected CompletableFuture<T> execute(Class<T> clazz) {
        return execute(clazz, Executors.newFixedThreadPool(1));
    }

    protected String sendRequest() throws IOException {
        var request = getRequest();
        buildHeaders(request);
        buildBody(request);

        Logger.info("sending http " + request.getMethod());

        //todo if request status code is not success, throw error.
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(request, (httpResponse -> new String(httpResponse.getEntity().getContent().readAllBytes())));
        } catch (IOException ex) {
            Logger.error("error sending request");
            throw ex;
        }
    }

    public abstract CompletableFuture<T> execute() throws FailedToInitiateUploadException;

    protected abstract HttpUriRequest getRequest();

    protected abstract void buildHeaders(HttpUriRequest request);

    protected abstract void buildBody(HttpUriRequest request);
}
