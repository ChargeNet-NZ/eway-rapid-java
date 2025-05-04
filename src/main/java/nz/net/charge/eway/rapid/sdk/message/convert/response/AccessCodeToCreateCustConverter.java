package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.entities.CreateCustomerResponse;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AccessCodeToCreateCustConverter implements BeanConverter<CreateAccessCodeResponse, CreateCustomerResponse> {

    public CreateCustomerResponse doConvert(CreateAccessCodeResponse response) {
        CreateCustomerResponse customerResponse = new CreateCustomerResponse();
        customerResponse.setAccessCode(response.getAccessCode());
        customerResponse.setFormActionUrl(response.getFormActionURL());
        InternalCustomerToCustomerConverter convert = new InternalCustomerToCustomerConverter();
        customerResponse.setCustomer(convert.doConvert(response.getCustomer()));
        if (!StringUtils.isBlank(response.getErrors())) {
            customerResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        return customerResponse;
    }

}
