package apps.makarov.com.whereismycurrency.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by makarov on 26/06/15.
 */

public class Bank extends RealmObject {

    @PrimaryKey
    private String key;
    private String name;
    private Date changeRate;
    private RealmList<Rate> rates = new RealmList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRates(RealmList<Rate> rates) {
        this.rates = rates;
    }

    public RealmList<Rate> getRates() {
        return rates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Date changeRate) {
        this.changeRate = changeRate;
    }

    public static String generateKey(Bank bank) {
        return bank.getName();
    }


}
