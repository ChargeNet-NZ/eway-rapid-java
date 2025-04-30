package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.ProcessingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.entities.CancelAuthorisationResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class CancelAuthorisationToRefundConverter implements BeanConverter<CancelAuthorisationResponse, RefundResponse> {

    private final Refund refund;

    public CancelAuthorisationToRefundConverter(Refund refund) {
        this.refund = refund;
    }

    public RefundResponse doConvert(CancelAuthorisationResponse cancel) throws RapidSdkException {
        RefundResponse response = new RefundResponse();
        response.setRefund(this.refund);

        TransactionStatus status = new TransactionStatus();
        status.setStatus(cancel.getTransactionStatus());
        ProcessingDetails detail = new ProcessingDetails();
        detail.setResponseCode(cancel.getResponseCode());
        status.setProcessingDetails(detail);
        status.setTransactionID(Integer.parseInt(cancel.getTransactionID()));
        response.setTransactionStatus(status);

        if (!StringUtils.isBlank(cancel.getErrors())) {
            response.getErrors().addAll(Arrays.asList(cancel.getErrors().split("\\s*,\\s*")));
        }
        return response;
    }

}
