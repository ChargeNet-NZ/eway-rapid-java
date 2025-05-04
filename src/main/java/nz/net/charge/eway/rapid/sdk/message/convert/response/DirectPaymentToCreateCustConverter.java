package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class DirectPaymentToCreateCustConverter implements BeanConverter<DirectPaymentResponse, CreateCustomerResponse> {

    public CreateCustomerResponse doConvert(DirectPaymentResponse response) throws RapidSdkException {
        CreateCustomerResponse customerResponse = new CreateCustomerResponse();
        BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> custConvert = new InternalCustomerToCustomerConverter();
        customerResponse.setCustomer(custConvert.doConvert(response.getCustomer()));
        if (!StringUtils.isBlank(response.getErrors())) {
            customerResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        return customerResponse;
    }

}
