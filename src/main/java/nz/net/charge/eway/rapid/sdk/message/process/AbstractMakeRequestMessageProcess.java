package nz.net.charge.eway.rapid.sdk.message.process;

import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Defines the work flow to send and receive web service messages. Before
 * sending a message, create a request object from input
 *
 * @param <T> Input generic class
 * @param <V> Output generic class
 */
public abstract class AbstractMakeRequestMessageProcess<T, V> extends AbstractMessageProcess<T, V> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public AbstractMakeRequestMessageProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected final Mono<? extends Response> processPostMsg(T t) throws RapidSdkException {
        Request req = createRequest(t);
        if (req != null) {
            return sendRequest(req);
        }
        return Mono.empty();
    }

    /**
     * The definition method that indicates subclass must override to create
     * request object
     *
     * @param t Input object (Mapping with generic input class)
     * @return The request object to send
     * @throws RapidSdkException base SDK exception
     */
    protected abstract Request createRequest(T t) throws RapidSdkException;

    /**
     * The abstract method defining the sending request message
     *
     * @param req Request object
     * @return Response object
     * @throws RapidSdkException base SDK exception
     */
    protected abstract Mono<? extends Response> sendRequest(Request req) throws RapidSdkException;
}
