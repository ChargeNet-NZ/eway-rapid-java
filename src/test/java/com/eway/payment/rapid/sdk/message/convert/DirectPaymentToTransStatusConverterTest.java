package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.FraudAction;
import com.eway.payment.rapid.sdk.beans.external.TransactionStatus;
import com.eway.payment.rapid.sdk.beans.internal.Payment;
import com.eway.payment.rapid.sdk.beans.internal.Verification;
import com.eway.payment.rapid.sdk.entities.DirectPaymentResponse;
import com.eway.payment.rapid.sdk.exception.ParameterInvalidException;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DirectPaymentToTransStatusConverterTest {

    private DirectPaymentToTransStatusConverter convert;

    @BeforeEach
    public void setup() {
        convert = new DirectPaymentToTransStatusConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        DirectPaymentResponse response = new DirectPaymentResponse();
        response.setTransactionID("123456");
        response.setBeagleScore(0d);
        response.setFraudAction(FraudAction.Allow.name());
        response.setTransactionCaptured("true");
        // response.setAuthorisationCode("1234");
        // response.setResponseCode("200");
        // response.setResponseMessage("A2000");
        response.setTransactionStatus(true);
        Payment payment = ObjectCreator.createPayment();
        response.setPayment(payment);
        Verification verification = ObjectCreator.createVerification();
        response.setVerification(verification);
        TransactionStatus status = convert.doConvert(response);
        assertThat(response.getBeagleScore()).isEqualByComparingTo(0d);
        assertThat(status.getFraudAction()).isEqualTo(FraudAction.Allow);
        assertThat(status.isCaptured()).isTrue();
    }

    @Test
    public void testInvalidTransactionId() throws RapidSdkException {

        // Given
        DirectPaymentResponse response = new DirectPaymentResponse();
        response.setTransactionID("abcd");
        Payment payment = ObjectCreator.createPayment();
        response.setPayment(payment);

        // When
        assertThatThrownBy(() -> convert.doConvert(response))

        // Then
                .isInstanceOf(ParameterInvalidException.class);
    }

    @Test
    public void testInvalidVerification() throws RapidSdkException {
        DirectPaymentResponse response = new DirectPaymentResponse();
        response.setTransactionID("1234");
        Payment payment = ObjectCreator.createPayment();
        response.setPayment(payment);
        Verification verification = ObjectCreator.createVerification();
        verification.setAddress("a");
        response.setVerification(verification);
        convert.doConvert(response);
    }
}
