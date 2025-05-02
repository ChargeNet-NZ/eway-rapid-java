package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerToInternalConverterTest {

    private CustomerToInternalCustomerConverter convert;

    @BeforeEach
    public void setup() {
        convert = new CustomerToInternalCustomerConverter();
    }

    @Test
    public void testConvertCustomerCardDetail() throws Exception {
        Customer cust = InputModelFactory.initCustomer();
        cust.setCardDetails(InputModelFactory.initCardDetails("12", "25"));
        com.eway.payment.rapid.sdk.beans.internal.Customer interCust = convert.doConvert(cust);
        CardDetails detail = interCust.getCardDetails();
        assertThat(detail).isNotNull();
        assertThat(detail.getExpiryMonth()).isNotNull();
        assertThat(detail.getExpiryYear()).isNotNull();
        assertThat(detail.getExpiryMonth()).isEqualTo("12");
        assertThat(detail.getExpiryYear()).isEqualTo("25");
    }

    @Test
    public void testConvertCustomerAddress() throws Exception {
        Customer cust = InputModelFactory.initCustomer();
        String city = "Sydney";
        String postalCode = "084";
        String state = "NSW";
        String country = "Autralia";
        Address add = InputModelFactory.createAddress(city, country, postalCode, state);
        cust.setAddress(add);
        com.eway.payment.rapid.sdk.beans.internal.Customer interCust = convert.doConvert(cust);
        assertThat(interCust.getState()).isNotNull();
        assertThat(interCust.getState()).isEqualTo(state);
        assertThat(interCust.getCountry()).isNotNull();
        assertThat(interCust.getCountry()).isEqualTo(country);
        assertThat(interCust.getCity()).isNotNull();
        assertThat(interCust.getCity()).isEqualTo(city);
        assertThat(interCust.getPostalCode()).isNotNull();
        assertThat(interCust.getPostalCode()).isEqualTo(postalCode);
    }

    @Test
    public void testConvertCustomerWithoutAddress() throws Exception {
        Customer cust = InputModelFactory.initCustomer();
        com.eway.payment.rapid.sdk.beans.internal.Customer interCust = convert.doConvert(cust);
        assertThat(interCust.getState()).isNull();
        assertThat(interCust.getCity()).isNull();
        assertThat(interCust.getCountry()).isNull();
        assertThat(interCust.getPostalCode()).isNull();
    }

}
