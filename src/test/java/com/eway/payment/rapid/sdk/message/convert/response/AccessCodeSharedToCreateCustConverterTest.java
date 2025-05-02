package com.eway.payment.rapid.sdk.message.convert.response;

import com.eway.payment.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import com.eway.payment.rapid.sdk.entities.CreateCustomerResponse;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.message.convert.BeanConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeSharedToCreateCustConverterTest {

    private BeanConverter<CreateAccessCodeSharedResponse, CreateCustomerResponse> convert;
    CreateAccessCodeSharedResponse response;

    @BeforeEach
    public void setup() throws Exception {
        convert = new AccessCodeSharedToCreateCustConverter();
        String escapedJson = "{\"SharedPaymentUrl\":\"https://secure-au.sandbox.ewaypayments.com/sharedpage/sharedpayment?AccessCode=44DD783Y-0cfpduADO64Y5L5xpfeqZPei7OFZEIjA0o4LwUjpwZDswkmLdc0l6B1GGend7JwTs1tcsT3jx3HbkqOR-V7BlgWuJlCrVyIHn-FpuKwFmaGmD-KYsLNrPlTWB9hl\",\"AccessCode\":\"44DD783Y-0cfpduADO64Y5L5xpfeqZPei7OFZEIjA0o4LwUjpwZDswkmLdc0l6B1GGend7JwTs1tcsT3jx3HbkqOR-V7BlgWuJlCrVyIHn-FpuKwFmaGmD-KYsLNrPlTWB9hl\",\"Customer\":{\"CardNumber\":\"\",\"CardStartMonth\":\"\",\"CardStartYear\":\"\",\"CardIssueNumber\":\"\",\"CardName\":\"\",\"CardExpiryMonth\":\"\",\"CardExpiryYear\":\"\",\"IsActive\":false,\"TokenCustomerID\":null,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Payment\":{\"TotalAmount\":0,\"InvoiceNumber\":null,\"InvoiceDescription\":null,\"InvoiceReference\":null,\"CurrencyCode\":\"AUD\"},\"FormActionURL\":\"https://secure-au.sandbox.ewaypayments.com/Process\",\"CompleteCheckoutURL\":null,\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, CreateAccessCodeSharedResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateCustomerResponse customerRes = convert.doConvert(response);
        assertThat(customerRes.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(customerRes.getCustomer().getAddress().getStreet1()).isEqualTo("Level 5");

    }
}
