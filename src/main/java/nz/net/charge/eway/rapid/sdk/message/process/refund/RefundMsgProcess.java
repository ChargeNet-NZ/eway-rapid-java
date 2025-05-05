package nz.net.charge.eway.rapid.sdk.message.process.refund;

import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundRequest;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundResponse;
import nz.net.charge.eway.rapid.sdk.entities.Request;
import nz.net.charge.eway.rapid.sdk.entities.Response;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.request.RefundToDirectRefundReqConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.response.DirectRefundToRefundResponseConverter;
import nz.net.charge.eway.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import nz.net.charge.eway.rapid.sdk.util.Constant;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Refund message process
 */
public class RefundMsgProcess extends AbstractMakeRequestMessageProcess<Refund, RefundResponse> {

    /**
     * @param webClient The web client to call Rapid API
     * @param requestPath Path of request URL. Used to make full web service URL
     */
    public RefundMsgProcess(WebClient webClient, String... requestPath) {
        super(webClient, requestPath);
    }

    @Override
    protected Request createRequest(Refund refund) throws RapidSdkException {
        BeanConverter<Refund, DirectRefundRequest> reqConverter = new RefundToDirectRefundReqConverter();
        return reqConverter.doConvert(refund);
    }

    @Override
    protected Mono<DirectRefundResponse> sendRequest(Request req) throws RapidSdkException {
        DirectRefundRequest request = (DirectRefundRequest) req;
        addRequestPath(request.getRefund().getOriginalTransactionID(), Constant.REFUND_SUBPATH_METHOD);
        return doPost(request, DirectRefundResponse.class);
    }

    @Override
    protected Mono<RefundResponse> makeResult(Mono<? extends Response> res) {
        return res.map(response -> {
            BeanConverter<DirectRefundResponse, RefundResponse> converter =
                    new DirectRefundToRefundResponseConverter();
            return converter.doConvert((DirectRefundResponse) response);
        });
    }

}
