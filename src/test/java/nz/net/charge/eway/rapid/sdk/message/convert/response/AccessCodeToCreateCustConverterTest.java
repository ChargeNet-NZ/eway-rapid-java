package nz.net.charge.eway.rapid.sdk.message.convert.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeToCreateCustConverterTest {

    private BeanConverter<CreateAccessCodeResponse, CreateCustomerResponse> convert;
    CreateAccessCodeResponse response;

    @BeforeEach
    public void setup() throws Exception {
        convert = new AccessCodeToCreateCustConverter();
        String escapedJson = "{\"AccessCode\":\"C3AB9ps780Y_gslZ4qpPNvOUwynNt-HOLzfvNDAoxxCfljjZiFPJIrYs4k0TpSAaYWw2AGxNH3hZfIlqKdCX_qGgzVTa8jBFSFSTv3wwk9CiNIWVAOm64gHAS6frxFkmqHmOc\",\"Customer\":{\"CardNumber\":\"\",\"CardStartMonth\":\"\",\"CardStartYear\":\"\",\"CardIssueNumber\":\"\",\"CardName\":\"\",\"CardExpiryMonth\":\"\",\"CardExpiryYear\":\"\",\"IsActive\":false,\"TokenCustomerID\":null,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Payment\":{\"TotalAmount\":0,\"InvoiceNumber\":null,\"InvoiceDescription\":null,\"InvoiceReference\":null,\"CurrencyCode\":\"AUD\"},\"FormActionURL\":\"https://secure-au.sandbox.ewaypayments.com/Process\",\"CompleteCheckoutURL\":null,\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, CreateAccessCodeResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateCustomerResponse customerRes = convert.doConvert(response);
        assertThat(customerRes.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(customerRes.getCustomer().getAddress().getStreet1()).isEqualTo("Level 5");

    }
}
