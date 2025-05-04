package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.PaymentToPaymentDetailsConverter;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AccessCodeToCreateTransConverter implements BeanConverter<CreateAccessCodeResponse, CreateTransactionResponse> {

    public CreateTransactionResponse doConvert(CreateAccessCodeResponse response) throws RapidSdkException {
        CreateTransactionResponse transactionResponse = new CreateTransactionResponse();
        Transaction transaction = new Transaction();
        InternalCustomerToCustomerConverter custConvert = new InternalCustomerToCustomerConverter();
        transaction.setCustomer(custConvert.doConvert(response.getCustomer()));
        BeanConverter<Payment, PaymentDetails> paymentConverter = new PaymentToPaymentDetailsConverter();
        transaction.setPaymentDetails(paymentConverter.doConvert(response.getPayment()));
        transactionResponse.setTransaction(transaction);

        if (!StringUtils.isBlank(response.getErrors())) {
            transactionResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        transactionResponse.setAccessCode(response.getAccessCode());
        transactionResponse.setFormActionUrl(response.getFormActionURL());
        transactionResponse.setAmexECEncryptedData(response.getAmexECEncryptedData());
        return transactionResponse;
    }

}
