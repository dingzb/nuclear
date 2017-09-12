package cc.idiary.nuclear.query.business.tax;

import cc.idiary.utils.common.StringTools;

/**
 * Created by Neo on 2017/5/26.
 */
public class FenjuQuery extends StatisticsQuery {
    private String userIdsStr; //xx,xx,xx


    public String[] getUserIds() {
        return StringTools.isEmpty(userIdsStr) ? new String[0] : userIdsStr.split(",");
    }

    public String getUserIdsStr() {
        return userIdsStr;
    }

    public void setUserIdsStr(String userIdsStr) {
        this.userIdsStr = userIdsStr;
    }
}
