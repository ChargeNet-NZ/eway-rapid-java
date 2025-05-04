package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AccessCodeSharedToCreateCustConverter implements BeanConverter<CreateAccessCodeSharedResponse, CreateCustomerResponse> {

    public CreateCustomerResponse doConvert(CreateAccessCodeSharedResponse response) {
        CreateCustomerResponse customerResponse = new CreateCustomerResponse();
        customerResponse.setAccessCode(response.getAccessCode());
        if (!StringUtils.isBlank(response.getErrors())) {
            customerResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        InternalCustomerToCustomerConverter custConvert = new InternalCustomerToCustomerConverter();
        customerResponse.setCustomer(custConvert.doConvert(response.getCustomer()));
        customerResponse.setSharedPaymentUrl(response.getSharedPaymentUrl());
        return customerResponse;
    }

}
