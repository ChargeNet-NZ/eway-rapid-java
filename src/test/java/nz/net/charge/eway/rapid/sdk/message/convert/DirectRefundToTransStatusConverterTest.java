package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundResponse;
import nz.net.charge.eway.rapid.sdk.exception.ParameterInvalidException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
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
        response.setTransactionStatus(true);
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
