package apps.makarov.com.whereismycurrency.Utils;

/**
 * Created by makarov on 27/06/15.
 */
public class StringUtils {

    public static String getGoodLong(String badLong) {
        return badLong.replace(",", ".").trim();
    }

}
