package nz.net.charge.eway.rapid.sdk.message.process.transaction;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedRequest;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.request.TransactionToCreateAccessCodeSharedRequestConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.AccessCodeSharedToCreateTransConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Create transaction with responsive shared message process
 */
public class TransResponsiveSharedMsgProcess extends AbstractMakeRequestMessageProcess<Transaction, CreateTransactionResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public TransResponsiveSharedMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Transaction input) throws RapidSdkException {
        BeanConverter<Transaction, CreateAccessCodeSharedRequest> converter = new TransactionToCreateAccessCodeSharedRequestConverter();
        return converter.doConvert(input);
    }

    @Override
    protected Mono<CreateTransactionResponse> makeResult(Mono<? extends Response> res) {
        return res.map(response -> {
            BeanConverter<CreateAccessCodeSharedResponse, CreateTransactionResponse> converter =
                    new AccessCodeSharedToCreateTransConverter();
            return converter.doConvert((CreateAccessCodeSharedResponse) response);
        });
    }

    @Override
    protected Mono<CreateAccessCodeSharedResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CreateAccessCodeSharedResponse.class);
    }

}
