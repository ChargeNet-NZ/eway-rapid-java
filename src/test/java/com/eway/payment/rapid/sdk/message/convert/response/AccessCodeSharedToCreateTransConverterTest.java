package com.eway.payment.rapid.sdk.message.convert.response;

import com.eway.payment.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.message.convert.BeanConverter;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeSharedToCreateTransConverterTest {

    private BeanConverter<CreateAccessCodeSharedResponse, CreateTransactionResponse> convert;
    CreateAccessCodeSharedResponse response;
    CreateAccessCodeSharedResponse errorResponse;

    @BeforeEach
    public void setup() throws Exception {
        convert = new AccessCodeSharedToCreateTransConverter();
        String escapedJson = "{\"SharedPaymentUrl\":\"https://secure-au.sandbox.ewaypayments.com/sharedpage/sharedpayment?AccessCode=F9802QqDnIqsLNfb1_rjKo2LA8P-RAalL-7TkBkYaDLRLuh-KDFqKVuvD0hhCmaWvqNhTnf07s2Kqhkru_nyySX-ZAjqRz6675u4tlZlFi1huKGXA2aFcS_l4BXlvmralGqtj\",\"AccessCode\":\"F9802QqDnIqsLNfb1_rjKo2LA8P-RAalL-7TkBkYaDLRLuh-KDFqKVuvD0hhCmaWvqNhTnf07s2Kqhkru_nyySX-ZAjqRz6675u4tlZlFi1huKGXA2aFcS_l4BXlvmralGqtj\",\"Customer\":{\"CardNumber\":\"\",\"CardStartMonth\":\"\",\"CardStartYear\":\"\",\"CardIssueNumber\":\"\",\"CardName\":\"\",\"CardExpiryMonth\":\"\",\"CardExpiryYear\":\"\",\"IsActive\":false,\"TokenCustomerID\":null,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Payment\":{\"TotalAmount\":1000,\"InvoiceNumber\":\"Inv 21540\",\"InvoiceDescription\":\"Individual Invoice Description\",\"InvoiceReference\":\"513456\",\"CurrencyCode\":\"AUD\"},\"FormActionURL\":\"https://secure-au.sandbox.ewaypayments.com/Process\",\"CompleteCheckoutURL\":null,\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        String escapedErrorJson = "{\"SharedPaymentUrl\":null,\"AccessCode\":null,\"Customer\":{\"CardNumber\":null,\"CardStartMonth\":null,\"CardStartYear\":null,\"CardIssueNumber\":null,\"CardName\":null,\"CardExpiryMonth\":null,\"CardExpiryYear\":null,\"IsActive\":false,\"TokenCustomerID\":null,\"Reference\":null,\"Title\":null,\"FirstName\":null,\"LastName\":null,\"CompanyName\":null,\"JobDescription\":null,\"Street1\":null,\"Street2\":null,\"City\":null,\"State\":null,\"PostalCode\":null,\"Country\":null,\"Email\":null,\"Phone\":null,\"Mobile\":null,\"Comments\":null,\"Fax\":null,\"Url\":null},\"Payment\":{\"TotalAmount\":1000,\"InvoiceNumber\":\"Inv 21540\",\"InvoiceDescription\":\"Individual Invoice Description\",\"InvoiceReference\":\"513456\",\"CurrencyCode\":\"AUD\"},\"FormActionURL\":null,\"CompleteCheckoutURL\":null,\"Errors\":\"V6047\"}";
        String errorJson = StringEscapeUtils.unescapeJson(escapedErrorJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, CreateAccessCodeSharedResponse.class);
        errorResponse = mapper.readValue(errorJson, CreateAccessCodeSharedResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateTransactionResponse transRes = convert.doConvert(response);
        assertThat(transRes.getTransaction().getPaymentDetails().getTotalAmount()).isEqualTo(1000);
        assertThat(transRes.getTransaction().getCustomer().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testError() throws RapidSdkException {
        CreateTransactionResponse transRes = convert.doConvert(errorResponse);
        assertThat(transRes.getErrors().get(0)).isEqualTo("V6047");
    }
}
