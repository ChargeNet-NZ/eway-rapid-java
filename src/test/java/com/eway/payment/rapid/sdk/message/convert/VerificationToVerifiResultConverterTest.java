package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.VerificationResult;
import com.eway.payment.rapid.sdk.beans.external.VerifyStatus;
import com.eway.payment.rapid.sdk.beans.internal.Verification;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerificationToVerifiResultConverterTest {

    private BeanConverter<Verification, VerificationResult> convert;

    @BeforeEach
    public void setup() {
        convert = new VerificationToVerifiResultConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Verification v = ObjectCreator.createVerification();
        VerificationResult vr = convert.doConvert(v);
        assertThat(vr.getCVN()).isEqualTo(VerifyStatus.Unchecked);
    }

    @Test
    public void testInvalidStatus() throws RapidSdkException {
        Verification v = ObjectCreator.createVerification();
        v.setAddress("a");
        VerificationResult result = convert.doConvert(v);
        assertThat(result.getAddress()).isNull();
    }
}
