package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

public class TransactionToPaymentConverter implements BeanConverter<Transaction, Payment> {

    public Payment doConvert(Transaction transaction) throws RapidSdkException {
        Payment payment = new Payment();
        PaymentDetails paymentDetails = transaction.getPaymentDetails();
        if (paymentDetails != null) {
            payment.setCurrencyCode(paymentDetails.getCurrencyCode());
            payment.setInvoiceDescription(paymentDetails.getInvoiceDescription());
            payment.setInvoiceNumber(paymentDetails.getInvoiceNumber());
            payment.setInvoiceReference(paymentDetails.getInvoiceReference());
            payment.setTotalAmount(paymentDetails.getTotalAmount());
        }
        return payment;
    }

}
