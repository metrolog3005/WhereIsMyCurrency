package apps.makarov.com.whereismycurrency.mappers.realm;

import apps.makarov.com.whereismycurrency.mappers.Mapper;
import apps.makarov.com.whereismycurrency.models.ResultOperation;
import apps.makarov.com.whereismycurrency.repository.realm.models.ResultOperationRealm;
import apps.makarov.com.whereismycurrency.repository.protocols.ResultOperationProtocol;

/**
 * Created by makarov on 11/08/15.
 */
public class ResultOperationRealmMapper implements Mapper<ResultOperation, ResultOperationProtocol> {

    private UserHistoryRealmMapper userHistoryRealmMapper = new UserHistoryRealmMapper();
    private RateRealmMapper rateRealmMapper = new RateRealmMapper();

    @Override
    public ResultOperation modelToData(ResultOperationProtocol obj) {
        return null;
    }

    @Override
    public ResultOperationProtocol dataToModel(ResultOperation resultOperation) {
        ResultOperationRealm pair = new ResultOperationRealm();
        pair.setExitRate(rateRealmMapper.dataToModel(resultOperation.getExitRate()));
        pair.setUserHistory(userHistoryRealmMapper.dataToModel(resultOperation.getUserHistory()));
        pair.setIsHistory(resultOperation.isHistory());
        pair.setDate(resultOperation.getDate());
        pair.setKey(generateKey(resultOperation));
        return pair;
    }

    public static String generateKey(ResultOperation resultOperation) {
        return resultOperation.getDate().toString();
    }
}
