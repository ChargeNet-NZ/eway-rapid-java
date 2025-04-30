package nz.net.charge.eway.rapid.sdk.message.process.customer;

import nz.net.charge.eway.rapid.sdk.entities.DirectCustomerSearchRequest;
import nz.net.charge.eway.rapid.sdk.entities.DirectCustomerSearchResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.DirectCustomerToQueryCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.QueryCustomerResponse;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Query customer message process
 */
public class QueryCustomerMsgProcess extends AbstractMakeRequestMessageProcess<String, QueryCustomerResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public QueryCustomerMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(String tokenCustomerID) throws RapidSdkException {
        DirectCustomerSearchRequest request = new DirectCustomerSearchRequest();
        request.setTokenCustomerID(tokenCustomerID);
        return request;
    }

    @Override
    protected Response sendRequest(Request req) throws RapidSdkException {
        return doPost(req, DirectCustomerSearchResponse.class);
    }

    @Override
    protected QueryCustomerResponse makeResult(Response res) throws RapidSdkException {
        DirectCustomerSearchResponse response = (DirectCustomerSearchResponse) res;
        BeanConverter<DirectCustomerSearchResponse, QueryCustomerResponse> convert = new DirectCustomerToQueryCustomerConverter();
        return convert.doConvert(response);
    }

}
