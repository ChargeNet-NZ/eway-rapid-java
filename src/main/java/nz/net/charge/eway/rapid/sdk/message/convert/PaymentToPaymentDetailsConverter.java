package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

public class PaymentToPaymentDetailsConverter implements BeanConverter<Payment, PaymentDetails> {

    public PaymentDetails doConvert(Payment u) throws RapidSdkException {
        PaymentDetails detail = new PaymentDetails();
        if (u != null) {
            detail.setCurrencyCode(u.getCurrencyCode());
            detail.setInvoiceDescription(u.getInvoiceDescription());
            detail.setInvoiceNumber(u.getInvoiceNumber());
            detail.setInvoiceReference(u.getInvoiceReference());
            detail.setTotalAmount(u.getTotalAmount());
        }
        return detail;
    }

}
