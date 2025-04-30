package nz.net.charge.eway.rapid.sdk.message.convert.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeToCreateTransConverterTest {

    private BeanConverter<CreateAccessCodeResponse, CreateTransactionResponse> convert;
    CreateAccessCodeResponse response;

    @BeforeEach
    public void setup() throws Exception {
        convert = new AccessCodeToCreateTransConverter();
        String escapedJson = "{\"AccessCode\":\"60CF3L9mWK-zQ9wxakOtyfjcBpNSRIngC2X2gudGR07jB7w_a8RgW6vp3YfmUCXK9cuywC8whzukY6TldEWlq0digXA_OOlwOr5rrUiHtL6EzruLWCQS-1byCuv_lgWz8hBMp\",\"Customer\":{\"CardNumber\":\"\",\"CardStartMonth\":\"\",\"CardStartYear\":\"\",\"CardIssueNumber\":\"\",\"CardName\":\"\",\"CardExpiryMonth\":\"\",\"CardExpiryYear\":\"\",\"IsActive\":false,\"TokenCustomerID\":null,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Payment\":{\"TotalAmount\":1000,\"InvoiceNumber\":\"Inv 21540\",\"InvoiceDescription\":\"Individual Invoice Description\",\"InvoiceReference\":\"513456\",\"CurrencyCode\":\"AUD\"},\"FormActionURL\":\"https://secure-au.sandbox.ewaypayments.com/Process\",\"CompleteCheckoutURL\":null,\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, CreateAccessCodeResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateTransactionResponse transRes = convert.doConvert(response);
        assertThat(transRes.getTransaction().getPaymentDetails().getTotalAmount()).isEqualTo(1000);
        assertThat(transRes.getTransaction().getCustomer().getFirstName()).isEqualTo("John");
    }
}
