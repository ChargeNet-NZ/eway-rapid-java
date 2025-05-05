package nz.net.charge.eway.rapid.sdk.message.process.transaction;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeRequest;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.request.TransactionToCreateAccessCodeRequestConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.AccessCodeToCreateTransConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Create transaction with transparent redirect method message process
 */
public class TransTransparentRedirectMsgProcess extends AbstractMakeRequestMessageProcess<Transaction, CreateTransactionResponse> {

    public TransTransparentRedirectMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Transaction input) throws RapidSdkException {
        BeanConverter<Transaction, CreateAccessCodeRequest> converter = new TransactionToCreateAccessCodeRequestConverter();
        return converter.doConvert(input);
    }

    @Override
    protected Mono<CreateTransactionResponse> makeResult(Mono<? extends Response> res) {
        return res.map(response -> {
            BeanConverter<CreateAccessCodeResponse, CreateTransactionResponse> converter = new AccessCodeToCreateTransConverter();
            return converter.doConvert((CreateAccessCodeResponse) response);
        });
    }

    @Override
    protected Mono<CreateAccessCodeResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CreateAccessCodeResponse.class);
    }

}
