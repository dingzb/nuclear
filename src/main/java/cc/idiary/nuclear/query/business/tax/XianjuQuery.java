package cc.idiary.nuclear.query.business.tax;

import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;

/**
 * Created by Neo on 2017/5/11.
 */
public class XianjuQuery extends StatisticsQuery {

    private String agencyIdsStr; //xx,xx,xx

    public String[] getAgencyIds() {
        return StringTools.isEmpty(agencyIdsStr) ? new String[0] : agencyIdsStr.split(",");
    }

    public String getAgencyIdsStr() {
        return agencyIdsStr;
    }

    public void setAgencyIdsStr(String agencyIdsStr) {
        this.agencyIdsStr = agencyIdsStr;
    }
}
