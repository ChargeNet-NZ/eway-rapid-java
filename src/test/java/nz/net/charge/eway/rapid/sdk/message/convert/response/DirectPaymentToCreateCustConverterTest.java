package nz.net.charge.eway.rapid.sdk.message.convert.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectPaymentToCreateCustConverterTest {

    private BeanConverter<DirectPaymentResponse, CreateCustomerResponse> convert;
    DirectPaymentResponse response;

    @BeforeEach
    public void setup() throws Exception {
        convert = new DirectPaymentToCreateCustConverter();
        String escapedJson = "{\"AuthorisationCode\":null,\"ResponseCode\":\"00\",\"ResponseMessage\":\"A2000\",\"TransactionID\":null,\"TransactionStatus\":false,\"TransactionType\":\"Purchase\",\"BeagleScore\":null,\"Verification\":{\"CVN\":0,\"Address\":0,\"Email\":0,\"Mobile\":0,\"Phone\":0},\"Customer\":{\"CardDetails\":{\"Number\":\"444433XXXXXX1111\",\"Name\":\"John Smith\",\"ExpiryMonth\":\"12\",\"ExpiryYear\":\"25\",\"StartMonth\":null,\"StartYear\":null,\"IssueNumber\":null},\"TokenCustomerID\":913079262890,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"Au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Payment\":{\"TotalAmount\":0,\"InvoiceNumber\":\"\",\"InvoiceDescription\":\"\",\"InvoiceReference\":\"\",\"CurrencyCode\":\"AUD\"},\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, DirectPaymentResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateCustomerResponse customerRes = convert.doConvert(response);
        assertThat(customerRes.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(customerRes.getCustomer().getAddress().getStreet1()).isEqualTo("Level 5");
        assertThat(customerRes.getCustomer().getCardDetails().getExpiryMonth()).isEqualTo("12");

    }
}
