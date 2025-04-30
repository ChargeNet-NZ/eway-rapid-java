package nz.net.charge.eway.rapid.sdk.message.process.transaction;

import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.entities.TransactionSearchResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.SearchToQueryTransConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.QueryTransactionResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Query transaction message process
 */
public class TransQueryMsgProcess extends AbstractMessageProcess<String, QueryTransactionResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public TransQueryMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Response processPostMsg(String req) throws RapidSdkException {
        return doGet(StringUtils.isBlank(req) ? "0" : req, TransactionSearchResponse.class);
    }

    @Override
    protected QueryTransactionResponse makeResult(Response res) throws RapidSdkException {
        TransactionSearchResponse response = (TransactionSearchResponse) res;
        BeanConverter<TransactionSearchResponse, QueryTransactionResponse> converter = new SearchToQueryTransConverter();
        return converter.doConvert(response);
    }

}
