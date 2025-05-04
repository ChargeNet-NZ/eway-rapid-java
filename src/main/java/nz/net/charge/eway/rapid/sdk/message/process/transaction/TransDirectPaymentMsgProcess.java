package nz.net.charge.eway.rapid.sdk.message.process.transaction;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentRequest;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.request.TransactionToDirectPaymentConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.DirectPaymentToCreateTransConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Create transaction with direct payment message process
 */
public class TransDirectPaymentMsgProcess extends AbstractMakeRequestMessageProcess<Transaction, CreateTransactionResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public TransDirectPaymentMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Transaction input) throws RapidSdkException {
        BeanConverter<Transaction, DirectPaymentRequest> reqConverter = new TransactionToDirectPaymentConverter();
        return reqConverter.doConvert(input);
    }

    @Override
    protected CreateTransactionResponse makeResult(Response res) throws RapidSdkException {
        DirectPaymentResponse response = (DirectPaymentResponse) res;
        BeanConverter<DirectPaymentResponse, CreateTransactionResponse> converter = new DirectPaymentToCreateTransConverter();
        return converter.doConvert(response);
    }

    @Override
    protected Response sendRequest(Request req) throws RapidSdkException {
        return doPost(req, DirectPaymentResponse.class);
    }

}
