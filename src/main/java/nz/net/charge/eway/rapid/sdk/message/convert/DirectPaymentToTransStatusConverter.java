package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.FraudAction;
import nz.net.charge.eway.rapid.sdk.beans.external.ProcessingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.beans.external.VerificationResult;
import nz.net.charge.eway.rapid.sdk.beans.internal.Verification;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.exception.ParameterInvalidException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import org.apache.commons.lang3.StringUtils;

public class DirectPaymentToTransStatusConverter implements BeanConverter<DirectPaymentResponse, TransactionStatus> {

    public TransactionStatus doConvert(DirectPaymentResponse response) throws RapidSdkException {
        TransactionStatus status = new TransactionStatus();
        status.setBeagleScore(response.getBeagleScore() != null ? response.getBeagleScore() : 0.0);
        status.setCaptured(Boolean.parseBoolean(response.getTransactionCaptured()));

        if (response.getFraudAction() == null || response.getFraudAction().equalsIgnoreCase("0") || response.getFraudAction().isEmpty()) {
            status.setFraudAction(FraudAction.valueOf(FraudAction.NotChallenged.name()));
        } else {
            status.setFraudAction(FraudAction.valueOf(response.getFraudAction()));
        }

        status.setProcessingDetails(getProcessingDetails(response));

        if (response.getTransactionStatus() != null) {
            status.setStatus(response.getTransactionStatus());
        }
        if (response.getPayment() != null) {
            status.setTotal(response.getPayment().getTotalAmount());
        }

        if (!StringUtils.isBlank(response.getTransactionID())) {
            try {
                status.setTransactionID(Integer.parseInt(response.getTransactionID()));
            } catch (Exception e) {
                throw new ParameterInvalidException("Invalid Transaction ID");
            }
        }
        BeanConverter<Verification, VerificationResult> verifiConvert = new VerificationToVerifiResultConverter();
        status.setVerificationResult(verifiConvert.doConvert(response.getVerification()));

        return status;
    }

    private ProcessingDetails getProcessingDetails(DirectPaymentResponse response) {
        ProcessingDetails processingDetails = new ProcessingDetails();
        processingDetails.setAuthorisationCode(response.getAuthorisationCode());
        processingDetails.setResponseCode(response.getResponseCode());
        processingDetails.setResponseMessage(response.getResponseMessage());
        return processingDetails;
    }
}
