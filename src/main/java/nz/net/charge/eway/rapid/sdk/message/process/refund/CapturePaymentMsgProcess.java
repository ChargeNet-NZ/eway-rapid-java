package nz.net.charge.eway.rapid.sdk.message.process.refund;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.CapturePaymentRequest;
import nz.net.charge.eway.rapid.sdk.entities.CapturePaymentResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.request.TransactionToCapturePaymentConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.CapturePaymentToCreateTransactionConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Capture payment message process
 */
public class CapturePaymentMsgProcess extends AbstractMakeRequestMessageProcess<Transaction, CreateTransactionResponse> {

    public CapturePaymentMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected CreateTransactionResponse makeResult(Response res) {

        BeanConverter<CapturePaymentResponse, CreateTransactionResponse> converter =
                new CapturePaymentToCreateTransactionConverter();
        return converter.doConvert((CapturePaymentResponse) res);
    }

    @Override
    protected Request createRequest(Transaction t) throws RapidSdkException {
        BeanConverter<Transaction, CapturePaymentRequest> converter = new TransactionToCapturePaymentConverter();
        return converter.doConvert(t);
    }

    @Override
    protected Mono<CapturePaymentResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CapturePaymentResponse.class);
    }

}
