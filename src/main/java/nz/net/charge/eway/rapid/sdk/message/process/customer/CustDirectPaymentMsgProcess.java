package nz.net.charge.eway.rapid.sdk.message.process.customer;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentRequest;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.DirectPaymentToCreateCustConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.util.Constant;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Create customer with Direct Payment method
 */
public class CustDirectPaymentMsgProcess extends AbstractMakeRequestMessageProcess<Customer, CreateCustomerResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public CustDirectPaymentMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Customer input) throws RapidSdkException {
        DirectPaymentRequest request = new DirectPaymentRequest();
        BeanConverter<Customer, nz.net.charge.eway.rapid.sdk.beans.internal.Customer> interCustConvert = new CustomerToInternalCustomerConverter(false);
        request.setCustomer(interCustConvert.doConvert(input));
        request.setCustomerIP(input.getCustomerDeviceIP());
        request.setMethod(Constant.CREATE_TOKEN_CUSTOMER_METHOD);
        request.setTransactionType(TransactionType.MOTO.name());
        return request;
    }

    @Override
    protected Mono<DirectPaymentResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, DirectPaymentResponse.class);
    }

    @Override
    protected Mono<CreateCustomerResponse> makeResult(Mono<? extends Response> res) {
        return res.map(response -> {
            DirectPaymentResponse directPaymentResponse = (DirectPaymentResponse) response;
            DirectPaymentToCreateCustConverter converter = new DirectPaymentToCreateCustConverter();
            return converter.doConvert(directPaymentResponse);
        });

    }
}
