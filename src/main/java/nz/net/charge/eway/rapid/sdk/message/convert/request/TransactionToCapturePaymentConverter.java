package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.entities.CapturePaymentRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;

public class TransactionToCapturePaymentConverter implements BeanConverter<Transaction, CapturePaymentRequest> {

    public CapturePaymentRequest doConvert(Transaction u) throws RapidSdkException {
        CapturePaymentRequest request = new CapturePaymentRequest();
        if (u.getAuthTransactionID() != null) {
            request.setTransactionId(String.valueOf(u.getAuthTransactionID()));
        }
        if (u.getPaymentDetails() != null) {
            Payment payment = new Payment();
            payment.setCurrencyCode(u.getPaymentDetails().getCurrencyCode());
            payment.setInvoiceDescription(u.getPaymentDetails().getInvoiceDescription());
            payment.setInvoiceNumber(u.getPaymentDetails().getInvoiceNumber());
            payment.setInvoiceReference(u.getPaymentDetails().getInvoiceReference());
            payment.setTotalAmount(u.getPaymentDetails().getTotalAmount());
            request.setPayment(payment);
        }
        return request;
    }

}
