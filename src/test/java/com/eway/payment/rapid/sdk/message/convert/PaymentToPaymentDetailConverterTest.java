package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.internal.Payment;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentToPaymentDetailConverterTest {

    private BeanConverter<Payment, PaymentDetails> convert;

    @BeforeEach
    public void setup() {
        convert = new PaymentToPaymentDetailsConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Payment payment = ObjectCreator.createPayment();
        PaymentDetails paymentDetails = convert.doConvert(payment);
        assertThat(paymentDetails.getTotalAmount()).isEqualTo(1000);
    }

    @Test
    public void testNullPayment() throws RapidSdkException {
        PaymentDetails paymentDetails = convert.doConvert(null);
        assertThat(paymentDetails.getTotalAmount()).isEqualTo(0);
    }
}
