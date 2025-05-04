package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.ProcessingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.entities.CapturePaymentResponse;
import nz.net.charge.eway.rapid.sdk.exception.ParameterInvalidException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class CapturePaymentToCreateTransactionConverter implements BeanConverter<CapturePaymentResponse, CreateTransactionResponse> {

    public CreateTransactionResponse doConvert(CapturePaymentResponse capture) throws RapidSdkException {
        CreateTransactionResponse response = new CreateTransactionResponse();
        TransactionStatus status = new TransactionStatus();
        if (!StringUtils.isBlank(capture.getTransactionID())) {
            try {
                status.setTransactionID(Integer.parseInt(capture.getTransactionID()));
            } catch (Exception e) {
                throw new ParameterInvalidException("Error converting transaction ID " + capture.getTransactionID() + " to integer", e);
            }
        }
        status.setStatus(capture.getTransactionStatus());
        ProcessingDetails detail = new ProcessingDetails();
        detail.setResponseCode(capture.getResponseCode());
        status.setProcessingDetails(detail);
        response.setTransactionStatus(status);
        if (!StringUtils.isBlank(capture.getErrors())) {
            response.setErrors(Arrays.asList(capture.getErrors().split("\\s*,\\s*")));
        }

        return response;
    }

}
