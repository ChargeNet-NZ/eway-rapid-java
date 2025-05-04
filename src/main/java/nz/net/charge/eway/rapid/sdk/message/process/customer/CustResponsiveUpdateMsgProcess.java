package nz.net.charge.eway.rapid.sdk.message.process.customer;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedRequest;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.AccessCodeSharedToCreateCustConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.util.Constant;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Update customer with responsive shared method message process
 */
public class CustResponsiveUpdateMsgProcess extends AbstractMakeRequestMessageProcess<Customer, CreateCustomerResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public CustResponsiveUpdateMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Customer input) throws RapidSdkException {
        CreateAccessCodeSharedRequest request = new CreateAccessCodeSharedRequest();
        CustomerToInternalCustomerConverter interCustConvert = new CustomerToInternalCustomerConverter();
        Payment payment = new Payment();
        payment.setTotalAmount(0);
        request.setPayment(payment);
        request.setCustomer(interCustConvert.doConvert(input));
        request.setMethod(Constant.UPDATE_TOKEN_CUSTOMER_METHOD);
        request.setTransactionType(TransactionType.Purchase.name());
        request.setRedirectUrl(input.getRedirectUrl());
        request.setCancelUrl(input.getCancelUrl());
        return request;
    }

    @Override
    protected CreateCustomerResponse makeResult(Response res) throws RapidSdkException {
        // Cast to implement response object
        CreateAccessCodeSharedResponse response = (CreateAccessCodeSharedResponse) res;
        BeanConverter<CreateAccessCodeSharedResponse, CreateCustomerResponse> converter = new AccessCodeSharedToCreateCustConverter();
        return converter.doConvert(response);
    }

    @Override
    protected Response sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CreateAccessCodeSharedResponse.class);
    }

}
