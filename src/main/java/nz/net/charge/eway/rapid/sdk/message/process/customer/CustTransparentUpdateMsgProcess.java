package nz.net.charge.eway.rapid.sdk.message.process.customer;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeRequest;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.AccessCodeToCreateCustConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.util.Constant;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Update customer with transparent redirect message process
 */
public class CustTransparentUpdateMsgProcess extends AbstractMakeRequestMessageProcess<Customer, CreateCustomerResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public CustTransparentUpdateMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Customer input) throws RapidSdkException {
        CreateAccessCodeRequest request = new CreateAccessCodeRequest();
        CustomerToInternalCustomerConverter interCustConvert = new CustomerToInternalCustomerConverter();
        Payment payment = new Payment();
        payment.setTotalAmount(0);
        request.setPayment(payment);
        request.setCustomer(interCustConvert.doConvert(input));
        request.setMethod(Constant.UPDATE_TOKEN_CUSTOMER_METHOD);
        request.setTransactionType(TransactionType.Purchase.name());
        request.setRedirectUrl(input.getRedirectUrl());
        return request;
    }

    @Override
    protected Mono<CreateCustomerResponse> makeResult(Mono<? extends Response> res) {
        return res.map(response -> {
            CreateAccessCodeResponse createAccessCodeResponse = (CreateAccessCodeResponse) response;
            AccessCodeToCreateCustConverter converter = new AccessCodeToCreateCustConverter();
            return converter.doConvert(createAccessCodeResponse);
        });
    }

    @Override
    protected Mono<CreateAccessCodeResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CreateAccessCodeResponse.class);
    }

}
