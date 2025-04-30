package nz.net.charge.eway.rapid.sdk.message.process;

import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.AuthenticationFailureException;
import nz.net.charge.eway.rapid.sdk.exception.CommunicationFailureException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.exception.SystemErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The abstract class to defines message process work flows
 *
 * @param <T> : Input generic class
 * @param <V> : Output generic class
 */
public abstract class AbstractMessageProcess<T, V> implements MessageProcess<T, V> {

    private final WebClient webClient;
    private T t;
    private final List<String> listRequestPath = new ArrayList<>();

    /**
     * @param client The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public AbstractMessageProcess(WebClient client, String... requestPath) {
        this.webClient = client;
        if (requestPath != null) {
            listRequestPath.addAll(Arrays.asList(requestPath));
        }
    }

    public final V doWork(T input) throws RapidSdkException {
        this.t = input;
        Response response = processPostMsg(input);
        if (response != null) {
            return makeResult(response);
        }
        throw new SystemErrorException("Response object is null");
    }

    /**
     * Call Rapid API with a POST request
     *
     * @param <U> Request class
     * @param <K> Response class
     * @param request Request object
     * @param responseClass The response class used for a successful result
     * @return Instance of response class
     * @throws RapidSdkException base SDK exception
     */
    protected final <U, K> U doPost(K request, Class<U> responseClass) throws RapidSdkException {
        try {
            WebClient client = getWebClient();

            U response = client.post()
                    .uri(builder -> {
                        for (String path : getRequestPath()) {
                            if (!StringUtils.isBlank(path)) {
                                builder.path(path);
                            }
                        }

                        return builder.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .block();

            return response;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new CommunicationFailureException("Internal system error communicating with Rapid API", e);
            } else {
                throw new SystemErrorException(e.getMessage(), e);
            }
        } catch (WebClientRequestException e) {
            throw new CommunicationFailureException("Error using TLS 1.2 to connect to Rapid: client exception", e);
        }
    }

    /**
     * Call Rapid API with a PUT request
     *
     * @param <U> Request class
     * @param <K> Response class
     * @param request Request object
     * @param responseClass The response class used for a successful result
     * @return Instance of response class
     * @throws RapidSdkException base SDK exception
     */
    protected final <U, K> U doPut(K request, Class<U> responseClass) throws RapidSdkException {
        try {
            WebClient client = getWebClient();

            U response = client.put()
                    .uri(builder -> {
                        for (String path : getRequestPath()) {
                            if (!StringUtils.isBlank(path)) {
                                builder.path(path);
                            }
                        }

                        return builder.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .block();

            return response;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new CommunicationFailureException("Internal system error communicating with Rapid API", e);
            } else {
                throw new SystemErrorException(e.getMessage(), e);
            }
        } catch (WebClientRequestException e) {
            throw new CommunicationFailureException("Error using TLS 1.2 to connect to Rapid: client exception", e);
        }
    }

    /**
     * Call Rapid API with a GET request
     *
     * @param <U> Response class
     * @param request Request object
     * @param responseClass The response class used for a successful result
     * @return Instance of response class
     * @throws RapidSdkException base SDK exception
     */
    protected final <U> U doGet(String request, Class<U> responseClass) throws RapidSdkException {
        try {
            WebClient client = getWebClient();

            return client
                    .get()
                    .uri(builder -> {
                        for (String path : getRequestPath()) {
                            if (!StringUtils.isBlank(path)) {
                                builder.path(path);
                            }
                        }

                        return builder.path(request).build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AuthenticationFailureException("Authentication failed on the endpoint", e);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new CommunicationFailureException("Internal system error communicating with Rapid API", e);
            } else {
                throw new SystemErrorException(e.getMessage(), e);
            }
        } catch (WebClientRequestException e) {
            throw new CommunicationFailureException("Error using TLS 1.2 to connect to Rapid: client exception", e);
        }
    }

    /**
     * Get input object
     *
     * @return Instance of input class
     */
    protected final T getInput() {
        return t;
    }

    /**
     * Get web resource request path
     *
     * @return Path of web resource
     */
    protected final List<String> getRequestPath() {
        return listRequestPath;
    }

    /**
     * Set web resource request path
     *
     * @param requestPath String to add to request path
     */
    protected final void addRequestPath(String... requestPath) {
        if (requestPath != null) {
            for (String path : requestPath) {
                if (!StringUtils.isBlank(path)) {
                    listRequestPath.add(path);
                }
            }
        }
    }

    /**
     * Post message to web service and return response object
     *
     * @param req Request object(instance of input class)
     * @return Instance of response class
     * @throws RapidSdkException base SDK exception
     */
    protected abstract Response processPostMsg(T req) throws RapidSdkException;

    /**
     * Create result from web service response object
     *
     * @param res Response object
     * @return Instance of output class
     * @throws RapidSdkException base SDK exception
     */
    protected abstract V makeResult(Response res) throws RapidSdkException;

    /**
     * Get web resource object to connect to Rapid API
     *
     * @return Web resource
     */
    protected final WebClient getWebClient() {
        return webClient;
    }

}
