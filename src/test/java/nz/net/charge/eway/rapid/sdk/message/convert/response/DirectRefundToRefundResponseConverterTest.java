package nz.net.charge.eway.rapid.sdk.message.convert.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectRefundToRefundResponseConverterTest {

    private BeanConverter<DirectRefundResponse, RefundResponse> convert;
    DirectRefundResponse response;

    @BeforeEach
    public void setup() throws Exception {
        convert = new DirectRefundToRefundResponseConverter();
        String escapedJson = "{\"AuthorisationCode\":\"480233\",\"ResponseCode\":null,\"ResponseMessage\":\"A2000\",\"TransactionID\":11735642,\"TransactionStatus\":true,\"Verification\":null,\"Customer\":{\"CardDetails\":{\"Number\":\"\",\"Name\":\"\",\"ExpiryMonth\":\"12\",\"ExpiryYear\":\"25\",\"StartMonth\":\"\",\"StartYear\":\"\",\"IssueNumber\":\"\"},\"TokenCustomerID\":null,\"Reference\":\"\",\"Title\":\"Mr.\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"CompanyName\":\"Example\",\"JobDescription\":\"Java Developer\",\"Street1\":\"Level 5\",\"Street2\":\"369 Queen Street\",\"City\":\"Sydney\",\"State\":\"NSW\",\"PostalCode\":\"2000\",\"Country\":\"au\",\"Email\":\"\",\"Phone\":\"0123456789\",\"Mobile\":\"0123456789\",\"Comments\":\"\",\"Fax\":\"1234\",\"Url\":\"http://www.ewaypayments.com\"},\"Refund\":{\"TransactionID\":\"11735641\",\"TotalAmount\":1000,\"InvoiceNumber\":\"Inv 21540\",\"InvoiceDescription\":\"Individual Invoice Description\",\"InvoiceReference\":\"513456\",\"CurrencyCode\":\"AUD\"},\"Errors\":null}";
        String json = StringEscapeUtils.unescapeJson(escapedJson);
        ObjectMapper mapper = new ObjectMapper();

        response = mapper.readValue(json, DirectRefundResponse.class);

    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        RefundResponse res = convert.doConvert(response);
        assertThat(res.getRefund().getRefundDetails().getTotalAmount()).isEqualTo(1000);
        assertThat(res.getRefund().getCustomer().getFirstName()).isEqualTo("John");
        assertThat(res.getRefund().getCustomer().getCardDetails().getExpiryMonth()).isEqualTo("12");
    }
}
