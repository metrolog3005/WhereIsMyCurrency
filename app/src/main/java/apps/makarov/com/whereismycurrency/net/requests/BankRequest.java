package apps.makarov.com.whereismycurrency.net.requests;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import apps.makarov.com.whereismycurrency.mappers.json.BankJsonMapper;
import apps.makarov.com.whereismycurrency.models.Bank;

/**
 * Created by makarov on 28/06/15.
 */
public class BankRequest extends WimcRequest<Bank> {

    public static final String TAG = FixerRequest.class.getSimpleName();
    public static final String BANK_RATES_URL = "http://informer.kovalut.ru/webmaster/xml-table.php?kod=7701";

    @Override
    public String getPath() {
        return BANK_RATES_URL;
    }

    @Override
    public TYPE getType() {
        return TYPE.GET;
    }

    @Override
    protected List<Bank> parseStringToList(String str) throws JSONException {

        JSONObject jsonObj = XML.toJSONObject(str);
        JSONArray actualRates = jsonObj.getJSONObject("Exchange_Rates").getJSONObject("Actual_Rates").getJSONArray("Bank");
        Log.d(TAG, actualRates.toString());
        List<Bank> list = new ArrayList<>();

        for (int i = 0; i < actualRates.length(); i++) {

            try {
                BankJsonMapper interpreter = new BankJsonMapper();
                Bank bank = interpreter.modelToData(actualRates.getJSONObject(i));
                list.add(bank);
            }catch (Exception e){
                Log.e(TAG, "parse error", e);
            }
        }

        return list;
    }

}
