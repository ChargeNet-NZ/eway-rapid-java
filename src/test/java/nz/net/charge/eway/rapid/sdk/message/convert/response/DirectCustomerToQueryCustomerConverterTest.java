package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.entities.DirectCustomerSearchResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.output.QueryCustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectCustomerToQueryCustomerConverterTest {

    private BeanConverter<DirectCustomerSearchResponse, QueryCustomerResponse> convert;
    DirectCustomerSearchResponse response;
    DirectCustomerSearchResponse errorResponse;

    @BeforeEach
    public void setup() throws Exception {
        convert = new DirectCustomerToQueryCustomerConverter();
        String escapedJson = "{\"Customers\":[{\"CardDetails\":{\"Number\":\"444433XXXXXX1111\",\"Name\":\"John Smith\",\"ExpiryMonth\":\"12\",\"ExpiryYear\":\"25\",\"StartMonth\":\"\",\"StartYear\":\"\",\"IssueNumber\":\"\"},\"TokenCustomerID\":912981244527,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5, 369 Queen Street\",\"Street2\":null,\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"Au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"}],\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        String escapedErrorJson = "{\"Customers\":[],\"Errors\":\"V6040\"}";
        String errorJson = StringEscapeUtils.unescapeJson(escapedErrorJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, DirectCustomerSearchResponse.class);
        errorResponse = mapper.readValue(errorJson, DirectCustomerSearchResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        QueryCustomerResponse customerRes = convert.doConvert(response);
        assertThat(customerRes.getCardDetail().getExpiryMonth()).isEqualTo("12");
        assertThat(customerRes.getFirstName()).isEqualTo("John");
        assertThat(customerRes.getCity()).isEqualTo("Sydney");

    }

    @Test
    public void testError() throws RapidSdkException {
        QueryCustomerResponse customerRes = convert.doConvert(errorResponse);
        assertThat(customerRes.getErrors().get(0)).isEqualTo("V6040");

    }
}
