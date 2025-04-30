package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.LineItem;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

import java.util.List;

public class TransactionToArrLineItemConverter implements BeanConverter<Transaction, LineItem[]> {

    public LineItem[] doConvert(Transaction transation) throws RapidSdkException {
        List<LineItem> lineItems = transation.getLineItems();
        if (lineItems != null) {
            return lineItems.toArray(new LineItem[lineItems.size()]);
        }
        return new LineItem[0];
    }

}
