package apps.makarov.com.whereismycurrency.repository;

import android.app.Application;

import java.util.Date;
import java.util.List;

import apps.makarov.com.whereismycurrency.models.BankData;
import apps.makarov.com.whereismycurrency.repository.models.CurrencyPair;
import apps.makarov.com.whereismycurrency.repository.models.Bank;
import apps.makarov.com.whereismycurrency.repository.models.CacheRequest;
import apps.makarov.com.whereismycurrency.repository.models.Rate;
import apps.makarov.com.whereismycurrency.repository.models.ResultOperation;
import apps.makarov.com.whereismycurrency.repository.models.UserHistory;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by makarov on 27/06/15.
 */

public class RealmRepository implements IRepository<RealmObject> {

    private final Application application;

    public RealmRepository(Application application) {
        this.application = application;
    }

    @Override
    public List<Bank> getBanks() {
        List<Bank> list = Realm.getInstance(application).where(Bank.class).findAll();
        Realm.getInstance(application).close();
        return list;
    }

    @Override
    public List<Rate> getRates(CurrencyPair pair, Date date, String bankName) {

        RealmQuery<Rate> query = Realm.getInstance(application)
                .where(Rate.class).equalTo("currencyPair.key", pair.getKey())
                .notEqualTo("bank", BankData.USER_RATE).equalTo("changeRate", date)
                ;

        if (bankName != BankData.DEFAULT)
            query = query.equalTo("bank", bankName);

        return query.findAll();
    }

    @Override
    public List<UserHistory> getUserHistory() {
        return Realm.getInstance(application).where(UserHistory.class).findAllSorted("date", false);
    }

    @Override
    public List<ResultOperation> getAllResultOperation() {

        RealmResults<ResultOperation> result = Realm.getInstance(application).where(ResultOperation.class)
                .findAllSorted("date", false);

        result.sort("isHistory", true);
        Realm.getInstance(application).close();

        return result;
    }

    @Override
    public <E extends RealmObject> void saveObject(E obj) {
        beginTransaction();
        Realm.getInstance(application).copyToRealmOrUpdate(obj);
        commitTransaction();
    }

    @Override
    public <E extends RealmObject> void removeObject(E obj) {
        beginTransaction();
        obj.removeFromRealm();
        commitTransaction();
    }

    @Override
    public void addUrlToCache(String url) {
        CacheRequest cacheRequest = new CacheRequest();
        cacheRequest.setUrl(url);
        cacheRequest.setDate(new Date());

        beginTransaction();
        Realm.getInstance(application).copyToRealmOrUpdate(cacheRequest);
        commitTransaction();
    }

    @Override
    public boolean hasUrlInCache(String url) {
        return Realm.getInstance(application).where(CacheRequest.class).equalTo("url", url).findFirst() != null;
    }

    @Override
    public ResultOperation getResultOperation(String key) {
        return Realm.getInstance(application).where(ResultOperation.class).equalTo("key", key).findFirst();
    }

    @Override
    public ResultOperation resultToHistory(ResultOperation resultOperation) {
        beginTransaction();
        resultOperation.setIsHistory(true);
        commitTransaction();
        return resultOperation;
    }

    @Override
    public ResultOperation removeResult(ResultOperation resultOperation) {
        removeObject(resultOperation);
        return resultOperation;
    }

    private void beginTransaction() {
        Realm.getInstance(application).beginTransaction();
    }

    private void commitTransaction() {
        Realm.getInstance(application).commitTransaction();
    }

}
