package nz.net.charge.eway.rapid.sdk.message.process;

import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import reactor.core.publisher.Mono;

/**
 * Base interface for all Rapid message process classes. It defines the abstract
 * method (doWork), which all subclass must override with business logic
 *
 * @param <T> : Input generic class
 * @param <V> : Output generic class
 */
public interface MessageProcess<T, V> {

    /**
     * @param input The input object
     * @return An instance of V(Output generic class)
     * @throws RapidSdkException The exception that indicates when there is a
     * business logic error
     */
    Mono<V> doWork(T input) throws RapidSdkException;
}
