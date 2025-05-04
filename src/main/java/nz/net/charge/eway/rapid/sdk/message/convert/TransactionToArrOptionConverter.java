package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Option;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

import java.util.ArrayList;
import java.util.List;

public class TransactionToArrOptionConverter implements BeanConverter<Transaction, Option[]> {

    public Option[] doConvert(Transaction u) throws RapidSdkException {
        if (u.getOptions() != null) {
            List<Option> listOption = new ArrayList<>();
            for (Option op : u.getOptions()) {
                Option option = new Option();
                option.setValue(op.getValue());
                listOption.add(option);
            }
            return listOption.toArray(new Option[listOption.size()]);
        }
        return new Option[3];
    }

}
