package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.TransactionStatus;
import com.eway.payment.rapid.sdk.entities.DirectRefundResponse;
import com.eway.payment.rapid.sdk.exception.ParameterInvalidException;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DirectRefundToTransStatusConverterTest {

    private BeanConverter<DirectRefundResponse, TransactionStatus> convert;

    @BeforeEach
    public void setup() {
        convert = new DirectRefundToTransStatusConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        DirectRefundResponse response = new DirectRefundResponse();
        response.setTransactionID("123456");
        response.setTransactionStatus(new Boolean(true));
        TransactionStatus status = convert.doConvert(response);
        assertThat(status.getTransactionID()).isEqualTo(123456);
        assertThat(status.isStatus()).isTrue();
    }

    @Test
    public void testInvalidTransactionId() {

        // Given
        DirectRefundResponse response = new DirectRefundResponse();
        response.setTransactionID("abcd");

        // When
        assertThatThrownBy(() -> convert.doConvert(response))

        // Then
            .isInstanceOf(ParameterInvalidException.class);
    }
}
