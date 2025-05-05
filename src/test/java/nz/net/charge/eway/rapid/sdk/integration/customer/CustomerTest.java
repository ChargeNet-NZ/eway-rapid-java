package nz.net.charge.eway.rapid.sdk.integration.customer;

import nz.net.charge.eway.rapid.sdk.InputModelFactory;
import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.RapidSDK;
import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentMethod;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.integration.IntegrationTest;
import nz.net.charge.eway.rapid.sdk.output.QueryCustomerResponse;
import nz.net.charge.eway.rapid.sdk.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest extends IntegrationTest {

    Customer cust;
    Address address;

    @BeforeEach
    public void setup() {
        cust = InputModelFactory.initCustomer();
        address = InputModelFactory.initAddress();
    }

    @Test
    public void testCreateCustomerDirect() {
        Customer customer = getCustomerDirect(cust, address);
        assertThat(customer.getFirstName()).isEqualTo(cust.getFirstName());
    }

    @Test
    public void testCreateCustomerDirectButAuthenFailure() {
        RapidClient client = RapidSDK.newRapidClient(APIKEY, "ABCXYZ", SANDBOX_ENDPOINT);
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        cust.setCardDetails(detail);
        cust.setAddress(address);
        CreateCustomerResponse response = client.create(PaymentMethod.Direct, cust).block();
        assertThat(response.getErrors())
                .isNotEmpty()
                .hasSize(1)
                .contains(Constant.AUTHENTICATION_FAILURE_ERROR_CODE);
    }

    @Test
    public void testCreateCustomerDirectButWrongEndpoint() throws Exception {
        String invalidEndpoint = "https://api.sandbox.ewaypayments.com";
        RapidClient client = RapidSDK.newRapidClient(APIKEY, "ABCXYZ", invalidEndpoint);
        String fakeWebUrl = "https://hhhhhhh.ggg";
        // Change invalid url
        Class<?> c = client.getClass();
        Field f = c.getDeclaredField("webUrl");
        f.setAccessible(true);
        f.set(client, fakeWebUrl);
        // Test
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        cust.setCardDetails(detail);
        cust.setAddress(address);
        CreateCustomerResponse response = client.create(PaymentMethod.Direct, cust).block();
        assertThat(response.getErrors())
                .isNotEmpty()
                .hasSize(1)
                .contains(Constant.COMMUNICATION_FAILURE_ERROR_CODE);
    }

    @Test
    public void testCreateCustomerInvalidPaymentMethod() throws Exception {
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.Wallet, cust).block();
        assertThat(response.getErrors())
                .isNotEmpty()
                .hasSize(1)
                .contains(Constant.INTERNAL_RAPID_API_ERROR_CODE);
    }

    @Test
    public void testCreateCustomerWithBlankInput() throws Exception {
        cust.setAddress(address);
        CardDetails detail = new CardDetails();
        cust.setCardDetails(detail);
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.Direct, cust).block();
        assertThat(response.getErrors()).isNotEmpty();
        String[] errorCode = {"V6021", "V6101", "V6101", "V6102", "V6102"}; //Rapid returns the wrong errors v6021,v6101,v6101,v6102,v6102, Should be V6021,V6022,V6101,V6102
        for (String errCheck : errorCode) assertThat(response.getErrors()).contains(errCheck);
    }

    @Test
    public void testQueryDirectValidCustomerId() {
        cust.setAddress(address);
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        cust.setCardDetails(detail);
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.Direct, cust).block();
        String tokenCustomerId = response.getCustomer().getTokenCustomerID();
        assertThat(tokenCustomerId).isNotNull();
        long tokenId = Long.parseLong(tokenCustomerId);
        QueryCustomerResponse custResponse = getSandboxClient().queryCustomer(tokenId).block();
        List<String> listError = custResponse.getErrors();
        assertThat(listError == null || listError.isEmpty()).isTrue();
    }

    @Test
    public void testCreateCustomerWithResponseShared() {
        cust.setAddress(address);
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        cust.setCardDetails(detail);
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.ResponsiveShared, cust).block();
        assertThat(StringUtils.isBlank(response.getSharedPaymentUrl())).isFalse();
    }

    @Test
    public void testCreateCustomerWithTransparentReDirect() {
        cust.setAddress(address);
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        cust.setCardDetails(detail);
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.TransparentRedirect, cust).block();
        assertThat(StringUtils.isBlank(response.getFormActionUrl())).isFalse();
    }

    @Test
    public void testUpdateCustomerDirect() {
        Customer customer = getCustomerDirect(cust, address);
        customer.setFirstName("Steve");
        customer.setLastName("Chistian");
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        customer.setCardDetails(detail);
        CreateCustomerResponse response = getSandboxClient().update(PaymentMethod.Direct, customer).block();
        assertThat(response.getCustomer().getTokenCustomerID()).isEqualTo(customer.getTokenCustomerID());
        assertThat(response.getCustomer().getFirstName()).isEqualTo("Steve");
        assertThat(response.getCustomer().getLastName()).isEqualTo("Chistian");
    }

    @Test
    public void testUpdateCustomerResponsive() {
        Customer customer = getCustomerDirect(cust, address);
        customer.setFirstName("Steve");
        customer.setLastName("Chistian");
        customer.setRedirectUrl("http://www.eway.com.au");
        customer.setCancelUrl("http://www.eway.com.au");
        CreateCustomerResponse response = getSandboxClient().update(PaymentMethod.ResponsiveShared, customer).block();
        assertThat(response.getCustomer().getFirstName()).isEqualTo("Steve");
        assertThat(response.getCustomer().getLastName()).isEqualTo("Chistian");
    }

    @Test
    public void testUpdateCustomerTransparent() {
        Customer customer = getCustomerDirect(cust, address);
        customer.setFirstName("Steve");
        customer.setLastName("Chistian");
        customer.setRedirectUrl("http://www.eway.com.au");
        CreateCustomerResponse response = getSandboxClient().update(PaymentMethod.TransparentRedirect, customer).block();
        assertThat(response.getCustomer().getFirstName()).isEqualTo("Steve");
        assertThat(response.getCustomer().getLastName()).isEqualTo("Chistian");
    }

    private Customer getCustomerDirect(Customer customer, Address address) {
        CardDetails detail = InputModelFactory.initCardDetails("12", "25");
        customer.setCardDetails(detail);
        customer.setAddress(address);
        CreateCustomerResponse response = getSandboxClient().create(PaymentMethod.Direct, customer).block();
        return response.getCustomer();
    }
}
