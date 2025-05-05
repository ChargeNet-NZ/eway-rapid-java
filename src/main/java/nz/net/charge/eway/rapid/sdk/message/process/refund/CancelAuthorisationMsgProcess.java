package nz.net.charge.eway.rapid.sdk.message.process.refund;

import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.internal.RefundDetails;
import nz.net.charge.eway.rapid.sdk.entities.CancelAuthorisationRequest;
import nz.net.charge.eway.rapid.sdk.entities.CancelAuthorisationResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.ParameterInvalidException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.CancelAuthorisationToRefundConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CancelAuthorisationMsgProcess extends AbstractMakeRequestMessageProcess<Refund, RefundResponse> {

    /**
     * @param client The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public CancelAuthorisationMsgProcess(WebClient client, String... requestPath) {
        super(client, requestPath);
    }

    @Override
    protected Request createRequest(Refund refund) throws RapidSdkException {
        CancelAuthorisationRequest request = new CancelAuthorisationRequest();
        RefundDetails detail = refund.getRefundDetails();
        if (detail == null) {
            throw new ParameterInvalidException("Refund details are null");
        }
        request.setTransactionId(detail.getOriginalTransactionID());
        return request;
    }

    @Override
    protected Mono<CancelAuthorisationResponse> sendRequest(Request req) throws RapidSdkException {
        return doPost(req, CancelAuthorisationResponse.class);
    }

    @Override
    protected RefundResponse makeResult(Response res) {

        BeanConverter<CancelAuthorisationResponse, RefundResponse> converter =
                new CancelAuthorisationToRefundConverter(getInput());
        return converter.doConvert((CancelAuthorisationResponse) res);
    }

}
