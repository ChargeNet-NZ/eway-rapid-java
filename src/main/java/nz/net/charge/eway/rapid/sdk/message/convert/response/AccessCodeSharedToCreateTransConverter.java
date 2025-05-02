package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeSharedResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.PaymentToPaymentDetailsConverter;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AccessCodeSharedToCreateTransConverter implements BeanConverter<CreateAccessCodeSharedResponse, CreateTransactionResponse> {

    public CreateTransactionResponse doConvert(CreateAccessCodeSharedResponse response) throws RapidSdkException {
        CreateTransactionResponse transactionResponse = new CreateTransactionResponse();
        Transaction transaction = new Transaction();
        BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> custConvert = new InternalCustomerToCustomerConverter();
        transaction.setCustomer(custConvert.doConvert(response.getCustomer()));

        BeanConverter<Payment, PaymentDetails> paymentConverter = new PaymentToPaymentDetailsConverter();
        transaction.setPaymentDetails(paymentConverter.doConvert(response.getPayment()));

        transactionResponse.setTransaction(transaction);
        if (!StringUtils.isBlank(response.getErrors())) {
            transactionResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        transactionResponse.setSharedPaymentUrl(response.getSharedPaymentUrl());
        transactionResponse.setAccessCode(response.getAccessCode());
        return transactionResponse;
    }

}
